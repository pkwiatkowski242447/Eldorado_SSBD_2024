-- -- GRAND PRIVILEGES
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.account        TO ssbd03mok;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.past_password  TO ssbd03mok;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.personal_data  TO ssbd03mok;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.user_level     TO ssbd03mok;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.client_data    TO ssbd03mok;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.staff_data     TO ssbd03mok;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.admin_data     TO ssbd03mok;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.token          TO ssbd03mok;

GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.reservation    TO ssbd03mop;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.parking        TO ssbd03mop;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.sector         TO ssbd03mop;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.parking_event  TO ssbd03mop;
GRANT SELECT                         ON TABLE public.account        TO ssbd03mop;
GRANT SELECT                         ON TABLE public.personal_data  TO ssbd03mop;
GRANT SELECT                         ON TABLE public.user_level     TO ssbd03mop;
GRANT SELECT                , UPDATE ON TABLE public.client_data    TO ssbd03mop;
--GRANT SELECT                         ON TABLE public.staff_data    TO ssbd03mop;

GRANT SELECT, UPDATE                 ON TABLE public.account        TO ssbd03auth;
GRANT SELECT                         ON TABLE public.personal_data  TO ssbd03auth;
GRANT SELECT                         ON TABLE public.user_level     TO ssbd03auth;
GRANT SELECT                         ON TABLE public.client_data    TO ssbd03auth;
GRANT SELECT                         ON TABLE public.staff_data     TO ssbd03auth;
GRANT SELECT                         ON TABLE public.admin_data     TO ssbd03auth;
GRANT SELECT, INSERT, DELETE         ON TABLE public.token          TO ssbd03auth;

GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.token          TO ssbd03mok;

