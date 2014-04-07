package lab3.repl;

import lab3.main.World;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class REPL implements Runnable {
    private final World world;

    public REPL(World world) {
        this.world = world;
    }

    @Override
    public void run() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                String s = br.readLine();
                if (s == null) {
                    System.out.println("Bye!");
                    System.exit(0);
                }
                Request rq = RequestParser.parse(s);
                rq.apply(world);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
