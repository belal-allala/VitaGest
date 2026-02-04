package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class VenteDTO {
    private Long id;
    private LocalDateTime date;
    private Double total;
    private String mode;
    private Long clientId;
    private Long vendeurId;
    private List<VenteLigneDTO> lignes;
}
