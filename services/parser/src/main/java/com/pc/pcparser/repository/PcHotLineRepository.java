package com.pc.pcparser.repository;

import com.pc.pcparser.model.Pc;
import com.pc.pcparser.model.PcMarker;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PcHotLineRepository extends JpaRepository<Pc, Long> {

    @Query("SELECT p FROM Pc p WHERE p.marker = :marker ORDER BY p.predictionGpuFpsFhd, p.price")
    List<Pc> findAllByMarkerOrderByPredictionPrice(@Param("marker") PcMarker marker);

    @Query("SELECT p FROM Pc p WHERE p.priceForFps != 0 "
            + " ORDER BY p.priceForFps, p.predictionGpuFpsFhd, p.price")
    List<Pc> findPcListWithNonZeroPriceForFpsOrdered();

}
