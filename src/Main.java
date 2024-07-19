import ast.*;
import lexer.Lexer;
import token.Token;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Main {
    public static void main(String args[]) throws IOException {
        String path = args[0];
        if (path.equals("--help")) {
            System.out.println("This is the help dialog.");
            return;
        }
        String code = Files.readString(Path.of(path), StandardCharsets.UTF_8);
        Lexer lexer = new Lexer(code);
        ArrayList<Token> tokens = new ArrayList(lexer.tokenize());
//        for (Token token: tokens) {
//            System.out.println(token.getType() + " : " + token.getValue());
//        }
//        Pattern pattern = Pattern.compile("^\\d{2}\\/\\d{2}");
//        if (pattern.matcher("02/12").find()) {
//            System.out.println("IT HAS A MATCH");
//        }
        Parser parser = new Parser(tokens);
        ProgramNode tree = parser.parse();
        System.out.println(tree);
        Interpreter interpreter = new Interpreter();
        interpreter.execute(tree);
//        System.out.println(tree);
    }
}

