package org.example.service;

import java.io.IOException;

public interface LocalFileService {
    /**
     * Recursively scan all files under the given directory path and write file content to outputPath. * Files and directories in the skip lists are ignored.
     */
    void scanDirectoryAndSave(String rootPath, String outputPath) throws IOException;
}