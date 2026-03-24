package com.example.demo.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CommandeLigneDTO {
    private Long id;
    
    @NotNull(message = "La quantité est obligatoire")
    @Positive(message = "La quantité doit être positive")
    private Integer quantite;
    
    @NotNull(message = "Le prix d'achat est obligatoire")
    @Positive(message = "Le prix d'achat doit être positif")
    private BigDecimal prixAchat;

    @NotNull(message = "La date d'expiration est obligatoire")
    @Future(message = "La date d'expiration doit être dans le futur")
    private LocalDate dateExpiration;
    
    @NotNull(message = "L'ID du médicament est obligatoire")
    private Long medicamentId;
}
