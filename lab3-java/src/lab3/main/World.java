package lab3.main;

import lab3.crypto.SHA256Hash;
import lab3.crypto.elgamal.KeyPair;
import lab3.crypto.elgamal.Params;
import lab3.crypto.elgamal.PublicKey;
import lab3.proto.ProtocolConfig;
import lab3.net.RequestRevisionList;
import lab3.structs.*;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.*;

public class World {
    private final KeyPair myKeys;
    private Map<PublicKey, SortedSet<RevisionFiles>> revisions;
    private Set<SHA256Hash> knownRevisionListHashes;
    private Map<SHA256Hash, FileInfo> knownFiles;
    private MutableFileList myFiles;
    private Set<SHA256Hash> currentlyDownloading;

    public World() {
        myKeys = new KeyPair(Params.DEFAULT);
        revisions = new HashMap<>();
        knownRevisionListHashes = new HashSet<>();
        myFiles = new MutableFileList(myKeys);
        knownFiles = new HashMap<>();
        currentlyDownloading = new HashSet<>();
    }

    public synchronized boolean notCurrentlyDownloadingAndAdd(SHA256Hash hash) {
        if (currentlyDownloading.contains(hash)) {
            return true;
        } else {
            currentlyDownloading.add(hash);
            return false;
        }
    }

    public synchronized void removeCurrentlyDownloading(SHA256Hash hash) {
        currentlyDownloading.remove(hash);
    }

    public synchronized RevisionList getRevisionList() {
        List<Revision> r = new ArrayList<>();
        revisions.forEach((k, files) -> {
            r.add(new Revision(k, files.last().version));
        });
        return new RevisionList(r);
    }

    public synchronized BroadcastMessage getBroadcastMessage() {
        return new BroadcastMessage(myKeys.publicKey, getRevisionList().hash());
    }

    public synchronized Optional<RevisionFiles> getRevisionFiles(PublicKey key) {
        if (revisions.containsKey(key)) {
            return Optional.of(revisions.get(key).last());
        } else {
            return Optional.empty();
        }
    }

    public synchronized Optional<FileInfo> getFileInfo(SHA256Hash hash) {
        if (knownFiles.containsKey(hash)) {
            FileInfo info = knownFiles.get(hash);
            Optional<RevisionFiles> oRev = getRevisionFiles(info.owner);
            if (oRev.isPresent() && oRev.get().files.contains(hash)) {
                return Optional.of(knownFiles.get(hash));
            } else {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    public synchronized boolean hasFileWithHash(SHA256Hash h) {
        return knownFiles.containsKey(h);
    }

    public synchronized void acceptBroadcastMessage(BroadcastMessage msg, InetAddress source) {
        System.err.println("HEARTBEAT " + msg + " from " + source);
        if (!knownRevisionListHashes.contains(msg.hash)) {
            System.err.println("... unknown hash");
            InetSocketAddress addr = new InetSocketAddress(source, ProtocolConfig.TCP_PORT);
            new Thread(new RequestRevisionList(addr, this)).start();
        }
    }

    public synchronized void acceptRevisionList(RevisionList list, InetAddress source) {
        System.err.println("RECEIVED REVISION_LIST " + list + " from " + source);
        InetSocketAddress addr = new InetSocketAddress(source, ProtocolConfig.TCP_PORT);
        new Thread(new DownloadMissedRevisionFiles(list, addr, this)).start();
    }

    public synchronized void commitRevisionList(RevisionList list) {
        knownRevisionListHashes.add(list.hash());
    }

    public synchronized void commitRevisionFiles(PublicKey key, RevisionFiles files) {
        if (!revisions.containsKey(key)) {
            revisions.put(key, new TreeSet<>(RevisionFiles.BY_VERSION));
        }
        revisions.get(key).add(files);
    }

    public synchronized void acceptRevisionFiles(PublicKey key, RevisionFiles files, InetAddress source) {
        System.err.println("RECEIVED REVISION_FILES " + key.toShortString() + " : " + files + " from " + source);
        if (!files.verify(myKeys.params, key)) {
            System.err.println("... signature verification failed");
//            return;
        }
        InetSocketAddress addr = new InetSocketAddress(source, ProtocolConfig.TCP_PORT);
        new Thread(new DownloadMissedFiles(key, files, addr, this)).start();
    }

    public synchronized void accept(FileInfo info) {
        System.err.println("RECEIVED " + info.location);
        knownFiles.put(info.hash(), info);
    }

    public synchronized void registerNewFile(File file) {
        try {
            myFiles.addFile(file);

            FileInfo info = new FileInfo(myKeys.publicKey, file);
            if (!knownFiles.containsKey(info.hash())) {
                knownFiles.put(info.hash(), info);
            }
            acceptRevisionFiles(myKeys.publicKey, myFiles.toRevisionFiles(), InetAddress.getLocalHost());
            System.out.println("Added " + file.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void unregisterFile(File file) {
        try {
            myFiles.removeFile(file);
            acceptRevisionFiles(myKeys.publicKey, myFiles.toRevisionFiles(), InetAddress.getLocalHost());
            System.out.println("Removed " + file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
