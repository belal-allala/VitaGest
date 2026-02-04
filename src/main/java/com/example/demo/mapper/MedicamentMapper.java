package com.example.demo.mapper;

import com.example.demo.dto.MedicamentDTO;
import com.example.demo.entity.Medicament;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MedicamentMapper {
    MedicamentDTO toDTO(Medicament medicament);
    Medicament toEntity(MedicamentDTO medicamentDTO);
}
