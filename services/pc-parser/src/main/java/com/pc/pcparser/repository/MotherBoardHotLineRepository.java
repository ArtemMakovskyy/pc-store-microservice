package com.pc.pcparser.repository;

import com.pc.pcparser.model.hotline.MotherBoardHotLine;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MotherBoardHotLineRepository extends JpaRepository<MotherBoardHotLine, Long> {

    /**
     * SELECT
     * m.*
     * FROM
     * parser.mother_boards_hotline m
     * JOIN (
     * SELECT
     * chipset,
     * MIN(avg_price) AS min_price
     * FROM
     * parser.mother_boards_hotline
     * WHERE
     * prices != 0 AND chipset IS NOT NULL and propositions_quantity >
     * 5 and socket_type is not null and case_type is not null
     * GROUP BY
     * chipset
     * ) grouped ON m.chipset = grouped.chipset AND m.avg_price = grouped.min_price
     * WHERE
     * m.prices != 0 AND m.chipset IS NOT NULL
     * ORDER BY
     * m.chipset, m.avg_price;
     */
    @Query(value = """
            SELECT 
                m.* 
            FROM 
                parser.mother_boards_hotline m
            JOIN (
                SELECT 
                    chipset, 
                    MIN(avg_price) AS min_price
                FROM 
                    parser.mother_boards_hotline
                WHERE 
                    prices != 0 AND chipset IS NOT NULL 
                    AND propositions_quantity > :minPropositionQuantity 
                    AND socket_type IS NOT NULL 
                    AND case_type IS NOT NULL
                GROUP BY 
                    chipset
            ) grouped ON m.chipset = grouped.chipset AND m.avg_price = grouped.min_price
            WHERE 
                m.prices != 0 AND m.chipset IS NOT NULL
            ORDER BY 
                m.chipset, m.avg_price
            """, nativeQuery = true)
    List<MotherBoardHotLine> findMinPriceGroupedByChipsetWithConditions(
            @Param("minPropositionQuantity") int minPropositionQuantity);

}
