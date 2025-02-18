package com.pc.pcparser.service.parse.utils;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class UserAgentProvider {

    private final Map<String, String> userAgents;

    public UserAgentProvider() {
        userAgents = new HashMap<>();
        userAgents.put("Chrome_Windows",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
                        + "(KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36");
        userAgents.put("Firefox_Windows",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:102.0) "
                        + "Gecko/20100101 Firefox/102.0");
        userAgents.put("Edge_Windows",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
                        + "(KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36 Edg/107.0.0.0");
        userAgents.put("Safari_macOS",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 "
                        + "(KHTML, like Gecko) Version/15.1 Safari/605.1.15");
        userAgents.put("Chrome_Android",
                "Mozilla/5.0 (Linux; Android 10; SM-G970F) AppleWebKit/537.36 "
                        + "(KHTML, like Gecko) Chrome/107.0.0.0 Mobile Safari/537.36");
        userAgents.put("Firefox_Android",
                "Mozilla/5.0 (Android 10; Mobile; rv:102.0) Gecko/102.0 Firefox/102.0");
        userAgents.put("Safari_iOS",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 14_0 like Mac OS X) AppleWebKit/605.1.15 "
                        + "(KHTML, like Gecko) Version/14.0 Mobile/15E148 Safari/604.1");
        userAgents.put("Chrome_macOS",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 "
                        + "(KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36");
        userAgents.put("Chrome_iOS",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 14_0 like Mac OS X) AppleWebKit/605.1.15 "
                        + "(KHTML, как Gecko) CriOS/107.0.0.0 Mobile/15E148 Safari/604.1");
        userAgents.put("Firefox_macOS",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7; rv:102.0) "
                        + "Gecko/20100101 Firefox/102.0");
    }

    public String getUserAgent(String key) {
        return userAgents.getOrDefault(key, userAgents.get("Chrome_Windows"));
    }
}
