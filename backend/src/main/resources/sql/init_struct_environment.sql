CREATE TABLE public.account (
    active boolean NOT NULL,
    blocked boolean NOT NULL,
    two_factor_auth boolean NOT NULL,
    unsuccessful_login_counter integer,
    verified boolean NOT NULL,
    blocked_timestamp timestamp(6) without time zone,
    creation_date timestamp(6) without time zone NOT NULL,
    last_successful_login_time timestamp(6) without time zone,
    last_unsuccessful_login_time timestamp(6) without time zone,
    version bigint NOT NULL,
    id uuid NOT NULL,
    language character varying(16) NOT NULL,
    last_successful_login_ip character varying(17),
    last_unsuccessful_login_ip character varying(17),
    login character varying(32) NOT NULL,
    phone_number character varying(32) NOT NULL,
    password character varying(60) NOT NULL
);


ALTER TABLE public.account OWNER TO ssbd03admin;

--
-- Name: admin_data; Type: TABLE; Schema: public; Owner: ssbd03admin
--

CREATE TABLE public.admin_data (
    id uuid NOT NULL
);


ALTER TABLE public.admin_data OWNER TO ssbd03admin;

--
-- Name: client_data; Type: TABLE; Schema: public; Owner: ssbd03admin
--

CREATE TABLE public.client_data (
    id uuid NOT NULL,
    type character varying(255) NOT NULL,
    CONSTRAINT client_data_type_check CHECK (((type)::text = ANY ((ARRAY['BASIC'::character varying, 'STANDARD'::character varying, 'PREMIUM'::character varying])::text[])))
);


ALTER TABLE public.client_data OWNER TO ssbd03admin;

--
-- Name: parking; Type: TABLE; Schema: public; Owner: ssbd03admin
--

CREATE TABLE public.parking (
    zip_code character varying(6) NOT NULL,
    version bigint NOT NULL,
    id uuid NOT NULL,
    city character varying(255) NOT NULL,
    street character varying(255) NOT NULL
);


ALTER TABLE public.parking OWNER TO ssbd03admin;

--
-- Name: parking_event; Type: TABLE; Schema: public; Owner: ssbd03admin
--

CREATE TABLE public.parking_event (
    date timestamp(6) without time zone NOT NULL,
    version bigint NOT NULL,
    id uuid NOT NULL,
    reservation_id uuid NOT NULL,
    type character varying(255) NOT NULL,
    CONSTRAINT parking_event_type_check CHECK (((type)::text = ANY ((ARRAY['ENTRY'::character varying, 'EXIT'::character varying])::text[])))
);


ALTER TABLE public.parking_event OWNER TO ssbd03admin;

--
-- Name: past_password; Type: TABLE; Schema: public; Owner: ssbd03admin
--

CREATE TABLE public.past_password (
    account_id uuid NOT NULL,
    past_password character varying(255)
);


ALTER TABLE public.past_password OWNER TO ssbd03admin;

--
-- Name: personal_data; Type: TABLE; Schema: public; Owner: ssbd03admin
--

CREATE TABLE public.personal_data (
    id uuid NOT NULL,
    email character varying(32) NOT NULL,
    lastname character varying(32) NOT NULL,
    name character varying(32) NOT NULL
);


ALTER TABLE public.personal_data OWNER TO ssbd03admin;

--
-- Name: reservation; Type: TABLE; Schema: public; Owner: ssbd03admin
--

CREATE TABLE public.reservation (
    begin_time timestamp(6) without time zone,
    end_time timestamp(6) without time zone,
    version bigint NOT NULL,
    client_id uuid,
    id uuid NOT NULL,
    sector_id uuid NOT NULL
);


ALTER TABLE public.reservation OWNER TO ssbd03admin;

--
-- Name: sector; Type: TABLE; Schema: public; Owner: ssbd03admin
--

