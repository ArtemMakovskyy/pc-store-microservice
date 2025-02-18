package com.pc.pcparser.dto.hotline;

import lombok.Data;

@Data
public class CpuHotLineParserDto {
    private String manufacturer;
    private String name;
    private String url;
    private String prices;
    private Double avgPrice;
    private String socketType;
    private String frequency;
    private String l3Cache;
    private String cores;
    private String threads;
    private String packageType;
    private Integer propositionsQuantity;
    private UserBenchmarkParserCpuDto userBenchmarkParserCpuDto;
}
