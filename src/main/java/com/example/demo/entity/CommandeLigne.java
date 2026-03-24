package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class CommandeLigne {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer quantite;
    private BigDecimal prixAchat;
    private LocalDate dateExpiration;

    @ManyToOne
    @JoinColumn(name = "commande_id")
    private Commande commande;

    @ManyToOne
    @JoinColumn(name = "medicament_id")
    private Medicament medicament;
}
