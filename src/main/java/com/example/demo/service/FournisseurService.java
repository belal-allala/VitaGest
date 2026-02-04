package com.example.demo.service;

import com.example.demo.dto.FournisseurDTO;
import com.example.demo.entity.Fournisseur;
import com.example.demo.mapper.FournisseurMapper;
import com.example.demo.repository.FournisseurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FournisseurService {

    private final FournisseurRepository fournisseurRepository;
    private final FournisseurMapper fournisseurMapper;

    public FournisseurDTO createFournisseur(FournisseurDTO fournisseurDTO) {
        Fournisseur fournisseur = fournisseurMapper.toEntity(fournisseurDTO);
        return fournisseurMapper.toDTO(fournisseurRepository.save(fournisseur));
    }

    public List<FournisseurDTO> getAllFournisseurs() {
        return fournisseurRepository.findAll().stream()
                .map(fournisseurMapper::toDTO)
                .collect(Collectors.toList());
    }

    public FournisseurDTO getFournisseurById(Long id) {
        return fournisseurRepository.findById(id)
                .map(fournisseurMapper::toDTO)
                .orElse(null);
    }

    public FournisseurDTO updateFournisseur(Long id, FournisseurDTO fournisseurDTO) {
        return fournisseurRepository.findById(id)
                .map(existingFournisseur -> {
                    Fournisseur updatedFournisseur = fournisseurMapper.toEntity(fournisseurDTO);
                    updatedFournisseur.setId(existingFournisseur.getId());
                    return fournisseurMapper.toDTO(fournisseurRepository.save(updatedFournisseur));
                })
                .orElse(null);
    }

    public void deleteFournisseur(Long id) {
        fournisseurRepository.deleteById(id);
    }
}
