package com.example.demo.mapper;

import com.example.demo.dto.LotDTO;
import com.example.demo.entity.Lot;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LotMapper {
    LotDTO toDTO(Lot lot);
    Lot toEntity(LotDTO lotDTO);
}
