package com.pc.pcparser.dto.mapper;

import com.pc.pcparser.config.MapperConfig;
import com.pc.pcparser.model.PcConfig;
import com.pc.pcparser.dto.PcConfigDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Mapper(config = MapperConfig.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {
                CpuUserBenchmarkMapper.class,
                GpuUserBenchmarkMapper.class,
                SsdHotLineMapper.class,
                PowerSupplierHotLineMapper.class,
                MotherBoardHotLineMapper.class,
                MemoryHotLineMapper.class,
                GpuHotLineMapper.class,
                CpuHotLineMapper.class
        }
)
public interface PcConfigMapper {

    @Mapping(target = "partNumber", ignore = true)
    @Mapping(target = "cpu", ignore = true)
    @Mapping(target = "cpuUrl", source = "cpu.url")
    @Mapping(target = "motherboard", ignore = true)
    @Mapping(target = "motherboardUrl", source = "motherboard.url")
    @Mapping(target = "memory", ignore = true)
    @Mapping(target = "memoryUrl", source = "memory.url")
    @Mapping(target = "gpu", ignore = true)
    @Mapping(target = "gpuUrl", source = "gpu.url")
    @Mapping(target = "ssd", ignore = true)
    @Mapping(target = "ssdUrl", source = "ssd.url")
    @Mapping(target = "powerSupplier", ignore = true)
    @Mapping(target = "powerSupplierUrl", source = "powerSupplier.url")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "predictionFps", source = "predictionGpuFpsFhd")
    @Mapping(target = "gamingScore", source = "gamingScore")
    @Mapping(target = "priceForFps", source = "priceForFps")
    @Mapping(target = "marker", source = "marker")
    PcConfigDto toDto(PcConfig pcConfig);

    @AfterMapping
    default void mapPartNumber(PcConfig pcConfig, @MappingTarget PcConfigDto dto) {
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        String partNumberStr = pcConfig.getId() + currentDate;
        dto.setPartNumber(Long.parseLong(partNumberStr));
    }

    @AfterMapping
    default void mapCpu(PcConfig pcConfig, @MappingTarget PcConfigDto dto) {
        if (pcConfig.getCpu() != null) {
            dto.setCpu(pcConfig.getCpu().getManufacturer() + " " + pcConfig.getCpu().getName());
        }
    }

    @AfterMapping
    default void mapMotherboard(PcConfig pcConfig, @MappingTarget PcConfigDto dto) {
        if (pcConfig.getMotherboard() != null) {
            dto.setMotherboard(pcConfig.getMotherboard().getManufacturer() + " " + pcConfig.getMotherboard().getName());
        }
    }

    @AfterMapping
    default void mapMemory(PcConfig pcConfig, @MappingTarget PcConfigDto dto) {
        if (pcConfig.getMemory() != null) {
            dto.setMemory(pcConfig.getMemory().getManufacturer() + " " + pcConfig.getMemory().getName());
        }
    }

    @AfterMapping
    default void mapGpu(PcConfig pcConfig, @MappingTarget PcConfigDto dto) {
        if (pcConfig.getGpu() != null) {
            dto.setGpu(pcConfig.getGpu().getManufacturer() + " " + pcConfig.getGpu().getName());
        }
    }

    @AfterMapping
    default void mapSsd(PcConfig pcConfig, @MappingTarget PcConfigDto dto) {
        if (pcConfig.getSsd() != null) {
            dto.setSsd(pcConfig.getSsd().getManufacturer() + " " + pcConfig.getSsd().getName());
        }
    }

    @AfterMapping
    default void mapPowerSupplier(PcConfig pcConfig, @MappingTarget PcConfigDto dto) {
        if (pcConfig.getPowerSupplier() != null) {
            dto.setPowerSupplier(pcConfig.getPowerSupplier().getManufacturer() + " " + pcConfig.getPowerSupplier().getName());
        }
    }
}
