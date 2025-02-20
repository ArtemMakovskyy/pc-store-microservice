package com.pc.pcparser.dto.mapper;

import com.pc.pcparser.config.MapperConfig;
import com.pc.pcparser.model.PcConfig;
import com.pc.pcparser.dto.PcConfigDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

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

    @Mapping(target = "partNumber", source = "id")
    @Mapping(target = "cpu", ignore = true)
    @Mapping(target = "cpuUrl", source = "cpu.url")
    @Mapping(target = "motherboard", source = "motherboard.name")
    @Mapping(target = "motherboardUrl", source = "motherboard.url")
    @Mapping(target = "memory", source = "memory.name")
    @Mapping(target = "memoryUrl", source = "memory.url")
    @Mapping(target = "gpu", source = "gpu.name")
    @Mapping(target = "gpuUrl", source = "gpu.url")
    @Mapping(target = "ssd", source = "ssd.name")
    @Mapping(target = "ssdUrl", source = "ssd.url")
    @Mapping(target = "powerSupplier", source = "powerSupplier.name")
    @Mapping(target = "powerSupplierUrl", source = "powerSupplier.url")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "predictionFps", source = "predictionGpuFpsFhd")
    @Mapping(target = "gamingScore", source = "gamingScore")
    @Mapping(target = "priceForFps", source = "priceForFps")
    @Mapping(target = "marker", source = "marker")
    PcConfigDto toDto(PcConfig pcConfig);

    @AfterMapping
    default void mapCpu(PcConfig pcConfig, @MappingTarget PcConfigDto dto) {
        if (pcConfig.getCpu() != null) {
            dto.setCpu(pcConfig.getCpu().getManufacturer() + " " + pcConfig.getCpu().getName());
        }
    }
}
