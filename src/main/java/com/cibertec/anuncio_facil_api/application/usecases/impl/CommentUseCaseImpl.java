package com.cibertec.anuncio_facil_api.application.usecases.impl;

import com.cibertec.anuncio_facil_api.application.dto.request.CommentRequest;
import com.cibertec.anuncio_facil_api.application.dto.response.BasicUserResponse;
import com.cibertec.anuncio_facil_api.application.dto.response.CommentResponse;
import com.cibertec.anuncio_facil_api.application.dto.response.PagedResponse;
import com.cibertec.anuncio_facil_api.application.service.CurrentUserService;
import com.cibertec.anuncio_facil_api.application.usecases.interfaces.CommentUseCase;
import com.cibertec.anuncio_facil_api.domain.model.Ad;
import com.cibertec.anuncio_facil_api.domain.model.Comment;
import com.cibertec.anuncio_facil_api.domain.model.User;
import com.cibertec.anuncio_facil_api.domain.service.interfaces.AdRepository;
import com.cibertec.anuncio_facil_api.domain.service.interfaces.CommentRepository;
import com.cibertec.anuncio_facil_api.domain.service.interfaces.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentUseCaseImpl implements CommentUseCase {

    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    @Override
    public CommentResponse add(Long adId, CommentRequest request) {
        User user = getCurrentUser();
        Ad ad = findAdOrThrow(adId);
        Comment comment = Comment.builder()
                .content(request.content())
                .ad(ad)
                .user(user)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return toResponse(commentRepository.save(comment));
    }

    @Override
    public CommentResponse update(Long adId, Long commentId, CommentRequest request) {
        Comment comment = findCommentOrThrow(commentId);
        if (!comment.getAd().getId().equals(adId)) {
            throw new RuntimeException("El comentario no pertenece al anuncio");
        }
        ensureOwnership(comment.getUser());
        comment.setContent(request.content());
        comment.setUpdatedAt(LocalDateTime.now());
        return toResponse(commentRepository.save(comment));
    }

    @Override
    public void delete(Long adId, Long commentId) {
        Comment comment = findCommentOrThrow(commentId);
        if (!comment.getAd().getId().equals(adId)) {
            throw new RuntimeException("El comentario no pertenece al anuncio");
        }
        ensureOwnership(comment.getUser());
        commentRepository.deleteById(commentId);
    }

    @Override
    public PagedResponse<CommentResponse> listByAd(Long adId, int page, int size) {
        findAdOrThrow(adId); // valida existencia
        Page<Comment> comments = commentRepository.findByAdId(adId, PageRequest.of(page, size));
        var content = comments.getContent().stream().map(this::toResponse).toList();
        return new PagedResponse<>(
                content,
                comments.getNumber(),
                comments.getSize(),
                comments.getTotalElements(),
                comments.getTotalPages(),
                comments.hasNext(),
                comments.hasPrevious()
        );
    }

    private Ad findAdOrThrow(Long adId) {
        return adRepository.findById(adId).orElseThrow(() -> new RuntimeException("Anuncio no encontrado"));
    }

    private Comment findCommentOrThrow(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comentario no encontrado"));
    }

    private User getCurrentUser() {
        String email = currentUserService.getCurrentUserEmail();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    private void ensureOwnership(User owner) {
        User current = getCurrentUser();
        boolean isOwner = owner.getId().equals(current.getId());
        boolean isAdmin = current.getRole() != null && "ADMIN".equalsIgnoreCase(current.getRole().getName());
        if (!isOwner && !isAdmin) {
            throw new RuntimeException("No autorizado para modificar este recurso");
        }
    }

    private CommentResponse toResponse(Comment comment) {
        BasicUserResponse user = comment.getUser() == null ? null :
                new BasicUserResponse(comment.getUser().getId(), comment.getUser().getEmail(), comment.getUser().getFirstName(), comment.getUser().getLastName());
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                user,
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
