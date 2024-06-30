package com.example.ertebatfardaboarding.repo;

import com.example.ertebatfardaboarding.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findAllByName(String roleName);
}