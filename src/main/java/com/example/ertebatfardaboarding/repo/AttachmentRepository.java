package com.example.ertebatfardaboarding.repo;

import com.example.ertebatfardaboarding.domain.Attachment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    Attachment findByToken(String token);

    Page<Attachment> findByUsername(String username, Pageable pageable);
}
