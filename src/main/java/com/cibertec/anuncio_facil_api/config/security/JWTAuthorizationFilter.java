package com.cibertec.anuncio_facil_api.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.security.SignatureException;

import java.io.IOException;
import java.util.Optional;

import static com.cibertec.anuncio_facil_api.config.security.Constants.AUTHORIZATION_HEADER;
import static com.cibertec.anuncio_facil_api.config.security.Constants.TOKEN_BEARER_PREFIX;
import com.cibertec.anuncio_facil_api.infrastructure.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            if (isJWTValid(request)) {
                Claims claims = getClaims(request);
                SecurityContextHolder.getContext().setAuthentication(jwtTokenProvider.getAuthentication(claims));
            } else {
                SecurityContextHolder.clearContext();
            }
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, Optional.ofNullable(e.getMessage()).orElse("Token inválido"));
        }

        //Indica a Spring que continue con los demas filtros
        chain.doFilter(request, response);
    }

    private Claims getClaims(HttpServletRequest request) {
        String jwtToken = request.getHeader(AUTHORIZATION_HEADER).replace(TOKEN_BEARER_PREFIX, "").trim();
        return jwtTokenProvider.getClaims(jwtToken);
    }

    private boolean isJWTValid(HttpServletRequest request) {
        String authenticationHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (authenticationHeader == null || !authenticationHeader.startsWith(TOKEN_BEARER_PREFIX)) {
            return false;
        }
        String token = authenticationHeader.replace(TOKEN_BEARER_PREFIX, "").trim();
        return !token.isEmpty() && !"null".equalsIgnoreCase(token);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/auth") || "OPTIONS".equalsIgnoreCase(request.getMethod());
    }
}
