package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MedicamentDTO {
    private Long id;
    
    @NotBlank(message = "Le DCI est obligatoire")
    private String dci;
    
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;
    
    private String forme;
    private String dosage;
    
    @NotNull(message = "Le prix est obligatoire")
    @Positive(message = "Le prix doit être positif")
    private BigDecimal prix;

    private String classe;
    private String codeAtc;
    private String vignetteUrl;
}
