package com.example.demo.mapper;

import com.example.demo.dto.VenteLigneDTO;
import com.example.demo.entity.VenteLigne;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VenteLigneMapper {
    @Mapping(source = "medicament.id", target = "medicamentId")
    @Mapping(source = "medicament", target = "medicament")
    VenteLigneDTO toDTO(VenteLigne venteLigne);
    
    @Mapping(source = "medicamentId", target = "medicament.id")
    VenteLigne toEntity(VenteLigneDTO venteLigneDTO);
}
