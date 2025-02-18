package com.pc.pcparser.dto.mapper;

import com.pc.pcparser.config.MapperConfig;
import com.pc.pcparser.dto.userbenchmark.CpuUserBenchmarkParserDto;
import com.pc.pcparser.model.user.benchmark.UserBenchmarkCpu;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(config = MapperConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CpuUserBenchmarkMapper {
    UserBenchmarkCpu toEntity(CpuUserBenchmarkParserDto dto);

    CpuUserBenchmarkParserDto toDto(UserBenchmarkCpu cpu);
}
