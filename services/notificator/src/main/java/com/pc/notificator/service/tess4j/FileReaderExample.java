package com.pc.notificator.service.tess4j;

import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FileReaderExample {
    @Value("${directory.path}")
    private String directoryPath;
    @PostConstruct
    public void main() {
        String filePath = directoryPath + "textfile.txt"; // Укажите путь к вашему текстовому файлу

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line); // Вывод каждой строки на консоль
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        }
    }
}