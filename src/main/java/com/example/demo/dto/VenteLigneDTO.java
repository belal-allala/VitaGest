package com.example.demo.dto;

import lombok.Data;

@Data
public class VenteLigneDTO {
    private Long id;
    private Integer quantite;
    private Double prixUnitaire;
    private Double remise;
    private Long medicamentId;
}
