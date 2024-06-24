package com.example.ertebatfardaboarding.repo;

import com.example.ertebatfardaboarding.utils.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
}
