package com.pc.pcparser.model.hotline;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Table(name = "ssd_hotline")
@ToString
public class SsdHotLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String url;
    private String prices;
    private Double avgPrice;
    private Integer propositionsQuantity;
    private String manufacturer;
    private String type;
    private String capacity;
    private String readingSpeed;
    private String writingSpeed;

}
