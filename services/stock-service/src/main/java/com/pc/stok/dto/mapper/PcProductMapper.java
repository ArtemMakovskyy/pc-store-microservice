package com.pc.stok.dto.mapper;


import com.pc.stok.config.MapperConfig;
import com.pc.stok.dto.PcConfigDto;
import com.pc.stok.model.PcProduct;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(config = MapperConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PcProductMapper {
    @Mapping(target = "costPrice", source = "price")
    PcProduct toEntity(PcConfigDto dto);

    @AfterMapping
    default void setBestPriceAndDeleted(@MappingTarget PcProduct product, PcConfigDto dto) {
        boolean isBestPrice = "BEST_PRICE".equals(dto.getMarker());
        product.setIsBestPrice(isBestPrice);
    }

}
