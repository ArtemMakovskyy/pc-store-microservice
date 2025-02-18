package com.pc.pcparser.service.hotline.impl;

import com.pc.pcparser.dto.hotline.PowerSupplierHotLineParserDto;
import com.pc.pcparser.dto.mapper.PowerSupplierHotLineMapper;
import com.pc.pcparser.exception.CustomServiceException;
import com.pc.pcparser.model.hotline.PowerSupplierHotLine;
import com.pc.pcparser.repository.PowerSupplierHotLineRepository;
import com.pc.pcparser.service.hotline.HotlineDataUpdateService;
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
public class PowerSupplierHotlineServiceHotline implements HotlineDataUpdateService {
    private final MultiThreadPagesParser<PowerSupplierHotLineParserDto>
            powerSupplierMultiThreadPagesParser;
    private final PowerSupplierHotLineRepository
            powerSupplierHotLineRepository;
    private final PowerSupplierHotLineMapper
            powerSupplierHotLineMapper;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public void refreshDatabaseWithParsedData(ExecutorService executor) {
        try {
            log.info("Starting power supplier data update process...");
            List<PowerSupplierHotLineParserDto> items
                    = powerSupplierMultiThreadPagesParser.parseAllMultiThread(executor);

            log.info("Parsed {} power supplier.", items.size());
            powerSupplierHotLineRepository.deleteAll();
            log.info("Deleted old power supplier data.");

            final List<PowerSupplierHotLine> powerSuppliersList = items.stream()
                    .map(powerSupplierHotLineMapper::toEntity)
                    .toList();

            List<PowerSupplierHotLine> powerSupplierHotLinesFromDb
                    = powerSupplierHotLineRepository.saveAll(powerSuppliersList);
            log.info("Saved {} new power supplier records.",
                    powerSupplierHotLinesFromDb.size());
        } catch (Exception e) {
            log.error("Error occurred during power supplier data update process: {}",
                    e.getMessage(), e);
            throw new CustomServiceException("Failed to process power supplier data", e);
        }
    }

}
