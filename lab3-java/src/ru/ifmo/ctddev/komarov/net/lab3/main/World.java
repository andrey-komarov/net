package ru.ifmo.ctddev.komarov.net.lab3.main;

import ru.ifmo.ctddev.komarov.net.lab3.crypto.SHA256Hash;
import ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal.KeyPair;
import ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal.Params;
import ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal.PublicKey;
import ru.ifmo.ctddev.komarov.net.lab3.net.ProtocolConfig;
import ru.ifmo.ctddev.komarov.net.lab3.net.RequestRevisionList;
import ru.ifmo.ctddev.komarov.net.lab3.structs.BroadcastMessage;
import ru.ifmo.ctddev.komarov.net.lab3.structs.Revision;
import ru.ifmo.ctddev.komarov.net.lab3.structs.RevisionFiles;
import ru.ifmo.ctddev.komarov.net.lab3.structs.RevisionList;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.*;

public class World {
    private final KeyPair myKeys;
    private Map<PublicKey, SortedSet<RevisionFiles>> revisions;
    private Set<SHA256Hash> knownRevisionListHashes;

    public World() {
        myKeys = new KeyPair(Params.DEFAULT);
        revisions = new HashMap<>();
        knownRevisionListHashes = new HashSet<>();
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

    public synchronized void acceptBroadcastMessage(BroadcastMessage msg, InetAddress source) {
        if (!knownRevisionListHashes.contains(msg.hash)) {
            InetSocketAddress addr = new InetSocketAddress(source, ProtocolConfig.TCP_PORT);
            new Thread(new RequestRevisionList(addr, this)).start();
        }
    }

    public synchronized void acceptRevisionList(RevisionList list) {
        System.out.println("new rev list!");
    }
}
