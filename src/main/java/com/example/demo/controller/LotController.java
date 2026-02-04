package com.example.demo.controller;

import com.example.demo.dto.LotDTO;
import com.example.demo.service.LotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lots")
@RequiredArgsConstructor
public class LotController {

    private final LotService lotService;

    @PostMapping
    public ResponseEntity<LotDTO> createLot(@RequestBody LotDTO lotDTO) {
        return ResponseEntity.ok(lotService.createLot(lotDTO));
    }

    @GetMapping
    public ResponseEntity<List<LotDTO>> getAllLots() {
        return ResponseEntity.ok(lotService.getAllLots());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LotDTO> getLotById(@PathVariable Long id) {
        LotDTO lot = lotService.getLotById(id);
        return lot != null ? ResponseEntity.ok(lot) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<LotDTO> updateLot(@PathVariable Long id, @RequestBody LotDTO lotDTO) {
        LotDTO updatedLot = lotService.updateLot(id, lotDTO);
        return updatedLot != null ? ResponseEntity.ok(updatedLot) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLot(@PathVariable Long id) {
        lotService.deleteLot(id);
        return ResponseEntity.noContent().build();
    }
}
