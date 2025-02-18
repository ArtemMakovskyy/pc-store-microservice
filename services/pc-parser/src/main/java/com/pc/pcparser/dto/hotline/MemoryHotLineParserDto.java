package com.pc.pcparser.dto.hotline;

import lombok.Data;

@Data
public class MemoryHotLineParserDto {
    private String name;
    private String url;
    private String prices;
    private Double avgPrice;
    private Integer propositionsQuantity;
    private String manufacturer;
    private String type;
    private String capacity;
    private String frequency;

}
