package com.cibertec.anuncio_facil_api.domain.service.interfaces;

import com.cibertec.anuncio_facil_api.domain.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CommentRepository {
    Comment save(Comment comment);
    Optional<Comment> findById(Long id);
    Page<Comment> findByAdId(Long adId, Pageable pageable);
    void deleteById(Long id);
}
