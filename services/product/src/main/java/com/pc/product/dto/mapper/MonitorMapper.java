package com.pc.product.dto.mapper;


import com.pc.product.config.MapperConfig;
import com.pc.product.dto.CreateMonitorDto;
import com.pc.product.dto.MonitorDto;
import com.pc.product.model.Monitor;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(config = MapperConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MonitorMapper {
    MonitorDto toDto(Monitor monitor);
    Monitor toEntity(CreateMonitorDto dto);
}
