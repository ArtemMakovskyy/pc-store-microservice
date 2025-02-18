package com.pc.pcparser.service.parse.benchmark.user;

import com.pc.pcparser.model.user.benchmark.UserBenchmarkCpu;
import com.pc.pcparser.service.LogService;
import com.pc.pcparser.service.parse.utils.ParseUtil;
import java.io.File;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class UserBenchmarkCpuDetailsPageParser {
    private static final int FAILURE_VALUE = -1;
    private static final int GAMING_SCORE_INDEX1 = 1;
    private static final int DESKTOP_SCORE_INDEX3 = 3;
    private static final int DESKTOP_SCORE_INDEX4 = 4;
    private static final int DESKTOP_SCORE_INDEX5 = 5;
    private static final int WORKSTATION_SCORE_INDEX5 = 5;
    private static final int WORKSTATION_SCORE_INDEX7 = 7;
    private static final int WORKSTATION_SCORE_INDEX9 = 9;
    private static final int CPU_QUANTITY_CORE_INDEX0 = 0;
    private static final int CPU_QUANTITY_THREADS_INDEX2 = 2;
    private static final String CPU_SPECIFICATION_CSS_SELECTOR1 = "p.cmp-cpt";
    private static final String CPU_TABLE_SCORE_SELECTOR2
            = "div.v-center:nth-child(1) > table:nth-child(2) "
            + "> tbody:nth-child(1) > tr:nth-child(1)";
    private static final String CPU_SCORE_ELEMENTS2 = "div.semi-strong";
    private static final String CPU_SCORE_ELEMENTS3 = "td .bsc-w > div:first-child";
    private static final String REGEX_REMOVE_NON_DIGITS = "\\D";
    private static final String REGEX_EXTRACT_CORES = ".*?(\\d+) Cores.*";
    private static final String GROUP_ONE_REFERENCE = "$1";
    private static final String SCORE_ELEMENTS1
            = "div.v-center:nth-child(1) > table:nth-child(2)";
    private static final ParseUtil.DelayInSeconds DELAY_IN_SECONDS
            = new ParseUtil.DelayInSeconds(3, 6);
    private final UserBenchmarkTestPage userBenchmarkTestPage;
    private final LogService logService;

    public void purseAndAddDetails(
            UserBenchmarkCpu cpu,
            WebDriver driver) {

        Document document = getDocument(driver);

        if (parseCpuScore(document, cpu) && parseCpuSpecification(document, cpu)) {
            log.info(cpu);
            logService.addLog(cpu.getModel() + " parsed");
        } else {
            log.error("Can't parse data from cpu ID: " + cpu.getId());
        }

    }

    public void parseFromFile(String pathToFile) {
        UserBenchmarkCpu cpu = new UserBenchmarkCpu();
        Document document = null;
        try {
            document = Jsoup.parse(new File(pathToFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        parseCpuScore(document, cpu);
        parseCpuSpecification(document, cpu);

        log.info(cpu);
    }

    private boolean parseCpuScore(Document document, UserBenchmarkCpu cpu) {
        parseCpuScoreTemplate1(document, cpu);
        System.out.println(cpu);
        if (isGetCpuScoreData(cpu)) {
            log.info("parseCpuScoreTemplate1 successfully");
            return true;
        }
        log.error("parseCpu Score Template1 fails");

        parseCpuScoresTemplate2(document, cpu);
        if (isGetCpuScoreData(cpu)) {
            log.info("parseCpuScoreTemplate2 successfully");
            return true;
        }
        log.error("parseCpu Score Template2  fails");

        parseCpuScoresTemplate3(document, cpu);
        if (isGetCpuScoreData(cpu)) {
            log.info("parseCpuScoreTemplate3 successfully");
            return true;
        }
        log.error("parseCpu Score Template3  fails");
        return false;
    }

    private boolean parseCpuSpecification(Document document, UserBenchmarkCpu cpu) {
        parseCpuSpecificationTemplate1(cpu, document);
        if (isGetCpuSpecificationData(cpu)) {
            log.info("parseCpu Specification Template1 successfully");
            return true;
        }
        log.error("parseCpu Specification Template1 fails");

        parseCpuSpecificationTemplate2(cpu, document);
        if (isGetCpuSpecificationData(cpu)) {
            log.info("parseCpu Specification Template2 successfully");
            return true;
        }
        log.error("parseCpu Specification Template2 fails");

        return false;
    }

    private void parseCpuScoresTemplate3(Document document, UserBenchmarkCpu cpu) {
        Elements scoreTableElement = document.select(CPU_TABLE_SCORE_SELECTOR2);
        Elements scoreElements = scoreTableElement.select(CPU_SCORE_ELEMENTS3);

        String[] scorePartsArray = scoreElements.text().split(" ");

        cpu.setGamingScore(
                extractScore(scorePartsArray, GAMING_SCORE_INDEX1));
        cpu.setDesktopScore(
                extractScore(scorePartsArray, DESKTOP_SCORE_INDEX3));
        cpu.setWorkstationScore(
                extractScore(scorePartsArray, WORKSTATION_SCORE_INDEX5));
    }

    private void parseCpuScoresTemplate2(Document document, UserBenchmarkCpu cpu) {
        Elements scoreTableElement = document.select(CPU_TABLE_SCORE_SELECTOR2);
        Elements scoreElements = scoreTableElement.select(CPU_SCORE_ELEMENTS2);

        String[] scorePartsArray = scoreElements.text().split(" ");

        cpu.setGamingScore(
                extractScore(scorePartsArray, GAMING_SCORE_INDEX1));
        cpu.setDesktopScore(
                extractScore(scorePartsArray, DESKTOP_SCORE_INDEX5));
        cpu.setWorkstationScore(
                extractScore(scorePartsArray, WORKSTATION_SCORE_INDEX9));
    }

    private void parseCpuScoreTemplate1(Document document, UserBenchmarkCpu cpu) {
        Elements scoreTableElement = document.select(SCORE_ELEMENTS1);

        String[] scorePartsArray = scoreTableElement.text().split(" ");

        cpu.setGamingScore(
                extractScore(scorePartsArray, GAMING_SCORE_INDEX1));
        cpu.setDesktopScore(
                extractScore(scorePartsArray, DESKTOP_SCORE_INDEX4));
        cpu.setWorkstationScore(
                extractScore(scorePartsArray, WORKSTATION_SCORE_INDEX7));
    }

    private void parseCpuSpecificationTemplate1(UserBenchmarkCpu cpu, Document document) {
        Elements elementCpuSpecification = document.select(CPU_SPECIFICATION_CSS_SELECTOR1);
        String cpuSpecification = elementCpuSpecification.text();
        String[] cpuSpecificationArray = cpuSpecification.split(" ");

        try {
            cpu.setCpuSpecification(cpuSpecification);
            cpu.setCoresQuantity(ParseUtil.stringToIntIfErrorReturnMinusOne(
                    cpuSpecificationArray[CPU_QUANTITY_CORE_INDEX0]));
            cpu.setThreadsQuantity(ParseUtil.stringToIntIfErrorReturnMinusOne(
                    cpuSpecificationArray[CPU_QUANTITY_THREADS_INDEX2]));
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            cpu.setCoresQuantity(FAILURE_VALUE);
            cpu.setThreadsQuantity(FAILURE_VALUE);
            log.error("Failed to parse CPU specifications. Setting defaults.", e);
        }

    }

    private void parseCpuSpecificationTemplate2(UserBenchmarkCpu cpu, Document document) {
        Elements elementCpuSpecification = document.select(CPU_SPECIFICATION_CSS_SELECTOR1);

        if (elementCpuSpecification.isEmpty()) {
            log.error("parseCpuSpecificationTemplate2(). Element not found!");
            setErrorsCpuValues(cpu);
            return;
        }

        try {
            String textCpuSpecification = elementCpuSpecification.text();

            String coresString = textCpuSpecification.replaceAll(
                    REGEX_EXTRACT_CORES, GROUP_ONE_REFERENCE);
            String frequencyString = textCpuSpecification.replaceAll(
                    REGEX_EXTRACT_CORES, GROUP_ONE_REFERENCE);

            if (coresString.isEmpty() || frequencyString.isEmpty()) {
                throw new IllegalArgumentException("Missing cores or frequency information.");
            }

            double frequency = Double.parseDouble(frequencyString.replace(",", "."));

            cpu.setCoresQuantity(ParseUtil.stringToIntIfErrorReturnMinusOne(coresString));
            cpu.setThreadsQuantity(0);
            cpu.setCpuSpecification(textCpuSpecification);

        } catch (Exception e) {
            setErrorsCpuValues(cpu);
            log.error("Error parsing CPU specification: {}", e.getMessage(), e);
        }
    }

    private void setErrorsCpuValues(UserBenchmarkCpu cpu) {
        cpu.setCoresQuantity(FAILURE_VALUE);
        cpu.setThreadsQuantity(FAILURE_VALUE);
        cpu.setCpuSpecification(null);
    }

    private double extractScore(String[] scorePartsArray, int index) {
        if (index >= scorePartsArray.length) {
            return FAILURE_VALUE;
        }
        try {
            Double result = ParseUtil.stringToDoubleIfErrorReturnMinusOne(scorePartsArray[index]
                    .replaceAll(REGEX_REMOVE_NON_DIGITS, ""));
            return result == 0 ? FAILURE_VALUE : result;
        } catch (NumberFormatException e) {
            return FAILURE_VALUE;
        }
    }

    private boolean isGetCpuScoreData(UserBenchmarkCpu cpu) {
        if (cpu.getGamingScore() == null || cpu.getGamingScore() == FAILURE_VALUE
                || cpu.getDesktopScore() == null || cpu.getDesktopScore() == FAILURE_VALUE
                || cpu.getWorkstationScore() == null || cpu.getWorkstationScore() == FAILURE_VALUE
        ) {
            return false;
        }
        return true;
    }

    private boolean isGetCpuSpecificationData(UserBenchmarkCpu cpu) {
        if (cpu.getCoresQuantity() == null || cpu.getCoresQuantity() == FAILURE_VALUE
                || cpu.getThreadsQuantity() == null || cpu.getThreadsQuantity() == FAILURE_VALUE
                || cpu.getCpuSpecification() == null || cpu.getCpuSpecification().isBlank()
        ) {
            return false;
        }
        return true;
    }

    private Document getDocument(WebDriver driver) {
        userBenchmarkTestPage.checkAndPassTestIfNecessary(driver);
        ParseUtil.applyRandomDelay(DELAY_IN_SECONDS, true);
        return Jsoup.parse(driver.getPageSource());
    }

}
