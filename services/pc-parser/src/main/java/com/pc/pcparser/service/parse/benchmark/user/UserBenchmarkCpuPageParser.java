package com.pc.pcparser.service.parse.benchmark.user;

import com.pc.pcparser.dto.userbenchmark.CpuUserBenchmarkParserDto;
import com.pc.pcparser.service.LogService;
import com.pc.pcparser.service.parse.WebDriverFactory;
import com.pc.pcparser.service.parse.utils.ParseUtil;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class UserBenchmarkCpuPageParser {
    private static final int TIMEOUT_SECONDS = 10;
    private static final int PARSE_ALL_PAGES_INDEX = -1;

    private static final String CSS_SELECTOR_TABLE_ROW = "tr.hovertarget";
    private static final String CSS_SELECTOR_MANUFACTURER = "td:nth-child(2) span.semi-strongs";
    private static final String CSS_SELECTOR_MODEL = "td:nth-child(2) span.semi-strongs a.nodec";
    private static final String CSS_SELECTOR_USER_RATING = "td:nth-child(3) div.mh-tc";
    private static final String CSS_SELECTOR_VALUE_PERCENTS = "td:nth-child(4) div.mh-tc";
    private static final String CSS_SELECTOR_AVG_BENCH = "td:nth-child(5) div.mh-tc";
    private static final String CSS_SELECTOR_MEMORY = "td:nth-child(6) div.mh-tc";
    private static final String CSS_SELECTOR_PRICE = "td:nth-child(10) div.mh-tc";
    private static final String CSS_SELECTOR_URL = "td a.nodec";

    private static final ParseUtil.DelayInSeconds SMALL_PAUSE = new ParseUtil.DelayInSeconds(2, 4);
    private static final ParseUtil.DelayInSeconds BIG_PAUSE = new ParseUtil.DelayInSeconds(4, 10);

    private static final String XPATH_NEXT_PAGE_BUTTON
            = "//*[@id=\"tableDataForm:j_idt225\"]";
    private static final String XPATH_LOCATOR_PAGE_QUANTITY
            = "/html/body/div[2]/div/div[6]/form/div[2]/nav/ul/li[1]/a";
    private static final String PAGE_QUANTITY_PATTERN = "Page \\d+ of (\\d+)";
    private static final String ONLY_DIGITS_PATTERN = "[^0-9]";
    private static final String XPATH_BUTTON_PRICE_SORT
            = "//*[@id=\"tableDataForm:mhtddyntac\"]/table/thead/tr//th[@data-mhth='MC_PRICE'][1]";
    private static final String XPATH_BUTTON_AGE_MONTH_SORT
            = "//*[@id='tableDataForm:mhtddyntac']"
            + "/table/thead/tr//th[@data-mhth='MC_RELEASEDATE'][1]";
    private static final String BASE_URL = "https://cpu.userbenchmark.com/";

    @Value("${show.web.windows.from.selenium}")
    private boolean showWebGraphicInterfaceFromSelenium;

    private final UserBenchmarkTestPage userBenchmarkTestPage;
    private final WebDriverFactory webDriverFactory;
    private final LogService logService;

    public List<CpuUserBenchmarkParserDto> loadAndParse(boolean sortByAge) {
        return loadAndParse(sortByAge, PARSE_ALL_PAGES_INDEX);
    }

    public List<CpuUserBenchmarkParserDto> loadAndParse(boolean sortByAge, int pages) {
        if (pages == 0 || pages < PARSE_ALL_PAGES_INDEX) {
            throw new RuntimeException("Enter correct number of pages");
        }
        WebDriver driver = null;
        try {
            driver = webDriverFactory.setUpWebDriver(
                    BASE_URL,
                    showWebGraphicInterfaceFromSelenium,
                    TIMEOUT_SECONDS);

            userBenchmarkTestPage.checkAndPassTestIfNecessary(driver);
            if (pages == PARSE_ALL_PAGES_INDEX) {
                pages = findPageQuantity(driver);
            }

            if (sortByAge) {
                sortByAgeMonthButton(driver);
                ParseUtil.applyRandomDelay(BIG_PAUSE, true);
                sortByAgeMonthButton(driver);
            } else {
                sortByPriceButton(driver);
            }
            return parsePages(driver, pages);
        } finally {
            driver.quit();
        }
    }

    private List<CpuUserBenchmarkParserDto> parsePages(WebDriver driver, int pages) {
        List<CpuUserBenchmarkParserDto> cpuUserBenchmarks = new ArrayList<>();
        int currentPage = 1;
        do {
            log.info("Current page is " + currentPage + " from " + pages + ".");
            logService.addLog("Current page is " + currentPage + " from " + pages + ".");
            final List<CpuUserBenchmarkParserDto> cpusUserBenchmarksOnPage = parsePage(driver);
            cpuUserBenchmarks.addAll(cpusUserBenchmarksOnPage);

            if (currentPage != pages) {
                log.info("Pause 3 in parsePages() before click on next page");
                ParseUtil.applyRandomDelay(BIG_PAUSE, true);
                clickNextPage(driver);
            }
            currentPage++;

        } while (currentPage < pages);
        log.info("Parse pages successfully stopped");
        return cpuUserBenchmarks;
    }

    private List<CpuUserBenchmarkParserDto> parsePage(WebDriver driver) {
        String currentHtmlPageSource = driver.getPageSource();
        List<CpuUserBenchmarkParserDto> cpuUserBenchmarksOnPage
                = pursePageSource(currentHtmlPageSource);

        log.info("Pause 2 in parsePage()");
        ParseUtil.applyRandomDelay(BIG_PAUSE, true);

        return cpuUserBenchmarksOnPage;
    }

    private List<CpuUserBenchmarkParserDto> pursePageSource(String pageSource) {
        Document htmlDocument = Jsoup.parse(pageSource);

        Elements rows = htmlDocument.select(CSS_SELECTOR_TABLE_ROW);
        CpuUserBenchmarkParserDto item;
        List<CpuUserBenchmarkParserDto> items = new ArrayList<>();
        for (Element row : rows) {
            item = rowToCpu(row);
            items.add(item);
        }

        return items;
    }

    private CpuUserBenchmarkParserDto rowToCpu(Element row) {
        String manufacturer = row.select(CSS_SELECTOR_MANUFACTURER).first().ownText().trim();
        String model = row.select(CSS_SELECTOR_MODEL).text().trim();
        String userRating = row.select(CSS_SELECTOR_USER_RATING).first().text()
                .replaceAll(ONLY_DIGITS_PATTERN, "");
        String valuePercents = row.select(CSS_SELECTOR_VALUE_PERCENTS).text();
        String avgBench = row.select(CSS_SELECTOR_AVG_BENCH).text().split(" ")[0];
        String memory = row.select(CSS_SELECTOR_MEMORY).text();
        String price = row.select(CSS_SELECTOR_PRICE).text()
                .replaceAll(ONLY_DIGITS_PATTERN, "");

        CpuUserBenchmarkParserDto cpu = new CpuUserBenchmarkParserDto();
        cpu.setModel(model);
        cpu.setModel(formatHlCpuName(model));
        cpu.setManufacturer(manufacturer);
        cpu.setUserRating(ParseUtil.stringToDoubleIfErrorReturnMinusOne(userRating));
        cpu.setValuePercents(ParseUtil.stringToDoubleIfErrorReturnMinusOne(valuePercents));
        cpu.setAvgBench(ParseUtil.stringToDoubleIfErrorReturnMinusOne(avgBench));
        cpu.setMemoryPercents(ParseUtil.stringToDoubleIfErrorReturnMinusOne(memory));
        cpu.setPrice(ParseUtil.stringToDoubleIfErrorReturnMinusOne(price));
        cpu.setUrlOfCpu(row.select(CSS_SELECTOR_URL).attr("href"));
        return cpu;
    }

    private static String formatHlCpuName(String input) {
        if (input.matches("Ryzen 7 5700G")) {
            return input.replace("Ryzen 7 5700G", "Ryzen 7 5700");
        } else if (input.matches(".*\\d+KF.*")) {
            return input.replaceAll("(\\d+)KF", "$1K");
        } else if (input.contains("Core i3-10105F")) {
            return input.replace("Core i3-10105F", "Core i3-10105");
        } else if (input.contains("Ryzen 5 1500X")) {
            return input.replace("Ryzen 5 1500X", "Ryzen 5 1500X");
        }

        return input;
    }

    private int findPageQuantity(WebDriver driver) {
        int pages;

        By pageInfoLocator = By.xpath(XPATH_LOCATOR_PAGE_QUANTITY);

        WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement pageInfoElement = wait2.until(
                ExpectedConditions.visibilityOfElementLocated(pageInfoLocator));

        String pageInfoText = pageInfoElement.getText();

        Pattern pattern = Pattern.compile(PAGE_QUANTITY_PATTERN);
        Matcher matcher = pattern.matcher(pageInfoText);

        if (matcher.find()) {
            pages = Integer.parseInt(matcher.group(1));
        } else {
            throw new RuntimeException("Unable to extract the maximum number of pages from text: "
                    + pageInfoText);
        }

        return pages;
    }

    private void sortByPriceButton(WebDriver driver) {
        By xpathPriceSortButton = By.xpath(XPATH_BUTTON_PRICE_SORT);
        final WebElement elementPriceButton = driver.findElement(xpathPriceSortButton);
        if (!elementPriceButton.isDisplayed()) {
            throw new RuntimeException("Price sort button is not visible.");
        }
        log.info("Pause 1, before click on price button");
        ParseUtil.applyRandomDelay(SMALL_PAUSE, true);
        elementPriceButton.click();
    }

    private void sortByAgeMonthButton(WebDriver driver) {
        By xpathPriceSortButton = By.xpath(XPATH_BUTTON_AGE_MONTH_SORT);
        final WebElement elementPriceButton = driver.findElement(xpathPriceSortButton);
        if (!elementPriceButton.isDisplayed()) {
            throw new RuntimeException("AgeMonthButton sort button is not visible.");
        }
        log.info("Pause 1, before click on price button");
        ParseUtil.applyRandomDelay(SMALL_PAUSE, true);
        elementPriceButton.click();
    }

    private void clickNextPage(WebDriver driver) {
        By xpathNextButton = By.xpath(XPATH_NEXT_PAGE_BUTTON);
        WebElement elementNextButton = driver.findElement(xpathNextButton);
        log.info("Pause 4 in clickNextPage click before click on next page");
        ParseUtil.applyRandomDelay(SMALL_PAUSE, true);
        elementNextButton.click();

        userBenchmarkTestPage.checkAndPassTestIfNecessary(driver);
    }

}
