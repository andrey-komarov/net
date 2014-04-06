package ru.ifmo.ctddev.komarov.net.lab3.bytes;

import java.io.InputStream;
import java.util.Optional;

public interface Loadable<T> {
    Optional<T> load(InputStream is);
}
