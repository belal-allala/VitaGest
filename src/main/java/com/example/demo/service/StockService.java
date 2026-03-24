package com.example.demo.service;

import com.example.demo.entity.Lot;
import com.example.demo.exception.InsufficientStockException;
import com.example.demo.repository.LotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {

    private final LotRepository lotRepository;

    @Transactional
    public void deductStock(Long medicamentId, Integer quantityRequested) {
        List<Lot> sortedLots = lotRepository.findLotsForDeduction(medicamentId);

        int totalStock = sortedLots.stream().mapToInt(Lot::getQuantite).sum();

        if (totalStock < quantityRequested) {
            throw new InsufficientStockException("Insufficient stock for medicament ID " + medicamentId +
                    ". Requested: " + quantityRequested + ", Available: " + totalStock);
        }

        int remainingQuantity = quantityRequested;
        for (Lot lot : sortedLots) {
            if (remainingQuantity <= 0) {
                break;
            }

            int quantityToDeduct = Math.min(remainingQuantity, lot.getQuantite());
            lot.setQuantite(lot.getQuantite() - quantityToDeduct);
            remainingQuantity -= quantityToDeduct;
            lotRepository.save(lot);
        }
    }

    @Transactional
    public void restock(Long medicamentId, Integer quantityToRestock) {
        // This is a simplified restock. A more robust implementation might need to know which specific lots to return to.
        // For now, we find the lot with the nearest expiration date and add the stock back.
        List<Lot> lots = lotRepository.findLotsForDeduction(medicamentId);
        if (lots.isEmpty()) {
            // This case is problematic - it means we sold stock that doesn't exist.
            // A better approach would be to create a new lot, but for now, we'll throw an exception.
            throw new IllegalStateException("No lots found to restock for medicament ID: " + medicamentId);
        }
        Lot lotToRestock = lots.get(0); // Simplistic: add to the first available lot.
        lotToRestock.setQuantite(lotToRestock.getQuantite() + quantityToRestock);
        lotRepository.save(lotToRestock);
    }
}
