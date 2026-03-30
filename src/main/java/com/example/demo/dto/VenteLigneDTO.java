package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.example.demo.dto.MedicamentDTO;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VenteLigneDTO {
    private Long id;
    
    @NotNull(message = "La quantité est obligatoire")
    @Positive(message = "La quantité doit être supérieure à 0")
    private Integer quantite;
    
    private BigDecimal prixUnitaire;
    private BigDecimal remise;
    
    @NotNull(message = "L'ID du médicament est obligatoire")
    private Long medicamentId;
    private MedicamentDTO medicament;
}
