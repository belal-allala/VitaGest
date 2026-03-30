package com.example.demo.mapper;

import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper {
    @Mapping(source = "nomComplet", target = "username")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "active", target = "active")
    UserDTO toDTO(User user);

    @Mapping(source = "username", target = "nomComplet")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "active", target = "active")
    User toEntity(UserDTO userDTO);
}
