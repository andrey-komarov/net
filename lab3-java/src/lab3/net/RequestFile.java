package lab3.net;

import lab3.bytes.ByteArrayLoader;
import lab3.bytes.IntLoader;
import lab3.crypto.SHA256;
import lab3.crypto.SHA256Hash;
import lab3.crypto.elgamal.PublicKey;
import lab3.main.World;
import lab3.proto.ProtocolConfig;
import lab3.structs.FileInfo;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

public class RequestFile implements Runnable {
    private final InetSocketAddress addr;
    private final SHA256Hash hash;
    private final World world;

    public RequestFile(InetSocketAddress addr, SHA256Hash hash, World world) {
        this.addr = addr;
        this.hash = hash;
        this.world = world;
    }

    @Override
    public void run() {
        if (world.notCurrentlyDownloadingAndAdd(hash)) {
            return;
        }
        try {
            Socket socket = new Socket();
            socket.connect(addr);
            OutputStream os = socket.getOutputStream();
            System.err.println("REQ FILE " + hash + " from " + addr);
            os.write(ProtocolConfig.GET_FILE);
            if (!hash.store(os)) {
                return;
            }

            InputStream is = socket.getInputStream();
            int r = is.read();
            switch (r) {
                case -1:
                case ProtocolConfig.DENIED:
                    return;
                case ProtocolConfig.OK:
                    Optional<PublicKey> oKey = PublicKey.loadFrom(is);
                    if (!oKey.isPresent()) {
                        return;
                    }
                    PublicKey key = oKey.get();

                    Optional<Integer> oNameLen = IntLoader.loadFrom(is);
                    if (!oNameLen.isPresent()) {
                        return;
                    }
                    int nameLen = oNameLen.get();

                    Optional<Integer> oContentLen = IntLoader.loadFrom(is);
                    if (!oContentLen.isPresent()) {
                        return;
                    }
                    int contentLen = oContentLen.get();

                    Optional<String> oName = new ByteArrayLoader(nameLen).load(is).map(String::new);
                    if (!oName.isPresent()) {
                        return;
                    }
                    String name = oName.get();

                    File tmp = File.createTempFile("dload", "");
                    FileOutputStream fos = new FileOutputStream(tmp);

                    for (int i = 0; i < contentLen; i++) {
                        fos.write(is.read());
                    }
                    fos.close();

                    FileInputStream fis = new FileInputStream(tmp);
                    SHA256Hash tmpHash = SHA256.hash(fis);

                    FileInfo info = new FileInfo(tmpHash, name, key, tmp);

                    if (info.hash().equals(hash)) {
                        File dir = new File(String.format("./files/%s", key.toShortString()));
                        dir.mkdir();
                        File saveTo = new File(String.format("./files/%s/%s", key.toShortString(), name));
                        System.out.println("New file: " + saveTo);
                        Files.move(tmp.toPath(), saveTo.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        world.accept(new FileInfo(tmpHash, name, key, saveTo));
                    }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            world.removeCurrentlyDownloading(hash);
        }
    }
}
