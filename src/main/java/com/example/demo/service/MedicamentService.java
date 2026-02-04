package com.example.demo.service;

import com.example.demo.dto.MedicamentDTO;
import com.example.demo.entity.Medicament;
import com.example.demo.mapper.MedicamentMapper;
import com.example.demo.repository.MedicamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicamentService {

    private final MedicamentRepository medicamentRepository;
    private final MedicamentMapper medicamentMapper;

    public MedicamentDTO createMedicament(MedicamentDTO medicamentDTO) {
        Medicament medicament = medicamentMapper.toEntity(medicamentDTO);
        return medicamentMapper.toDTO(medicamentRepository.save(medicament));
    }

    public List<MedicamentDTO> getAllMedicaments() {
        return medicamentRepository.findAll().stream()
                .map(medicamentMapper::toDTO)
                .collect(Collectors.toList());
    }

    public MedicamentDTO getMedicamentById(Long id) {
        return medicamentRepository.findById(id)
                .map(medicamentMapper::toDTO)
                .orElse(null);
    }

    public MedicamentDTO updateMedicament(Long id, MedicamentDTO medicamentDTO) {
        return medicamentRepository.findById(id)
                .map(existingMedicament -> {
                    Medicament updatedMedicament = medicamentMapper.toEntity(medicamentDTO);
                    updatedMedicament.setId(existingMedicament.getId());
                    return medicamentMapper.toDTO(medicamentRepository.save(updatedMedicament));
                })
                .orElse(null);
    }

    public void deleteMedicament(Long id) {
        medicamentRepository.deleteById(id);
    }
}
