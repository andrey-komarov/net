package lab3.repl;

import java.io.File;

public class RequestParser {
    static Request parse(String s) {
        s = s.trim();
        if (s.startsWith("add ")) {
            return new PutFileRequest(new File(s.substring(4)));
        } else if (s.startsWith("del ")) {
            return new DeleteFileRequest(new File(s.substring(4)));
        } else {
            return w -> System.out.println("Usage: add|del file");
        }
    }
}
