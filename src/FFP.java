import ast.*;
import lexer.Lexer;
import token.Token;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.*;

public class FFP {
    public static void main(String[] args) throws IOException {
        if (args.length == 0 || args[0].equals("--help") || args[0].equals("-help")) {
            showHelp();
            exit();
        }

        String code = readCode(args);
        PreProcessor processor = new PreProcessor();
        final String processedCode = processor.process(code);

        Runnable script = createScript(processedCode, processor);

        executeScript(script, processor.TIMEOUT);
    }

    private static String readCode(String[] args) throws IOException {
        if (args[0].equals("--code")) {
            return args[1];
        } else {
            Path path = Paths.get(args[0]);
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            StringBuilder content = new StringBuilder();
            for (String line : lines) {
                content.append(line).append("\n");
            }
            return content.toString();
        }
    }

    private static Runnable createScript(String processedCode, PreProcessor processor) {
        return () -> {
            try {
                start(processedCode, processor);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private static void executeScript(Runnable script, int timeout) {
        if (timeout > 0) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<?> future = executor.submit(script);
            try {
                future.get(timeout, TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                future.cancel(true);
                System.out.println("Script timed out.");
            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();
                System.out.println("Script execution failed.");
            } catch (Exception e) {

            } finally {
                executor.shutdown();
            }
        } else {
            script.run();
        }
    }

    private static void start(String code, PreProcessor processor) throws IOException {
        List<Token> tokens = tokenizeCode(code, processor.PRINTTOKENS);
        ProgramNode syntaxTree = parseTokens(tokens, processor.PRINTTREE);
        interpretSyntaxTree(syntaxTree, processor.AUTONEWLINE);
        exit();
    }

    private static List<Token> tokenizeCode(String code, boolean printTokens) {
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.tokenize();
        if (printTokens) {
            tokens.forEach(token -> System.out.printf("%-15s: %s%n", token.type, token.value));
        }
        return tokens;
    }

    private static ProgramNode parseTokens(List<Token> tokens, boolean printTree) {
        Parser parser = new Parser(tokens);
        ProgramNode syntaxTree = parser.parse();
        if (printTree) {
            System.out.println(syntaxTree);
        }
        return syntaxTree;
    }

    private static void interpretSyntaxTree(ProgramNode syntaxTree, boolean autoNewline) throws IOException {
        Interpreter interpreter = new Interpreter(autoNewline);
        interpreter.execute(syntaxTree);
    }

    private static void showHelp() throws IOException {
        InputStream helpStream = FFP.class.getClassLoader().getResourceAsStream("help.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(helpStream))) {
            reader.lines().forEach(System.out::println);
        }
    }

    private static void exit() {
        System.exit(0);
    }
}
