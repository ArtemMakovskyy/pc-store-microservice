package com.pc.pcparser.dto.hotline;

import lombok.Data;

@Data
public class PowerSupplierHotLineParserDto {

    private String name;
    private String url;
    private String prices;
    private Double avgPrice;
    private Integer propositionsQuantity;
    private String manufacturer;
    private String type;
    private Integer power;
    private String standard;
    private String motherboardConnection;
    private String gpuConnection;

}
