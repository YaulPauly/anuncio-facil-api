package com.cibertec.anuncio_facil_api.domain.service.interfaces;

import com.cibertec.anuncio_facil_api.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    List<User> findAll();
    User save(User user);
    boolean existsByEmail(String email);
}
