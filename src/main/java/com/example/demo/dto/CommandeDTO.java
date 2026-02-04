package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommandeDTO {
    private Long id;
    private LocalDateTime date;
    private String statut;
    private Long fournisseurId;
    private List<CommandeLigneDTO> lignes;
}
