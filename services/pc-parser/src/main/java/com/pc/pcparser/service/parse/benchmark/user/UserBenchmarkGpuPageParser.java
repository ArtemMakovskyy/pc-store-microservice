package com.pc.pcparser.service.parse.benchmark.user;

import com.pc.pcparser.dto.userbenchmark.GpuUserBenchmarkParserDto;
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
public class UserBenchmarkGpuPageParser {
    private static final int TIMEOUT_SECONDS = 10;
    private static final int PARSE_ALL_PAGES_INDEX = -1;
    private static final ParseUtil.DelayInSeconds SMALL_PAUSE =
            new ParseUtil.DelayInSeconds(2, 4);
    private static final ParseUtil.DelayInSeconds BIG_PAUSE =
            new ParseUtil.DelayInSeconds(4, 10);

    private static final String XPATH_BUTTON_PRICE_SORT =
            "//*[@id=\"tableDataForm:mhtddyntac\"]/table/thead/tr//th"
                    + "[@data-mhth='MC_PRICE'][1]";
    private static final String XPATH_LOCATOR_PAGE_QUANTITY =
            "//*[@id='tableDataForm:mhtddyntac']/nav/ul/li[1]/a";
    private static final String XPATH_NEXT_PAGE_BUTTON =
            "//*[@id=\"tableDataForm:j_idt225\"]";
    private static final String XPATH_BUTTON_AGE_MONTH_SORT =
            "/html/body/div[2]/div/div[6]/form/div[2]/table/thead/tr/th[7]";

    private static final String BASE_URL = "https://gpu.userbenchmark.com/";
    private static final String PAGE_QUANTITY_PATTERN = "Page \\d+ of (\\d+)";
    private static final String ONLY_DIGITS_PATTERN = "[^0-9]";
    private static final String TABLE_ROW_CSS_SELECTOR = "tr.hovertarget";
    private static final String MANUFACTURER_CSS_SELECTOR =
            "td:nth-child(2) span.semi-strongs";
    private static final String MODEL_CSS_SELECTOR =
            "td:nth-child(2) span.semi-strongs a.nodec";
    private static final String USER_RATING_CSS_SELECTOR =
            "td:nth-child(3) div.mh-tc";
    private static final String VALUE_PERCENTS_CSS_SELECTOR =
            "td:nth-child(4) div.mh-tc";
    private static final String AVG_BENCH_CSS_SELECTOR =
            "td:nth-child(5) div.mh-tc";
    private static final String PRICE_CSS_SELECTOR =
            "td:nth-child(8) div.mh-tc";
    private static final String URL_CSS_SELECTOR = "td a.nodec";

    @Value("${show.web.windows.from.selenium}")
    private boolean showWebGraphicInterfaceFromSelenium;
    private final UserBenchmarkTestPage userBenchmarkTestPage;
    private final WebDriverFactory webDriverFactory;

    public List<GpuUserBenchmarkParserDto> loadAndParse(boolean sortByAge, int pages) {
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

    private List<GpuUserBenchmarkParserDto> parsePages(WebDriver driver, int pages) {

        log.info("Start open pages. Total quality is: " + pages);
        List<GpuUserBenchmarkParserDto> gpuUserBenchmarks = new ArrayList<>();
        int currentPage = 1;
        do {
            log.info("Current page is " + currentPage + " from " + pages + ".");
            final List<GpuUserBenchmarkParserDto> gpusUserBenchmarksOnPage
                    = parsePage(driver);
            gpuUserBenchmarks.addAll(gpusUserBenchmarksOnPage);
            log.info("Pause 3 in parsePages() before click on next page");
            ParseUtil.applyRandomDelay(BIG_PAUSE, true);
            currentPage++;
            if (currentPage < pages) {
                clickNextPage(driver);
            }
        } while (currentPage < pages);
        log.info("Parse pages successfully stopped");
        return gpuUserBenchmarks;
    }

    private List<GpuUserBenchmarkParserDto> parsePage(WebDriver driver) {
        String currentHtmlPageSource = driver.getPageSource();
        List<GpuUserBenchmarkParserDto> gpuUserBenchmarksOnPage
                = parsePageSource(currentHtmlPageSource);

        log.info("Pause 2 in parsePage()");
        ParseUtil.applyRandomDelay(BIG_PAUSE, true);
        return gpuUserBenchmarksOnPage;
    }

    private List<GpuUserBenchmarkParserDto> parsePageSource(String pageSource) {
        Document htmlDocument = Jsoup.parse(pageSource);
        Elements rows = htmlDocument.select(TABLE_ROW_CSS_SELECTOR);
        GpuUserBenchmarkParserDto item;
        List<GpuUserBenchmarkParserDto> items = new ArrayList<>();
        for (Element row : rows) {
            item = rowToGpu(row);
            items.add(item);
        }
        return items;
    }

    private GpuUserBenchmarkParserDto rowToGpu(Element row) {
        GpuUserBenchmarkParserDto item = new GpuUserBenchmarkParserDto();
        item.setModel(getElementText(row, MODEL_CSS_SELECTOR));
        item.setModelHl(formatHlGpuName(item.getModel()));
        item.setManufacturer(getElementText(row, MANUFACTURER_CSS_SELECTOR));
        item.setUserRating(ParseUtil.stringToDoubleIfErrorReturnMinusOne(
                getElementText(row, USER_RATING_CSS_SELECTOR)
                        .replaceAll(ONLY_DIGITS_PATTERN, "")));
        item.setValuePercents(ParseUtil.stringToDoubleIfErrorReturnMinusOne(
                getElementText(row, VALUE_PERCENTS_CSS_SELECTOR)));
        item.setAvgBench(ParseUtil.stringToDoubleIfErrorReturnMinusOne(
                getElementText(row, AVG_BENCH_CSS_SELECTOR).split(" ")[0]));
        item.setPrice(ParseUtil.stringToDoubleIfErrorReturnMinusOne(
                getElementText(row, PRICE_CSS_SELECTOR)
                .replaceAll(ONLY_DIGITS_PATTERN, "")));
        item.setUrlOfGpu(row.select(URL_CSS_SELECTOR).attr("href"));
        return item;
    }

    private String getElementText(Element row, String selector) {
        Element element = row.select(selector).first();
        return element != null ? element.text().trim() : "";
    }

    private static String formatHlGpuName(String input) {
        if (input.contains("-TS (Ti-Super)")) {
            return input.replace("-TS (Ti-Super)", " Ti SUPER");
        } else if (input.contains("-S (Super)")) {
            return input.replace("-S (Super)", " SUPER");
        } else if (input.contains("S (Super)")) {
            return input.replace("S (Super)", " SUPER");
        } else if (input.contains("-Ti")) {
            return input.replace("-Ti", " Ti");
        } else if (input.contains("-XT")) {
            return input.replace("-XT", " XT");
        }
        return input;
    }

    private int findPageQuantity(WebDriver driver) {
        int pages;

        By pageInfoLocator = By.xpath(XPATH_LOCATOR_PAGE_QUANTITY);

        WebDriverWait wait2
                = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement pageInfoElement
                = wait2.until(ExpectedConditions
                .visibilityOfElementLocated(pageInfoLocator));

        String pageInfoText = pageInfoElement.getText();

        Pattern pattern = Pattern.compile(PAGE_QUANTITY_PATTERN);
        Matcher matcher = pattern.matcher(pageInfoText);

        if (matcher.find()) {
            pages = Integer.parseInt(matcher.group(1));
        } else {
            throw new RuntimeException(
                    "Unable to extract the maximum number of pages from text: "
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
