package com.pc.pcparser.dto.userbenchmark;

import lombok.Data;

@Data
public class CpuUserBenchmarkParserDto {
    private String model;
    private String modelHl;
    private String manufacturer;
    private Double userRating;
    private Double valuePercents;
    private Double avgBench;
    private Double memoryPercents;
    private Double price;
    private String urlOfCpu;
    private String partNumber;
    private String cpuSpecification;
    private Double desktopScore;
    private Double gamingScore;
    private Double workstationScore;
    private Integer coresQuantity;
    private Integer threadsQuantity;
}
