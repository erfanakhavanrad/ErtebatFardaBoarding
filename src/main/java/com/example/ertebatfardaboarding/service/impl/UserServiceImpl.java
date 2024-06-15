package com.example.ertebatfardaboarding.service.impl;

import com.example.ertebatfardaboarding.domain.ResponseModel;
import com.example.ertebatfardaboarding.domain.User;
import com.example.ertebatfardaboarding.domain.dto.UserDto;
import com.example.ertebatfardaboarding.domain.mapper.UserMapper;
import com.example.ertebatfardaboarding.domain.specification.UserSpecification;
import com.example.ertebatfardaboarding.repo.UserRepository;
import com.example.ertebatfardaboarding.service.UserService;
import com.example.ertebatfardaboarding.utils.GlobalConstants;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ResponseModel responseModel;

    @Resource(name = "faMessageSource")
    private MessageSource faMessageSource;

    public List<User> getUsers(UserDto userDto) {
        Specification<User> specification = Specification.where(null);

        if (userDto.getName() != null) {
            specification = specification.and(UserSpecification.hasName(userDto.getName()));
        }

        if (userDto.getEmail() != null) {
            specification = specification.and(UserSpecification.hasEmail(userDto.getEmail()));
        }
        return userRepository.findAll(specification);
    }

    @Override
    public UserDto registerUser(UserDto userDto, HttpServletRequest httpServletRequest) throws NoSuchAlgorithmException {
        User first = getUsers(userDto).get(0);
        if (first != null)
            throw new RuntimeException(faMessageSource.getMessage("ALREADY_EXISTS", null, Locale.ENGLISH));
        User user = UserMapper.userMapper.userDtoToUser(userDto);
        user.setPassword(hashPassword(userDto.getPassword()));
        User savedUser = userRepository.save(user);

        return UserMapper.userMapper.userToUserDto(savedUser);

    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(GlobalConstants.ALGORITHM);
        byte[] encodedHash = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));
        return messageDigest.toString();
    }


}
