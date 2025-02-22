package com.pc.product.dto.mapper;

import com.pc.product.config.MapperConfig;
import com.pc.product.dto.CreateMouseDto;
import com.pc.product.dto.MouseDto;
import com.pc.product.model.Mouse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(config = MapperConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MouseMapper {
    MouseDto toDto(Mouse mouse);

    Mouse toEntity(CreateMouseDto dto);

}
