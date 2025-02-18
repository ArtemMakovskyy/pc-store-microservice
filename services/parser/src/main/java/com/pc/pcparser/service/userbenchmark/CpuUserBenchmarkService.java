package com.pc.pcparser.service.userbenchmark;

import com.pc.pcparser.dto.mapper.CpuUserBenchmarkMapper;
import com.pc.pcparser.dto.userbenchmark.CpuUserBenchmarkParserDto;
import com.pc.pcparser.model.user.benchmark.UserBenchmarkCpu;
import com.pc.pcparser.repository.CpuUserBenchmarkRepository;
import com.pc.pcparser.service.parse.WebDriverFactory;
import com.pc.pcparser.service.parse.benchmark.user.UserBenchmarkCpuDetailsPageParser;
import com.pc.pcparser.service.parse.benchmark.user.UserBenchmarkCpuPageParser;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class CpuUserBenchmarkService {
    private static final int FAILURE_VALUE = -1;
    private static final int PROCESS_ALL_PAGES_VALUE_DEFAULT = -1;
    private static final int TIMEOUT_SECONDS = 10;
    @Value("${parse.pages.quantity.cpu.user.benchmark}")
    private int parsePagesQuantity;
    @Value("${show.web.windows.from.selenium}")
    private boolean showWebGraphicInterfaceFromSelenium;
    @Value("${sort.by.age.selenium}")
    private boolean sortByAge;
    private final CpuUserBenchmarkRepository cpuUserBenchmarkRepository;
    private final UserBenchmarkCpuPageParser userBenchmarkCpuPageParser;
    private final UserBenchmarkCpuDetailsPageParser userBenchmarkCpuDetailsPageParser;
    private final CpuUserBenchmarkMapper cpuUserBenchmarkMapper;
    private final WebDriverFactory webDriverFactory;

    public List<UserBenchmarkCpu> loadAndSaveNewItems() {
        int parsePages = parsePagesQuantity != 0
                ? parsePagesQuantity
                : PROCESS_ALL_PAGES_VALUE_DEFAULT;

        final List<CpuUserBenchmarkParserDto> cpuUserBenchmarkParserDtos =
                userBenchmarkCpuPageParser.loadAndParse(sortByAge, parsePages);

        final List<UserBenchmarkCpu> newItems = filterNewItems(cpuUserBenchmarkRepository.findAll(),
                cpuUserBenchmarkParserDtos
                        .stream()
                        .map(d -> cpuUserBenchmarkMapper.toEntity(d))
                        .toList());

        final List<UserBenchmarkCpu> userBenchmarkCpus = saveAllToDb(
                newItems.stream()
                        .map(cpuUserBenchmarkMapper::toDto)
                        .toList());

        log.info("Successfully Was added " + userBenchmarkCpus.size() + " new positions");
        return userBenchmarkCpus;
    }

    public void updateMissingSpecifications() {
        final List<UserBenchmarkCpu> byCpuSpecificationIsNull =
                cpuUserBenchmarkRepository.findByCpuSpecificationIsNull();

        WebDriver driver = webDriverFactory.setUpWebDriver(
                showWebGraphicInterfaceFromSelenium, TIMEOUT_SECONDS);
        int updated = 0;
        int notUpdated = 0;
        int progress = 0;

        try {
            for (UserBenchmarkCpu cpu : byCpuSpecificationIsNull) {
                progress++;
                driver.get(cpu.getUrlOfCpu());
                userBenchmarkCpuDetailsPageParser.purseAndAddDetails(cpu, driver);

                if (isCpuDetailsComplete(cpu)) {
                    log.info(cpu.getId() + " not updated: " + notUpdated++);
                } else {
                    updated++;
                    cpuUserBenchmarkRepository.save(cpu);
                }
                log.info("Progress: " + progress + " from: "
                        + byCpuSpecificationIsNull.size() + " "
                        + ". Updated :" + updated + ". Not updates: " + notUpdated);
            }
        } finally {
            driver.quit();
        }
    }

    private List<UserBenchmarkCpu> saveAllToDb(List<CpuUserBenchmarkParserDto> createDto) {
        return cpuUserBenchmarkRepository.saveAll(
                createDto.stream()
                        .map(cpuUserBenchmarkMapper::toEntity)
                        .toList());
    }

    private static List<UserBenchmarkCpu> filterNewItems(
            List<UserBenchmarkCpu> oldList, List<UserBenchmarkCpu> newList) {

        Set<String> oldNames = oldList.stream()
                .map(UserBenchmarkCpu::getModel)
                .collect(Collectors.toSet());

        return newList.stream()
                .filter(newItem -> !oldNames.contains(newItem.getModel()))
                .collect(Collectors.toList());
    }

    private boolean isCpuDetailsComplete(UserBenchmarkCpu cpu) {
        return cpu.getGamingScore() == null || cpu.getGamingScore() == FAILURE_VALUE
                || cpu.getDesktopScore() == null || cpu.getDesktopScore() == FAILURE_VALUE
                || cpu.getWorkstationScore() == null || cpu.getWorkstationScore() == FAILURE_VALUE
                || cpu.getCoresQuantity() == null || cpu.getCoresQuantity() == FAILURE_VALUE
                || cpu.getThreadsQuantity() == null || cpu.getThreadsQuantity() == FAILURE_VALUE
                || cpu.getCpuSpecification() == null || cpu.getCpuSpecification().isBlank();
    }

}
