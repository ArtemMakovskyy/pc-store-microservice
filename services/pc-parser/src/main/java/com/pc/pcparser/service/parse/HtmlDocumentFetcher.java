package com.pc.pcparser.service.parse;

import com.pc.pcparser.exception.DocumentFetchException;
import com.pc.pcparser.service.parse.utils.ParseUtil;
import com.pc.pcparser.service.parse.utils.UserAgentProvider;
import java.io.File;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class HtmlDocumentFetcher implements DocumentFetcher {

    private static final int DELAY_FROM_SECONDS_BY_DEFAULT = 2;
    private static final int DELAY_TO_SECONDS_BY_DEFAULT = 5;
    private static final int MAX_BODY_SIZE_NO_LIMIT = 0;
    private static final int TIMEOUT_CONNECTION = 60 * 1000;

    private final UserAgentProvider userAgentProvider;

    @Override
    public Document fetchDocument(
            String url, boolean useUserAgent, boolean useDelay, boolean isPrintDocumentToConsole) {
        return fetchDocument(
                url,
                useUserAgent,
                useDelay,
                DELAY_FROM_SECONDS_BY_DEFAULT,
                DELAY_TO_SECONDS_BY_DEFAULT,
                isPrintDocumentToConsole
        );
    }

    @Override
    public Document fetchDocument(
            String url,
            boolean useUserAgent,
            boolean useDelay,
            int delayFrom,
            int delayTo,
            boolean isPrintDocumentToConsole) {

        try {
            ParseUtil.applyRandomDelay(delayFrom, delayTo, useDelay);
            Connection connect = Jsoup.connect(url)
                    .maxBodySize(MAX_BODY_SIZE_NO_LIMIT)
                    .timeout(TIMEOUT_CONNECTION)
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true);

            if (useUserAgent) {
                connect.userAgent(userAgentProvider.getUserAgent("Chrome_Windows"));
            }

            Document document = connect.get();
            if (isPrintDocumentToConsole) {
                log.info(document.toString());
            }

            log.info("Connected to the page: " + url);
            return document;

        } catch (IOException e) {
            log.error("Can't get page: " + url, e);
            throw new DocumentFetchException("Failed to fetch document", e);
        }
    }

    public Document getHtmlDocumentFromFile(String link, boolean isPrintDocumentToConsole) {
        log.info("Reading from file: {}", link);
        File file = new File(link);

        if (!file.exists()) {
            log.error("File not found: {}", link);
            throw new IllegalArgumentException("File not found: " + link);
        }

        try {
            Document htmlDocument = Jsoup.parse(file, "UTF-8");
            if (isPrintDocumentToConsole) {
                log.info(htmlDocument.toString());
            }
            log.info("Document successfully parsed.");
            return htmlDocument;

        } catch (IOException e) {
            log.error("Error while parsing the file: {}", link, e);
            throw new DocumentFetchException("Error reading HTML document from file", e);
        }
    }

}
