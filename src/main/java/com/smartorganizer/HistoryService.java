package com.smartorganizer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class HistoryService {
    private static final Logger logger = LoggerFactory.getLogger(HistoryService.class);
    private final ObjectMapper objectMapper;
    private final Path historyFilePath;

    public HistoryService(Path targetDirectory) {
        this.objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        this.historyFilePath = targetDirectory.resolve("history.json");
    }

    public void saveHistory(List<FileMoveRecord> records) {
        try {
            objectMapper.writeValue(historyFilePath.toFile(), records);
            logger.info("Istoricul mutărilor a fost salvat în {}", historyFilePath);
        } catch (IOException e) {
            logger.error("Eroare la salvarea istoricului în JSON", e);
        }
    }

    public List<FileMoveRecord> loadHistory() {
        File file = historyFilePath.toFile();
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(file, new TypeReference<List<FileMoveRecord>>() {});
        } catch (IOException e) {
            logger.error("Eroare la citirea istoricului din JSON", e);
            return new ArrayList<>();
        }
    }

    public void clearHistory() {
        try {
            java.nio.file.Files.deleteIfExists(historyFilePath);
        } catch (IOException e) {
            logger.error("Nu s-a putut șterge fișierul history.json", e);
        }
    }
}