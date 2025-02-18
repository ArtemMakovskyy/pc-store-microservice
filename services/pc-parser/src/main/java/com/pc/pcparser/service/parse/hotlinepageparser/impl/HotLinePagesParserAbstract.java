package com.pc.pcparser.service.parse.hotlinepageparser.impl;

import com.pc.pcparser.service.LogService;
import com.pc.pcparser.service.parse.HtmlDocumentFetcher;
import com.pc.pcparser.service.parse.MultiThreadPagesParser;
import com.pc.pcparser.service.parse.utils.ParseUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;

@Log4j2
public abstract class HotLinePagesParserAbstract<T> implements MultiThreadPagesParser<T> {

    protected static final String NO_ELEMENT_CSS_SELECTOR
            = "div.list-item__value > div.list-item__value--overlay."
            + "list-item__value--full > div > div > div.m_b-10";
    protected static final String DIGITS_REGEX = "\\d+";
    protected static final String PROPOSITION_QUANTITY_CSS_SELECTOR
            = "a.link.link--black.text-sm.m_b-5";
    protected static final String DOMAIN_LINK = "https://hotline.ua";
    protected static final String PAGES_CSS_SELECTOR = "a.page";
    protected static final String TABLE_CSS_SELECTOR
            = "div.list-body__content.content.flex-wrap > div";

    private static final int SLEEP_FOR_RETRY_DELAY_FROM = 3;
    private static final int SLEEP_FOR_RETRY_DELAY_TO = 6;
    private static final int MAX_RETRIES = 5;

    protected final HtmlDocumentFetcher htmlDocumentFetcher;
    protected final LogService logService;
    protected final String baseUrl;

    @Value("${hotline.delay.from}")
    private int delayFrom;

    @Value("${hotline.delay.to}")
    private int delayTo;

    @Value("${hotline.delay.use.delay}")
    private boolean useDelay;

    public HotLinePagesParserAbstract(
            HtmlDocumentFetcher htmlDocumentFetcher,
            String baseUrl,
            LogService logService) {
        this.htmlDocumentFetcher = htmlDocumentFetcher;
        this.baseUrl = baseUrl;
        this.logService = logService;
    }

    @Override
    public List<T> parseAllMultiThread(ExecutorService executor) {
        int startPage = 1;
        int maxPage = findMaxPage(baseUrl + 1);
        List<T> parts = new ArrayList<>();
        List<Future<List<T>>> futures = new ArrayList<>();

        for (int i = startPage; i <= maxPage; i++) {
            int pageIndex = i;
            Future<List<T>> future = executor.submit(() -> {
                List<T> parse = parsePage(
                        baseUrl + pageIndex,
                        true,
                        true,
                        delayFrom,
                        delayTo,
                        false);
                log.info("... parsed page: " + pageIndex + " from: " + maxPage);
                return parse;
            });
            futures.add(future);
        }

        for (Future<List<T>> future : futures) {
            try {
                parts.addAll(future.get());
            } catch (InterruptedException | ExecutionException e) {
                log.error("Error parsing page", e);
                Thread.currentThread().interrupt();
            }
        }
        return parts;
    }

    @Override
    public List<T> parseAll() {
        int startPage = 1;
        int maxPage = findMaxPage(baseUrl + 1);
        List<T> parts = new ArrayList<>();

        for (int i = startPage; i <= maxPage; i++) {
            int pageIndex = i;
            final List<T> parse = parsePage(
                    baseUrl + pageIndex,
                    true,
                    true,
                    delayFrom,
                    delayTo,
                    false);
            parts.addAll(parse);
            log.info("... parsed page: " + i + " from: " + maxPage);
        }
        return parts;
    }

    @Override
    public List<T> parsePage(String url) {
        Document htmlDocument = fetchHtmlDocumentWithRetries(
                url, true, useDelay, delayFrom, delayTo, false);
        return parseData(htmlDocument);
    }

