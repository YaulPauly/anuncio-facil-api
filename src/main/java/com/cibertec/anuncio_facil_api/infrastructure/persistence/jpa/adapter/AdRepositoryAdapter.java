package com.cibertec.anuncio_facil_api.infrastructure.persistence.jpa.adapter;

import com.cibertec.anuncio_facil_api.domain.model.Ad;
import com.cibertec.anuncio_facil_api.domain.model.AdDetail;
import com.cibertec.anuncio_facil_api.domain.model.Category;
import com.cibertec.anuncio_facil_api.domain.model.User;
import com.cibertec.anuncio_facil_api.domain.service.interfaces.AdRepository;
import com.cibertec.anuncio_facil_api.infrastructure.persistence.jpa.entity.AdDetailEntity;
import com.cibertec.anuncio_facil_api.infrastructure.persistence.jpa.entity.AdEntity;
import com.cibertec.anuncio_facil_api.infrastructure.persistence.jpa.entity.CategoryEntity;
import com.cibertec.anuncio_facil_api.infrastructure.persistence.jpa.entity.UserEntity;
import com.cibertec.anuncio_facil_api.infrastructure.persistence.jpa.repository.AdJpaRepository;
import com.cibertec.anuncio_facil_api.infrastructure.persistence.jpa.repository.CategoryJpaRepository;
import com.cibertec.anuncio_facil_api.infrastructure.persistence.jpa.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AdRepositoryAdapter implements AdRepository {

    private final AdJpaRepository adJpaRepository;
    private final CategoryJpaRepository categoryJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Override
    public Ad save(Ad ad) {
        AdEntity entity = toEntity(ad);
        AdEntity saved = adJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Ad> findById(Long id) {
        return adJpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Ad> findAll() {
        return adJpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public List<Ad> findByUserId(Long userId) {
        return adJpaRepository.findByUserId(userId).stream().map(this::toDomain).toList();
    }

    @Override
    public Page<Ad> findByFilters(Long categoryId, String city, String district, com.cibertec.anuncio_facil_api.domain.model.AdStatus status, Pageable pageable) {
        return adJpaRepository.findByFilters(categoryId, city, district, status, pageable).map(this::toDomain);
    }

    private Ad toDomain(AdEntity entity) {
        Category category = Category.builder()
                .id(entity.getCategory().getId())
                .name(entity.getCategory().getName())
                .description(entity.getCategory().getDescription())
                .build();

        UserEntity ue = entity.getUser();
        User user = User.builder()
                .id(ue.getId())
                .email(ue.getEmail())
                .firstName(ue.getFirstName())
                .lastName(ue.getLastName())
                .role(com.cibertec.anuncio_facil_api.domain.model.Role.builder()
                        .id(ue.getRole().getId())
                        .name(ue.getRole().getName())
                        .build())
                .build();

        AdDetail detail = entity.getDetail() == null ? null : AdDetail.builder()
                .id(entity.getDetail().getId())
                .content(entity.getDetail().getContent())
                .build();

        return Ad.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .image(entity.getImage())
                .city(entity.getCity())
                .district(entity.getDistrict())
                .status(entity.getStatus())
                .category(category)
                .user(user)
                .detail(detail)
                .createdAt(entity.getCreatedAt())
                .build();
    }

    private AdEntity toEntity(Ad ad) {
        CategoryEntity category = categoryJpaRepository.findById(ad.getCategory().getId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        UserEntity user = userJpaRepository.findById(ad.getUser().getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        AdDetailEntity detail = ad.getDetail() == null ? null :
                AdDetailEntity.builder()
                        .id(ad.getDetail().getId())
                        .content(ad.getDetail().getContent())
                        .build();

        return AdEntity.builder()
                .id(ad.getId())
                .title(ad.getTitle())
                .description(ad.getDescription())
                .image(ad.getImage())
                .city(ad.getCity())
                .district(ad.getDistrict())
                .status(ad.getStatus())
                .category(category)
                .user(user)
                .detail(detail)
                .createdAt(ad.getCreatedAt())
                .build();
    }
}
