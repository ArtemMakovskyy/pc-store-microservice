package com.pc.pcparser.dto.mapper;

import com.pc.pcparser.config.MapperConfig;
import com.pc.pcparser.dto.userbenchmark.GpuUserBenchmarkParserDto;
import com.pc.pcparser.model.user.benchmark.UserBenchmarkGpu;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(config = MapperConfig.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface GpuUserBenchmarkMapper {
    UserBenchmarkGpu toEntity(GpuUserBenchmarkParserDto dto);
}
