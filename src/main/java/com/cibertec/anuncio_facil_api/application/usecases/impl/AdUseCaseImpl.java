package com.cibertec.anuncio_facil_api.application.usecases.impl;

import com.cibertec.anuncio_facil_api.application.dto.request.AdRequest;
import com.cibertec.anuncio_facil_api.application.dto.response.AdResponse;
import com.cibertec.anuncio_facil_api.application.dto.response.BasicUserResponse;
import com.cibertec.anuncio_facil_api.application.dto.response.CategoryResponse;
import com.cibertec.anuncio_facil_api.application.dto.response.PagedResponse;
import com.cibertec.anuncio_facil_api.application.service.CurrentUserService;
import com.cibertec.anuncio_facil_api.application.usecases.interfaces.AdUseCase;
import com.cibertec.anuncio_facil_api.domain.model.Ad;
import com.cibertec.anuncio_facil_api.domain.model.AdDetail;
import com.cibertec.anuncio_facil_api.domain.model.AdStatus;
import com.cibertec.anuncio_facil_api.domain.model.Category;
import com.cibertec.anuncio_facil_api.domain.model.User;
import com.cibertec.anuncio_facil_api.domain.service.interfaces.AdRepository;
import com.cibertec.anuncio_facil_api.domain.service.interfaces.CategoryRepository;
import com.cibertec.anuncio_facil_api.domain.service.interfaces.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdUseCaseImpl implements AdUseCase {

    private final AdRepository adRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    @Override
    public AdResponse create(AdRequest request) {
        User user = getCurrentUser();
        Category category = findCategoryOrThrow(request.categoryId());

        Ad ad = Ad.builder()
                .title(request.title())
                .description(request.description())
                .image(request.image())
                .city(request.city())
                .district(request.district())
                .status(AdStatus.ACTIVO)
                .category(category)
                .user(user)
                .detail(AdDetail.builder().content(request.detail()).build())
                .createdAt(LocalDateTime.now())
                .build();

        return toResponse(adRepository.save(ad));
    }

    @Override
    public AdResponse update(Long adId, AdRequest request) {
        Ad ad = findAdOrThrow(adId);
        ensureOwnershipOrAdmin(ad.getUser());
        Category category = findCategoryOrThrow(request.categoryId());

        String imageToUse = request.image() != null ? request.image() : ad.getImage();

        ad.setTitle(request.title());
        ad.setDescription(request.description());
        ad.setImage(imageToUse);
        ad.setCity(request.city());
        ad.setDistrict(request.district());
        ad.setCategory(category);
        if (ad.getDetail() == null) {
            ad.setDetail(new AdDetail());
        }
        ad.getDetail().setContent(request.detail());

        return toResponse(adRepository.save(ad));
    }

    @Override
    public AdResponse toggleStatus(Long adId, AdStatus status) {
        Ad ad = findAdOrThrow(adId);
        ensureOwnershipOrAdmin(ad.getUser());
        ad.setStatus(status);
        return toResponse(adRepository.save(ad));
    }

    @Override
    public AdResponse getById(Long adId) {
        Ad ad = findAdOrThrow(adId);
        return toResponse(ad);
    }

    @Override
    public PagedResponse<AdResponse> list(Long categoryId, String city, String district, AdStatus status, int page, int size) {
        Page<Ad> adsPage = adRepository.findByFilters(categoryId, city, district, status, PageRequest.of(page, size));
        List<AdResponse> content = adsPage.getContent().stream().map(this::toResponse).toList();
        return new PagedResponse<>(
                content,
                adsPage.getNumber(),
                adsPage.getSize(),
                adsPage.getTotalElements(),
                adsPage.getTotalPages(),
                adsPage.hasNext(),
                adsPage.hasPrevious()
        );
    }

    @Override
    public List<AdResponse> listByCurrentUser() {
        User user = getCurrentUser();
        return adRepository.findByUserId(user.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private User getCurrentUser() {
        String email = currentUserService.getCurrentUserEmail();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    private Category findCategoryOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    }

    private Ad findAdOrThrow(Long id) {
        return adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Anuncio no encontrado"));
    }

    private void ensureOwnershipOrAdmin(User owner) {
        User current = getCurrentUser();
        boolean isOwner = owner.getId().equals(current.getId());
        boolean isAdmin = current.getRole() != null && "ADMIN".equalsIgnoreCase(current.getRole().getName());
        if (!isOwner && !isAdmin) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No autorizado para modificar este recurso");
        }
    }

    private AdResponse toResponse(Ad ad) {
        CategoryResponse category = ad.getCategory() == null ? null :
                new CategoryResponse(ad.getCategory().getId(), ad.getCategory().getName(), ad.getCategory().getDescription());

        BasicUserResponse user = ad.getUser() == null ? null :
                new BasicUserResponse(ad.getUser().getId(), ad.getUser().getEmail(), ad.getUser().getFirstName(), ad.getUser().getLastName());

        String detail = ad.getDetail() == null ? null : ad.getDetail().getContent();

        return new AdResponse(
                ad.getId(),
                ad.getTitle(),
                ad.getDescription(),
                ad.getImage(),
                ad.getCity(),
                ad.getDistrict(),
                ad.getStatus(),
                category,
                user,
                detail,
                ad.getCreatedAt()
        );
    }
}
