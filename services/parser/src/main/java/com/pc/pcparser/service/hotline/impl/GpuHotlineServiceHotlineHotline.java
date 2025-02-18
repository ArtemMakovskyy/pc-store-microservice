package com.pc.pcparser.service.hotline.impl;

import com.pc.pcparser.dto.hotline.GpuHotLineParserDto;
import com.pc.pcparser.dto.mapper.GpuHotLineMapper;
import com.pc.pcparser.exception.CustomServiceException;
import com.pc.pcparser.model.hotline.GpuHotLine;
import com.pc.pcparser.model.user.benchmark.UserBenchmarkGpu;
import com.pc.pcparser.repository.GpuHotLineRepository;
import com.pc.pcparser.repository.GpuUserBenchmarkRepository;
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
public class GpuHotlineServiceHotlineHotline implements
        HotlineDataUpdateService, HotlineDatabaseSynchronizationService {
    private final MultiThreadPagesParser<GpuHotLineParserDto> gpuPageParserImpl;
    private final GpuHotLineRepository gpuHotLineRepository;
    private final GpuUserBenchmarkRepository gpuUserBenchmarkRepository;
    private final GpuHotLineMapper gpuHotLineMapper;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public void refreshDatabaseWithParsedData(ExecutorService executor) {
        try {
            log.info("Starting gpu data update process...");
            List<GpuHotLineParserDto> gpusHotLine = gpuPageParserImpl.parseAllMultiThread(executor);

            log.info("Parsed {} gpus.", gpusHotLine.size());
            gpuHotLineRepository.deleteAll();
            log.info("Deleted old gpu data.");

            final List<GpuHotLine> gpuHotLines = gpusHotLine.stream()
                    .map(gpuHotLineMapper::toEntity)
                    .toList();

            final List<GpuHotLine> gpuHotLinesFromDb = gpuHotLineRepository.saveAll(gpuHotLines);
            log.info("Saved {} new gpu records.", gpuHotLinesFromDb.size());

        } catch (Exception e) {
            log.error("Error occurred during gpu data update process: {}", e.getMessage(), e);
            throw new CustomServiceException("Failed to process gpu data", e);
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public void synchronizeWithBenchmarkData() {
        log.info("Started update gpu scores from User Benchmark DB");
        List<UserBenchmarkGpu> ubGpuSortByModelDesc
                = gpuUserBenchmarkRepository.findAllOrderByModelLengthDesc();
        List<GpuHotLine> gpuHl = gpuHotLineRepository.findAll();

        for (UserBenchmarkGpu gpuUB : ubGpuSortByModelDesc) {
            for (GpuHotLine gpuHotLine : gpuHl) {
                if (gpuHotLine.getName() != null
                        && gpuHotLine.getUserBenchmarkGpu() == null
                        && gpuUB.getModelHl() != null
                        && gpuHotLine.getName().contains(gpuUB.getModelHl())
                ) {
                    gpuHotLine.setUserBenchmarkGpu(gpuUB);
                }
            }
        }
        gpuHotLineRepository.saveAll(gpuHl);
        log.info("Updated " + gpuHl.size() + " items.");
    }

}
