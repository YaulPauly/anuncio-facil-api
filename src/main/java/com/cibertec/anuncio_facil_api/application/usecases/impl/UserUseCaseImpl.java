package com.cibertec.anuncio_facil_api.application.usecases.impl;

import com.cibertec.anuncio_facil_api.application.dto.request.UserStatusRequest;
import com.cibertec.anuncio_facil_api.application.dto.response.UserStatusResponse;
import com.cibertec.anuncio_facil_api.application.usecases.interfaces.UserUseCase;
import com.cibertec.anuncio_facil_api.domain.model.User;
import com.cibertec.anuncio_facil_api.domain.model.UserStatus;
import com.cibertec.anuncio_facil_api.domain.service.interfaces.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserUseCaseImpl implements UserUseCase {

    private final UserRepository userRepository;

    @Override
    public UserStatusResponse updateStatus(Long userId, UserStatusRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        if ("ADMIN".equalsIgnoreCase(user.getRole().getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No se puede bloquear/desbloquear usuarios con rol ADMIN");
        }

        user.setStatus(request.status());
        User saved = userRepository.save(user);
        String msg = saved.getStatus() == UserStatus.BLOCKED ? "Usuario bloqueado" : "Usuario activado";
        return new UserStatusResponse(saved.getId(), saved.getEmail(), saved.getStatus(), msg);
    }
}
