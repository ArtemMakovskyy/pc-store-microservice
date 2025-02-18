package com.pc.pcparser.dto.mapper;

import com.pc.pcparser.config.MapperConfig;
import com.pc.pcparser.dto.hotline.MemoryHotLineParserDto;
import com.pc.pcparser.model.hotline.MemoryHotLine;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(config = MapperConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemoryHotLineMapper {
    MemoryHotLine toEntity(MemoryHotLineParserDto dto);
}
