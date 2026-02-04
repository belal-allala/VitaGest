package com.example.demo.mapper;

import com.example.demo.dto.VenteDTO;
import com.example.demo.entity.Vente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {VenteLigneMapper.class})
public interface VenteMapper {
    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "vendeur.id", target = "vendeurId")
    VenteDTO toDTO(Vente vente);
    
    @Mapping(source = "clientId", target = "client.id")
    @Mapping(source = "vendeurId", target = "vendeur.id")
    Vente toEntity(VenteDTO venteDTO);
}
