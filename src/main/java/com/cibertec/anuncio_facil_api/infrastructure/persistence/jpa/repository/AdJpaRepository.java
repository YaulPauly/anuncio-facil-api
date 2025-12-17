package com.cibertec.anuncio_facil_api.infrastructure.persistence.jpa.repository;

import com.cibertec.anuncio_facil_api.domain.model.AdStatus;
import com.cibertec.anuncio_facil_api.infrastructure.persistence.jpa.entity.AdEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdJpaRepository extends JpaRepository<AdEntity, Long> {
    List<AdEntity> findByUserId(Long userId);

    @Query("""
            SELECT a FROM AdEntity a
            WHERE (:categoryId IS NULL OR a.category.id = :categoryId)
              AND (:city IS NULL OR a.city = :city)
              AND (:district IS NULL OR a.district = :district)
              AND (:status IS NULL OR a.status = :status)
            """)
    Page<AdEntity> findByFilters(@Param("categoryId") Long categoryId,
                                 @Param("city") String city,
                                 @Param("district") String district,
                                 @Param("status") AdStatus status,
                                 Pageable pageable);
}
