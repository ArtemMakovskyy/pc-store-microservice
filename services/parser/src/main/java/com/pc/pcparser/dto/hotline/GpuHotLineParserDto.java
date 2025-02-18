package com.pc.pcparser.dto.hotline;

import lombok.Data;

@Data
public class GpuHotLineParserDto {
    private String name;
    private String url;
    private String manufacturer;
    private String memoryType;
    private String memorySize;
    private String shina;
    private String port;
    private String prices;
    private Double avgPrice;
    private Integer propositionsQuantity;
    private UserBenchmarkParserGpuDto userBenchmarkParserGpuDto;

}
