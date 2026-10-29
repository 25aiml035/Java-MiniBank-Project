package service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;

public class TransactionLog {
    public static synchronized void append(Path file, String line) {
        try {
            if (!Files.exists(file.getParent())) {
                Files.createDirectories(file.getParent());
            }
            Files.write(file, Collections.singletonList(line), 
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
