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
GRANT SELECT, INSERT                 ON TABLE public.parking_history        TO ssbd03mop;

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
INSERT INTO public.client_data (id, type, total_reservation_hours) VALUES ('69507c7f-4c03-4087-85e6-3ae3b6fc2201', 'BASIC', 0);

-- Second Client
INSERT INTO public.account (id, login, password, suspended, active, two_factor_auth, language, phone_number, creation_timestamp, version, blocked) VALUES ('902d6e5b-2449-4898-a4f7-a92b0d8a04e1', 'jakubkoza', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', true, true, false, 'PL', '000000001', current_timestamp, 0, false);
INSERT INTO public.past_password (account_id, past_password) VALUES ('902d6e5b-2449-4898-a4f7-a92b0d8a04e1', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa');
INSERT INTO public.personal_data (id, name, lastname, email) VALUES ('902d6e5b-2449-4898-a4f7-a92b0d8a04e1', 'Jakub', 'Koza', 'jakubkoza@adresik.net');

INSERT INTO public.user_level (id, creation_timestamp, level, account_id, version) VALUES ('9428fadf-191c-4dd7-8626-01c3e0ff603c', current_timestamp, 'CLIENT', '902d6e5b-2449-4898-a4f7-a92b0d8a04e1', 0);
INSERT INTO public.client_data (id, type, total_reservation_hours) VALUES ('9428fadf-191c-4dd7-8626-01c3e0ff603c', 'BASIC', 0);

-- Third Client
INSERT INTO public.account (id, login, password, suspended, active, two_factor_auth, language, phone_number, creation_timestamp, version, blocked) VALUES ('02b0d9d7-a472-48d0-95e0-13a3e6a11d00', 'piotrnowak', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa', true, true, false, 'PL', '000000003', current_timestamp, 0, false);
INSERT INTO public.past_password (account_id, past_password) VALUES ('02b0d9d7-a472-48d0-95e0-13a3e6a11d00', '$2a$12$A1wGVanmSuv.GRqlKI4OuuvtV.AgP8pfb3I3fOyNuvgOHpuCiGzHa');
INSERT INTO public.personal_data (id, name, lastname, email) VALUES ('02b0d9d7-a472-48d0-95e0-13a3e6a11d00', 'Piotr', 'Nowak', 'piotrnowak@adresik.net');

INSERT INTO public.user_level (id, creation_timestamp, level, account_id, version) VALUES ('cbf34cb0-c96b-4037-80d4-1eef34890e85', current_timestamp, 'CLIENT', '02b0d9d7-a472-48d0-95e0-13a3e6a11d00', 0);
INSERT INTO public.client_data (id, type, total_reservation_hours) VALUES ('cbf34cb0-c96b-4037-80d4-1eef34890e85', 'BASIC', 0);

-- Parking
INSERT INTO public.parking (id, creation_timestamp, sector_strategy, zip_code, city, street, version) VALUES ('96a36faa-f2a2-41b8-9c3c-b6bef04ce6d1', current_timestamp, 'LEAST_OCCUPIED','00-000', 'BoatCity', 'Palki', 0);

-- First Sector S1
INSERT INTO public.sector (id, creation_timestamp, occupied_places, max_places, weight, name, parking_id, type, version) VALUES ('3e6a85db-d751-4549-bbb7-9705f0b2fa6b', current_timestamp, 30, 50, 1, 'S1', '96a36faa-f2a2-41b8-9c3c-b6bef04ce6d1', 'UNCOVERED', 0);

-- Second Sector S2
INSERT INTO public.sector (id, creation_timestamp, occupied_places, max_places, weight, name, parking_id, type, version) VALUES ('4ce920a0-6f4d-4e95-ba24-99ba32b66491', current_timestamp, 30, 60, 2, 'S2', '96a36faa-f2a2-41b8-9c3c-b6bef04ce6d1', 'UNCOVERED', 0);

-- Third Sector S3
INSERT INTO public.sector (id, creation_timestamp, occupied_places, max_places, weight, name, parking_id, type, version) VALUES ('c51557aa-284d-44a6-b38d-b6ceb9c23725', current_timestamp, 30, 70, 3, 'S3', '96a36faa-f2a2-41b8-9c3c-b6bef04ce6d1', 'UNCOVERED', 0);

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
INSERT INTO public.client_data (id, type, total_reservation_hours) VALUES ('4d1c0c8a-3af5-4ce9-ba95-09d25d289a76', 'BASIC', 0);

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
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('78d0c4cc-8e7e-4b07-9e8f-eea3fa991fb1', current_timestamp, TIMESTAMP '2024-12-12 03:00:00', NULL, '69507c7f-4c03-4087-85e6-3ae3b6fc2201', '3e6a85db-d751-4549-bbb7-9705f0b2fa6b', 0, 'IN_PROGRESS');
-- Parking event - ENTRY
INSERT INTO public.parking_event (id, reservation_id, date, type, version) VALUES ('a75547a9-6ea9-4454-aba5-0db6a2d160f4', '78d0c4cc-8e7e-4b07-9e8f-eea3fa991fb1', TIMESTAMP '2024-12-12 03:00:00', 'ENTRY', 0);


-- 2
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('075a099b-3447-43ae-b3db-5c66bbcc23df', current_timestamp, TIMESTAMP '2024-12-12 07:00:00', TIMESTAMP '2024-12-12 09:00:00', '69507c7f-4c03-4087-85e6-3ae3b6fc2201', '3e6a85db-d751-4549-bbb7-9705f0b2fa6b', 0, 'IN_PROGRESS');
-- Parking event - ENTRY
INSERT INTO public.parking_event (id, reservation_id, date, type, version) VALUES ('47cbef03-0d2d-4ebc-b14d-47c9b13cc0ee', '075a099b-3447-43ae-b3db-5c66bbcc23df', TIMESTAMP '2024-12-12 07:00:00', 'ENTRY', 0);


-- 3
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('799926b4-0a9a-4bec-b475-a6908ca95558', current_timestamp, TIMESTAMP '2024-12-12 07:00:00', TIMESTAMP '2024-12-12 10:00:00', '69507c7f-4c03-4087-85e6-3ae3b6fc2201', '3e6a85db-d751-4549-bbb7-9705f0b2fa6b', 0, 'IN_PROGRESS');
-- Parking event - ENTRY
INSERT INTO public.parking_event (id, reservation_id, date, type, version) VALUES ('5c476b72-e5ba-47a4-8026-4599a4006333', '799926b4-0a9a-4bec-b475-a6908ca95558', TIMESTAMP '2024-12-12 07:00:00', 'ENTRY', 0);
-- Parking event - EXIT
INSERT INTO public.parking_event (id, reservation_id, date, type, version) VALUES ('2e5b65a2-8e87-4023-b621-9035a0cb6ed4', '799926b4-0a9a-4bec-b475-a6908ca95558', TIMESTAMP '2024-12-12 08:00:00', 'EXIT', 0);


-- 4
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('98b4fa59-b03f-40f7-aec1-0395bcac0908', current_timestamp, TIMESTAMP '2024-12-12 10:00:00', TIMESTAMP '2024-12-12 11:00:00', '69507c7f-4c03-4087-85e6-3ae3b6fc2201', '3e6a85db-d751-4549-bbb7-9705f0b2fa6b', 0, 'AWAITING');

-- 5
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('b793547e-a164-4bc8-b4ff-1c59a736a6f4', current_timestamp, TIMESTAMP '2024-12-11 14:00:00', TIMESTAMP '2024-12-11 20:00:00', '69507c7f-4c03-4087-85e6-3ae3b6fc2201', '3e6a85db-d751-4549-bbb7-9705f0b2fa6b', 0, 'IN_PROGRESS');
-- Parking event - ENTRY
INSERT INTO public.parking_event (id, reservation_id, date, type, version) VALUES ('15c018aa-ab03-4ad9-9d1f-fa38e5f23126', 'b793547e-a164-4bc8-b4ff-1c59a736a6f4', TIMESTAMP '2024-12-11 14:00:00', 'ENTRY', 0);

-- 6
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('5345ca26-6104-44f6-807d-2aab9bb80c5c', current_timestamp, TIMESTAMP '2024-12-12 06:00:00', TIMESTAMP '2024-12-12 13:00:00', '69507c7f-4c03-4087-85e6-3ae3b6fc2201', '3e6a85db-d751-4549-bbb7-9705f0b2fa6b', 0, 'IN_PROGRESS');
-- Parking event - ENTRY
INSERT INTO public.parking_event (id, reservation_id, date, type, version) VALUES ('91cc92ce-d39b-42ce-ac7d-a81ff2178f92', '5345ca26-6104-44f6-807d-2aab9bb80c5c', TIMESTAMP '2024-12-12 06:00:00', 'ENTRY', 0);

-- 7
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('ba305c2c-8c9c-4b23-9365-50ba0ee77900', current_timestamp, TIMESTAMP '2024-12-12 13:00:00', TIMESTAMP '2024-12-12 17:00:00', '69507c7f-4c03-4087-85e6-3ae3b6fc2201', '3e6a85db-d751-4549-bbb7-9705f0b2fa6b', 0, 'AWAITING');

-- 8
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('c958b722-1c50-488f-867d-cef544855689', current_timestamp, TIMESTAMP '2024-12-12 16:00:00', TIMESTAMP '2024-12-12 20:00:00', '69507c7f-4c03-4087-85e6-3ae3b6fc2201', '3e6a85db-d751-4549-bbb7-9705f0b2fa6b', 0, 'AWAITING');

-- Not taken into account
-- 9
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('ecbb7e30-a155-47eb-b6e8-b08cf3b4fd11', current_timestamp, TIMESTAMP '2024-12-10 12:00:00', TIMESTAMP '2024-12-10 14:00:00', '69507c7f-4c03-4087-85e6-3ae3b6fc2201', '3e6a85db-d751-4549-bbb7-9705f0b2fa6b', 0, 'COMPLETED_MANUALLY');

-- 10
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('548e6ad8-bd84-456a-aee5-ed4f82af80ec', current_timestamp, TIMESTAMP '2024-12-12 04:00:00', TIMESTAMP '2024-12-12 06:00:00', '69507c7f-4c03-4087-85e6-3ae3b6fc2201', '3e6a85db-d751-4549-bbb7-9705f0b2fa6b', 0, 'COMPLETED_MANUALLY');
-- Parking event - ENTRY
INSERT INTO public.parking_event (id, reservation_id, date, type, version) VALUES ('3681a70e-ad56-47f5-93b5-ffa7cffb5944', '548e6ad8-bd84-456a-aee5-ed4f82af80ec', TIMESTAMP '2024-12-12 04:00:00', 'ENTRY', 0);
-- Parking event - EXIT
INSERT INTO public.parking_event (id, reservation_id, date, type, version) VALUES ('c552ef81-832c-42fd-983a-e6d285fdfcef', '548e6ad8-bd84-456a-aee5-ed4f82af80ec', TIMESTAMP '2024-12-12 05:00:00', 'EXIT', 0);

-- 11
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('7f4dc283-9140-4b6b-bec8-92b99b472856', current_timestamp, TIMESTAMP '2024-12-15 12:00:00', TIMESTAMP '2024-12-15 14:00:00', '69507c7f-4c03-4087-85e6-3ae3b6fc2201', '3e6a85db-d751-4549-bbb7-9705f0b2fa6b', 0, 'AWAITING');

-- 12 CANCELED
INSERT INTO public.reservation (id, creation_timestamp, begin_time, end_time, client_id, sector_id, version, status) VALUES ('836a3b41-c11d-466a-8c54-900f87ed1195', current_timestamp, TIMESTAMP '2024-12-12 6:00:00', TIMESTAMP '2024-12-12 15:00:00', '69507c7f-4c03-4087-85e6-3ae3b6fc2201', '3e6a85db-d751-4549-bbb7-9705f0b2fa6b', 0, 'CANCELLED');

-- Enter without reservation check methods tests

INSERT INTO public.parking (id, creation_timestamp, sector_strategy, zip_code, city, street, version) VALUES ('3591ced3-996e-49b4-8c56-40fe91193b1d', current_timestamp, 'LEAST_OCCUPIED', '99-999', 'test1', 'test1', 0);

INSERT INTO public.sector (id, creation_timestamp, occupied_places, max_places, weight, name, parking_id, type, version) VALUES ('14d51050-ffe2-4da2-abd2-4e6d06759ea5', current_timestamp, 0, 5, 3, 'UC-01', '3591ced3-996e-49b4-8c56-40fe91193b1d', 'UNCOVERED', 0);
INSERT INTO public.sector (id, creation_timestamp, occupied_places, max_places, weight, name, parking_id, type, version) VALUES ('933bcce5-a38c-4b09-bd60-2b746d9f40e8', current_timestamp, 1, 2, 3, 'UC-02', '3591ced3-996e-49b4-8c56-40fe91193b1d', 'UNCOVERED', 0);
INSERT INTO public.sector (id, creation_timestamp, occupied_places, max_places, weight, name, parking_id, type, version) VALUES ('828228e6-2fa7-418e-8cfe-7f4d79737557', current_timestamp, 1, 1, 3, 'UC-03', '3591ced3-996e-49b4-8c56-40fe91193b1d', 'UNCOVERED', 0);
INSERT INTO public.sector (id, creation_timestamp, occupied_places, max_places, weight, name, parking_id, type, version) VALUES ('38c70882-c413-467d-bdd8-c5ed5f9128d0', current_timestamp, 1, 1, 3, 'UC-04', '3591ced3-996e-49b4-8c56-40fe91193b1d', 'UNCOVERED', 0);

INSERT INTO public.sector (id, creation_timestamp, occupied_places, max_places, weight, name, parking_id, type, version) VALUES ('f274420a-1322-4530-bfcb-4e515dd5a920', current_timestamp, 0, 2, 3, 'CO-01', '3591ced3-996e-49b4-8c56-40fe91193b1d', 'COVERED', 0);
INSERT INTO public.sector (id, creation_timestamp, occupied_places, max_places, weight, name, parking_id, type, version) VALUES ('ae65eca6-669d-43ec-8c35-39f4eb6b72bb', current_timestamp, 1, 1, 3, 'CO-02', '3591ced3-996e-49b4-8c56-40fe91193b1d', 'COVERED', 0);

INSERT INTO public.sector (id, creation_timestamp, occupied_places, max_places, weight, name, parking_id, type, version) VALUES ('65c51075-0749-4304-984a-9cb926e65aab', current_timestamp, 0, 2, 3, 'UN-01', '3591ced3-996e-49b4-8c56-40fe91193b1d', 'UNDERGROUND', 0);
INSERT INTO public.sector (id, creation_timestamp, occupied_places, max_places, weight, name, parking_id, type, version) VALUES ('b9f5bb0c-d19f-4101-ac57-d758e063ac3e', current_timestamp, 1, 1, 3, 'UN-02', '3591ced3-996e-49b4-8c56-40fe91193b1d', 'UNDERGROUND', 0);

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