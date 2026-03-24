package com.example.demo.controller;

import com.example.demo.dto.MedicamentDTO;
import com.example.demo.service.MedicamentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/medicaments")
@RequiredArgsConstructor
public class MedicamentController {

    private final MedicamentService medicamentService;

    @PostMapping
    public ResponseEntity<MedicamentDTO> createMedicament(@RequestBody @Valid MedicamentDTO medicamentDTO) {
        return ResponseEntity.ok(medicamentService.createMedicament(medicamentDTO));
    }

    @GetMapping
    public ResponseEntity<List<MedicamentDTO>> getAllMedicaments() {
        return ResponseEntity.ok(medicamentService.getAllMedicaments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicamentDTO> getMedicamentById(@PathVariable Long id) {
        return ResponseEntity.ok(medicamentService.getMedicamentById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicamentDTO> updateMedicament(@PathVariable Long id, @RequestBody @Valid MedicamentDTO medicamentDTO) {
        return ResponseEntity.ok(medicamentService.updateMedicament(id, medicamentDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicament(@PathVariable Long id) {
        medicamentService.deleteMedicament(id);
        return ResponseEntity.noContent().build();
    }
}
