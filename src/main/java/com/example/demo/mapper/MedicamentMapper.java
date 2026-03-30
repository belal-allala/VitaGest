package com.example.demo.mapper;

import com.example.demo.dto.MedicamentDTO;
import com.example.demo.entity.Medicament;
import org.mapstruct.Mapper;

import org.mapstruct.Mapping;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MedicamentMapper {
    @Mapping(target = "formeDosage", expression = "java(joinFormeDosage(medicament.getForme(), medicament.getDosage()))")
    MedicamentDTO toDTO(Medicament medicament);

    @Mapping(target = "forme", ignore = true)
    @Mapping(target = "dosage", ignore = true)
    Medicament toEntity(MedicamentDTO medicamentDTO);

    @AfterMapping
    default void splitFormeDosage(MedicamentDTO dto, @MappingTarget Medicament entity) {
        if (dto.getFormeDosage() != null) {
            String fd = dto.getFormeDosage();
            int lastSpace = fd.lastIndexOf(" ");
            if (lastSpace > 0) {
                entity.setForme(fd.substring(0, lastSpace));
                entity.setDosage(fd.substring(lastSpace + 1));
            } else {
                entity.setForme(fd);
            }
        }
    }

    default String joinFormeDosage(String forme, String dosage) {
        if (forme == null) return dosage != null ? dosage : "";
        if (dosage == null) return forme;
        return forme + " " + dosage;
    }
}
