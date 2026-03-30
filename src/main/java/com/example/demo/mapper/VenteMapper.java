package com.example.demo.mapper;

import com.example.demo.dto.VenteDTO;
import com.example.demo.entity.Vente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {VenteLigneMapper.class})
public interface VenteMapper {
    @Mapping(source = "vendeur.id", target = "vendeurId")
    @Mapping(source = "vendeur.nomComplet", target = "vendeurName")
    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "client", target = "client")
    @Mapping(source = "date", target = "dateVente")
    @Mapping(source = "total", target = "total")
    VenteDTO toDTO(Vente vente);
    
    @Mapping(source = "vendeurId", target = "vendeur.id")
    @Mapping(source = "clientId", target = "client.id")
    @Mapping(source = "dateVente", target = "date")
    Vente toEntity(VenteDTO venteDTO);
}
