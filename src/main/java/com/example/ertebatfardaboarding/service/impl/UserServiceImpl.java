package com.example.ertebatfardaboarding.service.impl;

import com.example.ertebatfardaboarding.ErtebatFardaBoardingApplication;
import com.example.ertebatfardaboarding.domain.ResponseModel;
import com.example.ertebatfardaboarding.domain.Role;
import com.example.ertebatfardaboarding.domain.User;
import com.example.ertebatfardaboarding.domain.dto.UserDto;
import com.example.ertebatfardaboarding.domain.mapper.RoleMapper;
import com.example.ertebatfardaboarding.domain.mapper.UserMapper;
import com.example.ertebatfardaboarding.domain.responseDto.LoginResponseDto;
import com.example.ertebatfardaboarding.domain.responseDto.UserResponseDto;
import com.example.ertebatfardaboarding.domain.specification.UserSpecification;
import com.example.ertebatfardaboarding.exception.RoleException;
import com.example.ertebatfardaboarding.exception.UserException;
import com.example.ertebatfardaboarding.repo.RoleRepository;
import com.example.ertebatfardaboarding.repo.UserRepository;
import com.example.ertebatfardaboarding.security.SecurityService;
import com.example.ertebatfardaboarding.service.UserService;
import com.example.ertebatfardaboarding.utils.GlobalConstants;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    ResponseModel responseModel = new ResponseModel();

    @Autowired
    private SecurityService securityService;

    @Autowired
    EmailServiceImpl emailService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    UserMapper userMapper;

    @Resource(name = "faMessageSource")
    private MessageSource faMessageSource;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<UserResponseDto> getUsersBySearch(UserDto userDto) {
        Specification<User> specification = Specification.where(null);

        if (userDto.getName() != null) {
            specification = hasName(userDto.getName());
        }

        if (userDto.getEmail() != null) {
            specification = hasEmail(userDto.getEmail());
        }
        List<User> all = userRepository.findAll(specification);
        return userMapper.userListToUserResponseDtoList(all);
    }

    private static Specification<User> hasName(String name) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                "%" + name.toLowerCase() + "%"));
    }

    private static Specification<User> hasEmail(String email) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("email")),
                "%" + email.toLowerCase() + "%");
    }

    @Override
    public UserResponseDto registerUser(UserDto userDto, HttpServletRequest httpServletRequest) throws UserException {
        if (!getUsers(userDto).isEmpty())
            throw new UserException(faMessageSource.getMessage("ALREADY_EXISTS", null, Locale.ENGLISH));
        userDto.setPassword(passwordGenerator(userDto));
        userDto.setActivationCode(passwordGenerator());
        redisTemplate.opsForValue().set(userDto.getEmail(), userDto, 500, TimeUnit.SECONDS);
        log.info("User registered with email: {}", userDto.getEmail());
        return userMapper.userDtoToUserResponseDto(userDto);
    }

    @Override
    public UserResponseDto verifyUser(UserDto userDto, HttpServletRequest httpServletRequest) throws Exception {
        UserDto thisUserDto = (UserDto) redisTemplate.opsForValue().get(userDto.getEmail());
        log.info("Verifying user with email: {} - Found: {}", userDto.getEmail(), thisUserDto != null);
        if (thisUserDto == null || !Objects.equals(thisUserDto.getActivationCode(), userDto.getActivationCode()) || thisUserDto.getIsActive())
            throw new UserException(faMessageSource.getMessage("INVALID_OTP", null, Locale.ENGLISH) + faMessageSource.getMessage("INVALID_OTP", null, Locale.ENGLISH));
        else {
            thisUserDto.setIsActive(true);
            Role role = fillInRoleAndPrivileges(thisUserDto.getRoles().get(0).getId());
            List<Role> roleList = new ArrayList<>();
            roleList.add(role);
            thisUserDto.setRoles(RoleMapper.roleMapper.roleListToRoleDtoList(roleList));
            User newUser = UserMapper.userMapper.userDtoToUser(thisUserDto);
            User savedUser = userRepository.save(newUser);
            return userMapper.userToUserResponseDto(savedUser);
        }
    }

    private Role fillInRoleAndPrivileges(Long id) throws Exception {
        return roleRepository.findById(id).orElseThrow(() -> new RoleException(faMessageSource.getMessage("NOT_FOUND", null, Locale.ENGLISH)));

    }

    private String passwordGenerator() {
        return String.format("%06d", new Random().nextInt(1000000));
    }

    @Override
    public LoginResponseDto loginUser(UserDto userDto, HttpServletRequest httpServletRequest) {
        UserDto userDtoTemp = userDto;
        LoginResponseDto loginResponseDto;
        userDtoTemp.setName(null);
        List<User> savedUser = getUsers(userDtoTemp);
        if (savedUser.isEmpty())
            throw new UserException(faMessageSource.getMessage("INVALID_CREDENTIALS", null, Locale.ENGLISH));
        if (!savedUser.get(0).getIsActive()) {
            throw new UserException(faMessageSource.getMessage("NOT_ACTIVE", null, Locale.ENGLISH));
        }
        if (isPasswordValid(savedUser.get(0), userDto)) {
            List tokens = new ArrayList();
            tokens.add(securityService.createTokenByUserPasswordAuthentication(userDto.getEmail()));
            loginResponseDto = userMapper.userToLoginResponseDto(savedUser.get(0));
            loginResponseDto.setTokens(tokens);
        } else throw new UserException(faMessageSource.getMessage("INVALID_CREDENTIALS", null, Locale.ENGLISH));
        return loginResponseDto;
    }

    @Override
    public Page<UserResponseDto> getUsers(Integer pageNo, Integer perPage) throws Exception {
        Page<User> all = userRepository.findAll(ErtebatFardaBoardingApplication.createPagination(pageNo, perPage));
        return all.map(userMapper::userToUserResponseDto);
    }

    private String passwordGenerator(UserDto userDto) {
        try {
            String salt = userDto.getEmail().substring(2, 5);
            String readyPass = salt + userDto.getPassword();
            // Create MessageDigest instance for SHA-256
            MessageDigest digest = MessageDigest.getInstance(GlobalConstants.ALGORITHM);
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

    @Override
    public void deleteUser(Long id) throws Exception {
        userRepository.deleteById(id);
    }

    private List<User> getUsers(UserDto userDto) {
        Specification<User> specification = Specification.where(null);

        if (userDto.getName() != null) {
            specification = specification.and(UserSpecification.hasName(userDto.getName()));
        }

        if (userDto.getEmail() != null) {
            specification = specification.and(UserSpecification.hasEmail(userDto.getEmail()));
        }
        return userRepository.findAll(specification);
    }
}
