package com.pc.pcparser.model.hotline;

import com.pc.pcparser.model.user.benchmark.UserBenchmarkGpu;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Table(name = "gpus_hotline")
@ToString
public class GpuHotLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String url;
    private String manufacturer;
    private String memoryType;
    private String memorySize;
    private String shina;
    private String port;
    private String prices;
    private Double avgPrice;
    private Integer propositionsQuantity;
    @ManyToOne
    private UserBenchmarkGpu userBenchmarkGpu;

}
