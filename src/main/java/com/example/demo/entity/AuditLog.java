package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class AuditLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String action;   // CREATE, UPDATE, DELETE
    private String entity;   // Nom de la table
    private Long entityId;   // ID de l'objet touché
    private LocalDateTime timestamp;
    
    @Column(columnDefinition = "TEXT")
    private String details;  // Changements au format JSON

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
