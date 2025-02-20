package com.pc.pcparser.service;

import com.pc.pcparser.dto.mapper.PcConfigMapper;
import com.pc.pcparser.model.PcConfig;
import com.pc.pcparser.dto.PcConfigDto;
import com.pc.pcparser.model.PcMarker;
import com.pc.pcparser.model.hotline.CpuHotLine;
import com.pc.pcparser.model.hotline.GpuHotLine;
import com.pc.pcparser.model.hotline.MemoryHotLine;
import com.pc.pcparser.model.hotline.MotherBoardHotLine;
import com.pc.pcparser.model.hotline.PowerSupplierHotLine;
import com.pc.pcparser.model.hotline.SsdHotLine;
import com.pc.pcparser.repository.CpuHotLineRepository;
import com.pc.pcparser.repository.GpuHotLineRepository;
import com.pc.pcparser.repository.MemoryHotLineRepository;
import com.pc.pcparser.repository.MotherBoardHotLineRepository;
import com.pc.pcparser.repository.PcConfigRepository;
import com.pc.pcparser.repository.PowerSupplierHotLineRepository;
import com.pc.pcparser.repository.SsdHotLineRepository;
import com.pc.pcparser.service.hotline.HotlineDataUpdateCoordinatorService;
import com.pc.pcparser.service.userbenchmark.CpuUserBenchmarkService;
import com.pc.pcparser.service.userbenchmark.GpuUserBenchmarkService;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class PcConfigService {
    private static final int MIN_PROPOSITION_QUANTITY_DEFAULT = 5;
    private static final double CASE_PRICE_MIN = 15.0;
    private static final double CASE_PRICE_AVG = 25.0;
    private static final double CASE_PRICE_MAX = 50.0;
    @Value("${xlsx.directory.path}")
    private String directoryPath;
    @Value("${xlsx.file.name.prefix}")
    private String filePrefix;
    private final CpuHotLineRepository cpuHotLineRepository;
    private final MotherBoardHotLineRepository motherBoardHotLineRepository;
    private final MemoryHotLineRepository memoryHotLineRepository;
    private final GpuHotLineRepository gpuHotLineRepository;
    private final SsdHotLineRepository ssdHotLineRepository;
    private final PowerSupplierHotLineRepository powerSupplierHotLineRepository;
    private final PcConfigRepository pcConfigRepository;
    private final ExcelExporter excelExporter;
    private final CpuUserBenchmarkService cpuUserBenchmarkService;
    private final GpuUserBenchmarkService gpuUserBenchmarkService;
    private final HotlineDataUpdateCoordinatorService hotlineDataUpdateCoordinatorService;
    private final LogService logService;
    private final PcConfigMapper pcConfigMapper;

    public List<PcConfigDto> getAllPcConfigDto() {
        return pcConfigRepository.findAll()
                .stream()
                .map(pcConfigMapper::toDto)
                .toList();
    }

    public List<PcConfigDto> getAllPcConfigDtoByBestPrice() {
        return pcConfigRepository
                .findAllByMarkerOrderByPredictionPrice(PcMarker.BEST_PRICE)
                .stream()
                .map(pcConfigMapper::toDto)
                .toList();
    }

    public boolean updateDataAndCreatePcList(
            boolean updateUserBenchmarkCpu,
            boolean updateUserBenchmarkGpu,
            boolean updateHotline,
            boolean createPcList,
            boolean saveReportToExel) {
        try {
            log.info("Starting data update process ");
            logService.addLog("Starting data update process ");

            if (updateUserBenchmarkCpu) {
                logService.addLog("update UserBenchmarkCpu started");
                cpuUserBenchmarkService.loadAndSaveNewItems();
                cpuUserBenchmarkService.updateMissingSpecifications();
                logService.addLog("update UserBenchmarkCpu done");
            }

            if (updateUserBenchmarkGpu) {
                logService.addLog("update UserBenchmarkGpu started ");
                gpuUserBenchmarkService.loadAndSaveNewItems();
                logService.addLog("update UserBenchmarkGpu done");
            }

            if (updateHotline) {
                logService.addLog("update Hotline started ");
                hotlineDataUpdateCoordinatorService.updateAllData();
                logService.addLog("update Hotline done ");
            }

            if (createPcList) {
                logService.addLog("create Pc List started ");
                createFilterAndSaveOptimalPcList();
                logService.addLog("create Pc List done ");
            }

            if (saveReportToExel) {
                logService.addLog("save report started ");
                exportToExcelPcList(filePrefix,
                        pcConfigRepository.findPcListWithNonZeroPriceForFpsOrdered().stream()
                                .map(pcConfigMapper::toDto)
                                .toList());
                logService.addLog("save report done ");
            }

            log.info("Data update process completed successfully");
            logService.addLog("Data update process completed successfully");

        } catch (Exception e) {
            log.error("An error occurred during the update process: ", e);
            return false;
        }

        return true;
    }

    public void exportToExcelPcList(String fileName, List<PcConfigDto> pcConfigList) {
        log.info("Start save file to Excel");
        logService.addLog("Start save file to Excel");
        long executionTime = measureExecutionTime(() -> {
            if (!pcConfigList.isEmpty()) {
                String customPath = directoryPath;
                File directory = new File(customPath);
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm");
                String formattedDate = now.format(formatter);
                String fullFileName = fileName + " " + formattedDate + ".xlsx";
                String fullPath = Paths.get(customPath, fullFileName).toString();
                clearFilesDirectory();
                excelExporter.exportToExcelPcConfiguration(pcConfigList, fullPath);
                log.info("Export of PC list to Excel completed. File saved at: {}", fullPath);
            } else {
                log.warn("PC list is empty, export was not performed.");
            }
        });
        log.info("Excel export execution time: {} ms", executionTime);
    }

    private void clearFilesDirectory() {
        File directory = new File(directoryPath);

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        if (file.delete()) {
                            log.info("Deleted file: {}", file.getName());
                        } else {
                            log.warn("Failed to delete file: {}", file.getName());
                        }
                    }
                }
            }
        } else {
            log.warn("Directory does not exist or is not a directory: {}",
                    directoryPath);
        }
    }

    private void createFilterAndSaveOptimalPcList() {
        log.info("Start creating and filtering PC list");
        logService.addLog("Start creating and filtering PC list");
        try {
            pcConfigRepository.deleteAll();
            List<PcConfig> allPcListConfig = pcConfigRepository.saveAll(createPc());
            List<PcConfig> optimalPcListConfig = removeItemsWithUncompetitivePrice(allPcListConfig);
            insertMarker(optimalPcListConfig, PcMarker.BEST_PRICE);
            pcConfigRepository.saveAll(optimalPcListConfig);
        } catch (Exception e) {
            log.error("Error occurred during the process: ", e);
        }
        log.info("Finished creating and saving optimal PC list");
        logService.addLog("Finished creating and saving optimal PC list");
    }

    private void insertMarker(List<PcConfig> pcConfigList, PcMarker marker) {
        pcConfigList.stream()
                .peek(pcConfig -> pcConfig.setMarker(marker))
                .collect(Collectors.toList());
    }

    private List<PcConfig> createPc() {
        pcConfigRepository.deleteAll();
        List<CpuHotLine> cpus = cpuHotLineRepository
                .findCpusWithMinPropositions(
                        MIN_PROPOSITION_QUANTITY_DEFAULT);
        List<MotherBoardHotLine> motherBoards
                = motherBoardHotLineRepository
                .findMinPriceGroupedByChipsetWithConditions(MIN_PROPOSITION_QUANTITY_DEFAULT);
        List<MemoryHotLine> memories = memoryHotLineRepository
                .findMinPriceGroupedByTypeWithConditions(MIN_PROPOSITION_QUANTITY_DEFAULT);
        List<GpuHotLine> gpus = gpuHotLineRepository
                .findGroupedGpusByMinAvgPrice(MIN_PROPOSITION_QUANTITY_DEFAULT);
        SsdHotLine ssdFromDb = ssdHotLineRepository
                .findTopByAvgPrice(MIN_PROPOSITION_QUANTITY_DEFAULT);
        List<PowerSupplierHotLine> powerSupplierHotLines
                = powerSupplierHotLineRepository.findGroupedByPowerWithMinAvgPrice(
                MIN_PROPOSITION_QUANTITY_DEFAULT);

        validateData(cpus, motherBoards, memories, gpus, ssdFromDb, powerSupplierHotLines);

        List<PcConfig> pcConfigs = new ArrayList<>();

        cpus.forEach(cpu -> {
            Optional<MotherBoardHotLine> motherboardOpt
                    = getMotherboardFromCpuBySocket(motherBoards, cpu);
            if (motherboardOpt.isPresent()) {
                MotherBoardHotLine motherboard = motherboardOpt.get();
                MemoryHotLine memory = getMemoryFromMotherBoardBySocketType(
                        memories, motherboard);
                pcConfigs.addAll(video(
                        gpus, cpu, motherboard, memory, ssdFromDb, powerSupplierHotLines));
            }
        });
        log.info("Computer configurations were successfully assembled");
        logService.addLog("Computer configurations were successfully assembled");
        return filterPc(pcConfigs);
    }

    private List<PcConfig> removeItemsWithUncompetitivePrice(List<PcConfig> pcConfigList) {
        boolean process = true;

        while (process) {
            process = false;

            for (int i = 0; i < pcConfigList.size() - 1; i++) {
                if (pcConfigList.get(i).getPrice().compareTo(pcConfigList.get(i + 1).getPrice()) > 0) {
                    pcConfigList.get(i).setPrice(BigDecimal.ZERO);
                    process = true;
                }
            }
            pcConfigList.removeIf(pcConfig -> pcConfig.getPrice().compareTo(BigDecimal.ZERO) == 0);
        }
        return pcConfigList;
    }

    private List<PcConfig> filterPc(List<PcConfig> pcConfigList) {
        return pcConfigList.stream()
                .sorted(
                        Comparator
                                .comparing(PcConfig::getPredictionGpuFpsFhd,
                                        Comparator.nullsLast(Comparator.naturalOrder()))
                                .thenComparing(PcConfig::getPrice, Comparator.nullsLast(
                                        Comparator.naturalOrder()))
                )
                .collect(Collectors.toList());
    }

    private void validateData(
            List<CpuHotLine> cpus,
            List<MotherBoardHotLine> motherBoards,
            List<MemoryHotLine> memories,
            List<GpuHotLine> gpus,
            SsdHotLine ssdFromDb,
            List<PowerSupplierHotLine> powerSupplierHotLines) {
        if (cpus.isEmpty() || motherBoards.isEmpty()
                || memories.isEmpty() || gpus.isEmpty()
                || ssdFromDb == null || powerSupplierHotLines.isEmpty()) {
            throw new IllegalArgumentException("One or more required data lists are empty.");
        }
    }

    private List<PcConfig> video(List<GpuHotLine> gpus,
                                 CpuHotLine cpu,
                                 MotherBoardHotLine mb,
                                 MemoryHotLine memory,
                                 SsdHotLine ssd,
                                 List<PowerSupplierHotLine> powerSupplierHotLines) {
        List<PcConfig> pcConfigs = new ArrayList<>();

        gpus.forEach(gpu -> {
            PcConfig pcConfig = new PcConfig();
            initializePc(pcConfig, gpu, cpu, mb, memory, ssd, powerSupplierHotLines);
            pcConfigs.add(pcConfig);
        });

        return pcConfigs;
    }

    private void initializePc(PcConfig pcConfig, GpuHotLine gpu,
                              CpuHotLine cpu,
                              MotherBoardHotLine mb,
                              MemoryHotLine memory,
                              SsdHotLine ssd,
                              List<PowerSupplierHotLine> powerSupplierHotLines) {
        pcConfig.setGpu(gpu);
        pcConfig.setCpu(cpu);
        pcConfig.setMotherboard(mb);
        pcConfig.setMemory(memory);
        pcConfig.setSsd(ssd);
        pcConfig.setPowerSupplier(getPowerSupply(gpu, powerSupplierHotLines));
        pcConfig.setPrice(BigDecimal.valueOf(calculatePrice(pcConfig)));
        pcConfig.setAvgGpuBench(gpu.getUserBenchmarkGpu().getAvgBench());
        pcConfig.setDesktopScore(cpu.getUserBenchmarkCpu().getDesktopScore());
        pcConfig.setGamingScore(cpu.getUserBenchmarkCpu().getGamingScore());
        pcConfig.setWorkstationScore(cpu.getUserBenchmarkCpu().getWorkstationScore());
        pcConfig.setPredictionGpuFpsFhd(
                calculationPredictionGpuFpsHd(
                        cpu.getUserBenchmarkCpu().getGamingScore(),
                        gpu.getUserBenchmarkGpu().getAvgBench()
                )
        );
        pcConfig.setPriceForFps(calculatePriceForFpc(pcConfig));
    }

    private double calculatePrice(PcConfig pcConfig) {
        Double avgPriceCpu = pcConfig.getCpu().getAvgPrice();
        double coolingPrice = calculateCoolingPrice(pcConfig.getCpu());
        avgPriceCpu += calculateCasePrice(pcConfig.getCpu());

        Double avgPriceMb = getSafeAvgPrice(pcConfig.getMotherboard().getAvgPrice());
        Double avgPriceMemory = getSafeAvgPrice(pcConfig.getMemory().getAvgPrice());
        Double avgPriceGpu = getSafeAvgPrice(pcConfig.getGpu().getAvgPrice());
        Double avgPriceSsd = getSafeAvgPrice(pcConfig.getSsd().getAvgPrice());
        Double avgPricePs = getSafeAvgPrice(pcConfig.getPowerSupplier().getAvgPrice());

        return avgPriceCpu + coolingPrice + avgPriceMb
                + avgPriceMemory + avgPriceGpu + avgPriceSsd + avgPricePs;
    }

    private double calculateCoolingPrice(CpuHotLine cpu) {
        if (cpu.getPackageType() != null && cpu.getPackageType().contains("Tray")) {
            if (cpu.getName().contains("i9") || cpu.getName().contains("Ryzen 9")) {
                return 70;
            } else if (cpu.getName().contains("i7") || cpu.getName().contains("Ryzen 7")) {
                return 40;
            } else if (cpu.getName().contains("i5") || cpu.getName().contains("Ryzen 5")) {
                return 20;
            }
        }
        if (cpu.getPackageType() != null && cpu.getPackageType().contains("Box")) {
            if (cpu.getName().contains("Ryzen 9") && cpu.getName().contains("X")) {
                return 70;
            } else if (cpu.getName().contains("Ryzen 7 ") && cpu.getName().contains("X")) {
                return 40;
            } else if (cpu.getName().contains("Ryzen 5 ") && cpu.getName().contains("X")) {
                return 30;
            } else if (cpu.getName().contains("i9") && cpu.getName().contains("K")) {
                return 70;
            } else if (cpu.getName().contains("i7") && cpu.getName().contains("K")) {
                return 40;
            } else if (cpu.getName().contains("i5") && cpu.getName().contains("K")) {
                return 30;
            }
        }
        return 0;
    }

    private double calculateCasePrice(CpuHotLine cpu) {
        if (cpu.getPackageType() != null && cpu.getPackageType().contains("Tray")) {
            if (cpu.getName().contains("i9") || cpu.getName().contains("Ryzen 9")) {
                return CASE_PRICE_MAX;
            } else if (cpu.getName().contains("i7") || cpu.getName().contains("Ryzen 7")) {
                return CASE_PRICE_AVG;
            }
        }
        if (cpu.getPackageType() != null && cpu.getPackageType().contains("Box")) {
            if ((cpu.getName().contains("Ryzen 9") && cpu.getName().contains("X"))
                    || (cpu.getName().contains("i9") && cpu.getName().contains("K"))) {
                return CASE_PRICE_MAX;
            } else if ((cpu.getName().contains("Ryzen 7") && cpu.getName().contains("X"))
                    || (cpu.getName().contains("i7") && cpu.getName().contains("K"))) {
                return CASE_PRICE_AVG;
            } else if ((cpu.getName().contains("Ryzen 5") && cpu.getName().contains("X"))
                    || (cpu.getName().contains("i5") && cpu.getName().contains("K"))) {
                return CASE_PRICE_AVG;
            }
        }
        return CASE_PRICE_MIN;
    }

    private double getSafeAvgPrice(Double price) {
        return price != null ? price : 0;
    }

    private int calculationPredictionGpuFpsHd(double cpuGamingScore, double avgGpuBench) {
        return (int) (cpuGamingScore * avgGpuBench / 100);
    }

    private PowerSupplierHotLine getPowerSupply(
            GpuHotLine gpu, List<PowerSupplierHotLine> powerSupplierHotLines) {
        return powerSupplierHotLines.stream()
                .sorted(Comparator.comparingInt(PowerSupplierHotLine::getPower))
                .filter(p -> p.getPower() >= gpu.getUserBenchmarkGpu().getPowerRequirement())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No suitable power supply found for GPU: " + gpu.getName()));
    }

    private MemoryHotLine getMemoryFromMotherBoardBySocketType(
            List<MemoryHotLine> memoryHotLineList, MotherBoardHotLine motherBoardHotLine) {
        final String memoryType = motherBoardHotLine.getMemoryType();

        if (memoryType == null) {
            throw new IllegalArgumentException("Motherboard memory type is null.");
        }

        return memoryHotLineList.stream()
                .filter(m -> m.getType().contains(memoryType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No memory found for type: " + memoryType));
    }

    private Optional<MotherBoardHotLine> getMotherboardFromCpuBySocket(
            List<MotherBoardHotLine> motherBoards, CpuHotLine cpu) {
        int propositionQuantityThreshold = MIN_PROPOSITION_QUANTITY_DEFAULT;

        while (propositionQuantityThreshold >= 2) {
            List<MotherBoardHotLine> mbList = filterMotherboardsByCpu(motherBoards, cpu);

            if (!mbList.isEmpty()) {
                return mbList.stream()
                        .sorted(Comparator.comparing(
                                MotherBoardHotLine::getAvgPrice, Comparator.nullsLast(
                                        Comparator.naturalOrder())))
                        .findFirst();
            }

            propositionQuantityThreshold--;
            motherBoards = updateMotherBoardsList(propositionQuantityThreshold);
        }

        return Optional.empty();
    }

    private List<MotherBoardHotLine> filterMotherboardsByCpu(
            List<MotherBoardHotLine> motherBoards, CpuHotLine cpu) {
        return motherBoards.stream()
                .filter(mb -> mb.getSocketType().contains(cpu.getSocketType()))
                .filter(mb -> {
                    if (isIntelCpu(cpu) && cpuContaini5ori7ori9(cpu)
                            && cpu.getName().contains("K")) {
                        return mb.getChipset().charAt(0) == 'Z';
                    } else if (isAmdCpu(cpu) && cpuContainRyzen5or7or9(cpu)
                            && cpu.getName().contains("X")) {
                        return mb.getChipset().charAt(0) == 'X';
                    } else if (cpuContainRyzen5or7or9(cpu) || cpuContaini5ori7ori9(cpu)) {
                        return mb.getChipset().charAt(0) == 'B';
                    } else {
                        return true;
                    }
                })
                .collect(Collectors.toList());
    }

    private boolean isIntelCpu(CpuHotLine cpu) {
        return cpu.getManufacturer() != null && cpu.getManufacturer().contains("Intel");
    }

    private boolean isAmdCpu(CpuHotLine cpu) {
        return cpu.getManufacturer() != null && cpu.getManufacturer().contains("AMD");
    }

    private boolean cpuContainRyzen5or7or9(CpuHotLine cpu) {
        return cpu.getName().contains("Ryzen 5")
                || cpu.getName().contains("Ryzen 7")
                || cpu.getName().contains("Ryzen 9");
    }

    private boolean cpuContaini5ori7ori9(CpuHotLine cpu) {
        return cpu.getName().contains("Core i5")
                || cpu.getName().contains("Core i7")
                || cpu.getName().contains("Core i9");
    }

    private List<MotherBoardHotLine> updateMotherBoardsList(
            int propositionQuantityThreshold) {
        return motherBoardHotLineRepository
                .findMinPriceGroupedByChipsetWithConditions(propositionQuantityThreshold);
    }

    public Integer calculatePriceForFpc(PcConfig pcConfig) {
        BigDecimal someValue = pcConfig.getPrice();
        BigDecimal denominator = BigDecimal.valueOf(pcConfig.getPredictionGpuFpsFhd());
        if (denominator.compareTo(BigDecimal.ZERO) != 0) {
            return someValue.divide(denominator, RoundingMode.HALF_UP).intValue();
        } else {
            return 0;
        }
    }

    private long measureExecutionTime(Runnable task) {
        long start = System.currentTimeMillis();
        task.run();
        long end = System.currentTimeMillis();
        return end - start;
    }

}
