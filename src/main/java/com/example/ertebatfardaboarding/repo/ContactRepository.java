package com.example.ertebatfardaboarding.repo;

import com.example.ertebatfardaboarding.domain.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
//public interface ContactRepository extends CrudRepository<Contact, Long> {
public interface ContactRepository extends JpaRepository<Contact, Long> {

}
