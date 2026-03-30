package com.example.demo.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VenteDTO {
    private Long id;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateVente;
    private BigDecimal total;
    
    @NotNull(message = "Le mode de paiement est obligatoire")
    private String mode;
    
    private Long clientId;
    private ClientDTO client;
    private Long vendeurId;
    private String vendeurName;
    
    @NotEmpty(message = "Une vente doit contenir au moins une ligne")
    @Valid
    private List<VenteLigneDTO> lignes;
}
