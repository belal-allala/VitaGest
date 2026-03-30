package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AuditLogDTO {
    private Long id;
    private String action;
    private String entity;
    private Long entityId;
    private LocalDateTime timestamp;
    private String details;
    private Long userId;
    private String userName;
}
