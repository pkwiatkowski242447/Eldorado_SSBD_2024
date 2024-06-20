\c ssbd03 ssbd03admin

-- INSERT VALUE
-- Admin
INSERT INTO public.account (id, creation_timestamp, login, password, suspended, active, two_factor_auth, language, phone_number, version, blocked) VALUES ('b3b8c2ac-21ff-434b-b490-aa6d717447c0', current_timestamp, 'jerzybem', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', false, true, false, 'PL', '111111111', 0, false);
INSERT INTO public.past_password(account_id, past_password) VALUES ('b3b8c2ac-21ff-434b-b490-aa6d717447c0', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa');
INSERT INTO public.personal_data (id, name, lastname, email) VALUES ('b3b8c2ac-21ff-434b-b490-aa6d717447c0', 'Jerzy', 'Bem', 'jerzybem@spoko.pl');

-- Admin - admin role
INSERT INTO public.user_level (id, creation_timestamp, level, account_id, version) VALUES ('bdb52e59-0054-4ec5-a2af-3e0d2b187ce0', current_timestamp, 'ADMIN', 'b3b8c2ac-21ff-434b-b490-aa6d717447c0', 0);
INSERT INTO public.admin_data (id) VALUES ('bdb52e59-0054-4ec5-a2af-3e0d2b187ce0');

-- Admin - staff role
INSERT INTO public.user_level (id, creation_timestamp, level, account_id, version) VALUES ('9ba0d086-edec-475d-8569-69156e79be9d', current_timestamp, 'STAFF', 'b3b8c2ac-21ff-434b-b490-aa6d717447c0', 0);
INSERT INTO public.staff_data (id) VALUES ('9ba0d086-edec-475d-8569-69156e79be9d');

-- Staff
INSERT INTO public.account (id, creation_timestamp, login, password, suspended, active, two_factor_auth, language, phone_number, version, blocked) VALUES ('e0bf979b-6b42-432d-8462-544d88b1ab5f', current_timestamp, 'kamilslimak', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', false, true, false, 'PL', '222222222', 0, false);
INSERT INTO public.past_password(account_id, past_password) VALUES ('e0bf979b-6b42-432d-8462-544d88b1ab5f', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa');
INSERT INTO public.personal_data (id, name, lastname, email) VALUES ('e0bf979b-6b42-432d-8462-544d88b1ab5f', 'Kamil', 'Slimak', 'kamilslimak1@spoko.pl');

INSERT INTO public.user_level (id, creation_timestamp, level, account_id, version) VALUES ('2488831d-c7c4-4f61-b48a-3be87364271f', current_timestamp, 'STAFF', 'e0bf979b-6b42-432d-8462-544d88b1ab5f', 0);
INSERT INTO public.staff_data (id) VALUES ('2488831d-c7c4-4f61-b48a-3be87364271f');

-- First Client
INSERT INTO public.account (id, creation_timestamp, login, password, suspended, active, two_factor_auth, language, phone_number, version, blocked) VALUES ('0ca02f7e-d8e9-45d3-a332-a56015acb822', current_timestamp, 'michalkowal', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', false, true, false, 'PL', '000000000', 0, false);
INSERT INTO public.past_password(account_id, past_password) VALUES ('0ca02f7e-d8e9-45d3-a332-a56015acb822', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa');
INSERT INTO public.personal_data (id, name, lastname, email) VALUES ('0ca02f7e-d8e9-45d3-a332-a56015acb822', 'Michal', 'Kowalski', 'michalkowal@spoko.pl');

INSERT INTO public.user_level (id, creation_timestamp, level, account_id, version) VALUES ('69507c7f-4c03-4087-85e6-3ae3b6fc2201', current_timestamp, 'CLIENT', '0ca02f7e-d8e9-45d3-a332-a56015acb822', 0);
INSERT INTO public.client_data (id, type, total_reservation_hours) VALUES ('69507c7f-4c03-4087-85e6-3ae3b6fc2201', 'BASIC', 0);

-- Second Client
INSERT INTO public.account (id, creation_timestamp, login, password, suspended, active, two_factor_auth, language, phone_number, version, blocked) VALUES ('902d6e5b-2449-4898-a4f7-a92b0d8a04e1', current_timestamp, 'jakubkoza', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', false, true, false, 'EN', '000000001', 0, false);
INSERT INTO public.past_password(account_id, past_password) VALUES ('902d6e5b-2449-4898-a4f7-a92b0d8a04e1', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa');
INSERT INTO public.personal_data (id, name, lastname, email) VALUES ('902d6e5b-2449-4898-a4f7-a92b0d8a04e1', 'Jakub', 'Koza', 'jakubkoza@adresik.net');

INSERT INTO public.user_level (id, creation_timestamp, level, account_id, version) VALUES ('9428fadf-191c-4dd7-8626-01c3e0ff603c', current_timestamp, 'CLIENT', '902d6e5b-2449-4898-a4f7-a92b0d8a04e1', 0);
INSERT INTO public.client_data (id, type, total_reservation_hours) VALUES ('9428fadf-191c-4dd7-8626-01c3e0ff603c', 'BASIC', 0);

-- Third Client
INSERT INTO public.account (id, creation_timestamp, login, password, suspended, active, two_factor_auth, language, phone_number, version, blocked) VALUES ('02b0d9d7-a472-48d0-95e0-13a3e6a11d00', current_timestamp, 'piotrnowak', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', false, true, false, 'PL', '000000003', 0, false);
INSERT INTO public.past_password(account_id, past_password) VALUES ('02b0d9d7-a472-48d0-95e0-13a3e6a11d00', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa');
INSERT INTO public.personal_data (id, name, lastname, email) VALUES ('02b0d9d7-a472-48d0-95e0-13a3e6a11d00', 'Piotr', 'Nowak', 'piotrnowak1@adresik.net');

INSERT INTO public.user_level (id, creation_timestamp, level, account_id, version) VALUES ('cbf34cb0-c96b-4037-80d4-1eef34890e85', current_timestamp, 'CLIENT', '02b0d9d7-a472-48d0-95e0-13a3e6a11d00', 0);
INSERT INTO public.client_data (id, type, total_reservation_hours) VALUES ('cbf34cb0-c96b-4037-80d4-1eef34890e85', 'BASIC', 0);

-- Admin - test
INSERT INTO public.account (id, creation_timestamp, login, password, suspended, active, two_factor_auth, language, phone_number, version, blocked) VALUES ('9a333f13-5ccc-4109-bce3-0ad629843edf', current_timestamp, 'aandrus', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', false, true, false, 'pl', '111111112', 0, false);
INSERT INTO public.personal_data (id, name, lastname, email) VALUES ('9a333f13-5ccc-4109-bce3-0ad629843edf', 'Andrzej', 'Andrus', 'aandrus@example.com');
INSERT INTO public.user_level (id, creation_timestamp, level, account_id, version) VALUES ('c21bda59-3ed9-4370-8675-3b53ddb04f4e', current_timestamp, 'ADMIN', '9a333f13-5ccc-4109-bce3-0ad629843edf', 0);
INSERT INTO public.admin_data (id) VALUES ('c21bda59-3ed9-4370-8675-3b53ddb04f4e');

-- Staff - test
INSERT INTO public.account (id, creation_timestamp, login, password, suspended, active, two_factor_auth, language, phone_number, version, blocked) VALUES ('f512c0b6-40b2-4bcb-8541-46077ac02101', current_timestamp, 'tkarol', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', false, true, false, 'pl', '111111113', 0, false);
INSERT INTO public.personal_data (id, name, lastname, email) VALUES ('f512c0b6-40b2-4bcb-8541-46077ac02101', 'Tomasz', 'Karolak', 'tkarol@example.com');
INSERT INTO public.user_level (id, creation_timestamp, level, account_id, version) VALUES ('00568964-9f83-441e-b441-83e545d51733', current_timestamp, 'STAFF', 'f512c0b6-40b2-4bcb-8541-46077ac02101', 0);
INSERT INTO public.staff_data (id) VALUES ('00568964-9f83-441e-b441-83e545d51733');

-- Staff - test
INSERT INTO public.account (id, creation_timestamp, login, password, suspended, active, two_factor_auth, language, phone_number, version, blocked) VALUES ('f14ac5b1-16f3-42ff-8df3-dd95de69c368', current_timestamp, 'kwotyla', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', false, true, false, 'pl', '111111114', 0, false);
INSERT INTO public.personal_data (id, name, lastname, email) VALUES ('f14ac5b1-16f3-42ff-8df3-dd95de69c368', 'Krystian', 'Womyla', 'krystianwomyla@example.com');
INSERT INTO public.user_level (id, creation_timestamp, level, account_id, version) VALUES ('248a31fa-7fef-41d5-8042-e70a38d30a9d', current_timestamp, 'CLIENT', 'f14ac5b1-16f3-42ff-8df3-dd95de69c368', 0);
INSERT INTO public.client_data (id, type, total_reservation_hours) VALUES ('248a31fa-7fef-41d5-8042-e70a38d30a9d', 'BASIC', 0);

-- Parking
INSERT INTO public.parking (id, creation_timestamp, sector_strategy, zip_code, city, street, version) VALUES ('96a36faa-f2a2-41b8-9c3c-b6bef04ce6d1', current_timestamp, 'LEAST_OCCUPIED', '91-416', 'BoatCity', 'Palki', 0);
INSERT INTO public.parking (id, creation_timestamp, sector_strategy, zip_code, city, street, version) VALUES ('a54e7ae6-ba2c-4fac-8ef6-e9d27da48921', current_timestamp, 'LEAST_OCCUPIED', '00-000', 'Lodz', 'Pomorska', 20);
INSERT INTO public.parking (id, creation_timestamp, sector_strategy, zip_code, city, street, version) VALUES ('ddcae4ec-aeb5-4ece-aa2b-46819763d55f', current_timestamp, 'LEAST_OCCUPIED', '95-010', 'Strykow', 'Krotka', 9);

-- First Sector S1
INSERT INTO public.sector (id, creation_timestamp ,occupied_places, max_places, weight, name, parking_id, type, version, deactivation_time) VALUES ('3e6a85db-d751-4549-bbb7-9705f0b2fa6b', current_timestamp, 30, 50, 1, 'SA-01', '96a36faa-f2a2-41b8-9c3c-b6bef04ce6d1', 'UNCOVERED', 0, current_timestamp - interval '2 hours');

-- Second Sector S1
INSERT INTO public.sector (id, creation_timestamp, occupied_places, max_places, weight, name, parking_id, type, version) VALUES ('9f7f2969-1b7e-4bb3-ab84-6dbc31c01277', current_timestamp, 30, 50, 1, 'SB-01', '96a36faa-f2a2-41b8-9c3c-b6bef04ce6d1', 'UNCOVERED', 0);

-- Third Sector S1
INSERT INTO public.sector (id, creation_timestamp, occupied_places, max_places, weight, name, parking_id, type, version) VALUES ('99023647-d8d2-43a5-91c7-b24781f06e13', current_timestamp, 30, 50, 1, 'SB-02', 'a54e7ae6-ba2c-4fac-8ef6-e9d27da48921', 'UNCOVERED', 0);

-- Fourth Sector S1
INSERT INTO public.sector (id, creation_timestamp, occupied_places, max_places, weight, name, parking_id, type, version) VALUES ('bca50310-f4fb-4911-bf3c-68e00e517b95', current_timestamp, 30, 50, 1, 'SB-03', 'a54e7ae6-ba2c-4fac-8ef6-e9d27da48921', 'UNCOVERED', 0);

-- Fifth Sector S1
INSERT INTO public.sector (id, creation_timestamp, occupied_places, max_places, weight, name, parking_id, type, version) VALUES ('82b4ad91-a5ef-48bf-bc5b-390d7f4d1dea', current_timestamp, 30, 50, 1, 'SB-04', 'a54e7ae6-ba2c-4fac-8ef6-e9d27da48921', 'COVERED', 0);

-- Sixth Sector S1
INSERT INTO public.sector (id, creation_timestamp, occupied_places, max_places, weight, name, parking_id, type, version) VALUES ('36e19dcd-1d5b-4258-a5df-ba9f4372c58c', current_timestamp, 30, 50, 1, 'SB-05', 'a54e7ae6-ba2c-4fac-8ef6-e9d27da48921', 'UNDERGROUND', 0);

-- Seventh Sector S2
INSERT INTO public.sector (id, creation_timestamp, occupied_places, max_places, weight, name, parking_id, type, version) VALUES ('4ce920a0-6f4d-4e95-ba24-99ba32b66491', current_timestamp, 30, 60, 2, 'SA-02', '96a36faa-f2a2-41b8-9c3c-b6bef04ce6d1', 'UNCOVERED', 0);

-- Eighth Sector S3
INSERT INTO public.sector (id, creation_timestamp, occupied_places, max_places, weight, name, parking_id, type, version) VALUES ('c51557aa-284d-44a6-b38d-b6ceb9c23725', current_timestamp, 30, 70, 3, 'SA-03', '96a36faa-f2a2-41b8-9c3c-b6bef04ce6d1', 'UNCOVERED', 0);

-- First Reservation (for michalkowal)
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('90a0035d-6265-4b53-a547-901b3bbabd1d', current_timestamp, TIMESTAMP '2024-04-10 07:00:00', TIMESTAMP '2024-04-10 09:00:00', '69507c7f-4c03-4087-85e6-3ae3b6fc2201', '3e6a85db-d751-4549-bbb7-9705f0b2fa6b', 0, 'COMPLETED_MANUALLY');

-- Reservation for jakubkoza - expired
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('1ec7d685-71ac-4418-834a-ed7b6fc68fc8', current_timestamp, TIMESTAMP '2024-04-15 12:00:00', TIMESTAMP '2024-04-15 13:00:00', '9428fadf-191c-4dd7-8626-01c3e0ff603c', '4ce920a0-6f4d-4e95-ba24-99ba32b66491', 0, 'COMPLETED_AUTOMATICALLY');

-- Reservation for jakubkoza - valid
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('8c3c21c4-b332-4d72-9390-b0b6ad41d173', current_timestamp, current_timestamp, current_timestamp + '4 hours', '9428fadf-191c-4dd7-8626-01c3e0ff603c', '4ce920a0-6f4d-4e95-ba24-99ba32b66491', 0, 'COMPLETED_MANUALLY');

-- Reservation for jakubkoza - not started yet
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('4e892a98-ba3d-47da-aa56-26ad52746280', current_timestamp, current_timestamp + '6 days', current_timestamp + '7 days', '9428fadf-191c-4dd7-8626-01c3e0ff603c', '4ce920a0-6f4d-4e95-ba24-99ba32b66491', 0, 'COMPLETED_MANUALLY');

-- Third Reservation
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('a7709a4d-b7bc-40c4-8fd5-5c7cfcb0f146', current_timestamp, TIMESTAMP '2024-03-01 15:00:00', TIMESTAMP '2024-03-01 16:00:00', null, 'c51557aa-284d-44a6-b38d-b6ceb9c23725', 0, 'COMPLETED_MANUALLY');

-- First token
INSERT INTO public.token (id, creation_timestamp, account_id, token_value, type, version) VALUES ('4ac79a06-2b75-4519-b430-1abe0e05f04e', current_timestamp, '0ca02f7e-d8e9-45d3-a332-a56015acb822', 'TEST_VALUE', 'CONFIRM_EMAIL', 0);

-- Second token
INSERT INTO public.token (id, creation_timestamp, account_id, token_value, type, version) VALUES ('499c0085-c0b8-424e-97f5-84abe66f9bf6', current_timestamp, '902d6e5b-2449-4898-a4f7-a92b0d8a04e1', 'TEST_VALUE2', 'RESET_PASSWORD', 0);

-- Third token
INSERT INTO public.token (id, creation_timestamp, account_id, token_value, type, version) VALUES ('b269207d-7627-4a91-af86-87d2b975d487', current_timestamp, 'e0bf979b-6b42-432d-8462-544d88b1ab5f', 'TEST_VALUE3', 'CHANGE_OVERWRITTEN_PASSWORD', 0);

-- Parking event - Reservation 1
INSERT INTO public.parking_event (id, reservation_id, date, type, version) VALUES ('b922b5c3-08a8-4902-8ca0-e99e65506fa5', '90a0035d-6265-4b53-a547-901b3bbabd1d', TIMESTAMP '2024-04-10 07:00:00', 'ENTRY', 0);
INSERT INTO public.parking_event (id, reservation_id, date, type, version) VALUES ('eb6878af-7bb8-4094-96fc-84126e2dfb5e', '90a0035d-6265-4b53-a547-901b3bbabd1d', TIMESTAMP '2024-04-10 09:00:00', 'EXIT', 0);

-- Parking event - Reservation 3
INSERT INTO public.parking_event (id, reservation_id, date, type, version) VALUES ('12c8f77a-4a35-43f6-9134-4deddd5407b5', 'a7709a4d-b7bc-40c4-8fd5-5c7cfcb0f146', TIMESTAMP '2024-03-01 15:00:00', 'ENTRY', 0);
INSERT INTO public.parking_event (id, reservation_id, date, type, version) VALUES ('94d17bd1-da2e-4047-822b-942526dfbd5a', 'a7709a4d-b7bc-40c4-8fd5-5c7cfcb0f146', TIMESTAMP '2024-03-01 16:00:00', 'EXIT', 0);

-------------------------------------

-- Not suspended User 1 - Client
INSERT INTO public.account (id, creation_timestamp, login, password, suspended, active, two_factor_auth, language, phone_number, version, blocked) VALUES ('f5afc042-79b0-47fe-87ee-710c14af888c', current_timestamp, 'tonyhalik', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', false, true, false, 'PL', '100000000', 0, false);

INSERT INTO public.personal_data (id, name, lastname, email) VALUES ('f5afc042-79b0-47fe-87ee-710c14af888c', 'Tony', 'Halik', 'tonyhalik@example.com');

INSERT INTO public.user_level (id, creation_timestamp, level, account_id, version) VALUES ('4d1c0c8a-3af5-4ce9-ba95-09d25d289a76', current_timestamp, 'CLIENT', 'f5afc042-79b0-47fe-87ee-710c14af888c', 0);
INSERT INTO public.client_data (id, type, total_reservation_hours) VALUES ('4d1c0c8a-3af5-4ce9-ba95-09d25d289a76', 'BASIC', 0);

-- User 1 - Token 1
INSERT INTO public.token (id, creation_timestamp, account_id, token_value, type, version) VALUES ('582c432a-c5d9-4758-863a-4999a7d95de5', current_timestamp, 'f5afc042-79b0-47fe-87ee-710c14af888c', 'TEST_VALUE90', 'CONFIRM_EMAIL', 0);
-- User 1 - Token 2
INSERT INTO public.token (id, creation_timestamp, account_id, token_value, type, version) VALUES ('e0c86942-aed7-44a4-bda2-a89c3f4bdd37', current_timestamp, 'f5afc042-79b0-47fe-87ee-710c14af888c', 'TEST_VALUE91', 'CONFIRM_EMAIL', 0);

-- Not suspended User 2 - Staff
INSERT INTO public.account (id, creation_timestamp, login, password, suspended, active, two_factor_auth, language, phone_number, version, blocked) VALUES ('d20f860d-555a-479e-8783-67aee5b66692', current_timestamp, 'adamn', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', false, true, false, 'PL', '200000000', 0, false);

INSERT INTO public.personal_data (id, name, lastname, email) VALUES ('d20f860d-555a-479e-8783-67aee5b66692', 'Adam', 'Niezgodka', 'adamn@example.com');

INSERT INTO public.user_level (id, creation_timestamp, level, account_id, version) VALUES ('c09d20a0-69e9-4a50-af00-3973b5d1d85c', current_timestamp, 'STAFF', 'd20f860d-555a-479e-8783-67aee5b66692', 0);
INSERT INTO public.staff_data (id) VALUES ('c09d20a0-69e9-4a50-af00-3973b5d1d85c');

-- User 2 - Token 1
INSERT INTO public.token (id, creation_timestamp, account_id, token_value, type, version) VALUES ('9847130d-6e60-4b3d-a9c3-b913f50769da', current_timestamp, 'd20f860d-555a-479e-8783-67aee5b66692', 'TEST_VALUE93', 'CONFIRM_EMAIL', 0);

-------------------------------------

-- Blocked by admin User 1 - Client
INSERT INTO public.account (id, creation_timestamp, login, password, suspended, active, two_factor_auth, language, phone_number, version, blocked) VALUES ('16c2e579-6b85-41fd-8aae-7f3e3e279f24', current_timestamp, 'juleswinnfield', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', false, true, false, 'PL', '100000000', 0, true);

INSERT INTO public.personal_data (id, name, lastname, email) VALUES ('16c2e579-6b85-41fd-8aae-7f3e3e279f24', 'Jules', 'Winnfield', 'juleswinnfield@example.com');

INSERT INTO public.user_level (id, creation_timestamp, level, account_id, version) VALUES ('3989bada-0d14-49e1-8ff5-e66f095278d4', current_timestamp, 'CLIENT', '16c2e579-6b85-41fd-8aae-7f3e3e279f24', 0);
INSERT INTO public.client_data (id, type, total_reservation_hours) VALUES ('3989bada-0d14-49e1-8ff5-e66f095278d4', 'BASIC', 0);

-- Blocked by admin User 1 - Client
INSERT INTO public.account (id, creation_timestamp, login, password, suspended, active, two_factor_auth, language, phone_number, version, blocked, blocked_timestamp) VALUES ('3b79a81c-f3c8-4af7-9fd2-793f53ba8a28', current_timestamp, 'vincentvega', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', false, true, false, 'PL', '100000000', 0, true, current_timestamp + '7 days');

INSERT INTO public.personal_data (id, name, lastname, email) VALUES ('3b79a81c-f3c8-4af7-9fd2-793f53ba8a28', 'Vincent', 'Vega', 'vincentvega@example.com');

INSERT INTO public.user_level (id, creation_timestamp, level, account_id, version) VALUES ('bad2b2ba-4a30-4512-b91f-92ab55d057bc', current_timestamp, 'CLIENT', '3b79a81c-f3c8-4af7-9fd2-793f53ba8a28', 0);
INSERT INTO public.client_data (id, type, total_reservation_hours) VALUES ('bad2b2ba-4a30-4512-b91f-92ab55d057bc', 'BASIC', 0);

-------------------------------------

-- Activated User 1 - Client
INSERT INTO public.account (id, creation_timestamp, login, password, suspended, active, two_factor_auth, language, phone_number, version, blocked) VALUES ('c276cb93-5cfe-4bf5-9998-ecdeee8ba06b', current_timestamp, 'jchrystus', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', false, false, false, 'PL', '100000000', 0, false);

INSERT INTO public.personal_data (id, name, lastname, email) VALUES ('c276cb93-5cfe-4bf5-9998-ecdeee8ba06b', 'Jezus', 'Chrystus', 'jchrystus@example.com');

INSERT INTO public.user_level (id, creation_timestamp, level, account_id, version) VALUES ('900cbc37-2a95-4bd6-96f2-897c12155f85', current_timestamp, 'CLIENT', 'c276cb93-5cfe-4bf5-9998-ecdeee8ba06b', 0);
INSERT INTO public.client_data (id, type, total_reservation_hours) VALUES ('900cbc37-2a95-4bd6-96f2-897c12155f85', 'BASIC', 0);

-------------------------------------

-- Dictionary

INSERT INTO public.attribute_name(id, version, attribute_name) VALUES ('d4772c10-2997-4f64-8ef2-dbd6268aa7eb', 0, 'optional.attribute.theme');

INSERT INTO public.attribute_value(id, version, attribute_name_id, attribute_value) VALUES ('acd61f0b-3816-4af9-9544-fb3c9acd1595', 0, 'd4772c10-2997-4f64-8ef2-dbd6268aa7eb', 'light');
INSERT INTO public.attribute_value(id, version, attribute_name_id, attribute_value) VALUES ('5fa7fe47-7fed-41c3-8bc3-45df811ed949', 0, 'd4772c10-2997-4f64-8ef2-dbd6268aa7eb', 'dark');

INSERT INTO public.attribute_name(id, version, attribute_name) VALUES ('bd4acadd-ce76-46ce-9fad-aebe1d502aa0', 0, 'optional.attribute.timezone');
INSERT INTO public.attribute_value(id, version, attribute_name_id, attribute_value) VALUES ('67a2f603-876d-4bb8-8559-5eac202705a2', 0, 'bd4acadd-ce76-46ce-9fad-aebe1d502aa0', 'GMT-12');
INSERT INTO public.attribute_value(id, version, attribute_name_id, attribute_value) VALUES ('26f8d997-f84e-403d-9edb-74ad651228db', 0, 'bd4acadd-ce76-46ce-9fad-aebe1d502aa0', 'GMT-11');
INSERT INTO public.attribute_value(id, version, attribute_name_id, attribute_value) VALUES ('8274d5af-a8b0-4671-a0a5-fffba75b68d0', 0, 'bd4acadd-ce76-46ce-9fad-aebe1d502aa0', 'GMT-10');
INSERT INTO public.attribute_value(id, version, attribute_name_id, attribute_value) VALUES ('a552e1df-789d-40b9-8f22-357826365e13', 0, 'bd4acadd-ce76-46ce-9fad-aebe1d502aa0', 'GMT-9');
INSERT INTO public.attribute_value(id, version, attribute_name_id, attribute_value) VALUES ('e674de41-a377-4fc0-8187-2fddc30a64e8', 0, 'bd4acadd-ce76-46ce-9fad-aebe1d502aa0', 'GMT-8');
INSERT INTO public.attribute_value(id, version, attribute_name_id, attribute_value) VALUES ('8aa336a4-8694-4842-8726-2bfd18e6597b', 0, 'bd4acadd-ce76-46ce-9fad-aebe1d502aa0', 'GMT-7');
INSERT INTO public.attribute_value(id, version, attribute_name_id, attribute_value) VALUES ('35a60e86-6f06-441d-ab8c-13bdd8163d58', 0, 'bd4acadd-ce76-46ce-9fad-aebe1d502aa0', 'GMT-6');
INSERT INTO public.attribute_value(id, version, attribute_name_id, attribute_value) VALUES ('5790f481-b857-4a72-b591-5d4e507a535c', 0, 'bd4acadd-ce76-46ce-9fad-aebe1d502aa0', 'GMT-5');
INSERT INTO public.attribute_value(id, version, attribute_name_id, attribute_value) VALUES ('c9042f9f-1266-43cb-bac9-c495eee21843', 0, 'bd4acadd-ce76-46ce-9fad-aebe1d502aa0', 'GMT-4');
INSERT INTO public.attribute_value(id, version, attribute_name_id, attribute_value) VALUES ('fbcc9e43-476d-4db7-b237-7bd277386c19', 0, 'bd4acadd-ce76-46ce-9fad-aebe1d502aa0', 'GMT-3');
INSERT INTO public.attribute_value(id, version, attribute_name_id, attribute_value) VALUES ('b5428896-c95f-464a-9bc8-88130d945b8a', 0, 'bd4acadd-ce76-46ce-9fad-aebe1d502aa0', 'GMT-2');
INSERT INTO public.attribute_value(id, version, attribute_name_id, attribute_value) VALUES ('1a8f9fcc-c2df-4845-8a57-bbfe6a89bf67', 0, 'bd4acadd-ce76-46ce-9fad-aebe1d502aa0', 'GMT-1');
INSERT INTO public.attribute_value(id, version, attribute_name_id, attribute_value) VALUES ('e035dafa-ba25-4418-baa2-faa704526194', 0, 'bd4acadd-ce76-46ce-9fad-aebe1d502aa0', 'GMT+0');
INSERT INTO public.attribute_value(id, version, attribute_name_id, attribute_value) VALUES ('17b69ed1-c48f-4f97-8bda-58944be3ecb9', 0, 'bd4acadd-ce76-46ce-9fad-aebe1d502aa0', 'GMT+1');
INSERT INTO public.attribute_value(id, version, attribute_name_id, attribute_value) VALUES ('60ea38d5-f355-45e6-9317-fbf9ef246441', 0, 'bd4acadd-ce76-46ce-9fad-aebe1d502aa0', 'GMT+2');
INSERT INTO public.attribute_value(id, version, attribute_name_id, attribute_value) VALUES ('84b2838d-a577-4051-941a-b893841ebd9d', 0, 'bd4acadd-ce76-46ce-9fad-aebe1d502aa0', 'GMT+3');
INSERT INTO public.attribute_value(id, version, attribute_name_id, attribute_value) VALUES ('339f12c6-3e4d-4554-b463-4938c5427f15', 0, 'bd4acadd-ce76-46ce-9fad-aebe1d502aa0', 'GMT+4');
INSERT INTO public.attribute_value(id, version, attribute_name_id, attribute_value) VALUES ('efcdb744-49d9-4fbb-b342-837a5921847f', 0, 'bd4acadd-ce76-46ce-9fad-aebe1d502aa0', 'GMT+5');
INSERT INTO public.attribute_value(id, version, attribute_name_id, attribute_value) VALUES ('a21a6214-4f86-4ed6-9380-425472553c7c', 0, 'bd4acadd-ce76-46ce-9fad-aebe1d502aa0', 'GMT+6');
INSERT INTO public.attribute_value(id, version, attribute_name_id, attribute_value) VALUES ('ab48251f-0830-4100-92f7-8cacff976d02', 0, 'bd4acadd-ce76-46ce-9fad-aebe1d502aa0', 'GMT+7');
INSERT INTO public.attribute_value(id, version, attribute_name_id, attribute_value) VALUES ('9183941e-74f0-4325-b976-8f12aaf319b9', 0, 'bd4acadd-ce76-46ce-9fad-aebe1d502aa0', 'GMT+8');
INSERT INTO public.attribute_value(id, version, attribute_name_id, attribute_value) VALUES ('9bb9fdb0-7170-4a07-82c5-eb8344dfcd0b', 0, 'bd4acadd-ce76-46ce-9fad-aebe1d502aa0', 'GMT+9');
INSERT INTO public.attribute_value(id, version, attribute_name_id, attribute_value) VALUES ('a039865b-cc88-45f5-bdc6-bea68b18db2e', 0, 'bd4acadd-ce76-46ce-9fad-aebe1d502aa0', 'GMT+10');
INSERT INTO public.attribute_value(id, version, attribute_name_id, attribute_value) VALUES ('d8aab63d-85fe-443f-ad77-2b292dc61124', 0, 'bd4acadd-ce76-46ce-9fad-aebe1d502aa0', 'GMT+11');
INSERT INTO public.attribute_value(id, version, attribute_name_id, attribute_value) VALUES ('3eea023d-304c-4435-94af-23b7c63e19bc', 0, 'bd4acadd-ce76-46ce-9fad-aebe1d502aa0', 'GMT+12');


-- Sector to check enter without reservation conflict
INSERT INTO public.parking (id, creation_timestamp, sector_strategy, zip_code, city, street, version) VALUES ('2480e8a1-165f-4824-b6ae-11d10b0da04f', current_timestamp, 'LEAST_OCCUPIED', '99-999', 'EnterWithout', 'Reservation', 0);
INSERT INTO public.sector (id, creation_timestamp, occupied_places, max_places, weight, name, parking_id, type, version) VALUES ('14d51050-ffe2-4da2-abd2-4e6d06759ea5', current_timestamp, 0, 1, 3, 'ER-01', '2480e8a1-165f-4824-b6ae-11d10b0da04f', 'UNCOVERED', 0);

-- Sector to check making reservation conflict
INSERT INTO public.parking (id, creation_timestamp, sector_strategy, zip_code, city, street, version) VALUES ('1a91f421-4cff-449b-837c-adbaf182689b', current_timestamp, 'LEAST_OCCUPIED', '00-000', 'Make', 'Reservation', 0);
INSERT INTO public.sector (id, creation_timestamp, occupied_places, max_places, weight, name, parking_id, type, version) VALUES ('05eb42c9-f394-4dd9-b483-8f3af02104ff', current_timestamp, 0, 1, 3, 'MR-01', '1a91f421-4cff-449b-837c-adbaf182689b', 'UNCOVERED', 0);


-- Sector to check strategies
INSERT INTO public.parking (id, creation_timestamp, sector_strategy, zip_code, city, street, version) VALUES ('c94a751b-77e3-4a4d-bb64-74bfbe9c8857', current_timestamp, 'MOST_OCCUPIED', '21-037', 'Check', 'Strategies', 0);

INSERT INTO public.sector (id, creation_timestamp, occupied_places, max_places, weight, name, parking_id, type, version) VALUES ('6113f6d0-f74b-48ff-ac8c-99c2827b72b0', current_timestamp, 3, 10, 3, 'CR-01', 'c94a751b-77e3-4a4d-bb64-74bfbe9c8857', 'UNCOVERED', 0);
INSERT INTO public.sector (id, creation_timestamp, occupied_places, max_places, weight, name, parking_id, type, version) VALUES ('2dda962e-0fbe-4c9c-9dd5-59b945b7764f', current_timestamp, 1, 10, 99, 'CR-02', 'c94a751b-77e3-4a4d-bb64-74bfbe9c8857', 'UNCOVERED', 0);
INSERT INTO public.sector (id, creation_timestamp, occupied_places, max_places, weight, name, parking_id, type, version) VALUES ('74151bef-7b38-4915-85ce-b8691f5fa4a1', current_timestamp, 0, 10, 3, 'CR-03', 'c94a751b-77e3-4a4d-bb64-74bfbe9c8857', 'UNCOVERED', 0);

INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('17fc3479-43c7-49ad-96f4-5299304e8015', current_timestamp, TIMESTAMP '2024-06-14 10:00:00', TIMESTAMP '2024-06-14 20:00:00', '900cbc37-2a95-4bd6-96f2-897c12155f85', '6113f6d0-f74b-48ff-ac8c-99c2827b72b0', 0, 'IN_PROGRESS');
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('d92e7d7b-9f82-4391-8d36-27cb92e140cf', current_timestamp, TIMESTAMP '2024-06-14 10:30:00', TIMESTAMP '2024-06-14 21:00:00', '9428fadf-191c-4dd7-8626-01c3e0ff603c', '6113f6d0-f74b-48ff-ac8c-99c2827b72b0', 0, 'IN_PROGRESS');
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('62f21719-9470-4c75-8980-87eb835968dc', current_timestamp, TIMESTAMP '2024-06-14 10:30:00', TIMESTAMP '2024-06-14 21:00:00', '69507c7f-4c03-4087-85e6-3ae3b6fc2201', '6113f6d0-f74b-48ff-ac8c-99c2827b72b0', 0, 'IN_PROGRESS');

INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('fbec4998-23fa-4e11-9d18-cd8c066a4530', current_timestamp, TIMESTAMP '2024-06-14 10:30:00', TIMESTAMP '2024-06-14 21:00:00', '900cbc37-2a95-4bd6-96f2-897c12155f85', '2dda962e-0fbe-4c9c-9dd5-59b945b7764f', 0, 'IN_PROGRESS');