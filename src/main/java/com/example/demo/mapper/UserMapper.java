package com.example.demo.mapper;

import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper {
    @Mapping(source = "role", target = "role")
    UserDTO toDTO(User user);
    @Mapping(source = "role", target = "role")
    User toEntity(UserDTO userDTO);
}
