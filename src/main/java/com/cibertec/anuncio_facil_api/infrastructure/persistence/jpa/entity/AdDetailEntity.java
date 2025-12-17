package com.cibertec.anuncio_facil_api.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "ad_details")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @JdbcTypeCode(SqlTypes.LONGVARCHAR) // Usa TEXT en Postgres, evita OID/clob
    @Column(columnDefinition = "TEXT")
    private String content;
}
