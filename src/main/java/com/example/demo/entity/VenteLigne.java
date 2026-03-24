package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class VenteLigne {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer quantite;
    private BigDecimal prixUnitaire;
    private BigDecimal remise;

    @ManyToOne
    @JoinColumn(name = "vente_id")
    private Vente vente;

    @ManyToOne
    @JoinColumn(name = "medicament_id")
    private Medicament medicament;
}
