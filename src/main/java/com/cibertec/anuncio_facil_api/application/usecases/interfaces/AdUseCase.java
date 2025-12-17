package com.cibertec.anuncio_facil_api.application.usecases.interfaces;

import com.cibertec.anuncio_facil_api.application.dto.request.AdRequest;
import com.cibertec.anuncio_facil_api.application.dto.response.AdResponse;
import com.cibertec.anuncio_facil_api.application.dto.response.PagedResponse;
import com.cibertec.anuncio_facil_api.domain.model.AdStatus;

import java.util.List;

public interface AdUseCase {
    AdResponse create(AdRequest request);
    AdResponse update(Long adId, AdRequest request);
    AdResponse toggleStatus(Long adId, AdStatus status);
    AdResponse getById(Long adId);
    PagedResponse<AdResponse> list(Long categoryId, String city, String district, AdStatus status, int page, int size);
    List<AdResponse> listByCurrentUser();
}
