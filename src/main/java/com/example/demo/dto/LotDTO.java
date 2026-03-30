package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class LotDTO {
    private Long id;
    private String lotId;
    private LocalDate fabrication;
    private LocalDate dateExpiration;
    private Integer quantite;
    private Long medicamentId;
    private MedicamentDTO medicament;
}
