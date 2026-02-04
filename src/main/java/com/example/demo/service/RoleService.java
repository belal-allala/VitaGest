package com.example.demo.service;

import com.example.demo.dto.RoleDTO;
import com.example.demo.entity.Role;
import com.example.demo.mapper.RoleMapper;
import com.example.demo.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleDTO createRole(RoleDTO roleDTO) {
        Role role = roleMapper.toEntity(roleDTO);
        return roleMapper.toDTO(roleRepository.save(role));
    }

    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(roleMapper::toDTO)
                .collect(Collectors.toList());
    }

    public RoleDTO getRoleById(UUID id) {
        return roleRepository.findById(id)
                .map(roleMapper::toDTO)
                .orElse(null);
    }

    public RoleDTO updateRole(UUID id, RoleDTO roleDTO) {
        return roleRepository.findById(id)
                .map(existingRole -> {
                    Role updatedRole = roleMapper.toEntity(roleDTO);
                    updatedRole.setId(existingRole.getId());
                    return roleMapper.toDTO(roleRepository.save(updatedRole));
                })
                .orElse(null);
    }

    public void deleteRole(UUID id) {
        roleRepository.deleteById(id);
    }
}
