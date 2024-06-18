package com.example.ertebatfardaboarding.service.impl;

import com.example.ertebatfardaboarding.ErtebatFardaBoardingApplication;
import com.example.ertebatfardaboarding.domain.ResponseModel;
import com.example.ertebatfardaboarding.domain.User;
import com.example.ertebatfardaboarding.domain.dto.UserDto;
import com.example.ertebatfardaboarding.domain.mapper.UserMapper;
import com.example.ertebatfardaboarding.domain.specification.UserSpecification;
import com.example.ertebatfardaboarding.exception.UserException;
import com.example.ertebatfardaboarding.repo.UserRepository;
import com.example.ertebatfardaboarding.security.SecurityService;
import com.example.ertebatfardaboarding.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ResponseModel responseModel;

    @Resource(name = "faMessageSource")
    private MessageSource faMessageSource;
    @Autowired
    private SecurityService securityService;

    @Value("${SUCCESS_RESULT}")
    int success;

    @Value("${FAIL_RESULT}")
    int fail;

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
    public UserDto registerUser(UserDto userDto, HttpServletRequest httpServletRequest) throws UserException {
        if (!getUsers(userDto).isEmpty())
            throw new UserException(faMessageSource.getMessage("ALREADY_EXISTS", null, Locale.ENGLISH));
        User user = UserMapper.userMapper.userDtoToUser(userDto);
        user.setPassword(passwordGenerator(userDto));
//        user.setUsername(userDto.getEmail());
        User savedUser = userRepository.save(user);
        return UserMapper.userMapper.userToUserDto(savedUser);
    }

    @Override
    public UserDto loginUser(UserDto userDto, HttpServletRequest httpServletRequest) {
        UserDto userDtoTemp = userDto;
        userDtoTemp.setName(null);
        List<User> savedUser = getUsers(userDtoTemp);
        if (savedUser.isEmpty())
            throw new UserException(faMessageSource.getMessage("INVALID_CREDENTIALS", null, Locale.ENGLISH));
        if (isPasswordValid(savedUser.get(0), userDto)) {

        List tokens = new ArrayList();
        tokens.add(securityService.createTokenByUserPasswordAuthentication(userDto.getEmail()));
        responseModel.setContents(tokens);
        responseModel.setContent(savedUser);
        responseModel.setResult(success);
        } else throw new UserException("INVALID_CREDENTIALS");
        return userDto;
    }

    @Override
    public Page<User> getUsers(Integer pageNo, Integer perPage) throws Exception {
        return userRepository.findAll(ErtebatFardaBoardingApplication.createPagination(pageNo, perPage));
    }

    private String passwordGenerator(UserDto userDto) {
        try {
            String salt = userDto.getEmail().substring(2, 5);
            String readyPass = salt + userDto.getPassword();
            // Create MessageDigest instance for SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // Apply the hash function to the input bytes
            byte[] hashBytes = digest.digest(readyPass.getBytes());
            // Convert the byte array into a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error while hashing the string", e);
        }
    }

    private boolean isPasswordValid(User savedUser, UserDto userDto) {
        return Objects.equals(savedUser.getPassword(), passwordGenerator(userDto));
    }


}
