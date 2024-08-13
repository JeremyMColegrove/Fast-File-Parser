import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FileManager {
    private Map<String, BufferedReader> openReaders = new HashMap<>();
    private Map<String, BufferedWriter> openWriters = new HashMap<>();
    private Map<String, String> fileModes = new HashMap<>();

    private void openFile(String filePath, String mode) throws IOException {
        String currentMode = fileModes.get(filePath);
        if (!mode.equals(currentMode)) {
            closeFile(filePath);
            File file = new File(filePath);
            if ("r".equals(mode)) {
                if (!file.exists()) {
                    throw new FileNotFoundException(filePath);
                }
                if (!file.canRead()) {
                    throw new RuntimeException("Can not read file '" + filePath + "', permission denied.");
                }
                BufferedReader reader = new BufferedReader(new FileReader(filePath));
                openReaders.put(filePath, reader);
            } else {
                file.createNewFile(); // automatically checks if file exists
                if (!file.canWrite()) {
                    throw new RuntimeException("Can not write to file '" + filePath + "', permission denied.");
                }

                if ("w".equals(mode)) {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false));
                    openWriters.put(filePath, writer);
                } else if ("a".equals(mode)) {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true));
                    openWriters.put(filePath, writer);
                }
            }

            fileModes.put(filePath, mode);
        }
    }

    public void copyFile(String filePath, String destination) throws IOException {
        Path sourcePath = Paths.get(filePath); // Replace with your source file path
        Path targetPath = Paths.get(destination);
        closeFile(filePath);
        closeFile(destination);
        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
    }

    public void deleteFile(String filePath) throws IOException {
        closeFile(filePath);
        Path path = Paths.get(filePath);
        Files.deleteIfExists(path);
    }

    public String readFile(String filePath) throws IOException {
        openFile(filePath, "r");

        BufferedReader reader = openReaders.get(filePath);
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line).append("\n");
        }
        return content.toString();
    }

    public void writeFile(String filePath, String content) throws IOException {
        openFile(filePath, "w");

        BufferedWriter writer = openWriters.get(filePath);
        writer.write(content);
    }

    public void appendFile(String filePath, String content) throws IOException {
        openFile(filePath, "a");

        BufferedWriter writer = openWriters.get(filePath);
        writer.write(content);
    }

    private void closeFile(String filePath) throws IOException {
        if (openReaders.containsKey(filePath)) {
            BufferedReader reader = openReaders.remove(filePath);
            reader.close();
        }

        if (openWriters.containsKey(filePath)) {
            BufferedWriter writer = openWriters.remove(filePath);
            writer.flush(); // apply changes from the writer
            writer.close();
        }

        fileModes.remove(filePath);
    }

    public void closeAllFiles() throws IOException {
        Set<String> readers = openReaders.keySet();
        for (String filePath : readers) {
            closeFile(filePath);
        }
        Set<String> writers = openWriters.keySet();
        for (String filePath : writers) {
            closeFile(filePath);
        }
    }
}
