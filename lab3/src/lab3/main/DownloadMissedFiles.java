package lab3.main;

import lab3.crypto.elgamal.PublicKey;
import lab3.net.RequestFile;
import lab3.structs.RevisionFiles;

import java.net.InetSocketAddress;
import java.util.ArrayList;

public class DownloadMissedFiles implements Runnable {
    private final PublicKey key;
    private final RevisionFiles revisionFiles;
    private final InetSocketAddress addr;
    private final World world;

    public DownloadMissedFiles(PublicKey key, RevisionFiles revisionFiles, InetSocketAddress addr, World world) {
        this.key = key;
        this.revisionFiles = revisionFiles;
        this.addr = addr;
        this.world = world;
    }

    @Override
    public void run() {
        ArrayList<Thread> threads = new ArrayList<>();
        revisionFiles.files.stream().filter(h -> !world.hasFileWithHash(h)).forEach(h -> {
            Thread t = new Thread(new RequestFile(addr, h, world));
            threads.add(t);
            t.start();
        });

        System.err.println("... " + threads.size() + " files missing");

        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        if (revisionFiles.files.stream().allMatch(world::hasFileWithHash)) {
            world.commitRevisionFiles(key, revisionFiles);
        }
    }
}
