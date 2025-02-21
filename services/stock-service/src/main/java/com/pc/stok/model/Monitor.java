package com.pc.stok.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "monitors")
@Getter
@Setter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE monitors SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
public class Monitor extends AbstractProduct {
    private String resolution;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private boolean isDeleted = false;

}
