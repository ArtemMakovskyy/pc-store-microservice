package com.pc.pcparser.repository;

import com.pc.pcparser.model.hotline.MemoryHotLine;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemoryHotLineRepository extends JpaRepository<MemoryHotLine, Long> {

    /**
     * SELECT
     * m.*
     * FROM
     * parser.memory_hotline m
     * JOIN (
     * SELECT
     * type,
     * MIN(avg_price) AS min_price
     * FROM
     * parser.memory_hotline
     * WHERE
     * propositions_quantity > 5
     * GROUP BY type
     * ) grouped ON m.type = grouped.type AND m.avg_price = grouped.min_price
     * WHERE
     * m.propositions_quantity > 5
     * ORDER BY
     * m.type, m.avg_price;
     */

    @Query(value = """
            SELECT 
                m.* 
            FROM 
                parser.memory_hotline m
            JOIN (
                SELECT 
                    type, 
                    MIN(avg_price) AS min_price
                FROM 
                    parser.memory_hotline
                WHERE 
                    propositions_quantity > :minPropositionQuantity
                GROUP BY 
                    type
            ) grouped ON m.type = grouped.type AND m.avg_price = grouped.min_price
            WHERE 
                m.propositions_quantity > :minPropositionQuantity
            ORDER BY 
                m.type, m.avg_price
            """, nativeQuery = true)
    List<MemoryHotLine> findMinPriceGroupedByTypeWithConditions(
            @Param("minPropositionQuantity") int minPropositionQuantity);

}
