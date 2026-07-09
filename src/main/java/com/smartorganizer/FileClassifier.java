package com.smartorganizer;

import java.util.Optional;

public class FileClassifier {

    public String classify(String fileName) {
        return getExtension(fileName)
                .map(String::toLowerCase)
                .map(ext -> switch (ext) {
                    case "jpg", "jpeg", "png", "gif", "bmp" -> "Images";
                    case "pdf", "txt", "doc", "docx", "xls", "xlsx" -> "Documents";
                    case "mp4", "avi", "mkv", "mov" -> "Videos";
                    default -> "Others";
                })
                .orElse("Others");
    }

    private Optional<String> getExtension(String fileName) {
        int lastIndexOf = fileName.lastIndexOf(".");
        if (lastIndexOf == -1 || lastIndexOf == fileName.length() - 1) {
            return Optional.empty();
        }
        return Optional.of(fileName.substring(lastIndexOf + 1));
    }
}