-- -- GRAND PRIVILEGES
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.account        TO ssbd03mok;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.past_password  TO ssbd03mok;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.personal_data  TO ssbd03mok;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.user_level     TO ssbd03mok;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.client_data    TO ssbd03mok;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.staff_data     TO ssbd03mok;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.admin_data     TO ssbd03mok;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.token          TO ssbd03mok;

GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.account_attributes     TO ssbd03mok;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.attribute_association  TO ssbd03mok;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.attribute_name         TO ssbd03mok;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.attribute_value        TO ssbd03mok;

GRANT SELECT                         ON TABLE public.account_attributes     TO ssbd03auth;
GRANT SELECT                         ON TABLE public.attribute_association  TO ssbd03auth;
GRANT SELECT                         ON TABLE public.attribute_name         TO ssbd03auth;
GRANT SELECT                         ON TABLE public.attribute_value        TO ssbd03auth;

GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.reservation    TO ssbd03mop;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.parking        TO ssbd03mop;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.sector         TO ssbd03mop;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.parking_event  TO ssbd03mop;
GRANT SELECT                         ON TABLE public.account        TO ssbd03mop;
GRANT SELECT                         ON TABLE public.personal_data  TO ssbd03mop;
GRANT SELECT                         ON TABLE public.user_level     TO ssbd03mop;
GRANT SELECT                , UPDATE ON TABLE public.client_data    TO ssbd03mop;

GRANT SELECT                         ON TABLE public.account_attributes     TO ssbd03mop;
GRANT SELECT                         ON TABLE public.account_attributes     TO ssbd03mop;
GRANT SELECT                         ON TABLE public.attribute_association  TO ssbd03mop;
GRANT SELECT                         ON TABLE public.attribute_name         TO ssbd03mop;
GRANT SELECT                         ON TABLE public.attribute_value        TO ssbd03mop;

GRANT SELECT, UPDATE                 ON TABLE public.account        TO ssbd03auth;
GRANT SELECT                         ON TABLE public.personal_data  TO ssbd03auth;
GRANT SELECT                         ON TABLE public.user_level     TO ssbd03auth;
GRANT SELECT                         ON TABLE public.client_data    TO ssbd03auth;
GRANT SELECT                         ON TABLE public.staff_data     TO ssbd03auth;
GRANT SELECT                         ON TABLE public.admin_data     TO ssbd03auth;

GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.token          TO ssbd03mok;

