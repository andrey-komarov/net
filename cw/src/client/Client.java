package client;

import client.repl.REPL;
import crypto.KeyPair;
import crypto.Params;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Client {
    public static void main(String[] args) throws UnknownHostException {
        System.err.println(args.length);
        if (args.length != 1) {
            System.err.println("Usage: Client <address>");
        }
        InetAddress addr = InetAddress.getByName(args[0]);
        KeyPair keys = new KeyPair(Params.DEFAULT);

        World world = new World(addr, keys);

        new REPL(world).run();
    }
}
