package com.pc.pcparser.dto.mapper;

import com.pc.pcparser.config.MapperConfig;
import com.pc.pcparser.dto.hotline.SsdHotLineParserDto;
import com.pc.pcparser.model.hotline.SsdHotLine;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(config = MapperConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SsdHotLineMapper {
    SsdHotLine toEntity(SsdHotLineParserDto dto);
}
