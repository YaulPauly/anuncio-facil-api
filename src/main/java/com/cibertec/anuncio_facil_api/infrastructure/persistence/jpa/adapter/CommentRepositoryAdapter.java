package com.cibertec.anuncio_facil_api.infrastructure.persistence.jpa.adapter;

import com.cibertec.anuncio_facil_api.domain.model.Comment;
import com.cibertec.anuncio_facil_api.domain.model.Role;
import com.cibertec.anuncio_facil_api.domain.model.User;
import com.cibertec.anuncio_facil_api.domain.service.interfaces.CommentRepository;
import com.cibertec.anuncio_facil_api.infrastructure.persistence.jpa.entity.AdEntity;
import com.cibertec.anuncio_facil_api.infrastructure.persistence.jpa.entity.CommentEntity;
import com.cibertec.anuncio_facil_api.infrastructure.persistence.jpa.entity.UserEntity;
import com.cibertec.anuncio_facil_api.infrastructure.persistence.jpa.repository.AdJpaRepository;
import com.cibertec.anuncio_facil_api.infrastructure.persistence.jpa.repository.CommentJpaRepository;
import com.cibertec.anuncio_facil_api.infrastructure.persistence.jpa.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryAdapter implements CommentRepository {

    private final CommentJpaRepository commentJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final AdJpaRepository adJpaRepository;

    @Override
    public Comment save(Comment comment) {
        CommentEntity entity = toEntity(comment);
        CommentEntity saved = commentJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Comment> findById(Long id) {
        return commentJpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Page<Comment> findByAdId(Long adId, Pageable pageable) {
        return commentJpaRepository.findByAdId(adId, pageable).map(this::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        commentJpaRepository.deleteById(id);
    }

    private Comment toDomain(CommentEntity entity) {
        UserEntity ue = entity.getUser();
        User user = User.builder()
                .id(ue.getId())
                .email(ue.getEmail())
                .firstName(ue.getFirstName())
                .lastName(ue.getLastName())
                .role(Role.builder().id(ue.getRole().getId()).name(ue.getRole().getName()).build())
                .build();

        return Comment.builder()
                .id(entity.getId())
                .content(entity.getContent())
                .user(user)
                .ad(com.cibertec.anuncio_facil_api.domain.model.Ad.builder().id(entity.getAd().getId()).build())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private CommentEntity toEntity(Comment comment) {
        UserEntity user = userJpaRepository.findById(comment.getUser().getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        AdEntity ad = adJpaRepository.findById(comment.getAd().getId())
                .orElseThrow(() -> new RuntimeException("Anuncio no encontrado"));

        return CommentEntity.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .user(user)
                .ad(ad)
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
