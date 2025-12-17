package com.cibertec.anuncio_facil_api.infrastructure.persistence.jpa.entity;

import com.cibertec.anuncio_facil_api.domain.model.AdStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "ads")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 500)
    private String description;

    private String image;
    private String city;
    private String district;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AdStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "detail_id")
    private AdDetailEntity detail;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
