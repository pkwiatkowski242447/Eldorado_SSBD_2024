-- -- GRAND PRIVILEGES
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.account                TO ssbd03mok;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.past_password          TO ssbd03mok;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.personal_data          TO ssbd03mok;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.user_level             TO ssbd03mok;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.client_data            TO ssbd03mok;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.staff_data             TO ssbd03mok;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.admin_data             TO ssbd03mok;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.token                  TO ssbd03mok;
GRANT SELECT, INSERT                 ON TABLE public.account_history        TO ssbd03mok;

GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.account_attributes     TO ssbd03mok;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.attribute_association  TO ssbd03mok;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.attribute_name         TO ssbd03mok;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.attribute_value        TO ssbd03mok;

GRANT SELECT                         ON TABLE public.account_attributes     TO ssbd03auth;
GRANT SELECT                         ON TABLE public.attribute_association  TO ssbd03auth;
GRANT SELECT                         ON TABLE public.attribute_name         TO ssbd03auth;
GRANT SELECT                         ON TABLE public.attribute_value        TO ssbd03auth;

GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.reservation            TO ssbd03mop;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.parking                TO ssbd03mop;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.sector                 TO ssbd03mop;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.parking_event          TO ssbd03mop;
GRANT SELECT                         ON TABLE public.account                TO ssbd03mop;
GRANT SELECT                         ON TABLE public.personal_data          TO ssbd03mop;
GRANT SELECT                         ON TABLE public.user_level             TO ssbd03mop;
GRANT SELECT                , UPDATE ON TABLE public.client_data            TO ssbd03mop;
GRANT SELECT                         ON TABLE public.admin_data             TO ssbd03mop;
GRANT SELECT                         ON TABLE public.staff_data             TO ssbd03mop;
GRANT SELECT, INSERT, DELETE         ON TABLE public.entry_code             TO ssbd03mop;

GRANT SELECT                         ON TABLE public.account_attributes     TO ssbd03mop;
GRANT SELECT                         ON TABLE public.attribute_association  TO ssbd03mop;
GRANT SELECT                         ON TABLE public.attribute_name         TO ssbd03mop;
GRANT SELECT                         ON TABLE public.attribute_value        TO ssbd03mop;


GRANT SELECT, UPDATE                 ON TABLE public.account                TO ssbd03auth;
GRANT SELECT                         ON TABLE public.personal_data          TO ssbd03auth;
GRANT SELECT                         ON TABLE public.user_level             TO ssbd03auth;
GRANT SELECT                         ON TABLE public.client_data            TO ssbd03auth;
GRANT SELECT                         ON TABLE public.staff_data             TO ssbd03auth;
GRANT SELECT                         ON TABLE public.admin_data             TO ssbd03auth;
GRANT SELECT, INSERT, DELETE         ON TABLE public.token                  TO ssbd03auth;
GRANT SELECT, INSERT                 ON TABLE public.account_history        TO ssbd03auth;

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
INSERT INTO public.client_data (id, type) VALUES ('69507c7f-4c03-4087-85e6-3ae3b6fc2201', 'BASIC');

