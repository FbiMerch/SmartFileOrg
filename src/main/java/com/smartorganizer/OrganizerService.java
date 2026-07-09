package com.smartorganizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class OrganizerService {
    private static final Logger logger = LoggerFactory.getLogger(OrganizerService.class);
    private final FileClassifier classifier;
    private final FileMover mover;

    public OrganizerService() {
        this.classifier = new FileClassifier();
        this.mover = new FileMover();
    }

    public void organizeFolder(Path folderPath) throws IOException {
        HistoryService historyService = new HistoryService(folderPath);
        List<FileMoveRecord> records = new ArrayList<>();

        try (Stream<Path> stream = Files.list(folderPath)) {
            List<Path> filesToMove = stream
                    .filter(Files::isRegularFile)
                    .filter(path -> !path.getFileName().toString().equals("history.json"))
                    .filter(path -> !path.getFileName().toString().equals("organizer.log"))
                    .toList();

            for (Path file : filesToMove) {
                String folderName = classifier.classify(file.getFileName().toString());
                Path targetDir = folderPath.resolve(folderName);

                try {
                    Path finalPath = mover.moveFile(file, targetDir);
                    records.add(new FileMoveRecord(file.toAbsolutePath().toString(), finalPath.toAbsolutePath().toString()));
                } catch (IOException e) {
                    logger.error("Nu s-a putut muta fișierul: " + file.getFileName(), e);
                    throw e;
                }
            }
        }

        if (!records.isEmpty()) {
            historyService.saveHistory(records);
        }
    }

    public void undoLastOperation(Path folderPath) throws IOException {
        HistoryService historyService = new HistoryService(folderPath);
        List<FileMoveRecord> records = historyService.loadHistory();

        if (records.isEmpty()) {
            logger.warn("Nu s-a găsit niciun istoric pentru Undo în folderul specificat.");
            return;
        }

        for (FileMoveRecord record : records) {
            Path currentPath = Path.of(record.destination());
            Path originalPath = Path.of(record.source());

            if (Files.exists(currentPath)) {
                Files.createDirectories(originalPath.getParent());
                Files.move(currentPath, originalPath, StandardCopyOption.REPLACE_EXISTING);
                logger.info("Undo realizat pentru: {} -> {}", currentPath, originalPath);
            }
        }

        historyService.clearHistory();

        // Curățare foldere goale create de organizare
        String[] internalFolders = {"Images", "Documents", "Videos", "Others"};
        for (String folder : internalFolders) {
            Path p = folderPath.resolve(folder);
            if (Files.exists(p) && Files.isDirectory(p)) {
                try (Stream<Path> s = Files.list(p)) {
                    if (s.findAny().isEmpty()) {
                        Files.delete(p);
                        logger.info("Folderul gol a fost șters la Undo: {}", p);
                    }
                }
            }
        }
    }
}