-- INSERT VALUE
-- Admin
INSERT INTO public.account (id, login, password, suspended, active, two_factor_auth, language, phone_number, creation_timestamp, version, blocked) VALUES ('b3b8c2ac-21ff-434b-b490-aa6d717447c0', 'jerzybem', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', true, true, false, 'PL', '111111111', current_timestamp, 0, false);
INSERT INTO public.past_password (account_id, past_password) VALUES ('b3b8c2ac-21ff-434b-b490-aa6d717447c0', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa');
INSERT INTO public.personal_data (id, name, lastname, email) VALUES ('b3b8c2ac-21ff-434b-b490-aa6d717447c0', 'Jerzy', 'Bem', 'jerzybem@spoko.pl');

-- Admin - admin role
INSERT INTO public.user_level (id, creation_timestamp, level, account_id, version) VALUES ('bdb52e59-0054-4ec5-a2af-3e0d2b187ce0', current_timestamp, 'ADMIN', 'b3b8c2ac-21ff-434b-b490-aa6d717447c0', 0);
INSERT INTO public.admin_data (id) VALUES ('bdb52e59-0054-4ec5-a2af-3e0d2b187ce0');

-- Admin - staff role
INSERT INTO public.user_level (id, creation_timestamp, level, account_id, version) VALUES ('9ba0d086-edec-475d-8569-69156e79be9d', current_timestamp, 'STAFF', 'b3b8c2ac-21ff-434b-b490-aa6d717447c0', 0);
INSERT INTO public.staff_data (id) VALUES ('9ba0d086-edec-475d-8569-69156e79be9d');

-- Staff
INSERT INTO public.account (id, login, password, suspended, active, two_factor_auth, language, phone_number, creation_timestamp, version, blocked) VALUES ('e0bf979b-6b42-432d-8462-544d88b1ab5f', 'kamilslimak', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', true, true, false, 'PL', '222222222', current_timestamp, 0, false);
INSERT INTO public.past_password (account_id, past_password) VALUES ('e0bf979b-6b42-432d-8462-544d88b1ab5f', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa');
INSERT INTO public.personal_data (id, name, lastname, email) VALUES ('e0bf979b-6b42-432d-8462-544d88b1ab5f', 'Kamil', 'Slimak', 'kamilslimak1@spoko.pl');

INSERT INTO public.user_level (id, creation_timestamp, level, account_id, version) VALUES ('2488831d-c7c4-4f61-b48a-3be87364271f', current_timestamp, 'STAFF', 'e0bf979b-6b42-432d-8462-544d88b1ab5f', 0);
INSERT INTO public.staff_data (id) VALUES ('2488831d-c7c4-4f61-b48a-3be87364271f');

-- First Client
INSERT INTO public.account (id, login, password, suspended, active, two_factor_auth, language, phone_number, creation_timestamp, version, blocked) VALUES ('0ca02f7e-d8e9-45d3-a332-a56015acb822', 'michalkowal', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', true, true, false, 'PL', '000000000', current_timestamp, 0, false);
INSERT INTO public.past_password (account_id, past_password) VALUES ('0ca02f7e-d8e9-45d3-a332-a56015acb822', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa');
INSERT INTO public.personal_data (id, name, lastname, email) VALUES ('0ca02f7e-d8e9-45d3-a332-a56015acb822', 'Michal', 'Kowalski', 'michalkowal@spoko.pl');

INSERT INTO public.user_level (id, creation_timestamp, level, account_id, version) VALUES ('69507c7f-4c03-4087-85e6-3ae3b6fc2201', current_timestamp, 'CLIENT', '0ca02f7e-d8e9-45d3-a332-a56015acb822', 0);
INSERT INTO public.client_data (id, type) VALUES ('69507c7f-4c03-4087-85e6-3ae3b6fc2201', 'BASIC');

-- Second Client
INSERT INTO public.account (id, login, password, suspended, active, two_factor_auth, language, phone_number, creation_timestamp, version, blocked) VALUES ('902d6e5b-2449-4898-a4f7-a92b0d8a04e1', 'jakubkoza', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', true, true, false, 'PL', '000000001', current_timestamp, 0, false);
INSERT INTO public.past_password (account_id, past_password) VALUES ('902d6e5b-2449-4898-a4f7-a92b0d8a04e1', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa');
INSERT INTO public.personal_data (id, name, lastname, email) VALUES ('902d6e5b-2449-4898-a4f7-a92b0d8a04e1', 'Jakub', 'Koza', 'jakubkoza@adresik.net');

INSERT INTO public.user_level (id, creation_timestamp, level, account_id, version) VALUES ('9428fadf-191c-4dd7-8626-01c3e0ff603c', current_timestamp, 'CLIENT', '902d6e5b-2449-4898-a4f7-a92b0d8a04e1', 0);
INSERT INTO public.client_data (id, type) VALUES ('9428fadf-191c-4dd7-8626-01c3e0ff603c', 'BASIC');

-- Third Client
INSERT INTO public.account (id, login, password, suspended, active, two_factor_auth, language, phone_number, creation_timestamp, version, blocked) VALUES ('02b0d9d7-a472-48d0-95e0-13a3e6a11d00', 'piotrnowak', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', true, true, false, 'PL', '000000003', current_timestamp, 0, false);
INSERT INTO public.past_password (account_id, past_password) VALUES ('02b0d9d7-a472-48d0-95e0-13a3e6a11d00', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa');
INSERT INTO public.personal_data (id, name, lastname, email) VALUES ('02b0d9d7-a472-48d0-95e0-13a3e6a11d00', 'Piotr', 'Nowak', 'piotrnowak@porn.hub.pl');

INSERT INTO public.user_level (id, creation_timestamp, level, account_id, version) VALUES ('cbf34cb0-c96b-4037-80d4-1eef34890e85', current_timestamp, 'CLIENT', '02b0d9d7-a472-48d0-95e0-13a3e6a11d00', 0);
INSERT INTO public.client_data (id, type) VALUES ('cbf34cb0-c96b-4037-80d4-1eef34890e85', 'BASIC');

-- Parking
INSERT INTO public.parking (id, creation_timestamp, zip_code, city, street, version) VALUES ('96a36faa-f2a2-41b8-9c3c-b6bef04ce6d1', current_timestamp, '00-000', 'BoatCity', 'Palki', 0);

-- First Sector S1
INSERT INTO public.sector (id, creation_timestamp, active, available_places, max_places, weight, name, parking_id, type, version) VALUES ('3e6a85db-d751-4549-bbb7-9705f0b2fa6b', current_timestamp, true, 20, 50, 1, 'S1', '96a36faa-f2a2-41b8-9c3c-b6bef04ce6d1', 'UNCOVERED', 0);

-- Second Sector S2
INSERT INTO public.sector (id, creation_timestamp, active, available_places, max_places, weight, name, parking_id, type, version) VALUES ('4ce920a0-6f4d-4e95-ba24-99ba32b66491', current_timestamp, true, 30, 60, 2, 'S2', '96a36faa-f2a2-41b8-9c3c-b6bef04ce6d1', 'UNCOVERED', 0);

-- Third Sector S3
INSERT INTO public.sector (id, creation_timestamp, active, available_places, max_places, weight, name, parking_id, type, version) VALUES ('c51557aa-284d-44a6-b38d-b6ceb9c23725', current_timestamp, true, 40, 70, 3, 'S3', '96a36faa-f2a2-41b8-9c3c-b6bef04ce6d1', 'UNCOVERED', 0);

-- First Reservation
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('90a0035d-6265-4b53-a547-901b3bbabd1d', current_timestamp, TIMESTAMP '2024-04-10 07:00:00', TIMESTAMP '2024-04-10 09:00:00', '69507c7f-4c03-4087-85e6-3ae3b6fc2201', '3e6a85db-d751-4549-bbb7-9705f0b2fa6b', 0, 'AWAITING');

-- Second Reservation
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('1ec7d685-71ac-4418-834a-ed7b6fc68fc8', current_timestamp, TIMESTAMP '2024-04-15 12:00:00', TIMESTAMP '2024-04-15 13:00:00', '9428fadf-191c-4dd7-8626-01c3e0ff603c', '4ce920a0-6f4d-4e95-ba24-99ba32b66491', 0, 'AWAITING');

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
INSERT INTO public.account (id, login, password, suspended, active, two_factor_auth, language, phone_number, creation_timestamp, version, blocked) VALUES ('f5afc042-79b0-47fe-87ee-710c14af888c', 'tonyhalik', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', true, true, false, 'PL', '100000000', current_timestamp, 0, false);

INSERT INTO public.personal_data (id, name, lastname, email) VALUES ('f5afc042-79b0-47fe-87ee-710c14af888c', 'Tony', 'Halik', 'tonyhalik@example.com');

INSERT INTO public.user_level (id, creation_timestamp, level, account_id, version) VALUES ('4d1c0c8a-3af5-4ce9-ba95-09d25d289a76', current_timestamp, 'CLIENT', 'f5afc042-79b0-47fe-87ee-710c14af888c', 0);
INSERT INTO public.client_data (id, type) VALUES ('4d1c0c8a-3af5-4ce9-ba95-09d25d289a76', 'BASIC');

-- User 1 - Token 1
INSERT INTO public.token (id, creation_timestamp, account_id, token_value, type, version) VALUES ('582c432a-c5d9-4758-863a-4999a7d95de5', current_timestamp, 'f5afc042-79b0-47fe-87ee-710c14af888c', 'TEST_VALUE90', 'CONFIRM_EMAIL', 0);
-- User 1 - Token 2
INSERT INTO public.token (id, creation_timestamp, account_id, token_value, type, version) VALUES ('e0c86942-aed7-44a4-bda2-a89c3f4bdd37', current_timestamp, 'f5afc042-79b0-47fe-87ee-710c14af888c', 'TEST_VALUE91', 'CONFIRM_EMAIL', 0);

-- Not suspended User 2 - Staff
INSERT INTO public.account (id, login, password, suspended, active, two_factor_auth, language, phone_number, creation_timestamp, version, blocked) VALUES ('d20f860d-555a-479e-8783-67aee5b66692', 'adamn', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', true, true, false, 'PL', '200000000', current_timestamp, 0, false);

INSERT INTO public.personal_data (id, name, lastname, email) VALUES ('d20f860d-555a-479e-8783-67aee5b66692', 'Adam', 'Niezgodka', 'adamn@example.com');

INSERT INTO public.user_level (id, creation_timestamp, level, account_id, version) VALUES ('c09d20a0-69e9-4a50-af00-3973b5d1d85c', current_timestamp, 'STAFF', 'd20f860d-555a-479e-8783-67aee5b66692', 0);
INSERT INTO public.staff_data (id) VALUES ('c09d20a0-69e9-4a50-af00-3973b5d1d85c');

-- User 2 - Token 1
INSERT INTO public.token (id, creation_timestamp, account_id, token_value, type, version) VALUES ('9847130d-6e60-4b3d-a9c3-b913f50769da', current_timestamp, 'd20f860d-555a-479e-8783-67aee5b66692', 'TEST_VALUE93', 'CONFIRM_EMAIL', 0);


-- Multi reservations
-- 1
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version) VALUES ('78d0c4cc-8e7e-4b07-9e8f-eea3fa991fb1', current_timestamp, TIMESTAMP '2024-12-12 03:00:00', NULL, '69507c7f-4c03-4087-85e6-3ae3b6fc2201', '3e6a85db-d751-4549-bbb7-9705f0b2fa6b', 0);
    -- Parking event - ENTRY
INSERT INTO public.parking_event (id, reservation_id, date, type, version) VALUES ('a75547a9-6ea9-4454-aba5-0db6a2d160f4', '78d0c4cc-8e7e-4b07-9e8f-eea3fa991fb1', TIMESTAMP '2024-12-12 03:00:00', 'ENTRY', 0);


-- 2
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version) VALUES ('075a099b-3447-43ae-b3db-5c66bbcc23df', current_timestamp, TIMESTAMP '2024-12-12 07:00:00', TIMESTAMP '2024-12-12 09:00:00', '69507c7f-4c03-4087-85e6-3ae3b6fc2201', '3e6a85db-d751-4549-bbb7-9705f0b2fa6b', 0);
    -- Parking event - ENTRY
INSERT INTO public.parking_event (id, reservation_id, date, type, version) VALUES ('47cbef03-0d2d-4ebc-b14d-47c9b13cc0ee', '075a099b-3447-43ae-b3db-5c66bbcc23df', TIMESTAMP '2024-12-12 07:00:00', 'ENTRY', 0);


-- 3
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version) VALUES ('799926b4-0a9a-4bec-b475-a6908ca95558', current_timestamp, TIMESTAMP '2024-12-12 07:00:00', TIMESTAMP '2024-12-12 10:00:00', '69507c7f-4c03-4087-85e6-3ae3b6fc2201', '3e6a85db-d751-4549-bbb7-9705f0b2fa6b', 0);
    -- Parking event - ENTRY
INSERT INTO public.parking_event (id, reservation_id, date, type, version) VALUES ('5c476b72-e5ba-47a4-8026-4599a4006333', '799926b4-0a9a-4bec-b475-a6908ca95558', TIMESTAMP '2024-12-12 07:00:00', 'ENTRY', 0);
    -- Parking event - EXIT
INSERT INTO public.parking_event (id, reservation_id, date, type, version) VALUES ('2e5b65a2-8e87-4023-b621-9035a0cb6ed4', '799926b4-0a9a-4bec-b475-a6908ca95558', TIMESTAMP '2024-12-12 08:00:00', 'EXIT', 0);


-- 4
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version) VALUES ('98b4fa59-b03f-40f7-aec1-0395bcac0908', current_timestamp, TIMESTAMP '2024-12-12 10:00:00', TIMESTAMP '2024-12-12 11:00:00', '69507c7f-4c03-4087-85e6-3ae3b6fc2201', '3e6a85db-d751-4549-bbb7-9705f0b2fa6b', 0);

-- 5
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version) VALUES ('b793547e-a164-4bc8-b4ff-1c59a736a6f4', current_timestamp, TIMESTAMP '2024-12-11 14:00:00', TIMESTAMP '2024-12-11 20:00:00', '69507c7f-4c03-4087-85e6-3ae3b6fc2201', '3e6a85db-d751-4549-bbb7-9705f0b2fa6b', 0);
    -- Parking event - ENTRY
INSERT INTO public.parking_event (id, reservation_id, date, type, version) VALUES ('15c018aa-ab03-4ad9-9d1f-fa38e5f23126', 'b793547e-a164-4bc8-b4ff-1c59a736a6f4', TIMESTAMP '2024-12-11 14:00:00', 'ENTRY', 0);

-- 6
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version) VALUES ('5345ca26-6104-44f6-807d-2aab9bb80c5c', current_timestamp, TIMESTAMP '2024-12-12 06:00:00', TIMESTAMP '2024-12-12 13:00:00', '69507c7f-4c03-4087-85e6-3ae3b6fc2201', '3e6a85db-d751-4549-bbb7-9705f0b2fa6b', 0);
    -- Parking event - ENTRY
INSERT INTO public.parking_event (id, reservation_id, date, type, version) VALUES ('91cc92ce-d39b-42ce-ac7d-a81ff2178f92', '5345ca26-6104-44f6-807d-2aab9bb80c5c', TIMESTAMP '2024-12-12 06:00:00', 'ENTRY', 0);

-- 7
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version) VALUES ('ba305c2c-8c9c-4b23-9365-50ba0ee77900', current_timestamp, TIMESTAMP '2024-12-12 13:00:00', TIMESTAMP '2024-12-12 17:00:00', '69507c7f-4c03-4087-85e6-3ae3b6fc2201', '3e6a85db-d751-4549-bbb7-9705f0b2fa6b', 0);

-- 8
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version) VALUES ('c958b722-1c50-488f-867d-cef544855689', current_timestamp, TIMESTAMP '2024-12-12 16:00:00', TIMESTAMP '2024-12-12 20:00:00', '69507c7f-4c03-4087-85e6-3ae3b6fc2201', '3e6a85db-d751-4549-bbb7-9705f0b2fa6b', 0);

-- Not taken into account
-- 9
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version) VALUES ('ecbb7e30-a155-47eb-b6e8-b08cf3b4fd11', current_timestamp, TIMESTAMP '2024-12-10 12:00:00', TIMESTAMP '2024-12-10 14:00:00', '69507c7f-4c03-4087-85e6-3ae3b6fc2201', '3e6a85db-d751-4549-bbb7-9705f0b2fa6b', 0);

-- 10
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version) VALUES ('548e6ad8-bd84-456a-aee5-ed4f82af80ec', current_timestamp, TIMESTAMP '2024-12-12 04:00:00', TIMESTAMP '2024-12-12 06:00:00', '69507c7f-4c03-4087-85e6-3ae3b6fc2201', '3e6a85db-d751-4549-bbb7-9705f0b2fa6b', 0);
    -- Parking event - ENTRY
INSERT INTO public.parking_event (id, reservation_id, date, type, version) VALUES ('3681a70e-ad56-47f5-93b5-ffa7cffb5944', '548e6ad8-bd84-456a-aee5-ed4f82af80ec', TIMESTAMP '2024-12-12 04:00:00', 'ENTRY', 0);
    -- Parking event - EXIT
INSERT INTO public.parking_event (id, reservation_id, date, type, version) VALUES ('c552ef81-832c-42fd-983a-e6d285fdfcef', '548e6ad8-bd84-456a-aee5-ed4f82af80ec', TIMESTAMP '2024-12-12 05:00:00', 'EXIT', 0);

-- 11
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version) VALUES ('7f4dc283-9140-4b6b-bec8-92b99b472856', current_timestamp, TIMESTAMP '2024-12-15 12:00:00', TIMESTAMP '2024-12-15 14:00:00', '69507c7f-4c03-4087-85e6-3ae3b6fc2201', '3e6a85db-d751-4549-bbb7-9705f0b2fa6b', 0);
