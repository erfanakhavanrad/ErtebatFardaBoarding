package com.example.ertebatfardaboarding.domain.mapper;

import com.example.ertebatfardaboarding.domain.User;
import com.example.ertebatfardaboarding.domain.dto.UserDto;
import com.example.ertebatfardaboarding.domain.responseDto.UserResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    UserDto userToUserDto(User user);
    UserResponseDto userToUserResponseDto(User user);

    User userDtoToUser(UserDto userDto);

    UserResponseDto userDtoToUserResponseDto(UserDto userDto);

    List<UserResponseDto> userListToUserResponseDtoList(List<User> userList);

}
