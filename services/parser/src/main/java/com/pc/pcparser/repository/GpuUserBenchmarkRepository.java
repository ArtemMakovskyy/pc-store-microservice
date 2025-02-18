package com.pc.pcparser.repository;

import com.pc.pcparser.model.user.benchmark.UserBenchmarkGpu;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GpuUserBenchmarkRepository extends JpaRepository<UserBenchmarkGpu,Long> {
    @Query(value = "SELECT * FROM gpus_user_benchmark ORDER BY CHAR_LENGTH(model) DESC",
            nativeQuery = true)
    List<UserBenchmarkGpu> findAllOrderByModelLengthDesc();

    List<UserBenchmarkGpu> findAllByPowerRequirementIsNull();
}
