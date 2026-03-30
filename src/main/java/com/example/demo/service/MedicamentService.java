package com.example.demo.service;

import com.example.demo.aop.AuditAction;
import com.example.demo.dto.MedicamentDTO;
import com.example.demo.entity.Medicament;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.MedicamentMapper;
import com.example.demo.repository.MedicamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicamentService {

    private final MedicamentRepository medicamentRepository;
    private final MedicamentMapper medicamentMapper;

    @Transactional
    @AuditAction(action = "CREATION_MEDICAMENT", entityName = "Medicament")
    public MedicamentDTO createMedicament(MedicamentDTO medicamentDTO) {
        Medicament medicament = medicamentMapper.toEntity(medicamentDTO);
        return medicamentMapper.toDTO(medicamentRepository.save(medicament));
    }

    public List<MedicamentDTO> getAllMedicaments() {
        return medicamentRepository.findAll().stream()
                .map(this::toDTOWithStock)
                .collect(Collectors.toList());
    }

    private MedicamentDTO toDTOWithStock(Medicament m) {
        MedicamentDTO dto = medicamentMapper.toDTO(m);
        int totalStock = (m.getLots() != null) ? m.getLots().stream().mapToInt(lot -> lot.getQuantite()).sum() : 0;
        dto.setQuantiteEnStock(totalStock);
        return dto;
    }

    public MedicamentDTO getMedicamentById(Long id) {
        return medicamentRepository.findById(id)
                .map(this::toDTOWithStock)
                .orElseThrow(() -> new ResourceNotFoundException("Medicament not found with ID: " + id));
    }

    @Transactional
    @AuditAction(action = "MISE_A_JOUR_MEDICAMENT", entityName = "Medicament")
    public MedicamentDTO updateMedicament(Long id, MedicamentDTO medicamentDTO) {
        return medicamentRepository.findById(id)
                .map(existingMedicament -> {
                    Medicament updatedMedicament = medicamentMapper.toEntity(medicamentDTO);
                    updatedMedicament.setId(existingMedicament.getId());
                    return medicamentMapper.toDTO(medicamentRepository.save(updatedMedicament));
                })
                .orElseThrow(() -> new ResourceNotFoundException("Medicament not found with ID: " + id));
    }

    @Transactional
    @AuditAction(action = "SUPPRESSION_MEDICAMENT", entityName = "Medicament")
    public void deleteMedicament(Long id) {
        if (!medicamentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Medicament not found with ID: " + id);
        }
        medicamentRepository.deleteById(id);
    }
}
