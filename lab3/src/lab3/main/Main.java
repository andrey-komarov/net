package lab3.main;

import lab3.bytes.Storable;
import lab3.crypto.SHA256;
import lab3.net.BroadcastReceiver;
import lab3.net.BroadcastSender;
import lab3.proto.ProtocolConfig;
import lab3.net.TCPHandler;
import lab3.repl.REPL;
import lab3.structs.BroadcastMessage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Optional;

public class Main {
    public static void main(String[] args) throws IOException {
        File dir = new File("./files");
        if (!dir.exists()) {
            dir.mkdir();
        }

        final World world = new World();

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver(ProtocolConfig.UDP_PORT) {
            @Override
            public void receive(InetAddress addr, byte[] message) {
                Optional<BroadcastMessage> oMsg = BroadcastMessage.load(new ByteArrayInputStream(message));
                if (oMsg.isPresent()) {
                    world.acceptBroadcastMessage(oMsg.get(), addr);
                }
            }
        };

        BroadcastSender broadcastSender = new BroadcastSender(ProtocolConfig.UDP_PORT, ProtocolConfig.HEARTBEAT_TIMEOUT) {
            @Override
            public Storable getMessage() {
                return world.getBroadcastMessage();
            }
        };

        TCPHandler tcpHandler = new TCPHandler(ProtocolConfig.TCP_PORT, world);

        REPL repl = new REPL(world);

        Runnable[] toRun = new Runnable[]{broadcastReceiver, broadcastSender, tcpHandler, repl};

        for (Runnable r : toRun) {
            new Thread(r).start();
        }
    }
}
