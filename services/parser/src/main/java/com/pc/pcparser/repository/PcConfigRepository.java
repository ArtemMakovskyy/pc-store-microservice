package com.pc.pcparser.repository;

import com.pc.pcparser.model.PcConfig;
import com.pc.pcparser.model.PcMarker;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PcConfigRepository extends JpaRepository<PcConfig, Long> {

    @Query("SELECT p FROM PcConfig p WHERE p.marker = :marker ORDER BY p.predictionGpuFpsFhd, p.price")
    List<PcConfig> findAllByMarkerOrderByPredictionPrice(@Param("marker") PcMarker marker);

    @Query("SELECT p FROM PcConfig p WHERE p.priceForFps != 0 "
            + " ORDER BY p.priceForFps, p.predictionGpuFpsFhd, p.price")
    List<PcConfig> findPcListWithNonZeroPriceForFpsOrdered();

}
