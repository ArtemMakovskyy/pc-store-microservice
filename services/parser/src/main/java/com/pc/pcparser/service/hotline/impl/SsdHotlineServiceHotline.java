package com.pc.pcparser.service.hotline.impl;

import com.pc.pcparser.dto.hotline.SsdHotLineParserDto;
import com.pc.pcparser.dto.mapper.SsdHotLineMapper;
import com.pc.pcparser.exception.CustomServiceException;
import com.pc.pcparser.model.hotline.SsdHotLine;
import com.pc.pcparser.repository.SsdHotLineRepository;
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
public class SsdHotlineServiceHotline implements HotlineDataUpdateService {
    private static final String SSD_PAGE_LINK
            = "https://hotline.ua/ua/computer/diski-ssd/66705-301666-301678-301686-608078-667303/";

    private final MultiThreadPagesParser<SsdHotLineParserDto> ssdHotlinePageParserImpl;
    private final SsdHotLineRepository ssdHotLineRepository;
    private final SsdHotLineMapper ssdHotLineMapper;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public void refreshDatabaseWithParsedData(ExecutorService executor) {
        try {
            log.info("Starting ssd data update process...");
            List<SsdHotLineParserDto> ssds = ssdHotlinePageParserImpl.parsePage(SSD_PAGE_LINK);

            log.info("Parsed {} ssd.", ssds.size());
            ssdHotLineRepository.deleteAll();
            log.info("Deleted old ssd data.");

            final List<SsdHotLine> ssdList = ssds.stream()
                    .map(ssdHotLineMapper::toEntity)
                    .toList();

            final List<SsdHotLine> ssdHotLinesFromDb = ssdHotLineRepository.saveAll(ssdList);
            log.info("Saved {} new ssd records.", ssdHotLinesFromDb.size());

        } catch (Exception e) {
            log.error("Error occurred during ssd data update process: {}", e.getMessage(), e);
            throw new CustomServiceException("Failed to process ssd data", e);
        }
    }

}
