package com.pc.pcparser.service.userbenchmark;

import com.pc.pcparser.dto.mapper.GpuUserBenchmarkMapper;
import com.pc.pcparser.dto.userbenchmark.GpuUserBenchmarkParserDto;
import com.pc.pcparser.model.user.benchmark.UserBenchmarkGpu;
import com.pc.pcparser.repository.GpuUserBenchmarkRepository;
import com.pc.pcparser.service.parse.benchmark.user.UserBenchmarkGpuPageParser;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class GpuUserBenchmarkService {
    private static final int PROCESS_ALL_PAGES_VALUE_DEFAULT = -1;
    @Value("${parse.pages.quantity.gpu.user.benchmark}")
    private int parsePagesQuantity;
    @Value("${sort.by.age.selenium}")
    private boolean sortByAge;
    private final GpuUserBenchmarkRepository gpuUserBenchmarkRepository;
    private final UserBenchmarkGpuPageParser userBenchmarkGpuPageParser;
    private final GpuUserBenchmarkMapper gpuUserBenchmarkMapper;

    public List<UserBenchmarkGpu> getAllWerePowerRequirementIsNull() {
        return gpuUserBenchmarkRepository.findAllByPowerRequirementIsNull();
    }

    public void updateGpusPowerRequirement(List<Long> gpuIds, List<Integer> powerRequirements) {
        for (int i = 0; i < gpuIds.size(); i++) {
            UserBenchmarkGpu gpu = gpuUserBenchmarkRepository.findById(gpuIds.get(i)).orElse(null);
            if (gpu != null) {
                gpu.setPowerRequirement(powerRequirements.get(i));
                gpuUserBenchmarkRepository.save(gpu);
            }
        }
    }

    public List<UserBenchmarkGpu> loadAndSaveNewItems() {
        int parsePages = parsePagesQuantity != 0
                ? parsePagesQuantity
                : PROCESS_ALL_PAGES_VALUE_DEFAULT;
        log.info("Starting the loading and saving of new GPU User Benchmark records.");

        List<GpuUserBenchmarkParserDto> parsedGpuList = userBenchmarkGpuPageParser
                .loadAndParse(sortByAge, parsePages);
        log.info("Loaded {} records from the source.", parsedGpuList.size());

        List<UserBenchmarkGpu> existingGpuList = gpuUserBenchmarkRepository.findAll();
        log.info("Found {} records in the database.", existingGpuList.size());

        List<UserBenchmarkGpu> userBenchmarkGpus = parsedGpuList.stream()
                .map(gpuUserBenchmarkMapper::toEntity)
                .toList();

        List<UserBenchmarkGpu> newItems = filterNewItems(existingGpuList, userBenchmarkGpus);
        log.info("Detected {} new records for saving.", newItems.size());

        List<UserBenchmarkGpu> savedItems = gpuUserBenchmarkRepository.saveAll(newItems);
        log.info("Saved {} new records to the database.", savedItems.size());

        return savedItems;
    }

    private static List<UserBenchmarkGpu> filterNewItems(
            List<UserBenchmarkGpu> existingList,
            List<UserBenchmarkGpu> parsedList) {
        Set<String> existingModels = existingList.stream()
                .map(UserBenchmarkGpu::getModel)
                .collect(Collectors.toSet());

        return parsedList.stream()
                .filter(parsedItem -> !existingModels.contains(parsedItem.getModel()))
                .collect(Collectors.toList());
    }

}
