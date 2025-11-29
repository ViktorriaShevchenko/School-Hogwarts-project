/* PostgreSQL */
-- Создание таблиц "человек" и "машина"

CREATE TABLE car (
    id BIGSERIAL PRIMARY KEY,
    brand VARCHAR(50) NOT NULL,
    model VARCHAR(50) NOT NULL,
    price DECIMAL(10, 2) NOT NULL
);

CREATE TABLE person (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INTEGER NOT NULL,
    has_license BOOLEAN NOT NULL,
    car_id BIGINT,
    CONSTRAINT fk_person_car FOREIGN KEY (car_id) REFERENCES car(id)
);