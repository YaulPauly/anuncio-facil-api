USE anunciodb;

CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(120) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(60) NOT NULL,
    last_name VARCHAR(60) NOT NULL,
    role_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    CONSTRAINT fk_users_role FOREIGN KEY (role_id) REFERENCES roles (id)
);

CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(120) NOT NULL UNIQUE,
    description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS ad_details (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content TEXT
);

CREATE TABLE IF NOT EXISTS ads (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    description VARCHAR(500) NOT NULL,
    image VARCHAR(255),
    city VARCHAR(120),
    district VARCHAR(120),
    status VARCHAR(20) NOT NULL,
    category_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    detail_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_ads_category FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT fk_ads_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_ads_detail FOREIGN KEY (detail_id) REFERENCES ad_details (id)
);

CREATE TABLE IF NOT EXISTS comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content VARCHAR(500) NOT NULL,
    user_id BIGINT NOT NULL,
    ad_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_comments_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_comments_ad FOREIGN KEY (ad_id) REFERENCES ads (id)
);

INSERT INTO roles (name) VALUES ('ADMIN'), ('USER')
ON DUPLICATE KEY UPDATE name = VALUES(name);

INSERT INTO users (email, password, first_name, last_name, role_id, status) VALUES
    ('admin@anuncio.com', '$2y$10$QFDG2EY5n7PU3Hrd8cFRSebbAw0rYvnvPTM0XbEkuN7EkpugzsPgC', 'Admin', 'Anuncio', (SELECT id FROM roles WHERE name = 'ADMIN'), 'ACTIVE'),
    ('user@anuncio.com', '$2y$10$QFDG2EY5n7PU3Hrd8cFRSebbAw0rYvnvPTM0XbEkuN7EkpugzsPgC', 'Usuario', 'Anuncio', (SELECT id FROM roles WHERE name = 'USER'), 'ACTIVE')
ON DUPLICATE KEY UPDATE email = VALUES(email);

INSERT INTO categories (id, name, description) VALUES
    (1, 'Tecnologia', 'Equipos y gadgets'),
    (2, 'Hogar', 'Articulos para el hogar')
ON DUPLICATE KEY UPDATE name = VALUES(name), description = VALUES(description);

INSERT INTO ad_details (id, content) VALUES
    (1, 'Laptop con 16GB RAM y SSD 512GB'),
    (2, 'Juego de sartenes antiadherentes')
ON DUPLICATE KEY UPDATE content = VALUES(content);

INSERT INTO ads (id, title, description, image, city, district, status, category_id, user_id, detail_id) VALUES
    (1, 'Laptop en buen estado', 'Equipo usado, incluye cargador', NULL, 'Lima', 'Miraflores', 'ACTIVO', 1, (SELECT id FROM users WHERE email = 'admin@anuncio.com'), 1),
    (2, 'Sartenes nuevos', 'Set de 3 piezas', NULL, 'Lima', 'Surco', 'ACTIVO', 2, (SELECT id FROM users WHERE email = 'user@anuncio.com'), 2)
ON DUPLICATE KEY UPDATE title = VALUES(title), description = VALUES(description), status = VALUES(status), category_id = VALUES(category_id), user_id = VALUES(user_id), detail_id = VALUES(detail_id);

INSERT INTO comments (id, content, user_id, ad_id) VALUES
    (1, '¿Sigue disponible?', (SELECT id FROM users WHERE email = 'user@anuncio.com'), 1),
    (2, 'Interesado, ¿precio negociable?', (SELECT id FROM users WHERE email = 'admin@anuncio.com'), 2)
ON DUPLICATE KEY UPDATE content = VALUES(content);
