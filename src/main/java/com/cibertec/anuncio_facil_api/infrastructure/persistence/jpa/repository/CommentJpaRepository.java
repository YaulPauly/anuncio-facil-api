package com.cibertec.anuncio_facil_api.infrastructure.persistence.jpa.repository;

import com.cibertec.anuncio_facil_api.infrastructure.persistence.jpa.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentJpaRepository extends JpaRepository<CommentEntity, Long> {
    Page<CommentEntity> findByAdId(Long adId, Pageable pageable);
}
