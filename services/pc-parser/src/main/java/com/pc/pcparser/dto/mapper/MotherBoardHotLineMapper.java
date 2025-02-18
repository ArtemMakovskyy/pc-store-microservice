package com.pc.pcparser.dto.mapper;

import com.pc.pcparser.config.MapperConfig;
import com.pc.pcparser.dto.hotline.MotherBoardHotLineParserDto;
import com.pc.pcparser.model.hotline.MotherBoardHotLine;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(config = MapperConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MotherBoardHotLineMapper {
    MotherBoardHotLine toEntity(MotherBoardHotLineParserDto dto);
}
