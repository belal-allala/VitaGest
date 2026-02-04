package com.example.demo.service;

import com.example.demo.dto.LotDTO;
import com.example.demo.entity.Lot;
import com.example.demo.mapper.LotMapper;
import com.example.demo.repository.LotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LotService {

    private final LotRepository lotRepository;
    private final LotMapper lotMapper;

    public LotDTO createLot(LotDTO lotDTO) {
        Lot lot = lotMapper.toEntity(lotDTO);
        return lotMapper.toDTO(lotRepository.save(lot));
    }

    public List<LotDTO> getAllLots() {
        return lotRepository.findAll().stream()
                .map(lotMapper::toDTO)
                .collect(Collectors.toList());
    }

    public LotDTO getLotById(Long id) {
        return lotRepository.findById(id)
                .map(lotMapper::toDTO)
                .orElse(null);
    }

    public LotDTO updateLot(Long id, LotDTO lotDTO) {
        return lotRepository.findById(id)
                .map(existingLot -> {
                    Lot updatedLot = lotMapper.toEntity(lotDTO);
                    updatedLot.setId(existingLot.getId());
                    return lotMapper.toDTO(lotRepository.save(updatedLot));
                })
                .orElse(null);
    }

    public void deleteLot(Long id) {
        lotRepository.deleteById(id);
    }
}
