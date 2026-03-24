package com.example.demo.controller;

import com.example.demo.dto.VenteDTO;
import com.example.demo.service.VenteService;
import jakarta.validation.Valid;
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
    public ResponseEntity<VenteDTO> createVente(@RequestBody @Valid VenteDTO venteDTO) {
        return ResponseEntity.ok(venteService.createVente(venteDTO));
    }

    @GetMapping
    public ResponseEntity<List<VenteDTO>> getAllVentes() {
        return ResponseEntity.ok(venteService.getAllVentes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VenteDTO> getVenteById(@PathVariable Long id) {
        return ResponseEntity.ok(venteService.getVenteById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelVente(@PathVariable Long id) {
        venteService.cancelVente(id);
        return ResponseEntity.noContent().build();
    }
}
