CREATE TABLE IF NOT EXISTS roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(120) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(60) NOT NULL,
    last_name VARCHAR(60) NOT NULL,
    role_id BIGINT NOT NULL REFERENCES roles (id),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
);

CREATE TABLE IF NOT EXISTS categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(120) NOT NULL UNIQUE,
    description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS ad_details (
    id BIGSERIAL PRIMARY KEY,
    content TEXT
);

CREATE TABLE IF NOT EXISTS ads (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    description VARCHAR(500) NOT NULL,
    image VARCHAR(255),
    city VARCHAR(120),
    district VARCHAR(120),
    status VARCHAR(20) NOT NULL,
    category_id BIGINT NOT NULL REFERENCES categories (id),
    user_id BIGINT NOT NULL REFERENCES users (id),
    detail_id BIGINT REFERENCES ad_details (id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS comments (
    id BIGSERIAL PRIMARY KEY,
    content VARCHAR(500) NOT NULL,
    user_id BIGINT NOT NULL REFERENCES users (id),
    ad_id BIGINT NOT NULL REFERENCES ads (id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO roles (name) VALUES ('ADMIN') ON CONFLICT (name) DO NOTHING;
INSERT INTO roles (name) VALUES ('USER') ON CONFLICT (name) DO NOTHING;

INSERT INTO users (email, password, first_name, last_name, role_id, status)
VALUES
    ('admin@anuncio.com', '$2y$10$QFDG2EY5n7PU3Hrd8cFRSebbAw0rYvnvPTM0XbEkuN7EkpugzsPgC', 'Admin', 'Anuncio', (SELECT id FROM roles WHERE name = 'ADMIN'), 'ACTIVE'),
    ('user@anuncio.com', '$2y$10$QFDG2EY5n7PU3Hrd8cFRSebbAw0rYvnvPTM0XbEkuN7EkpugzsPgC', 'Usuario', 'Anuncio', (SELECT id FROM roles WHERE name = 'USER'), 'ACTIVE')
ON CONFLICT (email) DO NOTHING;

INSERT INTO categories (id, name, description) VALUES
    (1, 'Tecnologia', 'Equipos y gadgets'),
    (2, 'Hogar', 'Articulos para el hogar')
ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name, description = EXCLUDED.description;

INSERT INTO ad_details (id, content) VALUES
    (1, 'Laptop con 16GB RAM y SSD 512GB'),
    (2, 'Juego de sartenes antiadherentes')
ON CONFLICT (id) DO UPDATE SET content = EXCLUDED.content;

INSERT INTO ads (id, title, description, image, city, district, status, category_id, user_id, detail_id) VALUES
    (1, 'Laptop en buen estado', 'Equipo usado, incluye cargador', NULL, 'Lima', 'Miraflores', 'ACTIVO', 1, (SELECT id FROM users WHERE email = 'admin@anuncio.com'), 1),
    (2, 'Sartenes nuevos', 'Set de 3 piezas', NULL, 'Lima', 'Surco', 'ACTIVO', 2, (SELECT id FROM users WHERE email = 'user@anuncio.com'), 2)
ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title, description = EXCLUDED.description, status = EXCLUDED.status, category_id = EXCLUDED.category_id, user_id = EXCLUDED.user_id, detail_id = EXCLUDED.detail_id;

INSERT INTO comments (id, content, user_id, ad_id) VALUES
    (1, '¿Sigue disponible?', (SELECT id FROM users WHERE email = 'user@anuncio.com'), 1),
    (2, 'Interesado, ¿precio negociable?', (SELECT id FROM users WHERE email = 'admin@anuncio.com'), 2)
ON CONFLICT (id) DO UPDATE SET content = EXCLUDED.content;
