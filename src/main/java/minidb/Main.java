package minidb;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Storage storage = new Storage();
        Executor executor = new Executor(storage);
        Scanner scanner = new Scanner(System.in);

        System.out.println("MiniDB v1.0 — type 'exit' to quit");
        System.out.println("Supported: CREATE TABLE, INSERT INTO, SELECT ... FROM ... [WHERE]");
        System.out.println();

        while (true) {
            System.out.print("minidb> ");
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;
            if (line.equalsIgnoreCase("exit")) break;

            try {
                List<Lexer.Token> tokens = new Lexer(line).tokenize();
                AST.Statement stmt = new Parser(tokens).parse();
                String result = executor.execute(stmt);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            System.out.println();
        }

        System.out.println("Bye!");
    }
}
