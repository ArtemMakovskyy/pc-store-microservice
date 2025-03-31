package com.text.recognizer.service.tess4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;

//@Service
public class FileReaderExample {
    @Value("${directory.path}")
    private String directoryPath;
//    @PostConstruct
    public void main() {
        String filePath = directoryPath + "textfile.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        }
    }
}
