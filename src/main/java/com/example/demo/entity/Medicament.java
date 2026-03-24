package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Medicament {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String dci;
    private String nom;
    private String forme;
    private String dosage;
    private BigDecimal prix;
    private String classe;
    private String codeAtc;
    private String vignetteUrl;

    @OneToMany(mappedBy = "medicament", cascade = CascadeType.ALL)
    private List<Lot> lots;
}
