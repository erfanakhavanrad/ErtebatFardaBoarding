package com.example.ertebatfardaboarding.repo;

import com.example.ertebatfardaboarding.utils.Attachment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    Attachment findByToken(String token);
      Page<Attachment> findByUsername(String username, Pageable pageable);
//    org.springframework.data.domain.Page<Attachment> findAllByParentId(@Param("parentId") Long parentId, Pageable pageable);
//    @Param("parentId") Long parentId,
}
