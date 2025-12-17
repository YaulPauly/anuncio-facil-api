package com.cibertec.anuncio_facil_api.infrastructure.security;

import com.cibertec.anuncio_facil_api.domain.model.Role;
import com.cibertec.anuncio_facil_api.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

import static com.cibertec.anuncio_facil_api.config.security.Constants.AUTHORITIES_CLAIM;
import static com.cibertec.anuncio_facil_api.config.security.Constants.NAME_CLAIM;
import static com.cibertec.anuncio_facil_api.config.security.Constants.SURNAME_CLAIM;
import static com.cibertec.anuncio_facil_api.config.security.Constants.USER_ID_CLAIM;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-ms:86400000}")
    private long expirationMs;

    public String generateToken(User user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);
        List<String> roles = List.of(user.getRole().getName());

        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .claim(NAME_CLAIM, user.getFirstName())
                .claim(SURNAME_CLAIM, user.getLastName())
                .claim(USER_ID_CLAIM, user.getId())
                .claim(AUTHORITIES_CLAIM, roles)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Authentication getAuthentication(Claims claims) {
        List<String> authorities = claims.get(AUTHORITIES_CLAIM, List.class);
        List<SimpleGrantedAuthority> grantedAuthorities = authorities == null ? List.of()
                : authorities.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).toList();

        return new UsernamePasswordAuthenticationToken(claims.getSubject(), null, grantedAuthorities);
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
