package com.pc.product.dto.mapper;


import com.pc.product.config.MapperConfig;
import com.pc.product.dto.CreateKeyboardDto;
import com.pc.product.dto.KeyboardDto;
import com.pc.product.model.Keyboard;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(config = MapperConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface KeyboardMapper {
    KeyboardDto toDto(Keyboard keyboard);

    Keyboard toEntity(CreateKeyboardDto dto);
}
