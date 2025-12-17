package com.cibertec.anuncio_facil_api.domain.service.interfaces;

import com.cibertec.anuncio_facil_api.domain.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    User save(User user);
    boolean existsByEmail(String email);
}
