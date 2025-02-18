package com.pc.pcparser.controller;

import com.pc.pcparser.service.FileService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final FileService fileService;

    @GetMapping("/description")
    public String showDescriptionPage() {
        return "description";
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadLatestFile() {
        try {
            Resource resource = fileService.getLatestFile();
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=" + resource.getFilename())
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
