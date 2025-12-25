package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Lot {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String numeroLot;
    private LocalDate fabrication;
    private LocalDate expiration;
    private Integer quantite;

    @ManyToOne
    @JoinColumn(name = "medicament_id")
    private Medicament medicament;
}
