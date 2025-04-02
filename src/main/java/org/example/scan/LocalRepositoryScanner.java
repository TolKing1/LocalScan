package org.example.scan;

import org.example.service.LocalFileService;
import org.example.service.impl.LocalFileServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class LocalRepositoryScanner {
    private static final Logger logger = LoggerFactory.getLogger(LocalRepositoryScanner.class);
    private final LocalFileService localFileService;


    /**
     * Provide the file and directory skip lists.
     */
    public LocalRepositoryScanner(Set<String> filesToSkip, Set<String> directoriesToSkip) {
        this.localFileService = new LocalFileServiceImpl(filesToSkip, directoriesToSkip);
    }

    /**
     * Scans the folder at rootPath and saves the content to a timestamped output file.
     */
    public void scanAndSave(String rootPath) {
        String folderName = Paths.get(rootPath).getFileName().toString();
        String outputPath = generateOutputPath(folderName);
        try {
            localFileService.scanDirectoryAndSave(rootPath, outputPath);
        } catch (IOException e) {
            logger.error("Failed to scan and save content: {}", e.getMessage(), e);
        }
    }

    private String generateOutputPath(String folderName) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        return folderName + "_" + timestamp + ".txt";
    }
}