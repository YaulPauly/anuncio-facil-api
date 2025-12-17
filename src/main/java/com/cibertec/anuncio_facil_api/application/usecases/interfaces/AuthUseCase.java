package com.cibertec.anuncio_facil_api.application.usecases.interfaces;

import com.cibertec.anuncio_facil_api.application.dto.response.AuthResponse;
import com.cibertec.anuncio_facil_api.application.dto.request.RegisterRequest;
import com.cibertec.anuncio_facil_api.application.dto.response.BasicUserResponse;
import com.cibertec.anuncio_facil_api.application.dto.request.LoginRequest;

public interface AuthUseCase {
    AuthResponse login(LoginRequest request);
    BasicUserResponse register(RegisterRequest request);
}
