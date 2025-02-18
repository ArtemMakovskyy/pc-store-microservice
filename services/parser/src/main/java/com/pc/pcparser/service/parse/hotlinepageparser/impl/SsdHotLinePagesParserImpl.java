package com.pc.pcparser.service.parse.hotlinepageparser.impl;

import com.pc.pcparser.dto.hotline.SsdHotLineParserDto;
import com.pc.pcparser.service.LogService;
import com.pc.pcparser.service.parse.HtmlDocumentFetcher;
import com.pc.pcparser.service.parse.utils.ParseUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class SsdHotLinePagesParserImpl extends HotLinePagesParserAbstract<SsdHotLineParserDto> {
    private static final String BASE_URL = "https://hotline.ua/ua/computer/diski-ssd/66705/?p=";
    private static final String CHARACTERISTICS_BLOCK_CSS_SELECTOR = "div.specs__text";
    private static final String PRICE_CSS_SELECTOR = "div.list-item__value-price";
    private static final String NAME_CSS_SELECTOR = "div.list-item__title-container a";
    private static final String LINK_CSS_SELECTOR = "a.item-title.link--black";

    public SsdHotLinePagesParserImpl(
            HtmlDocumentFetcher htmlDocumentFetcher, LogService logService) {
        super(htmlDocumentFetcher, BASE_URL, logService);
    }

    @Override
    protected List<SsdHotLineParserDto> parseData(Document htmlDocument) {
        Elements tableElements = htmlDocument.select(TABLE_CSS_SELECTOR);
        List<SsdHotLineParserDto> ssdList = new ArrayList<>();

        for (Element itemBlock : tableElements) {
            SsdHotLineParserDto ssd = new SsdHotLineParserDto();
            setFields(ssd, itemBlock);
            ssdList.add(ssd);
        }
        return ssdList;
    }

    private void setFields(SsdHotLineParserDto ssd, Element itemBlock) {
        ssd.setUrl(DOMAIN_LINK + parseUrl(itemBlock));
        ssd.setPropositionsQuantity(setPropositionQuantity(itemBlock));
        parseAndSetManufacturerAndName(itemBlock, ssd);
        String prices = parsePrices(itemBlock);
        ssd.setPrices(prices);
        processTextToPriceAvg(prices, ssd);
        Element characteristicsBlock
                = itemBlock.select(CHARACTERISTICS_BLOCK_CSS_SELECTOR).first();
        parseDataFromCharacteristicsBlock(characteristicsBlock, ssd);
    }

    private String parsePrices(Element itemBlock) {
        return itemBlock.select(PRICE_CSS_SELECTOR).text();
    }

    private void processTextToPriceAvg(String input, SsdHotLineParserDto ssd) {
        input = input.replace("грн", "").trim();
        String[] parts = input.split("–");
        double num1;
        if (parts.length == 2) {
            num1 = ParseUtil.stringToDoubleIfErrorReturnMinusOne(
                    parts[0].trim().replace(" ", ""));
            double num2 = ParseUtil.stringToDoubleIfErrorReturnMinusOne(
                    parts[1].trim().replace(" ", ""));
            ssd.setAvgPrice(calculateBestAvgPrice(num1, num2));
        } else if (parts.length == 1) {
            num1 = ParseUtil.stringToDoubleIfErrorReturnMinusOne(
                    parts[0].trim().replace(" ", ""));
            ssd.setAvgPrice(num1);
        } else {
            ssd.setAvgPrice(0.00);
        }
    }

    private void parseAndSetManufacturerAndName(Element itemBlock, SsdHotLineParserDto ssd) {
        Element nameElement = itemBlock.select(NAME_CSS_SELECTOR).first();
        String text = nameElement != null ? nameElement.text().trim() : "Не найдено";
        String manufacturer = text.split(" ")[0];
        String name = text.substring(manufacturer.length()).trim();
        ssd.setManufacturer(manufacturer);
        ssd.setName(name);
    }

    private String parseUrl(Element itemBlock) {
        Element linkElement = itemBlock.select(LINK_CSS_SELECTOR).first();
        if (linkElement == null) {
            log.warn("Can't find url");
            return "";
        }
        return linkElement.attr("href");
    }

    private void parseDataFromCharacteristicsBlock(
            Element characteristicsBlock, SsdHotLineParserDto ssd) {
        if (characteristicsBlock == null) {
            log.warn("Can't find span element to parse data");
        } else {
            Elements positions = characteristicsBlock.select("span");

            boolean isCheckMode = false;
            if (isCheckMode) {
                iterateSpanCheckMode(positions);
            }

            for (Element position : positions) {
                extractData(position.text(), ssd);
            }
        }
    }

    private void iterateSpanCheckMode(Elements positions) {
        for (int i = 0; i < positions.size(); i++) {
            System.out.println(i + " " + positions.get(i).text());
        }
    }

    private void extractData(String text, SsdHotLineParserDto ssd) {
        if (text.contains(" ГБ")) {
            ssd.setCapacity(
                    splitAndExtractDataByIndex(text, 0)
            );
        } else if (text.contains("SSD ")) {
            ssd.setType(
                    "SSD"
            );
        } else if (text.contains("Швидкість читання: ")) {
            ssd.setReadingSpeed(
                    text.replace("Швидкість читання: ", "")
            );
        } else if (text.contains("Швидкість запису: ")) {
            ssd.setWritingSpeed(
                    text.replace("Швидкість запису: ", "")
            );
        }

    }

}
