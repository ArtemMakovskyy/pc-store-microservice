package com.pc.product.dto.mapper;

import com.pc.product.config.MapperConfig;
import com.pc.product.dto.ComputerDto;
import com.pc.product.dto.CreateComputerDto;
import com.pc.product.dto.PcConfigDto;
import com.pc.product.model.Computer;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(config = MapperConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ComputerMapper {

    ComputerDto toDto(Computer computer);

    @Mapping(target = "partNumber", ignore = true)
    @Mapping(target = "name", source = "marker")
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "costPrice", source = "price")
    @Mapping(target = "sellingPrice", source = "price")
    Computer configToEntity(PcConfigDto dto);

    @AfterMapping
    default void afterMapping(@MappingTarget Computer computer, PcConfigDto dto) {
        computer.setPartNumber(String.valueOf(dto.getPartNumber()));

        String description = String.join(", ",
                dto.getCpu(),
                dto.getMemory(),
                dto.getGpu(),
                dto.getSsd());
        computer.setDescription(description);
    }

    Computer toEntity(CreateComputerDto dto);

}
