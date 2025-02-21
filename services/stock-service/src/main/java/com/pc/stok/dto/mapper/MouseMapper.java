package com.pc.stok.dto.mapper;


import com.pc.stok.config.MapperConfig;
import com.pc.stok.dto.ComputerDto;
import com.pc.stok.dto.CreateMonitorDto;
import com.pc.stok.dto.CreateMouseDto;
import com.pc.stok.dto.MouseDto;
import com.pc.stok.dto.PcConfigDto;
import com.pc.stok.model.Computer;
import com.pc.stok.model.Monitor;
import com.pc.stok.model.Mouse;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(config = MapperConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MouseMapper {
    MouseDto toDto(Mouse mouse);

    Mouse toEntity(CreateMouseDto dto);

}
