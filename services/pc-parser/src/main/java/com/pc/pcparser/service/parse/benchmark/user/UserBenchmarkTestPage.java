package com.pc.pcparser.service.parse.benchmark.user;

import com.pc.pcparser.service.parse.utils.ParseUtil;
import java.time.Duration;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class UserBenchmarkTestPage {
    private static final String CODE_TEST_IF_NOT_ROBOT
            = "fa fa-4x fa-male";
    private static final String CSS_LOCATOR_TEST_IF_NOT_ROBOT
            = "div > a > i.fa.fa-4x.fa-male";
    private static final int PAUSE_FROM_SEC = 2;
    private static final int PAUSE_TO_SEC = 4;

    public void checkAndPassTestIfNecessary(WebDriver driver) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        if (driver.getPageSource().contains(CODE_TEST_IF_NOT_ROBOT)) {
            log.info("Page contain robot test.");
            By iconLocator = By.cssSelector(CSS_LOCATOR_TEST_IF_NOT_ROBOT);

            WebElement testButtonElement
                    = wait.until(ExpectedConditions.elementToBeClickable(iconLocator));

            if (!testButtonElement.isDisplayed()) {
                throw new RuntimeException("testButtonElement is not visible.");
            }
            log.info("Pause in method checkIfTestPageOpened(), before click on test page");
            ParseUtil.applyRandomDelay(PAUSE_FROM_SEC, PAUSE_TO_SEC, true);
            testButtonElement.click();
            log.info("Test on page passed");
        }
        log.info("Not found test page.");
    }

}
