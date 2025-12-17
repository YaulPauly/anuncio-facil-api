package com.cibertec.anuncio_facil_api.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ad {
    private Long id;
    private String title;
    private String description;
    private String image;
    private String city;
    private String district;
    private AdStatus status;
    private Category category;
    private User user;
    private AdDetail detail;
    private LocalDateTime createdAt;
}
