package com.example.ertebatfardaboarding.repo;

import com.example.ertebatfardaboarding.domain.Contact;
import com.example.ertebatfardaboarding.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long>, JpaSpecificationExecutor<Contact> {
    org.springframework.data.domain.Page<Contact> findAllByCreatedBy(User user, Pageable pageable);
    org.springframework.data.domain.Page<Contact> findAllByCreatedBy(User user, Specification specification, Pageable pageable);
}