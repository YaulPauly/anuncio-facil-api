package com.cibertec.anuncio_facil_api.config.security;

public class Constants {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String TOKEN_BEARER_PREFIX = "Bearer ";
    public static final String AUTHORITIES_CLAIM = "authorities";
    public static final String NAME_CLAIM = "name";
    public static final String SURNAME_CLAIM = "surname";

    public static final String ENV_JWT_SECRET = "JWT_SECRET";
    public static final String ENV_JWT_EXPIRATION_MS = "JWT_EXPIRATION_MS";
}
