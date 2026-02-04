package com.example.demo.dto;

import lombok.Data;

@Data
public class MedicamentDTO {
    private Long id;
    private String dci;
    private String nom;
    private String forme;
    private String dosage;
    private Double prix;
    private String classe;
    private String codeAtc;
    private String vignetteUrl;
}
