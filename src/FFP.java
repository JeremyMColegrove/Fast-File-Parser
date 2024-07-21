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
import java.util.Scanner;

public class FFP {
    public static void main(String args[]) throws IOException {

        boolean showTree = false;
        boolean showHelp = false;
        boolean showTokens = false;

        String path = null;
        for (String arg : args) {
            if (arg.equals("-d")) {
                showTree = true;
            } else if (arg.equals("-t")) {
                showTokens = true;
            } else if (arg.equals("--help")) {
                showHelp = true;
            } else {
                path = arg;
            }
        }
        if (path == null) {
            showHelp = true;
        }

        if (showHelp) {
            InputStream help = FFP.class.getClassLoader().getResourceAsStream("help.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(help));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            System.out.println(content);
            exit();
        }

        String code = Files.readString(Path.of(path), StandardCharsets.UTF_8);
        Lexer lexer = new Lexer(code);
        ArrayList<Token> tokens = new ArrayList(lexer.tokenize());
        if (showTokens) {
            for (Token token: tokens) {
                System.out.printf("%-15s: %s%n", token.type, token.value);
            }
        }

        Parser parser = new Parser(tokens);
        ProgramNode tree = parser.parse();
        if (showTree) {
            System.out.println(tree);
        }
        Interpreter interpreter = new Interpreter();
        interpreter.execute(tree);

        exit();
    }

    private static void exit() throws IOException {
        System.exit(0);
    }
}

