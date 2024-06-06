package com.luismateoh.gymcrm.mapper;

import com.luismateoh.gymcrm.domain.User;
import com.luismateoh.gymcrm.dto.UserDTO;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO userToUserDTO(User user);

    User userDTOToUser(UserDTO userDTO);
}
