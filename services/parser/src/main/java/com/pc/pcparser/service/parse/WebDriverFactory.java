package com.pc.pcparser.service.parse;

import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;

@Component
public class WebDriverFactory {

    public WebDriver setUpWebDriver(
            boolean showGraphicalInterface,
            int timeoutSeconds) {
        return setUpWebDriver(null, showGraphicalInterface, timeoutSeconds);
    }

    public WebDriver setUpWebDriver(String url,
                                    boolean showGraphicalInterface,
                                    int timeoutSeconds) {
        WebDriver driver = null;

        if (!showGraphicalInterface) {
            ChromeOptions options = new ChromeOptions();
            //Run without a graphical interface
            options.addArguments("--headless");
            // Optional, for improved compatibility
            options.addArguments("--disable-gpu");
            // Set the window size
            options.addArguments("--window-size=1920,1080");

            driver = new ChromeDriver(options);
        } else {
            driver = new ChromeDriver();
        }

        if (timeoutSeconds > 0) {
            driver.manage().timeouts().implicitlyWait(
                    Duration.ofSeconds(timeoutSeconds));
        }
        if (url != null) {
            driver.get(url);
        }

        return driver;
    }

}
