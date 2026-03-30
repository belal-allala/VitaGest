package com.example.demo.service;

import com.example.demo.aop.AuditAction;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @AuditAction(action = "CREATION_UTILISATEUR", entityName = "User")
    public UserDTO createUser(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        
        // Find existing role from DB to avoid duplicate key exceptions
        if (userDTO.getRole() != null && userDTO.getRole().getNom() != null) {
            String roleName = userDTO.getRole().getNom();
            user.setRole(roleRepository.findByNom(roleName)
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName)));
        }

        if (userDTO.getPassword() != null && !userDTO.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        return userMapper.toDTO(userRepository.save(user));
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }

    @Transactional
    @AuditAction(action = "MISE_A_JOUR_UTILISATEUR", entityName = "User")
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setNomComplet(userDTO.getUsername());
                    existingUser.setEmail(userDTO.getEmail());
                    existingUser.setActive(userDTO.isActive());
                    
                    // Update Role by finding it in DB
                    if (userDTO.getRole() != null && userDTO.getRole().getNom() != null) {
                        String roleName = userDTO.getRole().getNom();
                        existingUser.setRole(roleRepository.findByNom(roleName)
                                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName)));
                    }
                    
                    return userMapper.toDTO(userRepository.save(existingUser));
                })
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }

    @Transactional
    @AuditAction(action = "SUPPRESSION_UTILISATEUR", entityName = "User")
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }

    @Transactional
    @AuditAction(action = "CHANGEMENT_STATUT_UTILISATEUR", entityName = "User")
    public UserDTO toggleActiveStatus(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setActive(!user.isActive());
                    return userMapper.toDTO(userRepository.save(user));
                })
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }

    @Transactional
    @AuditAction(action = "REINITIALISATION_MOT_DE_PASSE", entityName = "User")
    public void resetPassword(Long id, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
