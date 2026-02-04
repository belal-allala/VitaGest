package com.example.demo.service;

import com.example.demo.dto.VenteDTO;
import com.example.demo.entity.Vente;
import com.example.demo.mapper.VenteMapper;
import com.example.demo.repository.VenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VenteService {

    private final VenteRepository venteRepository;
    private final VenteMapper venteMapper;

    public VenteDTO createVente(VenteDTO venteDTO) {
        Vente vente = venteMapper.toEntity(venteDTO);
        return venteMapper.toDTO(venteRepository.save(vente));
    }

    public List<VenteDTO> getAllVentes() {
        return venteRepository.findAll().stream()
                .map(venteMapper::toDTO)
                .collect(Collectors.toList());
    }

    public VenteDTO getVenteById(Long id) {
        return venteRepository.findById(id)
                .map(venteMapper::toDTO)
                .orElse(null);
    }

    public VenteDTO updateVente(Long id, VenteDTO venteDTO) {
        return venteRepository.findById(id)
                .map(existingVente -> {
                    Vente updatedVente = venteMapper.toEntity(venteDTO);
                    updatedVente.setId(existingVente.getId());
                    return venteMapper.toDTO(venteRepository.save(updatedVente));
                })
                .orElse(null);
    }

    public void deleteVente(Long id) {
        venteRepository.deleteById(id);
    }
}
