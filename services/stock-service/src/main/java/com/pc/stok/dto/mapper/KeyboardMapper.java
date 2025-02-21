package com.pc.stok.dto.mapper;


import com.pc.stok.config.MapperConfig;
import com.pc.stok.dto.CreateKeyboardDto;
import com.pc.stok.dto.CreateMonitorDto;
import com.pc.stok.dto.KeyboardDto;
import com.pc.stok.dto.MonitorDto;
import com.pc.stok.model.Keyboard;
import com.pc.stok.model.Monitor;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(config = MapperConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface KeyboardMapper {
    KeyboardDto toDto(Keyboard keyboard);

    Keyboard toEntity(CreateKeyboardDto dto);
}
