package com.pc.pcparser.dto.hotline;

import lombok.Data;

@Data
public class UserBenchmarkParserGpuDto {
    private String socketType;
    private String frequency;
    private String l3Cache;
    private String cores;
    private String threads;
    private String packageType;
    private String releaseDate;

}
