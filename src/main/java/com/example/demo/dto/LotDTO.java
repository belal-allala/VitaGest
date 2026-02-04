package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class LotDTO {
    private Long id;
    private String numeroLot;
    private LocalDate fabrication;
    private LocalDate expiration;
    private Integer quantite;
}
