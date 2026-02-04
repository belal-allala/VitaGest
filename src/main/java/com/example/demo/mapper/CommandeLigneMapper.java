package com.example.demo.mapper;

import com.example.demo.dto.CommandeLigneDTO;
import com.example.demo.entity.CommandeLigne;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommandeLigneMapper {
    @Mapping(source = "medicament.id", target = "medicamentId")
    CommandeLigneDTO toDTO(CommandeLigne commandeLigne);
    
    @Mapping(source = "medicamentId", target = "medicament.id")
    CommandeLigne toEntity(CommandeLigneDTO commandeLigneDTO);
}
