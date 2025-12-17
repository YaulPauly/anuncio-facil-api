package com.cibertec.anuncio_facil_api.infrastructure.persistence.jpa.adapter;

import com.cibertec.anuncio_facil_api.domain.model.Category;
import com.cibertec.anuncio_facil_api.domain.service.interfaces.CategoryRepository;
import com.cibertec.anuncio_facil_api.infrastructure.persistence.jpa.entity.CategoryEntity;
import com.cibertec.anuncio_facil_api.infrastructure.persistence.jpa.repository.CategoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryAdapter implements CategoryRepository {

    private final CategoryJpaRepository categoryJpaRepository;

    @Override
    public Category save(Category category) {
        CategoryEntity entity = toEntity(category);
        CategoryEntity saved = categoryJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryJpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Category> findAll() {
        return categoryJpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        categoryJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return categoryJpaRepository.existsByName(name);
    }

    private Category toDomain(CategoryEntity entity) {
        return Category.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
    }

    private CategoryEntity toEntity(Category category) {
        return CategoryEntity.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }
}
