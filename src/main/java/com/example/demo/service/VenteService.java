package com.example.demo.service;

import com.example.demo.aop.AuditAction;
import com.example.demo.dto.VenteDTO;
import com.example.demo.dto.VenteLigneDTO;
import com.example.demo.entity.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.VenteMapper;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VenteService {

    private final VenteRepository venteRepository;
    private final MedicamentRepository medicamentRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final StockService stockService;
    private final VenteMapper venteMapper;

    @Transactional
    @AuditAction(action = "VALIDATION_VENTE", entityName = "Vente")
    public VenteDTO createVente(VenteDTO venteDTO) {
        Vente vente = new Vente();
        vente.setDate(LocalDateTime.now());
        vente.setMode(venteDTO.getMode());

        if (venteDTO.getClientId() != null) {
            Client client = clientRepository.findById(venteDTO.getClientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Client not found with ID: " + venteDTO.getClientId()));
            vente.setClient(client);
        }

        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User vendeur = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found in database."));
        vente.setVendeur(vendeur);

        List<VenteLigne> venteLignes = venteDTO.getLignes().stream()
                .map(ligneDTO -> processVenteLigne(ligneDTO, vente))
                .collect(Collectors.toList());
        vente.setLignes(venteLignes);

        BigDecimal total = venteLignes.stream()
                .map(ligne -> {
                    BigDecimal prix = ligne.getPrixUnitaire();
                    BigDecimal qty = BigDecimal.valueOf(ligne.getQuantite());
                    BigDecimal discountFactor = BigDecimal.ONE.subtract(ligne.getRemise());
                    return prix.multiply(qty).multiply(discountFactor);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
        
        vente.setTotal(total);

        Vente savedVente = venteRepository.save(vente);

        if (vente.getClient() != null) {
            updateLoyaltyPoints(vente.getClient(), total, false); // Add points
        }

        return venteMapper.toDTO(savedVente);
    }

    private VenteLigne processVenteLigne(VenteLigneDTO ligneDTO, Vente vente) {
        Medicament medicament = medicamentRepository.findById(ligneDTO.getMedicamentId())
                .orElseThrow(() -> new ResourceNotFoundException("Medicament not found with ID: " + ligneDTO.getMedicamentId()));

        stockService.deductStock(medicament.getId(), ligneDTO.getQuantite());

        VenteLigne venteLigne = new VenteLigne();
        venteLigne.setVente(vente);
        venteLigne.setMedicament(medicament);
        venteLigne.setQuantite(ligneDTO.getQuantite());
        venteLigne.setPrixUnitaire(medicament.getPrix());
        venteLigne.setRemise(ligneDTO.getRemise() != null ? ligneDTO.getRemise() : BigDecimal.ZERO);
        return venteLigne;
    }

    private void updateLoyaltyPoints(Client client, BigDecimal total, boolean isRefund) {
        int points = total.divide(BigDecimal.TEN, 0, RoundingMode.DOWN).intValue();
        if (isRefund) {
            client.setPointsFidelite(Math.max(0, client.getPointsFidelite() - points));
        } else {
            client.setPointsFidelite(client.getPointsFidelite() + points);
        }
        clientRepository.save(client);
    }

    public List<VenteDTO> getAllVentes() {
        return venteRepository.findAll().stream()
                .map(venteMapper::toDTO)
                .collect(Collectors.toList());
    }

    public VenteDTO getVenteById(Long id) {
        return venteRepository.findById(id)
                .map(venteMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Vente not found with ID: " + id));
    }

    @Transactional
    @AuditAction(action = "ANNULATION_VENTE", entityName = "Vente")
    public void cancelVente(Long id) {
        Vente vente = venteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vente not found with ID: " + id));

        // Restock items
        for (VenteLigne ligne : vente.getLignes()) {
            stockService.restock(ligne.getMedicament().getId(), ligne.getQuantite());
        }

        // Refund loyalty points
        if (vente.getClient() != null) {
            updateLoyaltyPoints(vente.getClient(), vente.getTotal(), true); // Refund points
        }

        // Instead of deleting, we can mark it as cancelled to keep a record
        // For now, we delete as per the initial simplified instruction.
        venteRepository.delete(vente);
    }
}
