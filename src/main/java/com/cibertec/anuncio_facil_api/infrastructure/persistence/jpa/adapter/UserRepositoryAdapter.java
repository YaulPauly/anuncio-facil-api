package com.cibertec.anuncio_facil_api.infrastructure.persistence.jpa.adapter;

import com.cibertec.anuncio_facil_api.domain.model.Role;
import com.cibertec.anuncio_facil_api.domain.model.User;
import com.cibertec.anuncio_facil_api.domain.model.UserStatus;
import com.cibertec.anuncio_facil_api.domain.service.interfaces.UserRepository;
import com.cibertec.anuncio_facil_api.infrastructure.persistence.jpa.entity.RoleEntity;
import com.cibertec.anuncio_facil_api.infrastructure.persistence.jpa.entity.UserEntity;
import com.cibertec.anuncio_facil_api.infrastructure.persistence.jpa.repository.RoleJpaRepository;
import com.cibertec.anuncio_facil_api.infrastructure.persistence.jpa.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final RoleJpaRepository roleJpaRepository;

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .map(this::toDomain);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public User save(User user) {
        UserEntity entity = toEntity(user);
        UserEntity saved = userJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    private User toDomain(UserEntity entity) {
        Role role = Role.builder()
                .id(entity.getRole().getId())
                .name(entity.getRole().getName())
                .build();

        return User.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .role(role)
                .status(entity.getStatus())
                .build();
    }

    private UserEntity toEntity(User user) {
        RoleEntity role = getOrCreateRole(user.getRole());

        return UserEntity.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(role)
                .status(user.getStatus())
                .build();
    }

    private RoleEntity getOrCreateRole(Role role) {
        return roleJpaRepository.findByName(role.getName())
                .orElseGet(() -> roleJpaRepository.save(RoleEntity.builder().name(role.getName()).build()));
    }
}
