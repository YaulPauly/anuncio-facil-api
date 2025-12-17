package com.cibertec.anuncio_facil_api.application.usecases.interfaces;

import com.cibertec.anuncio_facil_api.application.dto.request.CommentRequest;
import com.cibertec.anuncio_facil_api.application.dto.response.CommentResponse;
import com.cibertec.anuncio_facil_api.application.dto.response.PagedResponse;

public interface CommentUseCase {
    CommentResponse add(Long adId, CommentRequest request);
    CommentResponse update(Long adId, Long commentId, CommentRequest request);
    void delete(Long adId, Long commentId);
    PagedResponse<CommentResponse> listByAd(Long adId, int page, int size);
}
