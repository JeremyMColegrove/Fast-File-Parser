using System;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Reflection;

namespace FFP
{
    internal class Program
    {
        static void Main(string[] args)
        {
            // Define the path to the JAR file
            var jarPath = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "bin\\ffp.jar");
            if (!File.Exists(jarPath))
            {
                Console.WriteLine("JAR file not found. Please ensure it is installed correctly.");
                return;
            }

            var javaPath = "java";
            // Construct the argument list to pass to Java
            var jarArguments = $"-ea -jar \"{jarPath}\" " + string.Join(" ", args.Select(arg => $"\"{arg}\""));

            var processInfo = new ProcessStartInfo(javaPath, jarArguments)
            {
                CreateNoWindow = false,
                UseShellExecute = false,
                RedirectStandardError = false,
                RedirectStandardOutput = false,
                WindowStyle = ProcessWindowStyle.Normal,
                RedirectStandardInput = true,
            };

            var process = Process.Start(processInfo);
            if (process != null)
            {
                process.WaitForExit();
                process.Close();
            } else
            {
                Console.WriteLine("Could not start FFP process.");
            }
        }
    }
}
