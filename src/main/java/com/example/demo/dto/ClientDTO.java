package com.example.demo.dto;

import lombok.Data;

@Data
public class ClientDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String tel;
    private String allergies;
    private Integer pointsFidelite;
    private Boolean consentRgpd;
}
