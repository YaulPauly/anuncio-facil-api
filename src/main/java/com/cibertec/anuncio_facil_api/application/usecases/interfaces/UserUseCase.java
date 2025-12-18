package com.cibertec.anuncio_facil_api.application.usecases.interfaces;

import com.cibertec.anuncio_facil_api.application.dto.request.UserStatusRequest;
import com.cibertec.anuncio_facil_api.application.dto.response.UserStatusResponse;
import com.cibertec.anuncio_facil_api.application.dto.response.UserResponse;

import java.util.List;

public interface UserUseCase {
    UserStatusResponse updateStatus(Long userId, UserStatusRequest request);
    List<UserResponse> listAll();
}
