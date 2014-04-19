package client;

import bytes.ByteArrayLoader;
import crypto.KeyPair;
import crypto.Params;
import crypto.Signature;
import crypto.Signer;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Authentificate {
    public static boolean run(KeyPair keys, InputStream is, OutputStream os) {
        boolean b = keys.publicKey.store(os);
        byte[] data = new ByteArrayLoader(128).load(is).get();
        Signer signer = new Signer(Params.DEFAULT, keys.privateKey);
        Signature sig = signer.sign(data);
        return b && sig.store(os);
    }
}
