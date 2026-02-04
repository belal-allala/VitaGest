package com.example.demo.dto;

import lombok.Data;

@Data
public class CommandeLigneDTO {
    private Long id;
    private Integer quantite;
    private Double prixAchat;
    private Long medicamentId;
}
