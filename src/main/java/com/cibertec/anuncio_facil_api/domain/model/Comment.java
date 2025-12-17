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
public class Comment {
    private Long id;
    private String content;
    private User user;
    private Ad ad;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
