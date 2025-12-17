package com.cibertec.anuncio_facil_api.application.usecases.interfaces;

import com.cibertec.anuncio_facil_api.application.dto.request.CategoryRequest;
import com.cibertec.anuncio_facil_api.application.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryUseCase {
    CategoryResponse create(CategoryRequest request);
    CategoryResponse update(Long id, CategoryRequest request);
    void delete(Long id);
    CategoryResponse getById(Long id);
    List<CategoryResponse> list();
}
