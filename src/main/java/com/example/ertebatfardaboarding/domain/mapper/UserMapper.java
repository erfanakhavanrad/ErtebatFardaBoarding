package com.example.ertebatfardaboarding.domain.mapper;

import com.example.ertebatfardaboarding.domain.User;
import com.example.ertebatfardaboarding.domain.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    UserDto userToUserDto(User user);

    User userDtoToUser(UserDto userDto);
}
