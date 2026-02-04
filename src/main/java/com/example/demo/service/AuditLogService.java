package com.example.demo.service;

import com.example.demo.dto.AuditLogDTO;
import com.example.demo.entity.AuditLog;
import com.example.demo.mapper.AuditLogMapper;
import com.example.demo.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;

    public AuditLogDTO createAuditLog(AuditLogDTO auditLogDTO) {
        AuditLog auditLog = auditLogMapper.toEntity(auditLogDTO);
        return auditLogMapper.toDTO(auditLogRepository.save(auditLog));
    }

    public List<AuditLogDTO> getAllAuditLogs() {
        return auditLogRepository.findAll().stream()
                .map(auditLogMapper::toDTO)
                .collect(Collectors.toList());
    }

    public AuditLogDTO getAuditLogById(Long id) {
        return auditLogRepository.findById(id)
                .map(auditLogMapper::toDTO)
                .orElse(null);
    }
}
