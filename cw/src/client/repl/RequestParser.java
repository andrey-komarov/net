package client.repl;

public class RequestParser {

    private static boolean isInt(String s) {
        try {
            int n = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    static Request parse(String s) {
        s = s.trim();
        if (s.startsWith("up")) {
            return new UpdateSongsListRequest();
        } if (s.startsWith("re")) {
            return new ReloadRequest();
        } else if (isInt(s)) {
            return new PlaySongRequest(Integer.parseInt(s));
        } else {
            return w -> System.out.println("Usage: update|<num>");
        }
    }
}
