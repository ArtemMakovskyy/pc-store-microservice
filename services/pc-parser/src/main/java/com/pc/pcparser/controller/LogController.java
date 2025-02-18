package com.pc.pcparser.controller;

import com.pc.pcparser.service.LogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogController {
    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping("/show-logs")
    public String showLogs(Model model) {
        model.addAttribute("logs", logService.getLogs());
        return "logs";
    }
}
