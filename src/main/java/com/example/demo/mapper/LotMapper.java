package com.example.demo.mapper;

import com.example.demo.dto.LotDTO;
import com.example.demo.entity.Lot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MedicamentMapper.class})
public interface LotMapper {
    @Mapping(source = "numeroLot", target = "lotId")
    @Mapping(source = "expiration", target = "dateExpiration")
    @Mapping(source = "medicament.id", target = "medicamentId")
    LotDTO toDTO(Lot lot);

    @Mapping(source = "lotId", target = "numeroLot")
    @Mapping(source = "dateExpiration", target = "expiration")
    Lot toEntity(LotDTO lotDTO);
}
