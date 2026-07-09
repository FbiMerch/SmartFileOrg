package com.smartorganizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileMover {
    private static final Logger logger = LoggerFactory.getLogger(FileMover.class);

    public Path moveFile(Path source, Path targetDir) throws IOException {
        if (!Files.exists(targetDir)) {
            Files.createDirectories(targetDir);
            logger.info("A fost creat folderul destinație: {}", targetDir);
        }

        Path targetPath = targetDir.resolve(source.getFileName());

        // Rezolvarea conflictelor de nume (Adaugă sufix numeric dacă fișierul există)
        if (Files.exists(targetPath)) {
            String originalName = source.getFileName().toString();
            String nameWithoutExt = originalName;
            String ext = "";

            int dotIndex = originalName.lastIndexOf(".");
            if (dotIndex != -1) {
                nameWithoutExt = originalName.substring(0, dotIndex);
                ext = originalName.substring(dotIndex);
            }

            int count = 1;
            while (Files.exists(targetPath)) {
                targetPath = targetDir.resolve(nameWithoutExt + "_" + count + ext);
                count++;
            }
            logger.info("Conflict de nume rezolvat. Redenumit în: {}", targetPath.getFileName());
        }

        Files.move(source, targetPath, StandardCopyOption.ATOMIC_MOVE);
        logger.info("Mutat cu succes: {} -> {}", source, targetPath);
        return targetPath;
    }
}