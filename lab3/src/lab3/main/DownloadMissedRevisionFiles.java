package lab3.main;

import lab3.crypto.elgamal.PublicKey;
import lab3.net.RequestRevisionFiles;
import lab3.structs.Revision;
import lab3.structs.RevisionFiles;
import lab3.structs.RevisionList;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Optional;

public class DownloadMissedRevisionFiles implements Runnable {
    private final RevisionList list;
    private final InetSocketAddress addr;
    private final World world;

    public DownloadMissedRevisionFiles(RevisionList list, InetSocketAddress addr, World world) {
        this.list = list;
        this.addr = addr;
        this.world = world;
    }

    @Override
    public void run() {
        ArrayList<Thread> threads = new ArrayList<>();
        for (Revision rev : list.getRevisions()) {
            Optional<RevisionFiles> known = world.getRevisionFiles(rev.key);
            if (!known.isPresent() || known.get().version < rev.version) {
                Thread t = new Thread(new RequestRevisionFiles(addr, rev.key, world));
                threads.add(t);
                t.start();
            }
        }

        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        if (list.getRevisions().stream().allMatch(rev -> {
            Optional<RevisionFiles> o = world.getRevisionFiles(rev.key);
            return o.isPresent() && o.get().version >= rev.version;
        })) {
            world.commitRevisionList(list);
        }
    }
}
