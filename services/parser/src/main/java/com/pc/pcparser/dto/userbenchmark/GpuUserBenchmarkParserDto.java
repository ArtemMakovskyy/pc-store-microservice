package com.pc.pcparser.dto.userbenchmark;

import lombok.Data;

@Data
public class GpuUserBenchmarkParserDto {
    private String model;
    private String modelHl;
    private String manufacturer;
    private Double userRating;
    private Double valuePercents;
    private Double avgBench;
    private Double price;
    private String urlOfGpu;
    private Integer powerRequirement;

}