CREATE TABLE public.sector (
    available_places integer NOT NULL,
    max_places integer NOT NULL,
    weight integer NOT NULL,
    name character varying(5) NOT NULL,
    version bigint NOT NULL,
    id uuid NOT NULL,
    parking_id uuid NOT NULL,
    type character varying(255) NOT NULL,
    CONSTRAINT sector_max_places_check CHECK ((max_places <= 1000)),
    CONSTRAINT sector_type_check CHECK (((type)::text = ANY ((ARRAY['COVERED'::character varying, 'UNCOVERED'::character varying, 'UNDERGROUND'::character varying])::text[]))),
    CONSTRAINT sector_weight_check CHECK (((weight <= 100) AND (weight >= 1)))
);


ALTER TABLE public.sector OWNER TO ssbd03admin;

--
-- Name: staff_data; Type: TABLE; Schema: public; Owner: ssbd03admin
--

CREATE TABLE public.staff_data (
    id uuid NOT NULL
);


ALTER TABLE public.staff_data OWNER TO ssbd03admin;

--
-- Name: token; Type: TABLE; Schema: public; Owner: ssbd03admin
--

CREATE TABLE public.token (
    version bigint NOT NULL,
    account_id uuid,
    id uuid NOT NULL,
    token_value character varying(512) NOT NULL,
    type character varying(255) NOT NULL,
    CONSTRAINT token_type_check CHECK (((type)::text = ANY ((ARRAY['REFRESH_TOKEN'::character varying, 'MULTI_FACTOR_AUTHENTICATION_CODE'::character varying, 'REGISTER'::character varying, 'RESET_PASSWORD'::character varying, 'CONFIRM_EMAIL'::character varying, 'CHANGE_OVERWRITTEN_PASSWORD'::character varying])::text[])))
);


ALTER TABLE public.token OWNER TO ssbd03admin;

--
-- Name: user_level; Type: TABLE; Schema: public; Owner: ssbd03admin
--

CREATE TABLE public.user_level (
    version bigint NOT NULL,
    account_id uuid NOT NULL,
    id uuid NOT NULL,
    level character varying(31) NOT NULL
);


ALTER TABLE public.user_level OWNER TO ssbd03admin;

--
-- Name: account account_login_key; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.account
    ADD CONSTRAINT account_login_key UNIQUE (login);


--
-- Name: account account_pkey; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.account
    ADD CONSTRAINT account_pkey PRIMARY KEY (id);


--
-- Name: admin_data admin_data_pkey; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.admin_data
    ADD CONSTRAINT admin_data_pkey PRIMARY KEY (id);


--
-- Name: client_data client_data_pkey; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.client_data
    ADD CONSTRAINT client_data_pkey PRIMARY KEY (id);


--
-- Name: parking parking_city_zip_code_street_key; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.parking
    ADD CONSTRAINT parking_city_zip_code_street_key UNIQUE (city, zip_code, street);


--
-- Name: parking_event parking_event_pkey; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.parking_event
    ADD CONSTRAINT parking_event_pkey PRIMARY KEY (id);


--
-- Name: parking parking_pkey; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.parking
    ADD CONSTRAINT parking_pkey PRIMARY KEY (id);


--
-- Name: personal_data personal_data_email_key; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.personal_data
    ADD CONSTRAINT personal_data_email_key UNIQUE (email);


--
-- Name: personal_data personal_data_pkey; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.personal_data
    ADD CONSTRAINT personal_data_pkey PRIMARY KEY (id);


--
-- Name: reservation reservation_pkey; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.reservation
    ADD CONSTRAINT reservation_pkey PRIMARY KEY (id);


--
-- Name: sector sector_name_key; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.sector
    ADD CONSTRAINT sector_name_key UNIQUE (name);


--
-- Name: sector sector_pkey; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.sector
    ADD CONSTRAINT sector_pkey PRIMARY KEY (id);


