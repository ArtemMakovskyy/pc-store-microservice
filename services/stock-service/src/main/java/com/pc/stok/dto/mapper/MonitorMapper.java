package com.pc.stok.dto.mapper;


import com.pc.stok.config.MapperConfig;
import com.pc.stok.dto.CreateMonitorDto;
import com.pc.stok.dto.MonitorDto;
import com.pc.stok.dto.MouseDto;
import com.pc.stok.model.Monitor;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(config = MapperConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MonitorMapper {
    MonitorDto toDto(Monitor monitor);
    Monitor toEntity(CreateMonitorDto dto);
}
