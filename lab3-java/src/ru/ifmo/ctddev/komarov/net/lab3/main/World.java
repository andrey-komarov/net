package ru.ifmo.ctddev.komarov.net.lab3.main;

import ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal.KeyPair;
import ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal.Params;
import ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal.PublicKey;
import ru.ifmo.ctddev.komarov.net.lab3.structs.BroadcastMessage;
import ru.ifmo.ctddev.komarov.net.lab3.structs.Revision;
import ru.ifmo.ctddev.komarov.net.lab3.structs.RevisionFiles;
import ru.ifmo.ctddev.komarov.net.lab3.structs.RevisionList;

import java.util.*;

public class World {
    private final KeyPair myKeys;
    private Map<PublicKey, SortedSet<RevisionFiles>> revisions;

    public World() {
        myKeys = new KeyPair(Params.DEFAULT);
        revisions = new HashMap<>();
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
}
