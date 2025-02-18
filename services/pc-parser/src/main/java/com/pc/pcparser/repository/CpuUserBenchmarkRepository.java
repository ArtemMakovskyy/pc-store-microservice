package com.pc.pcparser.repository;

import com.pc.pcparser.model.user.benchmark.UserBenchmarkCpu;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CpuUserBenchmarkRepository extends JpaRepository<UserBenchmarkCpu, Long> {
    List<UserBenchmarkCpu> findByCpuSpecificationIsNull();

    @Query(value = "SELECT * FROM cpus_user_benchmark ORDER BY CHAR_LENGTH(model) DESC",
            nativeQuery = true)
    List<UserBenchmarkCpu> findAllOrderByModelLengthDesc();
}
