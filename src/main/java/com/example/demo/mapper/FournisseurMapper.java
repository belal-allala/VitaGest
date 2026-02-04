package com.example.demo.mapper;

import com.example.demo.dto.FournisseurDTO;
import com.example.demo.entity.Fournisseur;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FournisseurMapper {
    FournisseurDTO toDTO(Fournisseur fournisseur);
    Fournisseur toEntity(FournisseurDTO fournisseurDTO);
}
