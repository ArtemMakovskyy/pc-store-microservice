package com.pc.pcparser.dto.mapper;

import com.pc.pcparser.config.MapperConfig;
import com.pc.pcparser.dto.hotline.GpuHotLineParserDto;
import com.pc.pcparser.model.hotline.GpuHotLine;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(config = MapperConfig.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = GpuUserBenchmarkMapper.class)
public interface GpuHotLineMapper {
    GpuHotLine toEntity(GpuHotLineParserDto dto);
}
