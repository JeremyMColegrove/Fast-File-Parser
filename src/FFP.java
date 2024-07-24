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

public class FFP {
    public static void main(String args[]) throws IOException {
        // CHECK ARGUMENTS
        if (args.length == 0 ||  args[0].equals("--help") || args[0].equals("-help")) {
            showHelp();
            exit();
        }

        String code;
        if (args[0].equals("--code")) {
            // READ CODE
            code = args[1];
        } else {
            // READ CODE
            Path path = Paths.get(args[0]);
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            StringBuilder content = new StringBuilder();
            for (String line : lines) {
                content.append(line).append("\n");
            }
            code = content.toString();
        }



        // PRE PROCESS
        PreProcessor processor = new PreProcessor();
        code = processor.process(code);

        // LEXICAL ANALYSIS
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.tokenize();
        // print tokens if requested
        if (processor.PRINTTOKENS) {
            for (Token token: tokens) {
                System.out.printf("%-15s: %s%n", token.type, token.value);
            }
        }

        // SYNTAX PARSING
        Parser parser = new Parser(tokens);
        ProgramNode tree = parser.parse();
        // print syntax tree if requested
        if (processor.PRINTTREE) {
            System.out.println(tree);
        }

        // INTERPRET
        Interpreter interpreter = new Interpreter(processor.AUTONEWLINE);
        interpreter.execute(tree);

        // EXIT
        exit();
    }

    private static void start(String code) {

    }
    private static void showHelp() throws IOException {
        InputStream help = FFP.class.getClassLoader().getResourceAsStream("help.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(help));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line).append("\n");
        }
        System.out.println(content);
    }
    private static void exit() throws IOException {
        System.exit(0);
    }
}

