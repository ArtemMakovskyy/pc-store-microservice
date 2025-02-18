package com.pc.pcparser.service.parse;

import org.jsoup.nodes.Document;

public interface DocumentFetcher {
    Document fetchDocument(String url, boolean useUserAgent, boolean useDelay,
                           int delayFrom, int delayTo, boolean isPrintDocumentToConsole);

    default Document fetchDocument(
            String url, boolean useUserAgent, boolean useDelay, boolean isPrintDocumentToConsole) {
        return fetchDocument(url, useUserAgent, useDelay, 2, 5, isPrintDocumentToConsole);
    }

}
