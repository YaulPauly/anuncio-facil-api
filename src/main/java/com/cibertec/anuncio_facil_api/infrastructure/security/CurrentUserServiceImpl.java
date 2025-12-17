package com.cibertec.anuncio_facil_api.infrastructure.security;

import com.cibertec.anuncio_facil_api.application.service.CurrentUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserServiceImpl implements CurrentUserService {
    @Override
    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new RuntimeException("No hay usuario autenticado");
        }
        return authentication.getName();
    }
}
