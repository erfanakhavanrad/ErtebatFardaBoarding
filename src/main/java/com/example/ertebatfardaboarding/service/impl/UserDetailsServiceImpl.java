package com.example.ertebatfardaboarding.service.impl;

import com.example.ertebatfardaboarding.domain.Privilege;
import com.example.ertebatfardaboarding.domain.User;
import com.example.ertebatfardaboarding.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = this.userRepository.findByEmail(username);
        List<Privilege> privileges = new ArrayList<>();
        if (user == null) {
            throw new UsernameNotFoundException(username);
        } else {
            user.getRoles().stream().forEach(role -> {
                role.getPrivileges().forEach(privilege -> privileges.add(privilege));
            });
            return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                    .authorities(privileges)
                    .accountExpired(false)
                    .accountLocked(false)
                    .credentialsExpired(false)
                    .disabled(false)
                    .password(user.getPassword())
                    .build();
        }
    }
}
