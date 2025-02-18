package com.pc.pcparser.model;

import com.pc.pcparser.model.hotline.CpuHotLine;
import com.pc.pcparser.model.hotline.GpuHotLine;
import com.pc.pcparser.model.hotline.MemoryHotLine;
import com.pc.pcparser.model.hotline.MotherBoardHotLine;
import com.pc.pcparser.model.hotline.PowerSupplierHotLine;
import com.pc.pcparser.model.hotline.SsdHotLine;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "pc")
@EqualsAndHashCode
public class Pc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private CpuHotLine cpu;
    @ManyToOne
    private MotherBoardHotLine motherboard;
    @ManyToOne
    private MemoryHotLine memory;
    @ManyToOne
    private GpuHotLine gpu;
    @ManyToOne
    private SsdHotLine ssd;
    @ManyToOne
    private PowerSupplierHotLine powerSupplier;
    private BigDecimal price;
    private Double desktopScore;
    private Double gamingScore;
    private Double workstationScore;
    private Double avgGpuBench;
    @Column(name = "prediction_gpu_fpsfhd")
    private Integer predictionGpuFpsFhd;
    private Integer priceForFps;
    @Enumerated(EnumType.STRING)
    private PcMarker marker;

    @Override
    public String toString() {
        return "Pc{"
                + "id=" + id
                + " | " + cpu.getName()
                + " | " + motherboard.getManufacturer() + " " + motherboard.getName()
                + " | " + memory.getManufacturer() + " " + memory.getName()
                + " | " + gpu.getManufacturer() + " " + gpu.getName() + " " + gpu.getMemorySize()
                + " | " + ssd.getManufacturer() + " " + ssd.getName()
                + " | " + powerSupplier.getManufacturer() + " " + powerSupplier.getName()
                + " " + powerSupplier.getPower() + "W"
                + " | avgGpuBench: " + avgGpuBench
                + " | gamingScore: " + gamingScore
                + " | predictionFpsFHD: " + predictionGpuFpsFhd + ",  priceForFps: " + priceForFps
                + " | " + price
                + '}';
    }
}
