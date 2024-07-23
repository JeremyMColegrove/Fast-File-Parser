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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FFP {
    public static void main(String args[]) throws IOException {
        if (args.length == 0 || args[0] == null || args[0].equals("--help") || args[0].equals("-help")) {
            showHelp();
            exit();
        }
        String path = args[0];
        // get code
        String code = Files.readString(Path.of(path), StandardCharsets.UTF_8);
        // pre process
        PreProcessor processor = new PreProcessor();
        code = processor.process(code);
        // lexer phase
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.tokenize();
        // print tokens if requested
        if (processor.SHOWTOKENS) {
            for (Token token: tokens) {
                System.out.printf("%-15s: %s%n", token.type, token.value);
            }
        }
        // syntax parsing
        Parser parser = new Parser(tokens);
        ProgramNode tree = parser.parse();
        // print syntax tree if requested
        if (processor.SHOWTREE) {
            System.out.println(tree);
        }
        // interpreter
        Interpreter interpreter = new Interpreter(processor.AUTONEWLINE);
        // execute!
        interpreter.execute(tree);
        // all done!
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

