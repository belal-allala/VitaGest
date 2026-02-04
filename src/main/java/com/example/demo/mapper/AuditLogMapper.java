package com.example.demo.mapper;

import com.example.demo.dto.AuditLogDTO;
import com.example.demo.entity.AuditLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuditLogMapper {
    @Mapping(source = "user.id", target = "userId")
    AuditLogDTO toDTO(AuditLog auditLog);
    
    @Mapping(source = "userId", target = "user.id")
    AuditLog toEntity(AuditLogDTO auditLogDTO);
}
