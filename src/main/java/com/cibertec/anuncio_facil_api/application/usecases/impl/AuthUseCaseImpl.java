package com.cibertec.anuncio_facil_api.application.usecases.impl;

import com.cibertec.anuncio_facil_api.application.dto.response.AuthResponse;
import com.cibertec.anuncio_facil_api.application.dto.request.LoginRequest;
import com.cibertec.anuncio_facil_api.application.dto.request.RegisterRequest;
import com.cibertec.anuncio_facil_api.application.dto.response.BasicUserResponse;
import com.cibertec.anuncio_facil_api.application.usecases.interfaces.AuthUseCase;
import com.cibertec.anuncio_facil_api.domain.model.User;
import com.cibertec.anuncio_facil_api.domain.model.UserStatus;
import com.cibertec.anuncio_facil_api.domain.model.Role;
import com.cibertec.anuncio_facil_api.domain.service.interfaces.UserRepository;
import com.cibertec.anuncio_facil_api.infrastructure.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
@RequiredArgsConstructor
public class AuthUseCaseImpl implements AuthUseCase {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );
            if (!authentication.isAuthenticated()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
            }
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        }

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas"));

        String token = jwtTokenProvider.generateToken(user);

        return new AuthResponse(
                token,
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole().getName()
        );
    }

    @Override
    public BasicUserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya está registrado");
        }

        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .firstName(request.firstName())
                .lastName(request.lastName())
                .role(Role.builder().name("USER").build())
                .status(UserStatus.ACTIVE)
                .build();

        User saved = userRepository.save(user);
        return new BasicUserResponse(saved.getId(), saved.getEmail(), saved.getFirstName(), saved.getLastName());
    }
}