    protected List<T> parsePage(
            String url,
            boolean useUserAgent,
            boolean useDelay,
            int delayFrom,
            int delayTo,
            boolean isPrintDocumentToConsole) {
        Document htmlDocument = fetchHtmlDocumentWithRetries(
                url, useUserAgent, useDelay, delayFrom, delayTo, isPrintDocumentToConsole);
        return parseData(htmlDocument);
    }

    protected Document fetchHtmlDocumentWithRetries(
            String url,
            boolean useUserAgent,
            boolean useDelay,
            int delayFrom,
            int delayTo,
            boolean isPrintDocumentToConsole) {
        int maxRetries = MAX_RETRIES;
        int attempt = 0;
        Document htmlDocument = null;

        while (attempt < maxRetries) {
            attempt++;
            try {
                log.info("Attempting to fetch page: " + url + " (Attempt " + attempt + ")");
                logService.addLog("Attempting to fetch page: " + url
                        + " (Attempt " + attempt + ")");
                htmlDocument = htmlDocumentFetcher.fetchDocument(
                        url,
                        useUserAgent,
                        useDelay,
                        delayFrom,
                        delayTo,
                        isPrintDocumentToConsole);

                if (htmlDocument != null && !htmlDocument.select(TABLE_CSS_SELECTOR).isEmpty()) {
                    return htmlDocument;
                } else {
                    log.warn("Page " + url + " loaded but no data found. Retrying...");
                }
            } catch (Exception e) {
                log.error("Error fetching page: " + url + " (Attempt " + attempt + ")", e);
            }
            // sleepForRetry
            ParseUtil.applyRandomDelay(
                    SLEEP_FOR_RETRY_DELAY_FROM, SLEEP_FOR_RETRY_DELAY_TO, useDelay);
        }

        log.error("Failed to load page after " + maxRetries + " attempts: " + url);
        throw new RuntimeException("Failed to load page: " + url);
    }

    protected abstract List<T> parseData(Document htmlDocument);

    protected int findMaxPage(String baseUrl) {
        Document htmlDocument = htmlDocumentFetcher.fetchDocument(
                baseUrl,
                true,
                false,
                false);

        Elements pages = htmlDocument.select(PAGES_CSS_SELECTOR);
        int maxPage = 0;

        for (Element page : pages) {
            String pageText = page.text().trim();
            try {
                int pageNumber = Integer.parseInt(pageText);
                maxPage = Math.max(maxPage, pageNumber);
            } catch (NumberFormatException e) {
                log.info("Cant get number with pageText: " + pageText + " Ignore it.");
            }
        }
        log.info("Pages quality: " + maxPage);
        return maxPage;
    }

    protected int setPropositionQuantity(Element itemBlock) {
        Elements noElement = itemBlock.select(NO_ELEMENT_CSS_SELECTOR);
        if (!noElement.isEmpty()) {
            String waitingText = noElement.text();

            if (waitingText.contains("Очікується в продажу")) {
                return 0;
            }
        }

        Elements propositionQuantityElement = itemBlock.select(PROPOSITION_QUANTITY_CSS_SELECTOR);
        if (!propositionQuantityElement.isEmpty()) {
            String text = propositionQuantityElement.text().trim();
            Pattern pattern = Pattern.compile(DIGITS_REGEX);
            Matcher matcher = pattern.matcher(text);

            if (matcher.find()) {
                String number = matcher.group();
                return ParseUtil.stringToIntIfErrorReturnMinusOne(number);
            }
        }
        return 1;
    }

    protected String splitAndExtractDataByIndex(String text, int index) {
        String[] textArray = text.split(" ");
        if (textArray.length < index) {
            log.warn(this.getClass() + ": Invalid index "
                    + index + ". Text array length is " + textArray.length);
            return "";
        }
        return textArray[index];
    }

    protected double calculateBestAvgPrice(double minPrice, double maxPrice) {
        double avgPrice = (minPrice + maxPrice) / 2;
        double optimalPrice = minPrice * 1.15;
        double min = Math.min(avgPrice, optimalPrice);
        return Math.round(min * 100.0) / 100.0;
    }
}
