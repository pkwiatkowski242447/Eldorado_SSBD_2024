USE ssbd03;

-- INSERT VALUE
-- Admin
INSERT INTO account (id, creation_timestamp, login, password, suspended, active, two_factor_auth, language, phone_number, version, blocked) VALUES (UUID_TO_BIN('b3b8c2ac-21ff-434b-b490-aa6d717447c0'), current_timestamp, 'jerzybem', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', false, true, false, 'PL', '111111111', 0, false);
INSERT INTO past_password(account_id, past_password) VALUES (UUID_TO_BIN('b3b8c2ac-21ff-434b-b490-aa6d717447c0'), '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa');
INSERT INTO personal_data (id, name, lastname, email) VALUES (UUID_TO_BIN('b3b8c2ac-21ff-434b-b490-aa6d717447c0'), 'Jerzy', 'Bem', 'jerzybem@spoko.pl');

-- Admin - admin role
INSERT INTO user_level (id, creation_timestamp, level, account_id, version) VALUES (UUID_TO_BIN('bdb52e59-0054-4ec5-a2af-3e0d2b187ce0'), current_timestamp, 'ADMIN', UUID_TO_BIN('b3b8c2ac-21ff-434b-b490-aa6d717447c0'), 0);
INSERT INTO admin_data (id) VALUES (UUID_TO_BIN('bdb52e59-0054-4ec5-a2af-3e0d2b187ce0'));

-- Admin - staff role
INSERT INTO user_level (id, creation_timestamp, level, account_id, version) VALUES (UUID_TO_BIN('9ba0d086-edec-475d-8569-69156e79be9d'), current_timestamp, 'STAFF', UUID_TO_BIN('b3b8c2ac-21ff-434b-b490-aa6d717447c0'), 0);
INSERT INTO staff_data (id) VALUES (UUID_TO_BIN('9ba0d086-edec-475d-8569-69156e79be9d'));

-- Staff
INSERT INTO account (id, creation_timestamp, login, password, suspended, active, two_factor_auth, language, phone_number, version, blocked) VALUES (UUID_TO_BIN('e0bf979b-6b42-432d-8462-544d88b1ab5f'), current_timestamp, 'kamilslimak', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', false, true, false, 'PL', '222222222', 0, false);
INSERT INTO past_password(account_id, past_password) VALUES (UUID_TO_BIN('e0bf979b-6b42-432d-8462-544d88b1ab5f'), '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa');
INSERT INTO personal_data (id, name, lastname, email) VALUES (UUID_TO_BIN('e0bf979b-6b42-432d-8462-544d88b1ab5f'), 'Kamil', 'Slimak', 'kamilslimak1@spoko.pl');

INSERT INTO user_level (id, creation_timestamp, level, account_id, version) VALUES (UUID_TO_BIN('2488831d-c7c4-4f61-b48a-3be87364271f'), current_timestamp, 'STAFF', UUID_TO_BIN('e0bf979b-6b42-432d-8462-544d88b1ab5f'), 0);
INSERT INTO staff_data (id) VALUES (UUID_TO_BIN('2488831d-c7c4-4f61-b48a-3be87364271f'));

-- First Client
INSERT INTO account (id, creation_timestamp, login, password, suspended, active, two_factor_auth, language, phone_number, version, blocked) VALUES (UUID_TO_BIN('0ca02f7e-d8e9-45d3-a332-a56015acb822'), current_timestamp, 'michalkowal', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', false, true, false, 'PL', '000000000', 0, false);
INSERT INTO past_password(account_id, past_password) VALUES (UUID_TO_BIN('0ca02f7e-d8e9-45d3-a332-a56015acb822'), '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa');
INSERT INTO personal_data (id, name, lastname, email) VALUES (UUID_TO_BIN('0ca02f7e-d8e9-45d3-a332-a56015acb822'), 'Michal', 'Kowalski', 'michalkowal@spoko.pl');

INSERT INTO user_level (id, creation_timestamp, level, account_id, version) VALUES (UUID_TO_BIN('69507c7f-4c03-4087-85e6-3ae3b6fc2201'), current_timestamp, 'CLIENT', UUID_TO_BIN('0ca02f7e-d8e9-45d3-a332-a56015acb822'), 0);
INSERT INTO client_data (id, type, total_reservation_hours) VALUES (UUID_TO_BIN('69507c7f-4c03-4087-85e6-3ae3b6fc2201'), 'BASIC', 0);

-- Second Client
INSERT INTO account (id, creation_timestamp, login, password, suspended, active, two_factor_auth, language, phone_number, version, blocked) VALUES (UUID_TO_BIN('902d6e5b-2449-4898-a4f7-a92b0d8a04e1'), current_timestamp, 'jakubkoza', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', false, true, false, 'EN', '000000001', 0, false);
INSERT INTO past_password(account_id, past_password) VALUES (UUID_TO_BIN('902d6e5b-2449-4898-a4f7-a92b0d8a04e1'), '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa');
INSERT INTO personal_data (id, name, lastname, email) VALUES (UUID_TO_BIN('902d6e5b-2449-4898-a4f7-a92b0d8a04e1'), 'Jakub', 'Koza', 'jakubkoza@adresik.net');

INSERT INTO user_level (id, creation_timestamp, level, account_id, version) VALUES (UUID_TO_BIN('9428fadf-191c-4dd7-8626-01c3e0ff603c'), current_timestamp, 'CLIENT', UUID_TO_BIN('902d6e5b-2449-4898-a4f7-a92b0d8a04e1'), 0);
INSERT INTO client_data (id, type, total_reservation_hours) VALUES (UUID_TO_BIN('9428fadf-191c-4dd7-8626-01c3e0ff603c'), 'BASIC', 0);

-- Third Client
INSERT INTO account (id, creation_timestamp, login, password, suspended, active, two_factor_auth, language, phone_number, version, blocked) VALUES (UUID_TO_BIN('02b0d9d7-a472-48d0-95e0-13a3e6a11d00'), current_timestamp, 'piotrnowak', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', false, true, false, 'PL', '000000003', 0, false);
INSERT INTO past_password(account_id, past_password) VALUES (UUID_TO_BIN('02b0d9d7-a472-48d0-95e0-13a3e6a11d00'), '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa');
INSERT INTO personal_data (id, name, lastname, email) VALUES (UUID_TO_BIN('02b0d9d7-a472-48d0-95e0-13a3e6a11d00'), 'Piotr', 'Nowak', 'piotrnowak1@adresik.net');

INSERT INTO user_level (id, creation_timestamp, level, account_id, version) VALUES (UUID_TO_BIN('cbf34cb0-c96b-4037-80d4-1eef34890e85'), current_timestamp, 'CLIENT', UUID_TO_BIN('02b0d9d7-a472-48d0-95e0-13a3e6a11d00'), 0);
INSERT INTO client_data (id, type, total_reservation_hours) VALUES (UUID_TO_BIN('cbf34cb0-c96b-4037-80d4-1eef34890e85'), 'BASIC', 0);

-- Admin - test
INSERT INTO account (id, creation_timestamp, login, password, suspended, active, two_factor_auth, language, phone_number, version, blocked) VALUES (UUID_TO_BIN('9a333f13-5ccc-4109-bce3-0ad629843edf'), current_timestamp, 'aandrus', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', false, true, false, 'pl', '111111112', 0, false);
INSERT INTO personal_data (id, name, lastname, email) VALUES (UUID_TO_BIN('9a333f13-5ccc-4109-bce3-0ad629843edf'), 'Andrzej', 'Andrus', 'aandrus@example.com');
INSERT INTO user_level (id, creation_timestamp, level, account_id, version) VALUES (UUID_TO_BIN('c21bda59-3ed9-4370-8675-3b53ddb04f4e'), current_timestamp, 'ADMIN', UUID_TO_BIN('9a333f13-5ccc-4109-bce3-0ad629843edf'), 0);
INSERT INTO admin_data (id) VALUES (UUID_TO_BIN('c21bda59-3ed9-4370-8675-3b53ddb04f4e'));

-- Staff - test
INSERT INTO account (id, creation_timestamp, login, password, suspended, active, two_factor_auth, language, phone_number, version, blocked) VALUES (UUID_TO_BIN('f512c0b6-40b2-4bcb-8541-46077ac02101'), current_timestamp, 'tkarol', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', false, true, false, 'pl', '111111113', 0, false);
INSERT INTO personal_data (id, name, lastname, email) VALUES (UUID_TO_BIN('f512c0b6-40b2-4bcb-8541-46077ac02101'), 'Tomasz', 'Karolak', 'tkarol@example.com');
INSERT INTO user_level (id, creation_timestamp, level, account_id, version) VALUES (UUID_TO_BIN('00568964-9f83-441e-b441-83e545d51733'), current_timestamp, 'STAFF', UUID_TO_BIN('f512c0b6-40b2-4bcb-8541-46077ac02101'), 0);
INSERT INTO staff_data (id) VALUES (UUID_TO_BIN('00568964-9f83-441e-b441-83e545d51733'));

-- Staff - test
INSERT INTO account (id, creation_timestamp, login, password, suspended, active, two_factor_auth, language, phone_number, version, blocked) VALUES (UUID_TO_BIN('f14ac5b1-16f3-42ff-8df3-dd95de69c368'), current_timestamp, 'kwotyla', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', false, true, false, 'pl', '111111114', 0, false);
INSERT INTO personal_data (id, name, lastname, email) VALUES (UUID_TO_BIN('f14ac5b1-16f3-42ff-8df3-dd95de69c368'), 'Krystian', 'Womyla', 'krystianwomyla@example.com');
INSERT INTO user_level (id, creation_timestamp, level, account_id, version) VALUES (UUID_TO_BIN('248a31fa-7fef-41d5-8042-e70a38d30a9d'), current_timestamp, 'CLIENT', UUID_TO_BIN('f14ac5b1-16f3-42ff-8df3-dd95de69c368'), 0);
INSERT INTO client_data (id, type, total_reservation_hours) VALUES (UUID_TO_BIN('248a31fa-7fef-41d5-8042-e70a38d30a9d'), 'BASIC', 0);

-- Parking
INSERT INTO parking (id, creation_timestamp, sector_strategy, zip_code, city, street, version) VALUES (UUID_TO_BIN('96a36faa-f2a2-41b8-9c3c-b6bef04ce6d1'), current_timestamp, 'LEAST_OCCUPIED', '91-416', 'Łódź', 'Palki', 0);
INSERT INTO parking (id, creation_timestamp, sector_strategy, zip_code, city, street, version) VALUES (UUID_TO_BIN('a54e7ae6-ba2c-4fac-8ef6-e9d27da48921'), current_timestamp, 'LEAST_OCCUPIED', '75-626', 'Łódź', 'Pomorska', 0);
INSERT INTO parking (id, creation_timestamp, sector_strategy, zip_code, city, street, version) VALUES (UUID_TO_BIN('ddcae4ec-aeb5-4ece-aa2b-46819763d55f'), current_timestamp, 'LEAST_OCCUPIED', '95-010', 'Stryków', 'Krótka', 0);

-- First Sector S1
INSERT INTO sector (id, creation_timestamp ,occupied_places, max_places, weight, name, parking_id, type, version, deactivation_time) VALUES (UUID_TO_BIN('3e6a85db-d751-4549-bbb7-9705f0b2fa6b'), current_timestamp, 0, 50, 1, 'SA-01', UUID_TO_BIN('96a36faa-f2a2-41b8-9c3c-b6bef04ce6d1'), 'UNCOVERED', 0, current_timestamp - interval 2 hour);

-- Second Sector S1
INSERT INTO sector (id, creation_timestamp, occupied_places, max_places, weight, name, parking_id, type, version) VALUES (UUID_TO_BIN('9f7f2969-1b7e-4bb3-ab84-6dbc31c01277'), current_timestamp, 0, 50, 1, 'SB-01', UUID_TO_BIN('96a36faa-f2a2-41b8-9c3c-b6bef04ce6d1'), 'UNCOVERED', 0);

-- Third Sector S1
INSERT INTO sector (id, creation_timestamp, occupied_places, max_places, weight, name, parking_id, type, version) VALUES (UUID_TO_BIN('99023647-d8d2-43a5-91c7-b24781f06e13'), current_timestamp, 0, 50, 1, 'SB-02', UUID_TO_BIN('a54e7ae6-ba2c-4fac-8ef6-e9d27da48921'), 'UNCOVERED', 0);

-- Fourth Sector S1
INSERT INTO sector (id, creation_timestamp, occupied_places, max_places, weight, name, parking_id, type, version) VALUES (UUID_TO_BIN('bca50310-f4fb-4911-bf3c-68e00e517b95'), current_timestamp, 0, 50, 1, 'SB-03', UUID_TO_BIN('a54e7ae6-ba2c-4fac-8ef6-e9d27da48921'), 'UNCOVERED', 0);

-- Fifth Sector S1
INSERT INTO sector (id, creation_timestamp, occupied_places, max_places, weight, name, parking_id, type, version) VALUES (UUID_TO_BIN('82b4ad91-a5ef-48bf-bc5b-390d7f4d1dea'), current_timestamp, 0, 50, 1, 'SB-04', UUID_TO_BIN('a54e7ae6-ba2c-4fac-8ef6-e9d27da48921'), 'COVERED', 0);

-- Sixth Sector S1
INSERT INTO sector (id, creation_timestamp, occupied_places, max_places, weight, name, parking_id, type, version) VALUES (UUID_TO_BIN('36e19dcd-1d5b-4258-a5df-ba9f4372c58c'), current_timestamp, 0, 50, 1, 'SB-05', UUID_TO_BIN('a54e7ae6-ba2c-4fac-8ef6-e9d27da48921'), 'UNDERGROUND', 0);

-- Seventh Sector S2
INSERT INTO sector (id, creation_timestamp, occupied_places, max_places, weight, name, parking_id, type, version) VALUES (UUID_TO_BIN('4ce920a0-6f4d-4e95-ba24-99ba32b66491'), current_timestamp, 0, 60, 2, 'SA-02', UUID_TO_BIN('96a36faa-f2a2-41b8-9c3c-b6bef04ce6d1'), 'UNCOVERED', 0);

-- Eighth Sector S3
INSERT INTO sector (id, creation_timestamp, occupied_places, max_places, weight, name, parking_id, type, version) VALUES (UUID_TO_BIN('c51557aa-284d-44a6-b38d-b6ceb9c23725'), current_timestamp, 0, 70, 3, 'SA-03', UUID_TO_BIN('96a36faa-f2a2-41b8-9c3c-b6bef04ce6d1'), 'UNCOVERED', 0);

-- First Reservation (for michalkowal)
INSERT INTO reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES (UUID_TO_BIN('90a0035d-6265-4b53-a547-901b3bbabd1d'), current_timestamp, TIMESTAMP '2024-04-10 07:00:00', TIMESTAMP '2024-04-10 09:00:00', UUID_TO_BIN('69507c7f-4c03-4087-85e6-3ae3b6fc2201'), UUID_TO_BIN('3e6a85db-d751-4549-bbb7-9705f0b2fa6b'), 0, 'COMPLETED_MANUALLY');

-- Second Reservation (for michalkowal)
INSERT INTO reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES (UUID_TO_BIN('9666e04e-636e-44de-b16e-d4698530318d'), TIMESTAMP '2024-05-01 00:00:00', TIMESTAMP '2024-05-08 10:00:00', TIMESTAMP '2024-05-08 18:00:00', UUID_TO_BIN('69507c7f-4c03-4087-85e6-3ae3b6fc2201'), UUID_TO_BIN('3e6a85db-d751-4549-bbb7-9705f0b2fa6b'), 0, 'TERMINATED');

-- Third Reservation (for michalkowal)
INSERT INTO reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES (UUID_TO_BIN('2fb9509f-7aea-431d-bc8f-4a997b404946'), TIMESTAMP '2024-05-12 00:00:00', TIMESTAMP '2024-05-14 08:00:00', TIMESTAMP '2024-05-14 16:00:00', UUID_TO_BIN('69507c7f-4c03-4087-85e6-3ae3b6fc2201'), UUID_TO_BIN('3e6a85db-d751-4549-bbb7-9705f0b2fa6b'), 0, 'CANCELLED');

-- Reservation for jakubkoza - expired
INSERT INTO reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES (UUID_TO_BIN('1ec7d685-71ac-4418-834a-ed7b6fc68fc8'), current_timestamp, TIMESTAMP '2024-04-15 12:00:00', TIMESTAMP '2024-04-15 13:00:00', UUID_TO_BIN('9428fadf-191c-4dd7-8626-01c3e0ff603c'), UUID_TO_BIN('4ce920a0-6f4d-4e95-ba24-99ba32b66491'), 0, 'COMPLETED_AUTOMATICALLY');

-- Reservation for jakubkoza - valid
INSERT INTO reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES (UUID_TO_BIN('8c3c21c4-b332-4d72-9390-b0b6ad41d173'), current_timestamp, current_timestamp, current_timestamp + interval 4 hour, UUID_TO_BIN('9428fadf-191c-4dd7-8626-01c3e0ff603c'), UUID_TO_BIN('4ce920a0-6f4d-4e95-ba24-99ba32b66491'), 0, 'COMPLETED_MANUALLY');

-- Reservation for jakubkoza - not started yet
INSERT INTO reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES (UUID_TO_BIN('4e892a98-ba3d-47da-aa56-26ad52746280'), current_timestamp, current_timestamp + interval 6 day, current_timestamp + interval 7 day, UUID_TO_BIN('9428fadf-191c-4dd7-8626-01c3e0ff603c'), UUID_TO_BIN('4ce920a0-6f4d-4e95-ba24-99ba32b66491'), 0, 'COMPLETED_MANUALLY');

-- Third Reservation
INSERT INTO reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES (UUID_TO_BIN('a7709a4d-b7bc-40c4-8fd5-5c7cfcb0f146'), current_timestamp, TIMESTAMP '2024-03-01 15:00:00', TIMESTAMP '2024-03-01 16:00:00', null, UUID_TO_BIN('c51557aa-284d-44a6-b38d-b6ceb9c23725'), 0, 'COMPLETED_MANUALLY');

-- First token
INSERT INTO token (id, creation_timestamp, account_id, token_value, type, version) VALUES (UUID_TO_BIN('4ac79a06-2b75-4519-b430-1abe0e05f04e'), current_timestamp, UUID_TO_BIN('0ca02f7e-d8e9-45d3-a332-a56015acb822'), 'TEST_VALUE', 'CONFIRM_EMAIL', 0);

-- Second token
INSERT INTO token (id, creation_timestamp, account_id, token_value, type, version) VALUES (UUID_TO_BIN('499c0085-c0b8-424e-97f5-84abe66f9bf6'), current_timestamp, UUID_TO_BIN('902d6e5b-2449-4898-a4f7-a92b0d8a04e1'), 'TEST_VALUE2', 'RESET_PASSWORD', 0);

-- Third token
INSERT INTO token (id, creation_timestamp, account_id, token_value, type, version) VALUES (UUID_TO_BIN('b269207d-7627-4a91-af86-87d2b975d487'), current_timestamp, UUID_TO_BIN('e0bf979b-6b42-432d-8462-544d88b1ab5f'), 'TEST_VALUE3', 'CHANGE_OVERWRITTEN_PASSWORD', 0);

-- Parking event - Reservation 1
INSERT INTO parking_event (id, reservation_id, date, type, version) VALUES (UUID_TO_BIN('b922b5c3-08a8-4902-8ca0-e99e65506fa5'), UUID_TO_BIN('90a0035d-6265-4b53-a547-901b3bbabd1d'), TIMESTAMP '2024-04-10 07:00:00', 'ENTRY', 0);
INSERT INTO parking_event (id, reservation_id, date, type, version) VALUES (UUID_TO_BIN('eb6878af-7bb8-4094-96fc-84126e2dfb5e'), UUID_TO_BIN('90a0035d-6265-4b53-a547-901b3bbabd1d'), TIMESTAMP '2024-04-10 09:00:00', 'EXIT', 0);

-- Parking event - Reservation 3
INSERT INTO parking_event (id, reservation_id, date, type, version) VALUES (UUID_TO_BIN('12c8f77a-4a35-43f6-9134-4deddd5407b5'), UUID_TO_BIN('a7709a4d-b7bc-40c4-8fd5-5c7cfcb0f146'), TIMESTAMP '2024-03-01 15:00:00', 'ENTRY', 0);
INSERT INTO parking_event (id, reservation_id, date, type, version) VALUES (UUID_TO_BIN('94d17bd1-da2e-4047-822b-942526dfbd5a'), UUID_TO_BIN('a7709a4d-b7bc-40c4-8fd5-5c7cfcb0f146'), TIMESTAMP '2024-03-01 16:00:00', 'EXIT', 0);

-- Not suspended User 1 - Client
INSERT INTO account (id, creation_timestamp, login, password, suspended, active, two_factor_auth, language, phone_number, version, blocked) VALUES (UUID_TO_BIN('f5afc042-79b0-47fe-87ee-710c14af888c'), current_timestamp, 'tonyhalik', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', false, true, false, 'PL', '100000000', 0, false);

INSERT INTO personal_data (id, name, lastname, email) VALUES (UUID_TO_BIN('f5afc042-79b0-47fe-87ee-710c14af888c'), 'Tony', 'Halik', 'tonyhalik@example.com');

INSERT INTO user_level (id, creation_timestamp, level, account_id, version) VALUES (UUID_TO_BIN('4d1c0c8a-3af5-4ce9-ba95-09d25d289a76'), current_timestamp, 'CLIENT', UUID_TO_BIN('f5afc042-79b0-47fe-87ee-710c14af888c'), 0);
INSERT INTO client_data (id, type, total_reservation_hours) VALUES (UUID_TO_BIN('4d1c0c8a-3af5-4ce9-ba95-09d25d289a76'), 'BASIC', 0);

-- User 1 - Token 1
INSERT INTO token (id, creation_timestamp, account_id, token_value, type, version) VALUES (UUID_TO_BIN('582c432a-c5d9-4758-863a-4999a7d95de5'), current_timestamp, UUID_TO_BIN('f5afc042-79b0-47fe-87ee-710c14af888c'), 'TEST_VALUE90', 'CONFIRM_EMAIL', 0);
-- User 1 - Token 2
INSERT INTO token (id, creation_timestamp, account_id, token_value, type, version) VALUES (UUID_TO_BIN('e0c86942-aed7-44a4-bda2-a89c3f4bdd37'), current_timestamp, UUID_TO_BIN('f5afc042-79b0-47fe-87ee-710c14af888c'), 'TEST_VALUE91', 'CONFIRM_EMAIL', 0);

-- Not suspended User 2 - Staff
INSERT INTO account (id, creation_timestamp, login, password, suspended, active, two_factor_auth, language, phone_number, version, blocked) VALUES (UUID_TO_BIN('d20f860d-555a-479e-8783-67aee5b66692'), current_timestamp, 'adamn', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', false, true, false, 'PL', '200000000', 0, false);

INSERT INTO personal_data (id, name, lastname, email) VALUES (UUID_TO_BIN('d20f860d-555a-479e-8783-67aee5b66692'), 'Adam', 'Niezgodka', 'adamn@example.com');

INSERT INTO user_level (id, creation_timestamp, level, account_id, version) VALUES (UUID_TO_BIN('c09d20a0-69e9-4a50-af00-3973b5d1d85c'), current_timestamp, 'STAFF', UUID_TO_BIN('d20f860d-555a-479e-8783-67aee5b66692'), 0);
INSERT INTO staff_data (id) VALUES (UUID_TO_BIN('c09d20a0-69e9-4a50-af00-3973b5d1d85c'));

-- User 2 - Token 1
INSERT INTO token (id, creation_timestamp, account_id, token_value, type, version) VALUES (UUID_TO_BIN('9847130d-6e60-4b3d-a9c3-b913f50769da'), current_timestamp, UUID_TO_BIN('d20f860d-555a-479e-8783-67aee5b66692'), 'TEST_VALUE93', 'CONFIRM_EMAIL', 0);

-- Blocked by admin User 1 - Client
INSERT INTO account (id, creation_timestamp, login, password, suspended, active, two_factor_auth, language, phone_number, version, blocked) VALUES (UUID_TO_BIN('16c2e579-6b85-41fd-8aae-7f3e3e279f24'), current_timestamp, 'juleswinnfield', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', false, true, false, 'PL', '100000000', 0, true);

INSERT INTO personal_data (id, name, lastname, email) VALUES (UUID_TO_BIN('16c2e579-6b85-41fd-8aae-7f3e3e279f24'), 'Jules', 'Winnfield', 'juleswinnfield@example.com');

INSERT INTO user_level (id, creation_timestamp, level, account_id, version) VALUES (UUID_TO_BIN('3989bada-0d14-49e1-8ff5-e66f095278d4'), current_timestamp, 'CLIENT', UUID_TO_BIN('16c2e579-6b85-41fd-8aae-7f3e3e279f24'), 0);
INSERT INTO client_data (id, type, total_reservation_hours) VALUES (UUID_TO_BIN('3989bada-0d14-49e1-8ff5-e66f095278d4'), 'BASIC', 0);

-- Blocked by admin User 1 - Client
INSERT INTO account (id, creation_timestamp, login, password, suspended, active, two_factor_auth, language, phone_number, version, blocked, blocked_timestamp) VALUES (UUID_TO_BIN('3b79a81c-f3c8-4af7-9fd2-793f53ba8a28'), current_timestamp, 'vincentvega', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', false, true, false, 'PL', '100000000', 0, true, current_timestamp + interval 7 day);

INSERT INTO personal_data (id, name, lastname, email) VALUES (UUID_TO_BIN('3b79a81c-f3c8-4af7-9fd2-793f53ba8a28'), 'Vincent', 'Vega', 'vincentvega@example.com');

INSERT INTO user_level (id, creation_timestamp, level, account_id, version) VALUES (UUID_TO_BIN('bad2b2ba-4a30-4512-b91f-92ab55d057bc'), current_timestamp, 'CLIENT', UUID_TO_BIN('3b79a81c-f3c8-4af7-9fd2-793f53ba8a28'), 0);
INSERT INTO client_data (id, type, total_reservation_hours) VALUES (UUID_TO_BIN('bad2b2ba-4a30-4512-b91f-92ab55d057bc'), 'BASIC', 0);

-- Activated User 1 - Client
INSERT INTO account (id, creation_timestamp, login, password, suspended, active, two_factor_auth, language, phone_number, version, blocked) VALUES (UUID_TO_BIN('c276cb93-5cfe-4bf5-9998-ecdeee8ba06b'), current_timestamp, 'jchrzan', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', false, false, false, 'PL', '100000000', 0, false);

INSERT INTO personal_data (id, name, lastname, email) VALUES (UUID_TO_BIN('c276cb93-5cfe-4bf5-9998-ecdeee8ba06b'), 'Jan', 'Chrzan', 'jchrzan@example.com');

INSERT INTO user_level (id, creation_timestamp, level, account_id, version) VALUES (UUID_TO_BIN('900cbc37-2a95-4bd6-96f2-897c12155f85'), current_timestamp, 'CLIENT', UUID_TO_BIN('c276cb93-5cfe-4bf5-9998-ecdeee8ba06b'), 0);
INSERT INTO client_data (id, type, total_reservation_hours) VALUES (UUID_TO_BIN('900cbc37-2a95-4bd6-96f2-897c12155f85'), 'BASIC', 0);

-- Dictionary

INSERT INTO attribute_name(id, version, attribute_name) VALUES (UUID_TO_BIN('d4772c10-2997-4f64-8ef2-dbd6268aa7eb'), 0, 'optional.attribute.theme');

INSERT INTO attribute_value(id, version, attribute_name_id, attribute_value) VALUES (UUID_TO_BIN('acd61f0b-3816-4af9-9544-fb3c9acd1595'), 0, UUID_TO_BIN('d4772c10-2997-4f64-8ef2-dbd6268aa7eb'), 'light');
INSERT INTO attribute_value(id, version, attribute_name_id, attribute_value) VALUES (UUID_TO_BIN('5fa7fe47-7fed-41c3-8bc3-45df811ed949'), 0, UUID_TO_BIN('d4772c10-2997-4f64-8ef2-dbd6268aa7eb'), 'dark');

INSERT INTO attribute_name(id, version, attribute_name) VALUES (UUID_TO_BIN('bd4acadd-ce76-46ce-9fad-aebe1d502aa0'), 0, 'optional.attribute.timezone');
INSERT INTO attribute_value(id, version, attribute_name_id, attribute_value) VALUES (UUID_TO_BIN('67a2f603-876d-4bb8-8559-5eac202705a2'), 0, UUID_TO_BIN('bd4acadd-ce76-46ce-9fad-aebe1d502aa0'), 'GMT-12');
INSERT INTO attribute_value(id, version, attribute_name_id, attribute_value) VALUES (UUID_TO_BIN('26f8d997-f84e-403d-9edb-74ad651228db'), 0, UUID_TO_BIN('bd4acadd-ce76-46ce-9fad-aebe1d502aa0'), 'GMT-11');
INSERT INTO attribute_value(id, version, attribute_name_id, attribute_value) VALUES (UUID_TO_BIN('8274d5af-a8b0-4671-a0a5-fffba75b68d0'), 0, UUID_TO_BIN('bd4acadd-ce76-46ce-9fad-aebe1d502aa0'), 'GMT-10');
INSERT INTO attribute_value(id, version, attribute_name_id, attribute_value) VALUES (UUID_TO_BIN('a552e1df-789d-40b9-8f22-357826365e13'), 0, UUID_TO_BIN('bd4acadd-ce76-46ce-9fad-aebe1d502aa0'), 'GMT-9');
INSERT INTO attribute_value(id, version, attribute_name_id, attribute_value) VALUES (UUID_TO_BIN('e674de41-a377-4fc0-8187-2fddc30a64e8'), 0, UUID_TO_BIN('bd4acadd-ce76-46ce-9fad-aebe1d502aa0'), 'GMT-8');
INSERT INTO attribute_value(id, version, attribute_name_id, attribute_value) VALUES (UUID_TO_BIN('8aa336a4-8694-4842-8726-2bfd18e6597b'), 0, UUID_TO_BIN('bd4acadd-ce76-46ce-9fad-aebe1d502aa0'), 'GMT-7');
INSERT INTO attribute_value(id, version, attribute_name_id, attribute_value) VALUES (UUID_TO_BIN('35a60e86-6f06-441d-ab8c-13bdd8163d58'), 0, UUID_TO_BIN('bd4acadd-ce76-46ce-9fad-aebe1d502aa0'), 'GMT-6');
INSERT INTO attribute_value(id, version, attribute_name_id, attribute_value) VALUES (UUID_TO_BIN('5790f481-b857-4a72-b591-5d4e507a535c'), 0, UUID_TO_BIN('bd4acadd-ce76-46ce-9fad-aebe1d502aa0'), 'GMT-5');
INSERT INTO attribute_value(id, version, attribute_name_id, attribute_value) VALUES (UUID_TO_BIN('c9042f9f-1266-43cb-bac9-c495eee21843'), 0, UUID_TO_BIN('bd4acadd-ce76-46ce-9fad-aebe1d502aa0'), 'GMT-4');
INSERT INTO attribute_value(id, version, attribute_name_id, attribute_value) VALUES (UUID_TO_BIN('fbcc9e43-476d-4db7-b237-7bd277386c19'), 0, UUID_TO_BIN('bd4acadd-ce76-46ce-9fad-aebe1d502aa0'), 'GMT-3');
INSERT INTO attribute_value(id, version, attribute_name_id, attribute_value) VALUES (UUID_TO_BIN('b5428896-c95f-464a-9bc8-88130d945b8a'), 0, UUID_TO_BIN('bd4acadd-ce76-46ce-9fad-aebe1d502aa0'), 'GMT-2');
INSERT INTO attribute_value(id, version, attribute_name_id, attribute_value) VALUES (UUID_TO_BIN('1a8f9fcc-c2df-4845-8a57-bbfe6a89bf67'), 0, UUID_TO_BIN('bd4acadd-ce76-46ce-9fad-aebe1d502aa0'), 'GMT-1');
INSERT INTO attribute_value(id, version, attribute_name_id, attribute_value) VALUES (UUID_TO_BIN('e035dafa-ba25-4418-baa2-faa704526194'), 0, UUID_TO_BIN('bd4acadd-ce76-46ce-9fad-aebe1d502aa0'), 'GMT+0');
INSERT INTO attribute_value(id, version, attribute_name_id, attribute_value) VALUES (UUID_TO_BIN('17b69ed1-c48f-4f97-8bda-58944be3ecb9'), 0, UUID_TO_BIN('bd4acadd-ce76-46ce-9fad-aebe1d502aa0'), 'GMT+1');
INSERT INTO attribute_value(id, version, attribute_name_id, attribute_value) VALUES (UUID_TO_BIN('60ea38d5-f355-45e6-9317-fbf9ef246441'), 0, UUID_TO_BIN('bd4acadd-ce76-46ce-9fad-aebe1d502aa0'), 'GMT+2');
INSERT INTO attribute_value(id, version, attribute_name_id, attribute_value) VALUES (UUID_TO_BIN('84b2838d-a577-4051-941a-b893841ebd9d'), 0, UUID_TO_BIN('bd4acadd-ce76-46ce-9fad-aebe1d502aa0'), 'GMT+3');
INSERT INTO attribute_value(id, version, attribute_name_id, attribute_value) VALUES (UUID_TO_BIN('339f12c6-3e4d-4554-b463-4938c5427f15'), 0, UUID_TO_BIN('bd4acadd-ce76-46ce-9fad-aebe1d502aa0'), 'GMT+4');
INSERT INTO attribute_value(id, version, attribute_name_id, attribute_value) VALUES (UUID_TO_BIN('efcdb744-49d9-4fbb-b342-837a5921847f'), 0, UUID_TO_BIN('bd4acadd-ce76-46ce-9fad-aebe1d502aa0'), 'GMT+5');
INSERT INTO attribute_value(id, version, attribute_name_id, attribute_value) VALUES (UUID_TO_BIN('a21a6214-4f86-4ed6-9380-425472553c7c'), 0, UUID_TO_BIN('bd4acadd-ce76-46ce-9fad-aebe1d502aa0'), 'GMT+6');
INSERT INTO attribute_value(id, version, attribute_name_id, attribute_value) VALUES (UUID_TO_BIN('ab48251f-0830-4100-92f7-8cacff976d02'), 0, UUID_TO_BIN('bd4acadd-ce76-46ce-9fad-aebe1d502aa0'), 'GMT+7');
INSERT INTO attribute_value(id, version, attribute_name_id, attribute_value) VALUES (UUID_TO_BIN('9183941e-74f0-4325-b976-8f12aaf319b9'), 0, UUID_TO_BIN('bd4acadd-ce76-46ce-9fad-aebe1d502aa0'), 'GMT+8');
INSERT INTO attribute_value(id, version, attribute_name_id, attribute_value) VALUES (UUID_TO_BIN('9bb9fdb0-7170-4a07-82c5-eb8344dfcd0b'), 0, UUID_TO_BIN('bd4acadd-ce76-46ce-9fad-aebe1d502aa0'), 'GMT+9');
INSERT INTO attribute_value(id, version, attribute_name_id, attribute_value) VALUES (UUID_TO_BIN('a039865b-cc88-45f5-bdc6-bea68b18db2e'), 0, UUID_TO_BIN('bd4acadd-ce76-46ce-9fad-aebe1d502aa0'), 'GMT+10');
INSERT INTO attribute_value(id, version, attribute_name_id, attribute_value) VALUES (UUID_TO_BIN('d8aab63d-85fe-443f-ad77-2b292dc61124'), 0, UUID_TO_BIN('bd4acadd-ce76-46ce-9fad-aebe1d502aa0'), 'GMT+11');
INSERT INTO attribute_value(id, version, attribute_name_id, attribute_value) VALUES (UUID_TO_BIN('3eea023d-304c-4435-94af-23b7c63e19bc'), 0, UUID_TO_BIN('bd4acadd-ce76-46ce-9fad-aebe1d502aa0'), 'GMT+12');


-- Sector to check enter without reservation conflict
INSERT INTO parking (id, creation_timestamp, sector_strategy, zip_code, city, street, version) VALUES (UUID_TO_BIN('2480e8a1-165f-4824-b6ae-11d10b0da04f'), current_timestamp, 'LEAST_OCCUPIED', '90-431', 'Warszawa', 'Rezerwowa', 0);
INSERT INTO sector (id, creation_timestamp, occupied_places, max_places, weight, name, parking_id, type, version) VALUES (UUID_TO_BIN('14d51050-ffe2-4da2-abd2-4e6d06759ea5'), current_timestamp, 0, 1, 3, 'ER-01', UUID_TO_BIN('2480e8a1-165f-4824-b6ae-11d10b0da04f'), 'UNCOVERED', 0);

-- Sector to check making reservation conflict
INSERT INTO parking (id, creation_timestamp, sector_strategy, zip_code, city, street, version) VALUES (UUID_TO_BIN('1a91f421-4cff-449b-837c-adbaf182689b'), current_timestamp, 'LEAST_OCCUPIED', '57-143', 'Łódź', 'Wólczańska', 0);
INSERT INTO sector (id, creation_timestamp, occupied_places, max_places, weight, name, parking_id, type, version) VALUES (UUID_TO_BIN('05eb42c9-f394-4dd9-b483-8f3af02104ff'), current_timestamp, 0, 1, 3, 'MR-01', UUID_TO_BIN('1a91f421-4cff-449b-837c-adbaf182689b'), 'UNCOVERED', 0);


-- Sector to check strategies
INSERT INTO parking (id, creation_timestamp, sector_strategy, zip_code, city, street, version) VALUES (UUID_TO_BIN('c94a751b-77e3-4a4d-bb64-74bfbe9c8857'), current_timestamp, 'MOST_OCCUPIED', '21-582', 'Łódź', 'Piłsudskiego', 0);

INSERT INTO sector (id, creation_timestamp, occupied_places, max_places, weight, name, parking_id, type, version) VALUES (UUID_TO_BIN('6113f6d0-f74b-48ff-ac8c-99c2827b72b0'), current_timestamp, 3, 10, 3, 'CR-01', UUID_TO_BIN('c94a751b-77e3-4a4d-bb64-74bfbe9c8857'), 'UNCOVERED', 0);
INSERT INTO sector (id, creation_timestamp, occupied_places, max_places, weight, name, parking_id, type, version) VALUES (UUID_TO_BIN('2dda962e-0fbe-4c9c-9dd5-59b945b7764f'), current_timestamp, 1, 10, 99, 'CR-02', UUID_TO_BIN('c94a751b-77e3-4a4d-bb64-74bfbe9c8857'), 'UNCOVERED', 0);
INSERT INTO sector (id, creation_timestamp, occupied_places, max_places, weight, name, parking_id, type, version) VALUES (UUID_TO_BIN('74151bef-7b38-4915-85ce-b8691f5fa4a1'), current_timestamp, 0, 10, 3, 'CR-03', UUID_TO_BIN('c94a751b-77e3-4a4d-bb64-74bfbe9c8857'), 'UNCOVERED', 0);

INSERT INTO reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES (UUID_TO_BIN('17fc3479-43c7-49ad-96f4-5299304e8015'), current_timestamp, TIMESTAMP '2024-06-14 10:00:00', TIMESTAMP '2024-06-14 20:00:00', UUID_TO_BIN('900cbc37-2a95-4bd6-96f2-897c12155f85'), UUID_TO_BIN('6113f6d0-f74b-48ff-ac8c-99c2827b72b0'), 0, 'IN_PROGRESS');
INSERT INTO reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES (UUID_TO_BIN('d92e7d7b-9f82-4391-8d36-27cb92e140cf'), current_timestamp, TIMESTAMP '2024-06-14 10:30:00', TIMESTAMP '2024-06-14 21:00:00', UUID_TO_BIN('9428fadf-191c-4dd7-8626-01c3e0ff603c'), UUID_TO_BIN('6113f6d0-f74b-48ff-ac8c-99c2827b72b0'), 0, 'IN_PROGRESS');
INSERT INTO reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES (UUID_TO_BIN('62f21719-9470-4c75-8980-87eb835968dc'), current_timestamp, TIMESTAMP '2024-06-14 10:30:00', TIMESTAMP '2024-06-14 21:00:00', UUID_TO_BIN('69507c7f-4c03-4087-85e6-3ae3b6fc2201'), UUID_TO_BIN('6113f6d0-f74b-48ff-ac8c-99c2827b72b0'), 0, 'IN_PROGRESS');

INSERT INTO reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES (UUID_TO_BIN('fbec4998-23fa-4e11-9d18-cd8c066a4530'), current_timestamp, TIMESTAMP '2024-06-14 10:30:00', TIMESTAMP '2024-06-14 21:00:00', UUID_TO_BIN('900cbc37-2a95-4bd6-96f2-897c12155f85'), UUID_TO_BIN('2dda962e-0fbe-4c9c-9dd5-59b945b7764f'), 0, 'IN_PROGRESS');