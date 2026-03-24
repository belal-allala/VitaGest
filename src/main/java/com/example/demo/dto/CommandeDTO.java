package com.example.demo.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommandeDTO {
    private Long id;
    private LocalDateTime date;
    private String statut;
    private BigDecimal totalAmount;
    
    @NotNull(message = "L'ID du fournisseur est obligatoire")
    private Long fournisseurId;
    
    @NotEmpty(message = "Une commande doit contenir au moins une ligne")
    @Valid
    private List<CommandeLigneDTO> lignes;
}
