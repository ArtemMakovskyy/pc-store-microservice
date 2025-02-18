package com.pc.pcparser.dto.hotline;

import lombok.Data;

@Data
public class MotherBoardHotLineParserDto {
    private String name;
    private String url;
    private String prices;
    private Double avgPrice;
    private Integer propositionsQuantity;
    private String manufacturer;
    private String socketType;
    private String chipset;
    private String chipsetManufacturer;
    private String memoryType;
    private String caseType;

}
