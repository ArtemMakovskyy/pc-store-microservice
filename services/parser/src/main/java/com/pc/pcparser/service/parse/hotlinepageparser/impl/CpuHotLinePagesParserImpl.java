package com.pc.pcparser.service.parse.hotlinepageparser.impl;

import com.pc.pcparser.dto.hotline.CpuHotLineParserDto;
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
public class CpuHotLinePagesParserImpl extends HotLinePagesParserAbstract<CpuHotLineParserDto> {
    private static final String BASE_URL = "https://hotline.ua/ua/computer/processory/?p=";
    private static final String CHARACTERISTICS_BLOCK_CSS_SELECTOR = "div.specs__text";
    private static final String PRICE_CSS_SELECTOR = "div.list-item__value-price";
    private static final String NAME_CSS_SELECTOR = "div.list-item__title-container a";
    private static final String LINK_CSS_SELECTOR = "a.item-title.link--black";

    public CpuHotLinePagesParserImpl(
            HtmlDocumentFetcher htmlDocumentFetcher, LogService logService) {
        super(htmlDocumentFetcher, BASE_URL, logService);
    }

    @Override
    protected List<CpuHotLineParserDto> parseData(Document htmlDocument) {
        Elements tableElements = htmlDocument.select(TABLE_CSS_SELECTOR);
        List<CpuHotLineParserDto> cpus = new ArrayList<>();

        for (Element itemBlock : tableElements) {
            CpuHotLineParserDto cpu = new CpuHotLineParserDto();
            setFields(cpu, itemBlock);
            cpus.add(cpu);
        }

        return cpus;
    }

    private void setFields(CpuHotLineParserDto cpu, Element itemBlock) {
        cpu.setUrl(DOMAIN_LINK + parseUrl(itemBlock));
        cpu.setPropositionsQuantity(setPropositionQuantity(itemBlock));

        parseAndSetManufacturerAndName(itemBlock, cpu);

        String prices = parsePrices(itemBlock);
        cpu.setPrices(prices);
        processTextToPriceAvg(prices, cpu);
        Element characteristicsBlock
                = itemBlock.select(CHARACTERISTICS_BLOCK_CSS_SELECTOR).first();
        parseDataFromCharacteristicsBlock(characteristicsBlock, cpu);
    }

    private void parseAndSetManufacturerAndName(Element itemBlock, CpuHotLineParserDto cpu) {
        Element nameElement = itemBlock.select(NAME_CSS_SELECTOR).first();
        String text = nameElement != null ? nameElement.text().trim() : "Не найдено";
        String manufacturer = text.split(" ")[0];
        String name = text.substring(manufacturer.length()).trim();
        cpu.setManufacturer(manufacturer);
        cpu.setName(name);
    }

    private String parsePrices(Element cpuBlock) {
        return cpuBlock.select(PRICE_CSS_SELECTOR).text();
    }

    private void processTextToPriceAvg(String input, CpuHotLineParserDto cpu) {
        input = input.replace("грн", "").trim();
        String[] parts = input.split("–");
        double minPrice;
        if (parts.length == 2) {
            minPrice = ParseUtil.stringToDoubleIfErrorReturnMinusOne(
                    parts[0].trim().replace(" ", ""));
            double maxPrice = ParseUtil.stringToDoubleIfErrorReturnMinusOne(
                    parts[1].trim().replace(" ", ""));

            cpu.setAvgPrice(calculateBestAvgPrice(minPrice, maxPrice));
        } else if (parts.length == 1) {
            minPrice = ParseUtil.stringToDoubleIfErrorReturnMinusOne(
                    parts[0].trim().replace(" ", ""));
            cpu.setAvgPrice(minPrice);
        } else {
            cpu.setAvgPrice(0.00);
        }
    }

    private String parseUrl(Element cpuBlock) {
        Element linkElement = cpuBlock.select(LINK_CSS_SELECTOR).first();
        if (linkElement == null) {
            log.warn("Can't find url");
            return "";
        }
        return linkElement.attr("href");
    }

    private void parseDataFromCharacteristicsBlock(
            Element characteristicsBlock, CpuHotLineParserDto cpu) {
        if (characteristicsBlock == null) {
            log.warn("Can't find span element to parse data");
        } else {
            Elements positions = characteristicsBlock.select("span");
            for (Element position : positions) {
                extractData(position.text(), cpu);
            }
        }
    }

    private void extractData(String text, CpuHotLineParserDto cpu) {

        if (text.contains("Роз'єм: Socket ")) {
            cpu.setSocketType(
                    splitAndExtractDataByIndex(text, 2)
            );
        } else if (text.contains("Частота: ")) {
            cpu.setFrequency(
                    splitAndExtractDataByIndex(text, 1)
            );
        } else if (text.contains("Кеш третього рівня: ")) {
            cpu.setL3Cache(
                    splitAndExtractDataByIndex(text, 3)
            );
        } else if (text.contains("Кількість ядер: ")) {
            cpu.setCores(
                    splitAndExtractDataByIndex(text, 2)
            );
        } else if (text.contains("Число потоків: ")) {
            cpu.setThreads(
                    splitAndExtractDataByIndex(text, 2)
            );
        } else if (text.contains("Комплектація: Tray")) {
            cpu.setPackageType(
                    "Tray"
            );
        } else if (text.contains("Комплектація: MPK")) {
            cpu.setPackageType(
                    "Tray"
            );
        } else if (text.contains("Комплектація: Box")) {
            cpu.setPackageType(
                    "Box"
            );
        }

    }

}
