package com.cibertec.anuncio_facil_api.application.usecases.impl;

import com.cibertec.anuncio_facil_api.application.dto.request.CategoryRequest;
import com.cibertec.anuncio_facil_api.application.dto.response.CategoryResponse;
import com.cibertec.anuncio_facil_api.application.usecases.interfaces.CategoryUseCase;
import com.cibertec.anuncio_facil_api.domain.model.Category;
import com.cibertec.anuncio_facil_api.domain.service.interfaces.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryUseCaseImpl implements CategoryUseCase {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponse create(CategoryRequest request) {
        if (categoryRepository.existsByName(request.name())) {
            throw new RuntimeException("La categoría ya existe");
        }
        Category category = Category.builder()
                .name(request.name())
                .description(request.description())
                .build();
        return toResponse(categoryRepository.save(category));
    }

    @Override
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        category.setName(request.name());
        category.setDescription(request.description());
        return toResponse(categoryRepository.save(category));
    }

    @Override
    public void delete(Long id) {
        try {
            categoryRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "La categoría está en uso. Elimina o reasigna los anuncios asociados antes de borrarla.",
                    e
            );
        }
    }

    @Override
    public CategoryResponse getById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        return toResponse(category);
    }

    @Override
    public List<CategoryResponse> list() {
        return categoryRepository.findAll().stream().map(this::toResponse).toList();
    }

    private CategoryResponse toResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getName(), category.getDescription());
    }
}
