package com.example.demo.aop;

import com.example.demo.entity.AuditLog;
import com.example.demo.entity.User;
import com.example.demo.repository.AuditLogRepository;
import com.example.demo.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class LoggingAspect {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @AfterReturning(pointcut = "@annotation(auditAction)", returning = "result")
    public void logAction(JoinPoint joinPoint, AuditAction auditAction, Object result) {
        try {
            User user = getCurrentUser().orElse(getSystemUser());

            String entityName = auditAction.entityName().isEmpty() ?
                    joinPoint.getSignature().getDeclaringType().getSimpleName().replace("Service", "") :
                    auditAction.entityName();

            Long entityId = extractEntityId(result);

            AuditLog auditLog = new AuditLog();
            auditLog.setAction(auditAction.action());
            auditLog.setEntity(entityName);
            auditLog.setEntityId(entityId);
            auditLog.setTimestamp(LocalDateTime.now());
            auditLog.setUser(user);
            auditLog.setDetails(buildDetails(joinPoint.getArgs()));

            auditLogRepository.save(auditLog);
        } catch (Exception e) {
            log.error("Failed to save audit log", e);
            // We don't rethrow the exception to avoid crashing the main transaction
        }
    }

    private Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return Optional.empty();
        }
        String userEmail = authentication.getName();
        return userRepository.findByEmail(userEmail);
    }

    private User getSystemUser() {
        // A fallback user for system-level actions where no user is logged in.
        // Ensure a "SYSTEM" user exists in your database or handle it gracefully.
        return userRepository.findByEmail("admin").orElse(null);
    }

    private Long extractEntityId(Object result) {
        if (result == null) return null;
        try {
            // Assumes the returned object has a 'getId' method that returns a Long.
            return (Long) result.getClass().getMethod("getId").invoke(result);
        } catch (Exception e) {
            // If the returned object is a simple type like Long (e.g., from a delete method), use it directly.
            if (result instanceof Long) {
                return (Long) result;
            }
            return null;
        }
    }

    private String buildDetails(Object[] args) {
        try {
            return objectMapper.writeValueAsString(args);
        } catch (JsonProcessingException e) {
            return "Could not serialize arguments";
        }
    }
}
