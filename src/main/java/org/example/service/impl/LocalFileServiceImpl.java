package org.example.service.impl;

import org.example.service.LocalFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LocalFileServiceImpl implements LocalFileService { private static final Logger logger = LoggerFactory.getLogger(LocalFileServiceImpl.class); private final Set<String> filesToSkip; private final Set<String> directoriesToSkip;



    /**
     * @param filesToSkip       Names (or partial names) of files to skip
     * @param directoriesToSkip Names (or partial names) of directories to skip
     */
    public LocalFileServiceImpl(Set<String> filesToSkip, Set<String> directoriesToSkip) {
        this.filesToSkip = filesToSkip;
        this.directoriesToSkip = directoriesToSkip;
    }

    /**
     * Opens the output file and recursively scans the root directory,
     * writing file paths and content to the output file as they are found.
     */
    @Override
    public void scanDirectoryAndSave(String rootPath, String outputPath) throws IOException {
        Path rootDir = Paths.get(rootPath);
        if (!Files.exists(rootDir)) {
            throw new IllegalArgumentException("Path does not exist: " + rootPath);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputPath), StandardCharsets.UTF_8)) {
            scanPath(rootDir, writer);
        }
        logger.info("Content saved to: {}", outputPath);
    }

    /**
     * Recursively scans the given path. If the path is a directory, its children are iterated.
     * For directories, if the directory name matches the skip list, the directory and its subdirectories are not scanned.
     * For files that are not skipped, their header (path) and content are written.
     */
    private void scanPath(Path path, BufferedWriter writer) throws IOException {
        if (Files.isDirectory(path)) {
            // If current directory is in the directoriesToSkip, don't process it.
            if (directoriesToSkip.contains(path.getFileName().toString())) {
                logger.info("Skipping directory: {}", path);
                return;
            }
            try (Stream<Path> stream = Files.list(path)) {
                for (Path child : (Iterable<Path>) stream::iterator) {
                    scanPath(child, writer);
                }
            } catch (IOException e) {
                logger.error("Error listing directory: " + path, e);
            }
        } else if (Files.isRegularFile(path)) {
            String fileName = path.getFileName().toString();
            if (filesToSkip.contains(fileName)) {
                logger.info("Skipping file: {}", path);
                return;
            }
            logger.info("Scanning file: {}", path);
            try {
                writer.write("# " + path.toString());
                writer.newLine();
                writer.newLine();
                String content = Files.readString(path, StandardCharsets.UTF_8);
                writer.write(content);
                writer.newLine();
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                logger.error("Error reading file: " + path, e);
            }
        }
    }
}