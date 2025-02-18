package com.pc.pcparser.dto.mapper;

import com.pc.pcparser.config.MapperConfig;
import com.pc.pcparser.dto.hotline.CpuHotLineParserDto;
import com.pc.pcparser.model.hotline.CpuHotLine;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(config = MapperConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = CpuUserBenchmarkMapper.class
)
public interface CpuHotLineMapper {
    CpuHotLine toEntity(CpuHotLineParserDto dto);

    CpuHotLineParserDto toDto(CpuHotLine cpu);
}
