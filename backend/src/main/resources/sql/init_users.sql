-- -- GRAND PRIVILEGES
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.testtable      TO ssbd03mok;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.testtable2     TO ssbd03mop;

GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.reservation    TO ssbd03mop;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.parking        TO ssbd03mop;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.sector         TO ssbd03mop;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.parking_event  TO ssbd03mop;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.account        TO ssbd03mop;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.personal_data  TO ssbd03mop;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.user_level     TO ssbd03mop;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.client_data    TO ssbd03mop;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.staff_data     TO ssbd03mop;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.admin_data     TO ssbd03mop;

-- -- INSERT VALUE
-- First Client
INSERT INTO public.account (id, login, password, verified, active, language, phone_number, creation_date, version) VALUES ('0ca02f7e-d8e9-45d3-a332-a56015acb822', 'mkowal', 'P@ssw0rd!', true, true, 'PL', '000000000', current_date, 0);
INSERT INTO public.personal_data (id, name, lastname, email) VALUES ('0ca02f7e-d8e9-45d3-a332-a56015acb822', 'Michal', 'Kowalski', 'mkowal@example.com');
INSERT INTO public.user_level (id, level, account_id, version) VALUES ('69507c7f-4c03-4087-85e6-3ae3b6fc2201', 'client', '0ca02f7e-d8e9-45d3-a332-a56015acb822', 0);
INSERT INTO public.client_data (id, type) VALUES ('69507c7f-4c03-4087-85e6-3ae3b6fc2201', 'BASIC');

-- Second Client
INSERT INTO public.account (id, login, password, verified, active, language, phone_number, creation_date, version) VALUES ('902d6e5b-2449-4898-a4f7-a92b0d8a04e1', 'jkoza', 'P@ssw0rd!', true, true, 'PL', '000000001', current_date, 0);
INSERT INTO public.personal_data (id, name, lastname, email) VALUES ('902d6e5b-2449-4898-a4f7-a92b0d8a04e1', 'Jakub', 'Koza', 'jkoza@example.com');
INSERT INTO public.user_level (id, level, account_id, version) VALUES ('9428fadf-191c-4dd7-8626-01c3e0ff603c', 'client', '902d6e5b-2449-4898-a4f7-a92b0d8a04e1', 0);
INSERT INTO public.client_data (id, type) VALUES ('9428fadf-191c-4dd7-8626-01c3e0ff603c', 'BASIC');

-- Second Client
INSERT INTO public.account (id, login, password, verified, active, language, phone_number, creation_date, version) VALUES ('02b0d9d7-a472-48d0-95e0-13a3e6a11d00', 'pnowak', 'P@ssw0rd!', true, true, 'PL', '000000003', current_date, 0);
INSERT INTO public.personal_data (id, name, lastname, email) VALUES ('02b0d9d7-a472-48d0-95e0-13a3e6a11d00', 'Piotr', 'Nowak', 'pnowak@example.com');
INSERT INTO public.user_level (id, level, account_id, version) VALUES ('cbf34cb0-c96b-4037-80d4-1eef34890e85', 'client', '02b0d9d7-a472-48d0-95e0-13a3e6a11d00', 0);
INSERT INTO public.client_data (id, type) VALUES ('cbf34cb0-c96b-4037-80d4-1eef34890e85', 'BASIC');

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
INSERT INTO public.reservation (id, begin_time, end_time, client_id, sector_id, version) VALUES ('a7709a4d-b7bc-40c4-8fd5-5c7cfcb0f146', TIMESTAMP '2024-03-01 15:00:00', TIMESTAMP '2024-03-01 16:00:00', null, '3e6a85db-d751-4549-bbb7-9705f0b2fa6b', 0);
