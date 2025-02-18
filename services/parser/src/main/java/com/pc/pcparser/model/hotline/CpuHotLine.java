package com.pc.pcparser.model.hotline;

import com.pc.pcparser.model.user.benchmark.UserBenchmarkCpu;
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
@Table(name = "cpus_hotline")
@ToString
public class CpuHotLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String manufacturer;
    private String name;
    private String url;
    private String prices;
    private Double avgPrice;
    private String socketType;
    private String frequency;
    private String l3Cache;
    private String cores;
    private String threads;
    private String packageType;
    private Integer propositionsQuantity;
    @ManyToOne
    private UserBenchmarkCpu userBenchmarkCpu;

}
