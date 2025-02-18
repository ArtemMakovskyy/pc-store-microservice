package com.pc.pcparser.model.user.benchmark;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Setter
@Table(name = "gpus_user_benchmark")
@ToString
@SQLDelete(sql = "UPDATE gpus_user_benchmark SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
public class UserBenchmarkGpu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String model;
    private String modelHl;
    private String manufacturer;
    private Double userRating;
    private Double valuePercents;
    private Double avgBench;
    private Double price;
    private String urlOfGpu;
    private Integer powerRequirement;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

}
