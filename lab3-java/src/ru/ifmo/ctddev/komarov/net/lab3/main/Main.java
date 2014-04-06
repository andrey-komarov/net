package ru.ifmo.ctddev.komarov.net.lab3.main;

import ru.ifmo.ctddev.komarov.net.lab3.bytes.Storable;
import ru.ifmo.ctddev.komarov.net.lab3.crypto.SHA256;
import ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal.*;
import ru.ifmo.ctddev.komarov.net.lab3.net.BroadcastReceiver;
import ru.ifmo.ctddev.komarov.net.lab3.net.BroadcastSender;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.SocketAddress;
import java.util.Optional;

public class Main {
    public static void main(String[] args) throws IOException {
        String hello = "asdfahasldkfhaksldhfkjsadflkashdfkjsahdlkjfhalsdjhfaksdhflkasjhflkjashfdlkjashdflkasjdlklshdfajkslhdfasdsf";
        for (int i = 0; i < 10; i++) {
            hello += hello;
        }
        byte[] b = hello.getBytes();
        System.out.println(SHA256.hash(b));
        System.out.println(SHA256.hash(new ByteArrayInputStream(b)));
        final KeyPair kp = new KeyPair(Params.DEFAULT);
        Signer s = new Signer(Params.DEFAULT);
        Verifier v = new Verifier(Params.DEFAULT);
        Signature sig = s.sign(kp.privateKey, b);
        System.out.println(v.verify(sig, kp.publicKey, b));
        b[1] = 123;
        System.out.println(v.verify(sig, kp.publicKey, b));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        kp.publicKey.store(baos);
        System.err.println(baos.size());

        Thread sender = new Thread(new BroadcastSender(1234, 1000) {
            @Override
            public Storable getMessage() {
                return kp.publicKey;
            }
        });
        Thread receiver = new Thread(new BroadcastReceiver(1234) {
            @Override
            public void receive(SocketAddress addr, byte[] message) {
                System.out.println("received " + message.length + " bytes from " + addr);
                ByteArrayInputStream bais = new ByteArrayInputStream(message);
                Optional<PublicKey> k = PublicKey.loadFrom(bais);
                System.out.println("read " + k);
            }
        });
        sender.start();
        receiver.start();
    }
}
