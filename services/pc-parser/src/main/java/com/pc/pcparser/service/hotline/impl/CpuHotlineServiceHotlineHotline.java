package com.pc.pcparser.service.hotline.impl;

import com.pc.pcparser.dto.hotline.CpuHotLineParserDto;
import com.pc.pcparser.dto.mapper.CpuHotLineMapper;
import com.pc.pcparser.exception.CustomServiceException;
import com.pc.pcparser.model.hotline.CpuHotLine;
import com.pc.pcparser.model.user.benchmark.UserBenchmarkCpu;
import com.pc.pcparser.repository.CpuHotLineRepository;
import com.pc.pcparser.repository.CpuUserBenchmarkRepository;
import com.pc.pcparser.service.hotline.HotlineDataUpdateService;
import com.pc.pcparser.service.hotline.HotlineDatabaseSynchronizationService;
import com.pc.pcparser.service.parse.MultiThreadPagesParser;
import java.util.List;
import java.util.concurrent.ExecutorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class CpuHotlineServiceHotlineHotline
        implements HotlineDataUpdateService, HotlineDatabaseSynchronizationService {
    private final MultiThreadPagesParser<CpuHotLineParserDto> cpuPageParser;
    private final CpuHotLineRepository cpuHotLineRepository;
    private final CpuUserBenchmarkRepository cpuUserBenchmarkRepository;
    private final CpuHotLineMapper cpuHotLineMapper;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public void refreshDatabaseWithParsedData(ExecutorService executor) {
        try {
            log.info("Starting cpu data update process...");
            List<CpuHotLineParserDto> cpusHotLine = cpuPageParser.parseAllMultiThread(executor);

            log.info("Parsed {} cpus.", cpusHotLine.size());
            cpuHotLineRepository.deleteAll();
            log.info("Deleted old cpu data.");

            final List<CpuHotLine> cpuHotLineList = cpusHotLine.stream()
                    .map(cpuHotLineMapper::toEntity)
                    .toList();

            final List<CpuHotLine> cpuHotLinesFromDb = cpuHotLineRepository.saveAll(cpuHotLineList);
            log.info("Saved {} new cpu records.", cpuHotLinesFromDb.size());

        } catch (Exception e) {
            log.error("Error occurred during cpu data update process: {}", e.getMessage(), e);
            throw new CustomServiceException("Failed to process cpu data", e);
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public void synchronizeWithBenchmarkData() {
        log.info("Started update cpu scores from User Benchmark DB");
        List<UserBenchmarkCpu> ubCpuSortByModelDesc
                = cpuUserBenchmarkRepository.findAllOrderByModelLengthDesc();
        final List<CpuHotLine> cpuHL = cpuHotLineRepository.findAll();

        for (UserBenchmarkCpu cpuUB : ubCpuSortByModelDesc) {
            for (CpuHotLine cpuHotLine : cpuHL) {
                if (cpuHotLine.getName() != null
                        && cpuHotLine.getUserBenchmarkCpu() == null
                        && cpuUB.getModelHl() != null
                        && cpuHotLine.getName().contains(cpuUB.getModelHl())
                ) {
                    cpuHotLine.setUserBenchmarkCpu(cpuUB);
                }
            }
        }
        cpuHotLineRepository.saveAll(cpuHL);
        log.info("Updated " + cpuHL.size() + " items.");
    }

}