--
-- Name: staff_data staff_data_pkey; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.staff_data
    ADD CONSTRAINT staff_data_pkey PRIMARY KEY (id);


--
-- Name: token token_pkey; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.token
    ADD CONSTRAINT token_pkey PRIMARY KEY (id);


--
-- Name: token token_tokenValue_key; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.token
    ADD CONSTRAINT token_token_value_key UNIQUE (token_value);


--
-- Name: user_level user_level_account_id_level_key; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.user_level
    ADD CONSTRAINT user_level_account_id_level_key UNIQUE (account_id, level);


--
-- Name: user_level user_level_pkey; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.user_level
    ADD CONSTRAINT user_level_pkey PRIMARY KEY (id);


--
-- Name: reservation reservation_sector_id_reference; Type: FK CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.reservation
    ADD CONSTRAINT reservation_sector_id_reference FOREIGN KEY (sector_id) REFERENCES public.sector(id);


--
-- Name: personal_data personal_data_account_id_reference; Type: FK CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.personal_data
    ADD CONSTRAINT personal_data_account_id_reference FOREIGN KEY (id) REFERENCES public.account(id);


--
-- Name: client_data client_data_user_level_id_reference; Type: FK CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.client_data
    ADD CONSTRAINT client_data_user_level_id_reference FOREIGN KEY (id) REFERENCES public.user_level(id);


--
-- Name: reservation reservation_client_data_id_reference; Type: FK CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.reservation
    ADD CONSTRAINT reservation_client_data_id_reference FOREIGN KEY (client_id) REFERENCES public.client_data(id);


--
-- Name: token token_account_id_reference; Type: FK CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.token
    ADD CONSTRAINT token_account_id_reference FOREIGN KEY (account_id) REFERENCES public.account(id);


--
-- Name: user_level user_level_account_id_reference; Type: FK CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.user_level
    ADD CONSTRAINT user_level_account_id_reference FOREIGN KEY (account_id) REFERENCES public.account(id);


--
-- Name: admin_data admin_data_user_level_id_reference; Type: FK CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.admin_data
    ADD CONSTRAINT admin_data_user_level_id_reference FOREIGN KEY (id) REFERENCES public.user_level(id);


--
-- Name: staff_data staff_data_user_level_id_reference; Type: FK CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.staff_data
    ADD CONSTRAINT staff_data_user_level_id_reference FOREIGN KEY (id) REFERENCES public.user_level(id);


--
-- Name: parking_event parking_event_reservation_id_reference; Type: FK CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.parking_event
    ADD CONSTRAINT parking_event_reservation_id_reference FOREIGN KEY (reservation_id) REFERENCES public.reservation(id);


--
-- Name: sector sector_parking_id_reference; Type: FK CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.sector
    ADD CONSTRAINT sector_parking_id_reference FOREIGN KEY (parking_id) REFERENCES public.parking(id);


--
-- Name: past_password past_password_account_id_reference; Type: FK CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.past_password
    ADD CONSTRAINT past_password_account_id_reference FOREIGN KEY (account_id) REFERENCES public.account(id);

--
-- Create index on foregin_key: Table parking_event 
--

CREATE INDEX idx_parking_event_reservation_id ON PARKING_EVENT (reservation_id);

--
-- Create index on foregin_key: Table past_password
--

CREATE INDEX idx_past_password_account_id ON PAST_PASSWORD (account_id);

--
-- Create index on foregin_key: Table reservation
--

CREATE INDEX idx_reservation_client_id ON RESERVATION (client_id);

--
-- Create index on foregin_key: Table reservation
--

CREATE INDEX idx_reservation_sector_id ON RESERVATION (sector_id);

--
-- Create index on foregin_key: Table sector
--

CREATE INDEX idx_sector_parking_id ON SECTOR (parking_id);

--
-- Create index on foregin_key: Table token
--

CREATE INDEX idx_token_account_id ON TOKEN (account_id);