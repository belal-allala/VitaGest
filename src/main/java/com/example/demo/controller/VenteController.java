package com.example.demo.controller;

import com.example.demo.dto.VenteDTO;
import com.example.demo.service.VenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ventes")
@RequiredArgsConstructor
public class VenteController {

    private final VenteService venteService;

    @PostMapping
    public ResponseEntity<VenteDTO> createVente(@RequestBody VenteDTO venteDTO) {
        return ResponseEntity.ok(venteService.createVente(venteDTO));
    }

    @GetMapping
    public ResponseEntity<List<VenteDTO>> getAllVentes() {
        return ResponseEntity.ok(venteService.getAllVentes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VenteDTO> getVenteById(@PathVariable Long id) {
        VenteDTO vente = venteService.getVenteById(id);
        return vente != null ? ResponseEntity.ok(vente) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<VenteDTO> updateVente(@PathVariable Long id, @RequestBody VenteDTO venteDTO) {
        VenteDTO updatedVente = venteService.updateVente(id, venteDTO);
        return updatedVente != null ? ResponseEntity.ok(updatedVente) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVente(@PathVariable Long id) {
        venteService.deleteVente(id);
        return ResponseEntity.noContent().build();
    }
}
