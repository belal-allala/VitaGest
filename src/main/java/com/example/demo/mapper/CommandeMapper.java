package com.example.demo.mapper;

import com.example.demo.dto.CommandeDTO;
import com.example.demo.entity.Commande;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CommandeLigneMapper.class})
public interface CommandeMapper {
    @Mapping(source = "fournisseur.id", target = "fournisseurId")
    CommandeDTO toDTO(Commande commande);
    
    @Mapping(source = "fournisseurId", target = "fournisseur.id")
    Commande toEntity(CommandeDTO commandeDTO);
}
