package com.pc.pcparser.dto.mapper;

import com.pc.pcparser.config.MapperConfig;
import com.pc.pcparser.dto.hotline.PowerSupplierHotLineParserDto;
import com.pc.pcparser.model.hotline.PowerSupplierHotLine;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(config = MapperConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PowerSupplierHotLineMapper {
    PowerSupplierHotLine toEntity(PowerSupplierHotLineParserDto dto);
}
