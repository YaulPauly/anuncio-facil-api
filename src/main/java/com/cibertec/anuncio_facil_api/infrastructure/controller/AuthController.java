package com.cibertec.anuncio_facil_api.infrastructure.controller;

import com.cibertec.anuncio_facil_api.application.dto.response.AuthResponse;
import com.cibertec.anuncio_facil_api.application.dto.request.LoginRequest;
import com.cibertec.anuncio_facil_api.application.dto.request.RegisterRequest;
import com.cibertec.anuncio_facil_api.application.dto.response.BasicUserResponse;
import com.cibertec.anuncio_facil_api.application.usecases.interfaces.AuthUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthUseCase authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<BasicUserResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
}
