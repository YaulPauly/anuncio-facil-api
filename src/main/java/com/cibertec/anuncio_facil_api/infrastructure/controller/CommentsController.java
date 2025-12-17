package com.cibertec.anuncio_facil_api.infrastructure.controller;

import com.cibertec.anuncio_facil_api.application.dto.request.CommentRequest;
import com.cibertec.anuncio_facil_api.application.dto.response.CommentResponse;
import com.cibertec.anuncio_facil_api.application.usecases.interfaces.CommentUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ads/{adId}/comments")
@RequiredArgsConstructor
public class CommentsController {

    private final CommentUseCase commentUseCase;

    @PostMapping
    public ResponseEntity<CommentResponse> add(@PathVariable Long adId, @Valid @RequestBody CommentRequest request) {
        return ResponseEntity.ok(commentUseCase.add(adId, request));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> update(@PathVariable Long adId,
                                                  @PathVariable Long commentId,
                                                  @Valid @RequestBody CommentRequest request) {
        return ResponseEntity.ok(commentUseCase.update(adId, commentId, request));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(@PathVariable Long adId, @PathVariable Long commentId) {
        commentUseCase.delete(adId, commentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<com.cibertec.anuncio_facil_api.application.dto.response.PagedResponse<CommentResponse>> listByAd(
            @PathVariable Long adId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(commentUseCase.listByAd(adId, page, size));
    }
}
