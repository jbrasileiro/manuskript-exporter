package com.github.osphuhula.core.fs;

import java.nio.file.Files;
import java.nio.file.Path;

public final class Files2 {

    private Files2() {}

    public static void ensureDir(Path dir) throws Exception {
        if (Files.exists(dir) && !Files.isDirectory(dir)) {
            throw new IllegalArgumentException("Not a directory: " + dir);
        }
        if (!Files.exists(dir)) Files.createDirectories(dir);
    }

    public static void requireFile(Path file, String messageIfMissing) {
        if (file == null) throw new IllegalArgumentException(messageIfMissing);
        if (!Files.exists(file) || Files.isDirectory(file)) {
            throw new IllegalArgumentException(messageIfMissing);
        }
    }
}
