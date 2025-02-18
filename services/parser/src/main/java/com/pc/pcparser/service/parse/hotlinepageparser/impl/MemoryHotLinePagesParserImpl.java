package com.pc.pcparser.service.parse.hotlinepageparser.impl;

import com.pc.pcparser.dto.hotline.MemoryHotLineParserDto;
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
public class MemoryHotLinePagesParserImpl
        extends HotLinePagesParserAbstract<MemoryHotLineParserDto> {
    private static final String BASE_URL
            = "https://hotline.ua/ua/computer/moduli-pamyati-dlya-pk-i-noutbukov/3102/?p=";
    private static final String CHARACTERISTICS_BLOCK_CSS_SELECTOR = "div.specs__text";
    private static final String PRICE_CSS_SELECTOR = "div.list-item__value-price";
    private static final String NAME_CSS_SELECTOR = "div.list-item__title-container a";
    private static final String LINK_CSS_SELECTOR = "a.item-title.link--black";

    public MemoryHotLinePagesParserImpl(
            HtmlDocumentFetcher htmlDocumentFetcher, LogService logService) {
        super(htmlDocumentFetcher, BASE_URL, logService);
    }

    @Override
    protected List<MemoryHotLineParserDto> parseData(Document htmlDocument) {
        Elements tableElements = htmlDocument.select(TABLE_CSS_SELECTOR);
        List<MemoryHotLineParserDto> memories = new ArrayList<>();

        for (Element itemBlock : tableElements) {
            MemoryHotLineParserDto memory = new MemoryHotLineParserDto();
            setFields(memory, itemBlock);
            memories.add(memory);
        }

        return memories;
    }

    private void setFields(MemoryHotLineParserDto memory, Element itemBlock) {
        memory.setUrl(DOMAIN_LINK + parseUrl(itemBlock));
        memory.setPropositionsQuantity(setPropositionQuantity(itemBlock));
        parseAndSetManufacturerAndName(itemBlock, memory);
        String prices = parsePrices(itemBlock);
        memory.setPrices(prices);
        processTextToPriceAvg(prices, memory);
        Element characteristicsBlock
                = itemBlock.select(CHARACTERISTICS_BLOCK_CSS_SELECTOR).first();
        parseDataFromCharacteristicsBlock(characteristicsBlock, memory);
    }

    private String parsePrices(Element itemBlock) {
        return itemBlock.select(PRICE_CSS_SELECTOR).text();
    }

    private void processTextToPriceAvg(String input, MemoryHotLineParserDto memory) {
        input = input.replace("грн", "").trim();
        String[] parts = input.split("–");
        double num1;
        if (parts.length == 2) {
            num1 = ParseUtil.stringToDoubleIfErrorReturnMinusOne(
                    parts[0].trim().replace(" ", ""));
            double num2 = ParseUtil.stringToDoubleIfErrorReturnMinusOne(
                    parts[1].trim().replace(" ", ""));
            memory.setAvgPrice(calculateBestAvgPrice(num1, num2));
        } else if (parts.length == 1) {
            num1 = ParseUtil.stringToDoubleIfErrorReturnMinusOne(
                    parts[0].trim().replace(" ", ""));
            memory.setAvgPrice(num1);
        } else {
            memory.setAvgPrice(0.00);
        }
    }

    private void parseAndSetManufacturerAndName(Element itemBlock, MemoryHotLineParserDto memory) {
        Element nameElement = itemBlock.select(NAME_CSS_SELECTOR).first();
        String text = nameElement != null ? nameElement.text().trim() : "Не найдено";
        String manufacturer = text.split(" ")[0];
        String name = text.substring(manufacturer.length()).trim();
        memory.setManufacturer(manufacturer);
        memory.setName(name);
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
            Element characteristicsBlock, MemoryHotLineParserDto memory) {
        if (characteristicsBlock == null) {
            log.warn("Can't find span element to parse data");
        } else {
            Elements positions = characteristicsBlock.select("span");

            boolean isCheckMode = false;
            if (isCheckMode) {
                iterateSpanCheckMode(positions);
            }

            for (Element position : positions) {
                extractData(position.text(), memory);
            }
        }
    }

    private void iterateSpanCheckMode(Elements positions) {
        for (int i = 0; i < positions.size(); i++) {
            System.out.println(i + " " + positions.get(i).text());
        }
    }

    private void extractData(String text, MemoryHotLineParserDto memory) {
        if (text.contains(" ГБ")) {
            memory.setCapacity(
                    splitAndExtractDataByIndex(text, 0)
            );
        } else if (text.contains("DDR2")) {
            memory.setType(
                    "DDR2"
            );
        } else if (text.contains("DDR3")) {
            memory.setType(
                    "DDR3"
            );
        } else if (text.contains("DDR4")) {
            memory.setType(
                    "DDR4"
            );
        } else if (text.contains("DDR5")) {
            memory.setType(
                    "DDR5"
            );
        } else if (text.contains("DDR6")) {
            memory.setType(
                    "DDR6"
            );
        } else if (text.contains(" МГц")) {
            memory.setFrequency(
                    splitAndExtractDataByIndex(text, 0)
            );
        }

    }

}
