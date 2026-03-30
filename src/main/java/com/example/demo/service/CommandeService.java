package com.example.demo.service;

import com.example.demo.aop.AuditAction;
import com.example.demo.dto.CommandeDTO;
import com.example.demo.entity.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.CommandeMapper;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommandeService {

    private final CommandeRepository commandeRepository;
    private final LotRepository lotRepository;
    private final FournisseurRepository fournisseurRepository;
    private final CommandeMapper commandeMapper;

    @Transactional
    @AuditAction(action = "CREATION_COMMANDE", entityName = "Commande")
    public CommandeDTO createCommande(CommandeDTO commandeDTO) {
        Commande commande = commandeMapper.toEntity(commandeDTO);
        commande.setDate(LocalDateTime.now());
        commande.setStatut("BROUILLON");

        if (commande.getLignes() != null) {
            commande.getLignes().forEach(ligne -> ligne.setCommande(commande));
        }

        BigDecimal total = (commande.getLignes() == null) ? BigDecimal.ZERO :
                commande.getLignes().stream()
                .map(ligne -> {
                    BigDecimal prix = ligne.getPrixAchat() != null ? ligne.getPrixAchat() : BigDecimal.ZERO;
                    return prix.multiply(BigDecimal.valueOf(ligne.getQuantite()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
        commande.setTotalAmount(total);

        return commandeMapper.toDTO(commandeRepository.save(commande));
    }

    @Transactional
    @AuditAction(action = "VALIDATION_COMMANDE", entityName = "Commande")
    public CommandeDTO validerCommande(Long commandeId) {
        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new ResourceNotFoundException("Commande not found with ID: " + commandeId));
        if (!"BROUILLON".equals(commande.getStatut())) {
            throw new IllegalStateException("Seules les commandes en brouillon peuvent être validées.");
        }
        commande.setStatut("EN_ATTENTE");
        return commandeMapper.toDTO(commandeRepository.save(commande));
    }

    @Transactional
    @AuditAction(action = "RECEPTION_COMMANDE", entityName = "Commande")
    public CommandeDTO recevoirCommande(Long commandeId) {
        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new ResourceNotFoundException("Commande not found with ID: " + commandeId));

        if ("RECUE".equals(commande.getStatut())) {
            throw new IllegalStateException("La commande a déjà été reçue.");
        }

        commande.setStatut("RECUE");

        for (CommandeLigne ligne : commande.getLignes()) {
            Lot newLot = new Lot();
            newLot.setMedicament(ligne.getMedicament());
            newLot.setNumeroLot("CMD-" + commande.getId() + "-" + ligne.getId());
            newLot.setQuantite(ligne.getQuantite());
            newLot.setFabrication(LocalDateTime.now().toLocalDate());
            newLot.setExpiration(ligne.getDateExpiration()); // Use the provided expiration date
            lotRepository.save(newLot);
        }

        updateFournisseurPerformance(commande);

        return commandeMapper.toDTO(commandeRepository.save(commande));
    }

    private void updateFournisseurPerformance(Commande commande) {
        if (commande.getFournisseur() != null) {
            Fournisseur fournisseur = commande.getFournisseur();
            long deliveryDays = ChronoUnit.DAYS.between(commande.getDate(), LocalDateTime.now());
            
            Integer currentDelai = fournisseur.getDelaiMoyen();
            if (currentDelai == null || currentDelai == 0) {
                fournisseur.setDelaiMoyen((int) deliveryDays);
            } else {
                fournisseur.setDelaiMoyen((currentDelai + (int) deliveryDays) / 2);
            }
            fournisseurRepository.save(fournisseur);
        }
    }

    public List<CommandeDTO> getAllCommandes() {
        return commandeRepository.findAll().stream()
                .map(commandeMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CommandeDTO getCommandeById(Long id) {
        return commandeRepository.findById(id)
                .map(commandeMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Commande not found with ID: " + id));
    }

    @Transactional
    @AuditAction(action = "MISE_A_JOUR_COMMANDE", entityName = "Commande")
    public CommandeDTO updateCommande(Long id, CommandeDTO commandeDTO) {
        return commandeRepository.findById(id)
                .map(existingCommande -> {
                    if ("RECUE".equals(existingCommande.getStatut())) {
                        throw new IllegalStateException("Cannot update a received order.");
                    }
                    Commande updatedCommande = commandeMapper.toEntity(commandeDTO);
                    updatedCommande.setId(existingCommande.getId());
                    if (updatedCommande.getLignes() != null) {
                        updatedCommande.getLignes().forEach(ligne -> ligne.setCommande(updatedCommande));
                    }
                    BigDecimal total = (updatedCommande.getLignes() == null) ? BigDecimal.ZERO :
                        updatedCommande.getLignes().stream()
                        .map(ligne -> {
                            BigDecimal prix = ligne.getPrixAchat() != null ? ligne.getPrixAchat() : BigDecimal.ZERO;
                            return prix.multiply(BigDecimal.valueOf(ligne.getQuantite()));
                        })
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .setScale(2, RoundingMode.HALF_UP);
                    updatedCommande.setTotalAmount(total);
                    return commandeMapper.toDTO(commandeRepository.save(updatedCommande));
                })
                .orElseThrow(() -> new ResourceNotFoundException("Commande not found with ID: " + id));
    }

    @Transactional
    @AuditAction(action = "SUPPRESSION_COMMANDE", entityName = "Commande")
    public void deleteCommande(Long id) {
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commande not found with ID: " + id));
        if ("RECUE".equals(commande.getStatut())) {
            throw new IllegalStateException("Cannot delete a received order.");
        }
        commandeRepository.deleteById(id);
    }
}
