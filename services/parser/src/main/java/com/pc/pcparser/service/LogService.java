package com.pc.pcparser.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LogService {
    private final List<String> logs = new ArrayList<>();
    private final LogWebSocketHandler logWebSocketHandler;

    public LogService(LogWebSocketHandler logWebSocketHandler) {
        this.logWebSocketHandler = logWebSocketHandler;
    }

    public void addLog(String message) {
        logs.add(message);
        logWebSocketHandler.sendLog(message);
    }

    public List<String> getLogs() {
        return logs;
    }
}
