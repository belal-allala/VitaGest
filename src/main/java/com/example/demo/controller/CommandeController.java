package com.example.demo.controller;

import com.example.demo.dto.CommandeDTO;
import com.example.demo.service.CommandeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/commandes")
@RequiredArgsConstructor
public class CommandeController {

    private final CommandeService commandeService;

    @PostMapping
    public ResponseEntity<CommandeDTO> createCommande(@RequestBody @Valid CommandeDTO commandeDTO) {
        return ResponseEntity.ok(commandeService.createCommande(commandeDTO));
    }

    @PostMapping("/{id}/reception")
    public ResponseEntity<CommandeDTO> recevoirCommande(@PathVariable Long id) {
        return ResponseEntity.ok(commandeService.recevoirCommande(id));
    }

    @GetMapping
    public ResponseEntity<List<CommandeDTO>> getAllCommandes() {
        return ResponseEntity.ok(commandeService.getAllCommandes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommandeDTO> getCommandeById(@PathVariable Long id) {
        return ResponseEntity.ok(commandeService.getCommandeById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommandeDTO> updateCommande(@PathVariable Long id, @RequestBody @Valid CommandeDTO commandeDTO) {
        return ResponseEntity.ok(commandeService.updateCommande(id, commandeDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommande(@PathVariable Long id) {
        commandeService.deleteCommande(id);
        return ResponseEntity.noContent().build();
    }
}
