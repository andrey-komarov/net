package lab3.net;

import lab3.crypto.elgamal.PublicKey;
import lab3.main.World;
import lab3.proto.ProtocolConfig;
import lab3.structs.RevisionFiles;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Optional;

public class RequestRevisionFiles implements Runnable {
    private final InetSocketAddress addr;
    private final World world;
    private final PublicKey key;

    public RequestRevisionFiles(InetSocketAddress addr, PublicKey key, World world) {
        this.addr = addr;
        this.key = key;
        this.world = world;
    }

    @Override
    public void run() {
        try {
            System.err.println("Requesting RevisionFiles from " + addr);
            Socket socket = new Socket();
            socket.connect(addr);
            OutputStream os = socket.getOutputStream();
            os.write(ProtocolConfig.GET_REVISION_FILES);
            if (!key.store(os)) {
                return;
            }

            InputStream is = socket.getInputStream();
            int r = is.read();
            switch (r) {
                case -1:
                case ProtocolConfig.DENIED:
                    return;
                case ProtocolConfig.OK:
                    Optional<RevisionFiles> oRFiles = RevisionFiles.loadFrom(is);
                    if (oRFiles.isPresent()) {
                        world.acceptRevisionFiles(key, oRFiles.get(), addr.getAddress());
                    }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
