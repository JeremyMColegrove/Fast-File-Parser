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

public class Facil {
    public static void main(String args[]) throws IOException {
        String path = args.length > 0 ? args[0] : "--help";
        if (path.equals("--help")) {
            InputStream help = Facil.class.getClassLoader().getResourceAsStream("help.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(help));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            System.out.println(content);
            return;
        }

        String code = Files.readString(Path.of(path), StandardCharsets.UTF_8);
        Lexer lexer = new Lexer(code);
        ArrayList<Token> tokens = new ArrayList(lexer.tokenize());
//        for (Token token: tokens) {
//            System.out.println(token.getType() + " : " + token.getValue());
//        }
        Parser parser = new Parser(tokens);
        ProgramNode tree = parser.parse();
//        System.out.println(tree);
        Interpreter interpreter = new Interpreter();
        interpreter.execute(tree);
    }
}