-- INSERT VALUE
-- Admin
INSERT INTO public.account (id, login, password, suspended, active, two_factor_auth, language, phone_number, creation_date, version, blocked) VALUES ('b3b8c2ac-21ff-434b-b490-aa6d717447c0', 'jerzybem', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', false, true, false, 'PL', '111111111', current_timestamp, 0, false);
INSERT INTO public.past_password(account_id, past_password) VALUES ('b3b8c2ac-21ff-434b-b490-aa6d717447c0', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa');
INSERT INTO public.personal_data (id, name, lastname, email) VALUES ('b3b8c2ac-21ff-434b-b490-aa6d717447c0', 'Jerzy', 'Bem', 'jerzybem@spoko.pl');

-- Admin - admin role
INSERT INTO public.user_level (id, level, account_id, version) VALUES ('bdb52e59-0054-4ec5-a2af-3e0d2b187ce0', 'ADMIN', 'b3b8c2ac-21ff-434b-b490-aa6d717447c0', 0);
INSERT INTO public.admin_data (id) VALUES ('bdb52e59-0054-4ec5-a2af-3e0d2b187ce0');

-- Admin - staff role
INSERT INTO public.user_level (id, level, account_id, version) VALUES ('9ba0d086-edec-475d-8569-69156e79be9d', 'STAFF', 'b3b8c2ac-21ff-434b-b490-aa6d717447c0', 0);
INSERT INTO public.staff_data (id) VALUES ('9ba0d086-edec-475d-8569-69156e79be9d');

-- Staff
INSERT INTO public.account (id, login, password, suspended, active, two_factor_auth, language, phone_number, creation_date, version, blocked) VALUES ('e0bf979b-6b42-432d-8462-544d88b1ab5f', 'kamilslimak', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', false, true, false, 'PL', '222222222', current_timestamp, 0, false);
INSERT INTO public.past_password(account_id, past_password) VALUES ('e0bf979b-6b42-432d-8462-544d88b1ab5f', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa');
INSERT INTO public.personal_data (id, name, lastname, email) VALUES ('e0bf979b-6b42-432d-8462-544d88b1ab5f', 'Kamil', 'Slimak', 'kamilslimak1@spoko.pl');

INSERT INTO public.user_level (id, level, account_id, version) VALUES ('2488831d-c7c4-4f61-b48a-3be87364271f', 'STAFF', 'e0bf979b-6b42-432d-8462-544d88b1ab5f', 0);
INSERT INTO public.staff_data (id) VALUES ('2488831d-c7c4-4f61-b48a-3be87364271f');

-- First Client
INSERT INTO public.account (id, login, password, suspended, active, two_factor_auth, language, phone_number, creation_date, version, blocked) VALUES ('0ca02f7e-d8e9-45d3-a332-a56015acb822', 'michalkowal', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', false, true, false, 'PL', '000000000', current_timestamp, 0, false);
INSERT INTO public.past_password(account_id, past_password) VALUES ('0ca02f7e-d8e9-45d3-a332-a56015acb822', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa');
INSERT INTO public.personal_data (id, name, lastname, email) VALUES ('0ca02f7e-d8e9-45d3-a332-a56015acb822', 'Michal', 'Kowalski', 'michalkowal@spoko.pl');

INSERT INTO public.user_level (id, level, account_id, version) VALUES ('69507c7f-4c03-4087-85e6-3ae3b6fc2201', 'CLIENT', '0ca02f7e-d8e9-45d3-a332-a56015acb822', 0);
INSERT INTO public.client_data (id, type) VALUES ('69507c7f-4c03-4087-85e6-3ae3b6fc2201', 'BASIC');

-- Second Client
INSERT INTO public.account (id, login, password, suspended, active, two_factor_auth, language, phone_number, creation_date, version, blocked) VALUES ('902d6e5b-2449-4898-a4f7-a92b0d8a04e1', 'jakubkoza', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', false, true, false, 'EN', '000000001', current_timestamp, 0, false);
INSERT INTO public.past_password(account_id, past_password) VALUES ('902d6e5b-2449-4898-a4f7-a92b0d8a04e1', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa');
INSERT INTO public.personal_data (id, name, lastname, email) VALUES ('902d6e5b-2449-4898-a4f7-a92b0d8a04e1', 'Jakub', 'Koza', 'jakubkoza@adresik.net');

INSERT INTO public.user_level (id, level, account_id, version) VALUES ('9428fadf-191c-4dd7-8626-01c3e0ff603c', 'CLIENT', '902d6e5b-2449-4898-a4f7-a92b0d8a04e1', 0);
INSERT INTO public.client_data (id, type) VALUES ('9428fadf-191c-4dd7-8626-01c3e0ff603c', 'BASIC');

-- Third Client
INSERT INTO public.account (id, login, password, suspended, active, two_factor_auth, language, phone_number, creation_date, version, blocked) VALUES ('02b0d9d7-a472-48d0-95e0-13a3e6a11d00', 'piotrnowak', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', false, true, false, 'PL', '000000003', current_timestamp, 0, false);
INSERT INTO public.past_password(account_id, past_password) VALUES ('02b0d9d7-a472-48d0-95e0-13a3e6a11d00', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa');
INSERT INTO public.personal_data (id, name, lastname, email) VALUES ('02b0d9d7-a472-48d0-95e0-13a3e6a11d00', 'Piotr', 'Nowak', 'piotrnowak@porn.hub.pl');

INSERT INTO public.user_level (id, level, account_id, version) VALUES ('cbf34cb0-c96b-4037-80d4-1eef34890e85', 'CLIENT', '02b0d9d7-a472-48d0-95e0-13a3e6a11d00', 0);
INSERT INTO public.client_data (id, type) VALUES ('cbf34cb0-c96b-4037-80d4-1eef34890e85', 'BASIC');

-- Admin - test
INSERT INTO public.account (id, login, password, suspended, active, two_factor_auth, language, phone_number, creation_date, version, blocked) VALUES ('9a333f13-5ccc-4109-bce3-0ad629843edf', 'aandrus', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', false, true, false, 'pl', '111111112', current_timestamp, 0, false);
INSERT INTO public.personal_data (id, name, lastname, email) VALUES ('9a333f13-5ccc-4109-bce3-0ad629843edf', 'Andrzej', 'Andrus', 'aandrus@example.com');
INSERT INTO public.user_level (id, level, account_id, version) VALUES ('c21bda59-3ed9-4370-8675-3b53ddb04f4e', 'ADMIN', '9a333f13-5ccc-4109-bce3-0ad629843edf', 0);
INSERT INTO public.admin_data (id) VALUES ('c21bda59-3ed9-4370-8675-3b53ddb04f4e');

-- Staff - test
INSERT INTO public.account (id, login, password, suspended, active, two_factor_auth, language, phone_number, creation_date, version, blocked) VALUES ('f512c0b6-40b2-4bcb-8541-46077ac02101', 'tkarol', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', false, true, false, 'pl', '111111113', current_timestamp, 0, false);
INSERT INTO public.personal_data (id, name, lastname, email) VALUES ('f512c0b6-40b2-4bcb-8541-46077ac02101', 'Tomasz', 'Karolak', 'tkarol@example.com');
INSERT INTO public.user_level (id, level, account_id, version) VALUES ('00568964-9f83-441e-b441-83e545d51733', 'STAFF', 'f512c0b6-40b2-4bcb-8541-46077ac02101', 0);
INSERT INTO public.staff_data (id) VALUES ('00568964-9f83-441e-b441-83e545d51733');

-- Staff - test
INSERT INTO public.account (id, login, password, suspended, active, two_factor_auth, language, phone_number, creation_date, version, blocked) VALUES ('f14ac5b1-16f3-42ff-8df3-dd95de69c368', 'kwotyla', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', false, true, false, 'pl', '111111114', current_timestamp, 0, false);
INSERT INTO public.personal_data (id, name, lastname, email) VALUES ('f14ac5b1-16f3-42ff-8df3-dd95de69c368', 'Krystian', 'Womyla', 'krystianwomyla@example.com');
INSERT INTO public.user_level (id, level, account_id, version) VALUES ('248a31fa-7fef-41d5-8042-e70a38d30a9d', 'CLIENT', 'f14ac5b1-16f3-42ff-8df3-dd95de69c368', 0);
INSERT INTO public.client_data (id, type) VALUES ('248a31fa-7fef-41d5-8042-e70a38d30a9d', 'BASIC');

-- Parking
INSERT INTO public.parking (id, zip_code, city, street, version) VALUES ('96a36faa-f2a2-41b8-9c3c-b6bef04ce6d1', '00-000', 'BoatCity', 'Palki', 0);

-- First Sector S1
INSERT INTO public.sector (id, available_places, max_places, weight, name, parking_id, type, version) VALUES ('3e6a85db-d751-4549-bbb7-9705f0b2fa6b', 20, 50, 1, 'S1', '96a36faa-f2a2-41b8-9c3c-b6bef04ce6d1', 'UNCOVERED', 0);

-- Second Sector S2
INSERT INTO public.sector (id, available_places, max_places, weight, name, parking_id, type, version) VALUES ('4ce920a0-6f4d-4e95-ba24-99ba32b66491', 30, 60, 2, 'S2', '96a36faa-f2a2-41b8-9c3c-b6bef04ce6d1', 'UNCOVERED', 0);

-- Third Sector S3
INSERT INTO public.sector (id, available_places, max_places, weight, name, parking_id, type, version) VALUES ('c51557aa-284d-44a6-b38d-b6ceb9c23725', 40, 70, 3, 'S3', '96a36faa-f2a2-41b8-9c3c-b6bef04ce6d1', 'UNCOVERED', 0);

-- First Reservation
INSERT INTO public.reservation (id, begin_time, end_time, client_id, sector_id, version) VALUES ('90a0035d-6265-4b53-a547-901b3bbabd1d', TIMESTAMP '2024-04-10 07:00:00', TIMESTAMP '2024-04-10 09:00:00', '69507c7f-4c03-4087-85e6-3ae3b6fc2201', '3e6a85db-d751-4549-bbb7-9705f0b2fa6b', 0);

-- Second Reservation
INSERT INTO public.reservation (id, begin_time, end_time, client_id, sector_id, version) VALUES ('1ec7d685-71ac-4418-834a-ed7b6fc68fc8', TIMESTAMP '2024-04-15 12:00:00', TIMESTAMP '2024-04-15 13:00:00', '9428fadf-191c-4dd7-8626-01c3e0ff603c', '4ce920a0-6f4d-4e95-ba24-99ba32b66491', 0);

-- Third Reservation
INSERT INTO public.reservation (id, begin_time, end_time, client_id, sector_id, version) VALUES ('a7709a4d-b7bc-40c4-8fd5-5c7cfcb0f146', TIMESTAMP '2024-03-01 15:00:00', TIMESTAMP '2024-03-01 16:00:00', null, 'c51557aa-284d-44a6-b38d-b6ceb9c23725', 0);

-- First token
INSERT INTO public.token (id, account_id, token_value, type, version) VALUES ('4ac79a06-2b75-4519-b430-1abe0e05f04e', '0ca02f7e-d8e9-45d3-a332-a56015acb822', 'TEST_VALUE', 'CONFIRM_EMAIL', 0);

-- Second token
INSERT INTO public.token (id, account_id, token_value, type, version) VALUES ('499c0085-c0b8-424e-97f5-84abe66f9bf6', '902d6e5b-2449-4898-a4f7-a92b0d8a04e1', 'TEST_VALUE2', 'RESET_PASSWORD', 0);

-- Third token
INSERT INTO public.token (id, account_id, token_value, type, version) VALUES ('b269207d-7627-4a91-af86-87d2b975d487', 'e0bf979b-6b42-432d-8462-544d88b1ab5f', 'TEST_VALUE3', 'CHANGE_OVERWRITTEN_PASSWORD', 0);

-- Parking event - Reservation 1
INSERT INTO public.parking_event (id, reservation_id, date, type, version) VALUES ('b922b5c3-08a8-4902-8ca0-e99e65506fa5', '90a0035d-6265-4b53-a547-901b3bbabd1d', TIMESTAMP '2024-04-10 07:00:00', 'ENTRY', 0);
INSERT INTO public.parking_event (id, reservation_id, date, type, version) VALUES ('eb6878af-7bb8-4094-96fc-84126e2dfb5e', '90a0035d-6265-4b53-a547-901b3bbabd1d', TIMESTAMP '2024-04-10 09:00:00', 'EXIT', 0);

-- Parking event - Reservation 3
INSERT INTO public.parking_event (id, reservation_id, date, type, version) VALUES ('12c8f77a-4a35-43f6-9134-4deddd5407b5', 'a7709a4d-b7bc-40c4-8fd5-5c7cfcb0f146', TIMESTAMP '2024-03-01 15:00:00', 'ENTRY', 0);
INSERT INTO public.parking_event (id, reservation_id, date, type, version) VALUES ('94d17bd1-da2e-4047-822b-942526dfbd5a', 'a7709a4d-b7bc-40c4-8fd5-5c7cfcb0f146', TIMESTAMP '2024-03-01 16:00:00', 'EXIT', 0);

-------------------------------------

-- Not suspended User 1 - Client
INSERT INTO public.account (id, login, password, suspended, active, two_factor_auth, language, phone_number, creation_date, version, blocked) VALUES ('f5afc042-79b0-47fe-87ee-710c14af888c', 'tonyhalik', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', false, true, false, 'PL', '100000000', current_timestamp, 0, false);

INSERT INTO public.personal_data (id, name, lastname, email) VALUES ('f5afc042-79b0-47fe-87ee-710c14af888c', 'Tony', 'Halik', 'tonyhalik@example.com');

INSERT INTO public.user_level (id, level, account_id, version) VALUES ('4d1c0c8a-3af5-4ce9-ba95-09d25d289a76', 'CLIENT', 'f5afc042-79b0-47fe-87ee-710c14af888c', 0);
INSERT INTO public.client_data (id, type) VALUES ('4d1c0c8a-3af5-4ce9-ba95-09d25d289a76', 'BASIC');

-- User 1 - Token 1
INSERT INTO public.token (id, account_id, token_value, type, version) VALUES ('582c432a-c5d9-4758-863a-4999a7d95de5', 'f5afc042-79b0-47fe-87ee-710c14af888c', 'TEST_VALUE90', 'CONFIRM_EMAIL', 0);
-- User 1 - Token 2
INSERT INTO public.token (id, account_id, token_value, type, version) VALUES ('e0c86942-aed7-44a4-bda2-a89c3f4bdd37', 'f5afc042-79b0-47fe-87ee-710c14af888c', 'TEST_VALUE91', 'CONFIRM_EMAIL', 0);

-- Not suspended User 2 - Staff
INSERT INTO public.account (id, login, password, suspended, active, two_factor_auth, language, phone_number, creation_date, version, blocked) VALUES ('d20f860d-555a-479e-8783-67aee5b66692', 'adamn', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', false, true, false, 'PL', '200000000', current_timestamp, 0, false);

INSERT INTO public.personal_data (id, name, lastname, email) VALUES ('d20f860d-555a-479e-8783-67aee5b66692', 'Adam', 'Niezgodka', 'adamn@example.com');

INSERT INTO public.user_level (id, level, account_id, version) VALUES ('c09d20a0-69e9-4a50-af00-3973b5d1d85c', 'STAFF', 'd20f860d-555a-479e-8783-67aee5b66692', 0);
INSERT INTO public.staff_data (id) VALUES ('c09d20a0-69e9-4a50-af00-3973b5d1d85c');

-- User 2 - Token 1
INSERT INTO public.token (id, account_id, token_value, type, version) VALUES ('9847130d-6e60-4b3d-a9c3-b913f50769da', 'd20f860d-555a-479e-8783-67aee5b66692', 'TEST_VALUE93', 'CONFIRM_EMAIL', 0);

-------------------------------------

-- Blocked by admin User 1 - Client
INSERT INTO public.account (id, login, password, suspended, active, two_factor_auth, language, phone_number, creation_date, version, blocked) VALUES ('16c2e579-6b85-41fd-8aae-7f3e3e279f24', 'juleswinnfield', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', false, true, false, 'PL', '100000000', current_timestamp, 0, true);

INSERT INTO public.personal_data (id, name, lastname, email) VALUES ('16c2e579-6b85-41fd-8aae-7f3e3e279f24', 'Jules', 'Winnfield', 'juleswinnfield@example.com');

INSERT INTO public.user_level (id, level, account_id, version) VALUES ('3989bada-0d14-49e1-8ff5-e66f095278d4', 'CLIENT', '16c2e579-6b85-41fd-8aae-7f3e3e279f24', 0);
INSERT INTO public.client_data (id, type) VALUES ('3989bada-0d14-49e1-8ff5-e66f095278d4', 'BASIC');

-- Blocked by admin User 1 - Client
INSERT INTO public.account (id, login, password, suspended, active, two_factor_auth, language, phone_number, creation_date, version, blocked, blocked_timestamp) VALUES ('3b79a81c-f3c8-4af7-9fd2-793f53ba8a28', 'vincentvega', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', false, true, false, 'PL', '100000000', current_timestamp, 0, true, current_timestamp + '7 days');

INSERT INTO public.personal_data (id, name, lastname, email) VALUES ('3b79a81c-f3c8-4af7-9fd2-793f53ba8a28', 'Vincent', 'Vega', 'vincentvega@example.com');

INSERT INTO public.user_level (id, level, account_id, version) VALUES ('bad2b2ba-4a30-4512-b91f-92ab55d057bc', 'CLIENT', '3b79a81c-f3c8-4af7-9fd2-793f53ba8a28', 0);
INSERT INTO public.client_data (id, type) VALUES ('bad2b2ba-4a30-4512-b91f-92ab55d057bc', 'BASIC');

-------------------------------------

-- Activated User 1 - Client
INSERT INTO public.account (id, login, password, suspended, active, two_factor_auth, language, phone_number, creation_date, version, blocked) VALUES ('c276cb93-5cfe-4bf5-9998-ecdeee8ba06b', 'jchrystus', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', false, false, false, 'PL', '100000000', current_timestamp, 0, false);

INSERT INTO public.personal_data (id, name, lastname, email) VALUES ('c276cb93-5cfe-4bf5-9998-ecdeee8ba06b', 'Jezus', 'Chrystus', 'jchrystus@example.com');

INSERT INTO public.user_level (id, level, account_id, version) VALUES ('900cbc37-2a95-4bd6-96f2-897c12155f85', 'CLIENT', 'c276cb93-5cfe-4bf5-9998-ecdeee8ba06b', 0);
INSERT INTO public.client_data (id, type) VALUES ('900cbc37-2a95-4bd6-96f2-897c12155f85', 'BASIC');