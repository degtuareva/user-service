DELETE FROM address;
DELETE FROM users;

INSERT INTO users (id, login, password, role)
VALUES (5, 'testLogin', 'testPassword', 'USER');

INSERT INTO address (id, user_id, country, city, street, postal_code, house, housing, apartment)
VALUES (10, 5, 'testCountry', 'testCity', 'testStreet', 'testPostalCode', 1, 'testHousing', 1);