package com.cibertec.anuncio_facil_api.infrastructure.persistence.jpa.repository;

import com.cibertec.anuncio_facil_api.infrastructure.persistence.jpa.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, Long> {
    Optional<CategoryEntity> findByName(String name);
    boolean existsByName(String name);
}
