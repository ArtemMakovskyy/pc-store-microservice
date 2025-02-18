package com.pc.pcparser.repository;

import com.pc.pcparser.model.hotline.GpuHotLine;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GpuHotLineRepository extends JpaRepository<GpuHotLine, Long> {
    @Query("SELECT g FROM GpuHotLine g WHERE LOWER(g.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<GpuHotLine> findByPartialNameIgnoreCase(@Param("name") String name);

    @Query("SELECT g FROM GpuHotLine g WHERE LOWER(g.name) "
            + " LIKE LOWER(CONCAT('%', :name, '%')) AND g.userBenchmarkGpu IS NULL")
    List<GpuHotLine> findByPartialNameIgnoreCaseAndUserBenchmarkGpuIsNull(
            @Param("name") String name);

    /**
     * SELECT *
     * FROM (
     * SELECT *,
     * ROW_NUMBER() OVER (PARTITION BY name, memory_size ORDER BY avg_price ASC) AS row_num
     * FROM parser.gpus_hotline
     * WHERE propositions_quantity > 1
     * AND user_benchmark_gpu_id IS NOT NULL
     * ) AS grouped_data
     * WHERE row_num = 1
     * ORDER BY name, memory_size;
     */
    @Query(value = "SELECT * "
            + "FROM ( "
            + "    SELECT *, "
            + "           ROW_NUMBER() OVER (PARTITION BY name, memory_size "
            + "  ORDER BY avg_price ASC) AS row_num "
            + "    FROM parser.gpus_hotline "
            + "    WHERE propositions_quantity > :minPropositionQuantity "
            + "    AND user_benchmark_gpu_id IS NOT NULL "
            + ") AS grouped_data "
            + "WHERE row_num = 1 "
            + "ORDER BY name, memory_size", nativeQuery = true)
    List<GpuHotLine> findGroupedGpusByMinAvgPrice(
            @Param("minPropositionQuantity") int minPropositionQuantity);
}
