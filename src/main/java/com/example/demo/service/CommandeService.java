package com.example.demo.service;

import com.example.demo.dto.CommandeDTO;
import com.example.demo.entity.Commande;
import com.example.demo.mapper.CommandeMapper;
import com.example.demo.repository.CommandeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommandeService {

    private final CommandeRepository commandeRepository;
    private final CommandeMapper commandeMapper;

    public CommandeDTO createCommande(CommandeDTO commandeDTO) {
        Commande commande = commandeMapper.toEntity(commandeDTO);
        return commandeMapper.toDTO(commandeRepository.save(commande));
    }

    public List<CommandeDTO> getAllCommandes() {
        return commandeRepository.findAll().stream()
                .map(commandeMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CommandeDTO getCommandeById(Long id) {
        return commandeRepository.findById(id)
                .map(commandeMapper::toDTO)
                .orElse(null);
    }

    public CommandeDTO updateCommande(Long id, CommandeDTO commandeDTO) {
        return commandeRepository.findById(id)
                .map(existingCommande -> {
                    Commande updatedCommande = commandeMapper.toEntity(commandeDTO);
                    updatedCommande.setId(existingCommande.getId());
                    return commandeMapper.toDTO(commandeRepository.save(updatedCommande));
                })
                .orElse(null);
    }

    public void deleteCommande(Long id) {
        commandeRepository.deleteById(id);
    }
}
