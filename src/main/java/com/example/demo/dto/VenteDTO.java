package com.example.demo.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class VenteDTO {
    private Long id;
    private LocalDateTime date;
    private BigDecimal total;
    
    @NotNull(message = "Le mode de paiement est obligatoire")
    private String mode;
    
    private Long clientId;
    private Long vendeurId;
    
    @NotEmpty(message = "Une vente doit contenir au moins une ligne")
    @Valid
    private List<VenteLigneDTO> lignes;
}
