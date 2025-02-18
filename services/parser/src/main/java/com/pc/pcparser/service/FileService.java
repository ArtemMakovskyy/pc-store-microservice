package com.pc.pcparser.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class FileService {
    @Value("${xlsx.directory.path}")
    private String directoryPath;
    @Value("${xlsx.file.name.prefix}")
    private String filePrefix;

    public Resource getLatestFile() throws IOException {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles(
                (dir, name) -> name.startsWith(filePrefix) && name.endsWith(".xlsx"));

        if (files == null || files.length == 0) {
            throw new FileNotFoundException("File not found "
                    + directoryPath + filePrefix + ".xlsx");
        }

        File latestFile = Arrays.stream(files)
                .max(Comparator.comparingLong(File::lastModified))
                .orElseThrow();

        return new FileSystemResource(latestFile);
    }

}
