package com.example.demo.dto;

import lombok.Data;

@Data
public class FournisseurDTO {
    private Long id;
    private String nom;
    private String email;
    private String tel;
    private Integer delaiMoyen;
    private Double note;
}
