package com.pc.pcparser.repository;

import com.pc.pcparser.model.hotline.PowerSupplierHotLine;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PowerSupplierHotLineRepository extends JpaRepository<PowerSupplierHotLine, Long> {
    /**
     * SELECT *
     * FROM (
     * SELECT *,
     * ROW_NUMBER() OVER (PARTITION BY power ORDER BY avg_price ASC) AS row_num
     * FROM parser.power_supplier_hotline
     * WHERE prices > 0
     * AND propositions_quantity > 5
     * AND type IS NOT NULL
     * ) AS grouped_data
     * WHERE row_num = 1
     * ORDER BY power;
     */

    @Query(value = """
            SELECT *
            FROM (
                SELECT *,
                       ROW_NUMBER() OVER (PARTITION BY power ORDER BY avg_price ASC) AS row_num
                FROM parser.power_supplier_hotline
                WHERE prices > 0 
                  AND propositions_quantity > :minPropositionQuantity 
                  AND type IS NOT NULL
            ) AS grouped_data
            WHERE row_num = 1
            ORDER BY power
            """, nativeQuery = true)
    List<PowerSupplierHotLine> findGroupedByPowerWithMinAvgPrice(
            @Param("minPropositionQuantity") int minPropositionQuantity);
}

