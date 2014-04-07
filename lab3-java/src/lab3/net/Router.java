package lab3.net;

import lab3.crypto.SHA256Hash;
import lab3.crypto.elgamal.PublicKey;
import lab3.main.World;
import lab3.proto.ProtocolConfig;
import lab3.structs.FileInfo;
import lab3.structs.RevisionFiles;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Optional;

public class Router implements Runnable {
    private final Socket socket;
    private final World world;

    public Router(Socket socket, World world) {
        this.socket = socket;
        this.world = world;
    }

    @Override
    public void run() {
        try {
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            int type = is.read();
            switch (type) {
                case -1:
                    return;
                case ProtocolConfig.GET_REVISION_LIST:
                    world.getRevisionList().store(os);
                    socket.close();
                    return;
                case ProtocolConfig.GET_REVISION_FILES:
                    Optional<PublicKey> oKey = PublicKey.loadFrom(is);
                    Optional<RevisionFiles> oRevFiles = oKey.flatMap(world::getRevisionFiles);
                    if (oRevFiles.isPresent()) {
                        os.write(ProtocolConfig.OK);
                        oRevFiles.get().store(os);
                    } else {
                        os.write(ProtocolConfig.DENIED);
                    }
                    socket.close();
                    return;
                case ProtocolConfig.GET_FILE:
                    Optional<SHA256Hash> oHash = SHA256Hash.loadFrom(is);
                    Optional<FileInfo> oInfo = oHash.flatMap(world::getFileInfo);
                    if (oInfo.isPresent()) {
                        os.write(ProtocolConfig.OK);
                        oInfo.get().store(os);
                    } else {
                        os.write(ProtocolConfig.DENIED);
                    }
                    socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