-- Second Client
INSERT INTO public.account (id, creation_timestamp, login, password, suspended, active, two_factor_auth, language, phone_number, version, blocked) VALUES ('902d6e5b-2449-4898-a4f7-a92b0d8a04e1', current_timestamp, 'jakubkoza', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', false, true, false, 'EN', '000000001', 0, false);
INSERT INTO public.past_password(account_id, past_password) VALUES ('902d6e5b-2449-4898-a4f7-a92b0d8a04e1', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa');
INSERT INTO public.personal_data (id, name, lastname, email) VALUES ('902d6e5b-2449-4898-a4f7-a92b0d8a04e1', 'Jakub', 'Koza', 'jakubkoza@adresik.net');

INSERT INTO public.user_level (id, creation_timestamp, level, account_id, version) VALUES ('9428fadf-191c-4dd7-8626-01c3e0ff603c', current_timestamp, 'CLIENT', '902d6e5b-2449-4898-a4f7-a92b0d8a04e1', 0);
INSERT INTO public.client_data (id, type) VALUES ('9428fadf-191c-4dd7-8626-01c3e0ff603c', 'BASIC');

-- Third Client
INSERT INTO public.account (id, creation_timestamp, login, password, suspended, active, two_factor_auth, language, phone_number, version, blocked) VALUES ('02b0d9d7-a472-48d0-95e0-13a3e6a11d00', current_timestamp, 'piotrnowak', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', false, true, false, 'PL', '000000003', 0, false);
INSERT INTO public.past_password(account_id, past_password) VALUES ('02b0d9d7-a472-48d0-95e0-13a3e6a11d00', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa');
INSERT INTO public.personal_data (id, name, lastname, email) VALUES ('02b0d9d7-a472-48d0-95e0-13a3e6a11d00', 'Piotr', 'Nowak', 'piotrnowak1@adresik.net');

INSERT INTO public.user_level (id, creation_timestamp, level, account_id, version) VALUES ('cbf34cb0-c96b-4037-80d4-1eef34890e85', current_timestamp, 'CLIENT', '02b0d9d7-a472-48d0-95e0-13a3e6a11d00', 0);
INSERT INTO public.client_data (id, type) VALUES ('cbf34cb0-c96b-4037-80d4-1eef34890e85', 'BASIC');

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
INSERT INTO public.client_data (id, type) VALUES ('248a31fa-7fef-41d5-8042-e70a38d30a9d', 'BASIC');

-- Parking
INSERT INTO public.parking (id, creation_timestamp, sector_strategy, zip_code, city, street, version) VALUES ('96a36faa-f2a2-41b8-9c3c-b6bef04ce6d1', current_timestamp, 'LEAST_OCCUPIED', '91-416', 'BoatCity', 'Palki', 0);
INSERT INTO public.parking (id, creation_timestamp, sector_strategy, zip_code, city, street, version) VALUES ('a54e7ae6-ba2c-4fac-8ef6-e9d27da48921', current_timestamp, 'LEAST_OCCUPIED', '00-000', 'Lodz', 'Pomorska', 20);
INSERT INTO public.parking (id, creation_timestamp, sector_strategy, zip_code, city, street, version) VALUES ('ddcae4ec-aeb5-4ece-aa2b-46819763d55f', current_timestamp, 'LEAST_OCCUPIED', '95-010', 'Strykow', 'Krotka', 9);

-- First Sector S1
INSERT INTO public.sector (id, creation_timestamp, active ,occupied_places, max_places, weight, name, parking_id, type, version) VALUES ('3e6a85db-d751-4549-bbb7-9705f0b2fa6b', current_timestamp, true, 30, 50, 1, 'SA-01', '96a36faa-f2a2-41b8-9c3c-b6bef04ce6d1', 'UNCOVERED', 0);

-- Second Sector S1
INSERT INTO public.sector (id, creation_timestamp, active, occupied_places, max_places, weight, name, parking_id, type, version) VALUES ('9f7f2969-1b7e-4bb3-ab84-6dbc31c01277', current_timestamp, true, 30, 50, 1, 'S1', '96a36faa-f2a2-41b8-9c3c-b6bef04ce6d1', 'UNCOVERED', 0);

-- Third Sector S1
INSERT INTO public.sector (id, creation_timestamp, active, occupied_places, max_places, weight, name, parking_id, type, version) VALUES ('99023647-d8d2-43a5-91c7-b24781f06e13', current_timestamp, true, 30, 50, 1, 'S2', 'a54e7ae6-ba2c-4fac-8ef6-e9d27da48921', 'UNCOVERED', 0);

-- Fourth Sector S1
INSERT INTO public.sector (id, creation_timestamp, active, occupied_places, max_places, weight, name, parking_id, type, version) VALUES ('bca50310-f4fb-4911-bf3c-68e00e517b95', current_timestamp, true, 30, 50, 1, 'S3', 'a54e7ae6-ba2c-4fac-8ef6-e9d27da48921', 'UNCOVERED', 0);

-- Fifth Sector S1
INSERT INTO public.sector (id, creation_timestamp, active, occupied_places, max_places, weight, name, parking_id, type, version) VALUES ('82b4ad91-a5ef-48bf-bc5b-390d7f4d1dea', current_timestamp, true, 30, 50, 1, 'S4', 'a54e7ae6-ba2c-4fac-8ef6-e9d27da48921', 'COVERED', 0);

-- Sixth Sector S1
INSERT INTO public.sector (id, creation_timestamp, active, occupied_places, max_places, weight, name, parking_id, type, version) VALUES ('36e19dcd-1d5b-4258-a5df-ba9f4372c58c', current_timestamp, true, 30, 50, 1, 'S5', 'a54e7ae6-ba2c-4fac-8ef6-e9d27da48921', 'UNDERGROUND', 0);

-- Seventh Sector S2
INSERT INTO public.sector (id, creation_timestamp, active, occupied_places, max_places, weight, name, parking_id, type, version) VALUES ('4ce920a0-6f4d-4e95-ba24-99ba32b66491', current_timestamp, true, 30, 60, 2, 'SA-02', '96a36faa-f2a2-41b8-9c3c-b6bef04ce6d1', 'UNCOVERED', 0);

-- Eighth Sector S3
INSERT INTO public.sector (id, creation_timestamp, active, occupied_places, max_places, weight, name, parking_id, type, version) VALUES ('c51557aa-284d-44a6-b38d-b6ceb9c23725', current_timestamp, false, 30, 70, 3, 'SA-03', '96a36faa-f2a2-41b8-9c3c-b6bef04ce6d1', 'UNCOVERED', 0);

-- First Reservation (for michalkowal)
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('90a0035d-6265-4b53-a547-901b3bbabd1d', current_timestamp, TIMESTAMP '2024-04-10 07:00:00', TIMESTAMP '2024-04-10 09:00:00', '69507c7f-4c03-4087-85e6-3ae3b6fc2201', '3e6a85db-d751-4549-bbb7-9705f0b2fa6b', 0, 'AWAITING');

-- Reservation for jakubkoza - expired
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('1ec7d685-71ac-4418-834a-ed7b6fc68fc8', current_timestamp, TIMESTAMP '2024-04-15 12:00:00', TIMESTAMP '2024-04-15 13:00:00', '9428fadf-191c-4dd7-8626-01c3e0ff603c', '4ce920a0-6f4d-4e95-ba24-99ba32b66491', 0, 'COMPLETED_AUTOMATICALLY');

-- Reservation for jakubkoza - valid
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('8c3c21c4-b332-4d72-9390-b0b6ad41d173', current_timestamp, current_timestamp, current_timestamp + '4 hours', '9428fadf-191c-4dd7-8626-01c3e0ff603c', '4ce920a0-6f4d-4e95-ba24-99ba32b66491', 0, 'AWAITING');

-- Reservation for jakubkoza - not started yet
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('4e892a98-ba3d-47da-aa56-26ad52746280', current_timestamp, current_timestamp + '6 days', current_timestamp + '7 days', '9428fadf-191c-4dd7-8626-01c3e0ff603c', '4ce920a0-6f4d-4e95-ba24-99ba32b66491', 0, 'AWAITING');

-- Third Reservation
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('a7709a4d-b7bc-40c4-8fd5-5c7cfcb0f146', current_timestamp, TIMESTAMP '2024-03-01 15:00:00', TIMESTAMP '2024-03-01 16:00:00', null, 'c51557aa-284d-44a6-b38d-b6ceb9c23725', 0, 'AWAITING');

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
INSERT INTO public.client_data (id, type) VALUES ('4d1c0c8a-3af5-4ce9-ba95-09d25d289a76', 'BASIC');

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
INSERT INTO public.client_data (id, type) VALUES ('3989bada-0d14-49e1-8ff5-e66f095278d4', 'BASIC');

-- Blocked by admin User 1 - Client
INSERT INTO public.account (id, creation_timestamp, login, password, suspended, active, two_factor_auth, language, phone_number, version, blocked, blocked_timestamp) VALUES ('3b79a81c-f3c8-4af7-9fd2-793f53ba8a28', current_timestamp, 'vincentvega', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', false, true, false, 'PL', '100000000', 0, true, current_timestamp + '7 days');

INSERT INTO public.personal_data (id, name, lastname, email) VALUES ('3b79a81c-f3c8-4af7-9fd2-793f53ba8a28', 'Vincent', 'Vega', 'vincentvega@example.com');

INSERT INTO public.user_level (id, creation_timestamp, level, account_id, version) VALUES ('bad2b2ba-4a30-4512-b91f-92ab55d057bc', current_timestamp, 'CLIENT', '3b79a81c-f3c8-4af7-9fd2-793f53ba8a28', 0);
INSERT INTO public.client_data (id, type) VALUES ('bad2b2ba-4a30-4512-b91f-92ab55d057bc', 'BASIC');

-------------------------------------

-- Activated User 1 - Client
INSERT INTO public.account (id, creation_timestamp, login, password, suspended, active, two_factor_auth, language, phone_number, version, blocked) VALUES ('c276cb93-5cfe-4bf5-9998-ecdeee8ba06b', current_timestamp, 'jchrystus', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', false, false, false, 'PL', '100000000', 0, false);

INSERT INTO public.personal_data (id, name, lastname, email) VALUES ('c276cb93-5cfe-4bf5-9998-ecdeee8ba06b', 'Jezus', 'Chrystus', 'jchrystus@example.com');

INSERT INTO public.user_level (id, creation_timestamp, level, account_id, version) VALUES ('900cbc37-2a95-4bd6-96f2-897c12155f85', current_timestamp, 'CLIENT', 'c276cb93-5cfe-4bf5-9998-ecdeee8ba06b', 0);
INSERT INTO public.client_data (id, type) VALUES ('900cbc37-2a95-4bd6-96f2-897c12155f85', 'BASIC');

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

-- Enter without reservation check methods tests

INSERT INTO public.parking (id, creation_timestamp, sector_strategy, zip_code, city, street, version) VALUES ('3591ced3-996e-49b4-8c56-40fe91193b1d', current_timestamp, 'LEAST_OCCUPIED', '99-999', 'test1', 'test1', 0);

INSERT INTO public.sector (id, creation_timestamp, active, available_places, max_places, weight, name, parking_id, type, version) VALUES ('14d51050-ffe2-4da2-abd2-4e6d06759ea5', current_timestamp, true, 0, 5, 3, 'UC-01', '3591ced3-996e-49b4-8c56-40fe91193b1d', 'UNCOVERED', 0);
INSERT INTO public.sector (id, creation_timestamp, active, available_places, max_places, weight, name, parking_id, type, version) VALUES ('933bcce5-a38c-4b09-bd60-2b746d9f40e8', current_timestamp, true, 1, 2, 3, 'UC-02', '3591ced3-996e-49b4-8c56-40fe91193b1d', 'UNCOVERED', 0);
INSERT INTO public.sector (id, creation_timestamp, active, available_places, max_places, weight, name, parking_id, type, version) VALUES ('828228e6-2fa7-418e-8cfe-7f4d79737557', current_timestamp, true, 1, 1, 3, 'UC-03', '3591ced3-996e-49b4-8c56-40fe91193b1d', 'UNCOVERED', 0);
INSERT INTO public.sector (id, creation_timestamp, active, available_places, max_places, weight, name, parking_id, type, version) VALUES ('38c70882-c413-467d-bdd8-c5ed5f9128d0', current_timestamp, true, 1, 1, 3, 'UC-04', '3591ced3-996e-49b4-8c56-40fe91193b1d', 'UNCOVERED', 0);

INSERT INTO public.sector (id, creation_timestamp, active, available_places, max_places, weight, name, parking_id, type, version) VALUES ('f274420a-1322-4530-bfcb-4e515dd5a920', current_timestamp, true, 0, 2, 3, 'CO-01', '3591ced3-996e-49b4-8c56-40fe91193b1d', 'COVERED', 0);
INSERT INTO public.sector (id, creation_timestamp, active, available_places, max_places, weight, name, parking_id, type, version) VALUES ('ae65eca6-669d-43ec-8c35-39f4eb6b72bb', current_timestamp, true, 1, 1, 3, 'CO-02', '3591ced3-996e-49b4-8c56-40fe91193b1d', 'COVERED', 0);

INSERT INTO public.sector (id, creation_timestamp, active, available_places, max_places, weight, name, parking_id, type, version) VALUES ('65c51075-0749-4304-984a-9cb926e65aab', current_timestamp, true, 0, 2, 3, 'UN-01', '3591ced3-996e-49b4-8c56-40fe91193b1d', 'UNDERGROUND', 0);
INSERT INTO public.sector (id, creation_timestamp, active, available_places, max_places, weight, name, parking_id, type, version) VALUES ('b9f5bb0c-d19f-4101-ac57-d758e063ac3e', current_timestamp, true, 1, 1, 3, 'UN-02', '3591ced3-996e-49b4-8c56-40fe91193b1d', 'UNDERGROUND', 0);

-- Reservations to the first sector, all reservation cases
--1 -- completed - non blocking
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('98a2fa9f-3b67-4a77-842d-33fa859d1933', current_timestamp, TIMESTAMP '2023-12-12 04:00:00', TIMESTAMP '2023-12-12 06:00:00', '69507c7f-4c03-4087-85e6-3ae3b6fc2201', '14d51050-ffe2-4da2-abd2-4e6d06759ea5', 0, 'COMPLETED_MANUALLY');
INSERT INTO public.parking_event (id, reservation_id, date, type, version) VALUES ('ea600708-036e-43a9-8020-9589c48c3d35', '98a2fa9f-3b67-4a77-842d-33fa859d1933', TIMESTAMP '2023-12-12 06:00:00', 'ENTRY', 0);
INSERT INTO public.parking_event (id, reservation_id, date, type, version) VALUES ('d71cd61b-616b-4c09-9d77-50b15ce70ec6', '98a2fa9f-3b67-4a77-842d-33fa859d1933', TIMESTAMP '2023-12-12 06:00:00', 'EXIT', 0);

--2.1 -- starts and ends in the past, but still in progress - overlaps - one entry - blocking
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('7a7035f8-d15b-48c9-86c2-eb1264c93993', current_timestamp, current_timestamp - interval '3 hour', current_timestamp - interval '1 hour', '69507c7f-4c03-4087-85e6-3ae3b6fc2201', '14d51050-ffe2-4da2-abd2-4e6d06759ea5', 0, 'IN_PROGRESS');
INSERT INTO public.parking_event (id, reservation_id, date, type, version) VALUES ('96923bbc-3f9d-4e5c-a5c4-f3428aaa6d7e', '7a7035f8-d15b-48c9-86c2-eb1264c93993', current_timestamp - interval '1 hour', 'ENTRY', 0);

--2.2 -- starts and ends in the past, but still in progress - overlaps - one entry, one exit - blocking
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('1b1282c0-ae25-4cfe-b22d-fe1f3abf4fdc', current_timestamp, current_timestamp - interval '3 hour', current_timestamp - interval '1 hour', '69507c7f-4c03-4087-85e6-3ae3b6fc2201', '14d51050-ffe2-4da2-abd2-4e6d06759ea5', 0, 'IN_PROGRESS');
INSERT INTO public.parking_event (id, reservation_id, date, type, version) VALUES ('3ba9685e-3676-4331-b73e-1c63526c0d30', '1b1282c0-ae25-4cfe-b22d-fe1f3abf4fdc', current_timestamp - interval '2 hours', 'ENTRY', 0);
INSERT INTO public.parking_event (id, reservation_id, date, type, version) VALUES ('93df7dbc-030f-4b45-8c4f-b514419aa1e9', '1b1282c0-ae25-4cfe-b22d-fe1f3abf4fdc', current_timestamp - interval '1 hour 30 minutes', 'EXIT', 0);

--3 -- starts and ends in the past, but still in progress - doesn't overlap - one entry, one exit - non blocking
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('972dc388-7cd7-49b5-8e4d-57a693d81cc3', current_timestamp, current_timestamp - interval '48 hours', current_timestamp - interval '46 hour', '69507c7f-4c03-4087-85e6-3ae3b6fc2201', '14d51050-ffe2-4da2-abd2-4e6d06759ea5', 0, 'IN_PROGRESS');
INSERT INTO public.parking_event (id, reservation_id, date, type, version) VALUES ('7a5ac6a9-654b-4845-9f34-95eccfc5342b', '972dc388-7cd7-49b5-8e4d-57a693d81cc3', current_timestamp - interval '48 hours', 'ENTRY', 0);
INSERT INTO public.parking_event (id, reservation_id, date, type, version) VALUES ('e0fd29ce-ae33-42fa-a79c-1e2b72c48ee7', '972dc388-7cd7-49b5-8e4d-57a693d81cc3', current_timestamp - interval '47 hours', 'EXIT', 0);

--4.1 -- started in past ends in future - one entry - blocking
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('1fe14106-1c04-44c4-b69b-4ce46797a71b', current_timestamp, current_timestamp - interval '1 hour', current_timestamp + interval '1 hour', '69507c7f-4c03-4087-85e6-3ae3b6fc2201', '14d51050-ffe2-4da2-abd2-4e6d06759ea5', 0, 'IN_PROGRESS');
INSERT INTO public.parking_event (id, reservation_id, date, type, version) VALUES ('e79969aa-e684-4f2e-840a-a4118f4c027f', '1fe14106-1c04-44c4-b69b-4ce46797a71b', current_timestamp - interval '1 hour', 'ENTRY', 0);

--4.2 -- started in past ends in future - one entry, one exit - blocking
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('a6de4f42-a573-4cea-a4e4-141d2b7a2121', current_timestamp, current_timestamp - interval '1 hour', current_timestamp + interval '1 hour', '69507c7f-4c03-4087-85e6-3ae3b6fc2201', '14d51050-ffe2-4da2-abd2-4e6d06759ea5', 0, 'IN_PROGRESS');
INSERT INTO public.parking_event (id, reservation_id, date, type, version) VALUES ('449da003-d227-4693-a8cf-7dc81cfd0ee6', 'a6de4f42-a573-4cea-a4e4-141d2b7a2121', current_timestamp - interval '1 hour', 'ENTRY', 0);
INSERT INTO public.parking_event (id, reservation_id, date, type, version) VALUES ('86379208-420d-42f5-b1fa-eaa90840fd97', 'a6de4f42-a573-4cea-a4e4-141d2b7a2121', current_timestamp - interval '30 minutes', 'EXIT', 0);

--5 -- starts in the future - overlaps - blocking
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('0866b147-62c8-49c6-9721-6fd345597292', current_timestamp, current_timestamp + interval '1 hour', current_timestamp + interval '3 hour', '69507c7f-4c03-4087-85e6-3ae3b6fc2201', '14d51050-ffe2-4da2-abd2-4e6d06759ea5', 0, 'AWAITING');

--6 -- starts in the future - doesn't overlap - non blocking
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('1d86b115-9c18-4fd6-81be-e8878ad28a5d', current_timestamp, current_timestamp + interval '25 hour', current_timestamp + interval '28 hour', '69507c7f-4c03-4087-85e6-3ae3b6fc2201', '14d51050-ffe2-4da2-abd2-4e6d06759ea5', 0, 'AWAITING');

--bonus -- starts past end time null - blocking
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('731c7497-19f9-44af-8143-8d5281d264f0', current_timestamp, current_timestamp - interval '1 hour', null, null, '14d51050-ffe2-4da2-abd2-4e6d06759ea5', 0, 'IN_PROGRESS');


-- Reservations to the second sector, one blocking case and all non blocking
--1 -- completed - non blocking
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('5f2048b1-f964-4c0a-99f9-2f006c304794', current_timestamp, TIMESTAMP '2023-12-12 04:00:00', TIMESTAMP '2023-12-12 06:00:00', '69507c7f-4c03-4087-85e6-3ae3b6fc2201', '933bcce5-a38c-4b09-bd60-2b746d9f40e8', 0, 'COMPLETED_MANUALLY');
INSERT INTO public.parking_event (id, reservation_id, date, type, version) VALUES ('199e1411-677c-44d9-82e8-13fc12822af1', '5f2048b1-f964-4c0a-99f9-2f006c304794', TIMESTAMP '2023-12-12 06:00:00', 'ENTRY', 0);
INSERT INTO public.parking_event (id, reservation_id, date, type, version) VALUES ('b827ce01-4d40-480d-a3bd-813fc9919703', '5f2048b1-f964-4c0a-99f9-2f006c304794', TIMESTAMP '2023-12-12 06:00:00', 'EXIT', 0);

--3 -- starts and ends in the past, but still in progress - doesn't overlap - one entry, one exit - non blocking
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('121dc917-c508-4f03-8471-9becb82e9835', current_timestamp, current_timestamp - interval '48 hours', current_timestamp - interval '46 hour', '69507c7f-4c03-4087-85e6-3ae3b6fc2201', '933bcce5-a38c-4b09-bd60-2b746d9f40e8', 0, 'IN_PROGRESS');
INSERT INTO public.parking_event (id, reservation_id, date, type, version) VALUES ('18bb2c32-3323-44cc-8eac-f7e526e3a962', '121dc917-c508-4f03-8471-9becb82e9835', current_timestamp - interval '48 hours', 'ENTRY', 0);
INSERT INTO public.parking_event (id, reservation_id, date, type, version) VALUES ('e68bc0f3-745e-4058-996a-37a330ce2c18', '121dc917-c508-4f03-8471-9becb82e9835', current_timestamp - interval '47 hours', 'EXIT', 0);

--6 -- starts in the future - doesn't overlap - non blocking
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('a195cf4d-5ae3-46e8-be57-85bec9089c0d', current_timestamp, current_timestamp + interval '25 hour', current_timestamp + interval '28 hour', '69507c7f-4c03-4087-85e6-3ae3b6fc2201', '933bcce5-a38c-4b09-bd60-2b746d9f40e8', 0, 'IN_PROGRESS');

--5 -- starts in the future - overlaps - blocking
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('0b883304-fd50-40dc-a7df-3634cacfef9c', current_timestamp, current_timestamp + interval '1 hour', current_timestamp + interval '3 hour', '69507c7f-4c03-4087-85e6-3ae3b6fc2201', '933bcce5-a38c-4b09-bd60-2b746d9f40e8', 0, 'IN_PROGRESS');

-- Reservations to the third sector, all non blocking
--1 -- completed - non blocking
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('62de17c1-a3ff-4d2a-8d61-dc2e280f3be6', current_timestamp, TIMESTAMP '2023-12-12 04:00:00', TIMESTAMP '2023-12-12 06:00:00', '69507c7f-4c03-4087-85e6-3ae3b6fc2201', '828228e6-2fa7-418e-8cfe-7f4d79737557', 0, 'COMPLETED_MANUALLY');
INSERT INTO public.parking_event (id, reservation_id, date, type, version) VALUES ('94f62649-2962-4015-adaf-a921f384ea55', '62de17c1-a3ff-4d2a-8d61-dc2e280f3be6', TIMESTAMP '2023-12-12 06:00:00', 'ENTRY', 0);
INSERT INTO public.parking_event (id, reservation_id, date, type, version) VALUES ('2c6d47c5-1c12-46cb-869f-ba5d0068ae0d', '62de17c1-a3ff-4d2a-8d61-dc2e280f3be6', TIMESTAMP '2023-12-12 06:00:00', 'EXIT', 0);

--3 -- starts and ends in the past, but still in progress - doesn't overlap - one entry, one exit - non blocking
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('27b8f933-3bdf-4bfb-affe-0d19260a51cc', current_timestamp, current_timestamp - interval '48 hours', current_timestamp - interval '46 hour', '69507c7f-4c03-4087-85e6-3ae3b6fc2201', '828228e6-2fa7-418e-8cfe-7f4d79737557', 0, 'IN_PROGRESS');
INSERT INTO public.parking_event (id, reservation_id, date, type, version) VALUES ('da4d6921-f627-48ee-960f-422e6988c67d', '27b8f933-3bdf-4bfb-affe-0d19260a51cc', current_timestamp - interval '48 hours', 'ENTRY', 0);
INSERT INTO public.parking_event (id, reservation_id, date, type, version) VALUES ('c074563c-c503-47f6-9222-0443c517e47d', '27b8f933-3bdf-4bfb-affe-0d19260a51cc', current_timestamp - interval '47 hours', 'EXIT', 0);

--6 -- starts in the future - doesn't overlap - non blocking
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('e598892e-7b3b-4e33-ad24-472fb05b0e8f', current_timestamp, current_timestamp + interval '25 hour', current_timestamp + interval '28 hour', '69507c7f-4c03-4087-85e6-3ae3b6fc2201', '828228e6-2fa7-418e-8cfe-7f4d79737557', 0, 'IN_PROGRESS');

-- Reservations to first covered sector - no available places
--4.2 -- started in past ends in future - one entry, one exit - blocking
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('4fcc2377-e95a-4f6a-a69b-185c2cc3bc36', current_timestamp, current_timestamp - interval '1 hour', current_timestamp + interval '1 hour', '69507c7f-4c03-4087-85e6-3ae3b6fc2201', 'f274420a-1322-4530-bfcb-4e515dd5a920', 0, 'IN_PROGRESS');
INSERT INTO public.parking_event (id, reservation_id, date, type, version) VALUES ('73f5a6b6-9d15-4ace-8fca-98afaf76f7c4', '4fcc2377-e95a-4f6a-a69b-185c2cc3bc36', current_timestamp - interval '1 hour', 'ENTRY', 0);
INSERT INTO public.parking_event (id, reservation_id, date, type, version) VALUES ('8ac1b58b-541e-4011-bdc0-d175223c7b26', '4fcc2377-e95a-4f6a-a69b-185c2cc3bc36', current_timestamp - interval '30 minutes', 'EXIT', 0);

--5 -- starts in the future - overlaps - blocking
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('7b8ef319-1a84-4f7d-9cb6-933f73927682', current_timestamp, current_timestamp + interval '1 hour', current_timestamp + interval '3 hour', '69507c7f-4c03-4087-85e6-3ae3b6fc2201', 'f274420a-1322-4530-bfcb-4e515dd5a920', 0, 'IN_PROGRESS');

-- Reservations to first underground sector - no available places
--4.2 -- started in past ends in future - one entry, one exit - blocking
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('41d93d74-9d4b-4e93-a044-ec77a01c4a32', current_timestamp, current_timestamp - interval '1 hour', current_timestamp + interval '1 hour', '69507c7f-4c03-4087-85e6-3ae3b6fc2201', '65c51075-0749-4304-984a-9cb926e65aab', 0, 'IN_PROGRESS');
INSERT INTO public.parking_event (id, reservation_id, date, type, version) VALUES ('7c22038c-e215-4479-9526-4d558cb3ac48', '41d93d74-9d4b-4e93-a044-ec77a01c4a32', current_timestamp - interval '1 hour', 'ENTRY', 0);
INSERT INTO public.parking_event (id, reservation_id, date, type, version) VALUES ('bb47ce52-9efb-4f65-b9f5-251f5639f538', '41d93d74-9d4b-4e93-a044-ec77a01c4a32', current_timestamp - interval '30 minutes', 'EXIT', 0);

--5 -- starts in the future - overlaps - blocking
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('fcae0865-d856-4dba-8094-04dc60b8a027', current_timestamp, current_timestamp + interval '1 hour', current_timestamp + interval '3 hour', '69507c7f-4c03-4087-85e6-3ae3b6fc2201', '65c51075-0749-4304-984a-9cb926e65aab', 0, 'IN_PROGRESS');
