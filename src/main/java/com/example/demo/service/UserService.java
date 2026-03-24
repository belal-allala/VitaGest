package com.example.demo.service;

import com.example.demo.aop.AuditAction;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    @AuditAction(action = "CREATION_UTILISATEUR", entityName = "User")
    public UserDTO createUser(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        // Password should be set via a dedicated registration flow, not here.
        // This method is for admin-created users.
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
                    User updatedUser = userMapper.toEntity(userDTO);
                    updatedUser.setId(existingUser.getId());
                    // Ensure password is not accidentally overwritten
                    updatedUser.setPassword(existingUser.getPassword());
                    return userMapper.toDTO(userRepository.save(updatedUser));
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
}
