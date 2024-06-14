\c ssbd03 ssbd03admin
--
-- Name: account; Type: TABLE; Schema: public; Owner: ssbd03admin
--

CREATE TABLE public.account (
                                active boolean NOT NULL,
                                blocked boolean NOT NULL,
                                suspended boolean NOT NULL,
                                two_factor_auth boolean NOT NULL,
                                unsuccessful_login_counter integer,
                                activation_timestamp timestamp(6) without time zone,
                                blocked_timestamp timestamp(6) without time zone,
                                creation_timestamp timestamp(6) without time zone NOT NULL,
                                last_successful_login_time timestamp(6) without time zone,
                                last_unsuccessful_login_time timestamp(6) without time zone,
                                update_timestamp timestamp(6) without time zone,
                                version bigint NOT NULL,
                                id uuid NOT NULL,
                                language character varying(16) NOT NULL,
                                last_successful_login_ip character varying(17),
                                last_unsuccessful_login_ip character varying(17),
                                login character varying(32) NOT NULL,
                                phone_number character varying(32) NOT NULL,
                                password character varying(60) NOT NULL,
                                created_by character varying(255),
                                updated_by character varying(255)
);


ALTER TABLE public.account OWNER TO ssbd03admin;

--
-- Name: account_attributes; Type: TABLE; Schema: public; Owner: ssbd03admin
--

CREATE TABLE public.account_attributes (
                                           account_id uuid NOT NULL,
                                           attribute_name_id uuid NOT NULL
);


ALTER TABLE public.account_attributes OWNER TO ssbd03admin;

--
-- Name: account_history; Type: TABLE; Schema: public; Owner: ssbd03admin
--

CREATE TABLE public.account_history (
                                        active boolean NOT NULL,
                                        blocked boolean NOT NULL,
                                        language character varying(2) NOT NULL,
                                        suspended boolean NOT NULL,
                                        two_factor_auth boolean NOT NULL,
                                        unsuccessful_login_counter integer NOT NULL,
                                        blocked_time timestamp(6) without time zone,
                                        last_successful_login_time timestamp(6) without time zone,
                                        last_unsuccessful_login_time timestamp(6) without time zone,
                                        modification_time timestamp(6) without time zone NOT NULL,
                                        version bigint NOT NULL,
                                        id uuid NOT NULL,
                                        modified_by uuid,
                                        phone_number character varying(16) NOT NULL,
                                        last_successful_login_ip character varying(17),
                                        last_unsuccessful_login_ip character varying(17),
                                        login character varying(32) NOT NULL,
                                        password character varying(60) NOT NULL,
                                        email character varying(64) NOT NULL,
                                        first_name character varying(64) NOT NULL,
                                        last_name character varying(64) NOT NULL,
                                        operation_type character varying(64) NOT NULL,
                                        CONSTRAINT account_history_operation_type_check CHECK (((operation_type)::text = ANY ((ARRAY['REGISTRATION'::character varying, 'LOGIN'::character varying, 'ACTIVATION'::character varying, 'BLOCK'::character varying, 'UNBLOCK'::character varying, 'PASSWORD_CHANGE'::character varying, 'EMAIL_CHANGE'::character varying, 'SUSPEND'::character varying, 'RESTORE_ACCESS'::character varying, 'PERSONAL_DATA_MODIFICATION'::character varying])::text[])))
);


ALTER TABLE public.account_history OWNER TO ssbd03admin;

--
-- Name: admin_data; Type: TABLE; Schema: public; Owner: ssbd03admin
--

CREATE TABLE public.admin_data (
                                   id uuid NOT NULL
);


ALTER TABLE public.admin_data OWNER TO ssbd03admin;

--
-- Name: attribute_association; Type: TABLE; Schema: public; Owner: ssbd03admin
--

CREATE TABLE public.attribute_association (
                                              version bigint NOT NULL,
                                              attribute_name_id uuid NOT NULL,
                                              attribute_value_id uuid NOT NULL,
                                              id uuid NOT NULL
);


ALTER TABLE public.attribute_association OWNER TO ssbd03admin;

--
-- Name: attribute_name; Type: TABLE; Schema: public; Owner: ssbd03admin
--

CREATE TABLE public.attribute_name (
                                       version bigint NOT NULL,
                                       id uuid NOT NULL,
                                       attribute_name character varying(255) NOT NULL
);


ALTER TABLE public.attribute_name OWNER TO ssbd03admin;

--
-- Name: attribute_value; Type: TABLE; Schema: public; Owner: ssbd03admin
--

CREATE TABLE public.attribute_value (
                                        version bigint NOT NULL,
                                        attribute_name_id uuid NOT NULL,
                                        id uuid NOT NULL,
                                        attribute_value character varying(255)
);


ALTER TABLE public.attribute_value OWNER TO ssbd03admin;

--
-- Name: client_data; Type: TABLE; Schema: public; Owner: ssbd03admin
--

CREATE TABLE public.client_data (
                                    total_reservation_hours bigint NOT NULL,
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
                                creation_timestamp timestamp(6) without time zone NOT NULL,
                                update_timestamp timestamp(6) without time zone,
                                version bigint NOT NULL,
                                id uuid NOT NULL,
                                city character varying(50) NOT NULL,
                                street character varying(50) NOT NULL,
                                created_by character varying(255),
                                sector_strategy character varying(255) NOT NULL,
                                updated_by character varying(255),
                                CONSTRAINT parking_sector_strategy_check CHECK (((sector_strategy)::text = ANY ((ARRAY['LEAST_OCCUPIED'::character varying, 'MOST_OCCUPIED'::character varying, 'LEAST_OCCUPIED_WEIGHTED'::character varying])::text[])))
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
                                      created_by character varying(255),
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
                                    creation_timestamp timestamp(6) without time zone NOT NULL,
                                    end_time timestamp(6) without time zone,
                                    update_timestamp timestamp(6) without time zone,
                                    version bigint NOT NULL,
                                    client_id uuid,
                                    id uuid NOT NULL,
                                    sector_id uuid NOT NULL,
                                    created_by character varying(255),
                                    status character varying(255) NOT NULL,
                                    updated_by character varying(255),
                                    CONSTRAINT reservation_status_check CHECK (((status)::text = ANY ((ARRAY['AWAITING'::character varying, 'IN_PROGRESS'::character varying, 'COMPLETED_MANUALLY'::character varying, 'COMPLETED_AUTOMATICALLY'::character varying, 'CANCELLED'::character varying, 'TERMINATED'::character varying])::text[])))
);


ALTER TABLE public.reservation OWNER TO ssbd03admin;

--
-- Name: sector; Type: TABLE; Schema: public; Owner: ssbd03admin
--

CREATE TABLE public.sector (
                               max_places integer NOT NULL,
                               occupied_places integer NOT NULL,
                               weight integer NOT NULL,
                               name character varying(5) NOT NULL,
                               creation_timestamp timestamp(6) without time zone NOT NULL,
                               deactivation_time timestamp(6) without time zone,
                               update_timestamp timestamp(6) without time zone,
                               version bigint NOT NULL,
                               id uuid NOT NULL,
                               parking_id uuid NOT NULL,
                               created_by character varying(255),
                               type character varying(255) NOT NULL,
                               updated_by character varying(255),
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
                              creation_timestamp timestamp(6) without time zone NOT NULL,
                              version bigint NOT NULL,
                              account_id uuid NOT NULL,
                              id uuid NOT NULL,
                              token_value character varying(512) NOT NULL,
                              created_by character varying(255),
                              type character varying(255) NOT NULL,
                              CONSTRAINT token_type_check CHECK (((type)::text = ANY ((ARRAY['REFRESH_TOKEN'::character varying, 'MULTI_FACTOR_AUTHENTICATION_CODE'::character varying, 'REGISTER'::character varying, 'RESET_PASSWORD'::character varying, 'CONFIRM_EMAIL'::character varying, 'CHANGE_OVERWRITTEN_PASSWORD'::character varying, 'RESTORE_ACCESS_TOKEN'::character varying])::text[])))
);


ALTER TABLE public.token OWNER TO ssbd03admin;

--
-- Name: user_level; Type: TABLE; Schema: public; Owner: ssbd03admin
--

CREATE TABLE public.user_level (
                                   creation_timestamp timestamp(6) without time zone NOT NULL,
                                   update_timestamp timestamp(6) without time zone,
                                   version bigint NOT NULL,
                                   account_id uuid NOT NULL,
                                   id uuid NOT NULL,
                                   level character varying(31) NOT NULL,
                                   created_by character varying(255)
);


ALTER TABLE public.user_level OWNER TO ssbd03admin;

--
-- Name: account_attributes account_attributes_pkey; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.account_attributes
    ADD CONSTRAINT account_attributes_pkey PRIMARY KEY (account_id, attribute_name_id);


--
-- Name: account_history account_history_pkey; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.account_history
    ADD CONSTRAINT account_history_pkey PRIMARY KEY (id, version);


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
-- Name: attribute_association attribute_association_pkey; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.attribute_association
    ADD CONSTRAINT attribute_association_pkey PRIMARY KEY (id);


--
-- Name: attribute_name attribute_name_attribute_name_key; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.attribute_name
    ADD CONSTRAINT attribute_name_attribute_name_key UNIQUE (attribute_name);


--
-- Name: attribute_name attribute_name_pkey; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.attribute_name
    ADD CONSTRAINT attribute_name_pkey PRIMARY KEY (id);


--
-- Name: attribute_value attribute_value_attribute_value_attribute_name_id_key; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.attribute_value
    ADD CONSTRAINT attribute_value_attribute_value_attribute_name_id_key UNIQUE (attribute_value, attribute_name_id);


--
-- Name: attribute_value attribute_value_pkey; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.attribute_value
    ADD CONSTRAINT attribute_value_pkey PRIMARY KEY (id);


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
-- Name: past_password past_password_account_id_past_password_key; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.past_password
    ADD CONSTRAINT past_password_account_id_past_password_key UNIQUE (account_id, past_password);


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
-- Name: sector sector_name_parking_id_key; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.sector
    ADD CONSTRAINT sector_name_parking_id_key UNIQUE (name, parking_id);


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
-- Name: token token_token_value_key; Type: CONSTRAINT; Schema: public; Owner: ssbd03admin
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
-- Name: idx_account_attribute_account_id; Type: INDEX; Schema: public; Owner: ssbd03admin
--

CREATE INDEX idx_account_attribute_account_id ON public.account_attributes USING btree (account_id);


--
-- Name: idx_account_attribute_attribute_name_id; Type: INDEX; Schema: public; Owner: ssbd03admin
--

CREATE INDEX idx_account_attribute_attribute_name_id ON public.account_attributes USING btree (attribute_name_id);


--
-- Name: idx_account_hist_account_id; Type: INDEX; Schema: public; Owner: ssbd03admin
--

CREATE INDEX idx_account_hist_account_id ON public.account_history USING btree (modified_by);


--
-- Name: idx_account_id; Type: INDEX; Schema: public; Owner: ssbd03admin
--

CREATE INDEX idx_account_id ON public.past_password USING btree (account_id);


--
-- Name: idx_admin_data_user_level_id; Type: INDEX; Schema: public; Owner: ssbd03admin
--

CREATE INDEX idx_admin_data_user_level_id ON public.admin_data USING btree (id);


--
-- Name: idx_attribute_record_attribute_name_id; Type: INDEX; Schema: public; Owner: ssbd03admin
--

CREATE INDEX idx_attribute_record_attribute_name_id ON public.attribute_association USING btree (attribute_name_id);


--
-- Name: idx_attribute_record_attribute_value_id; Type: INDEX; Schema: public; Owner: ssbd03admin
--

CREATE INDEX idx_attribute_record_attribute_value_id ON public.attribute_association USING btree (attribute_value_id);


--
-- Name: idx_attribute_value_attribute_name_id; Type: INDEX; Schema: public; Owner: ssbd03admin
--

CREATE INDEX idx_attribute_value_attribute_name_id ON public.attribute_value USING btree (attribute_name_id);


--
-- Name: idx_client_data_user_level_id; Type: INDEX; Schema: public; Owner: ssbd03admin
--

CREATE INDEX idx_client_data_user_level_id ON public.client_data USING btree (id);


--
-- Name: idx_parking_event_reservation_id; Type: INDEX; Schema: public; Owner: ssbd03admin
--

CREATE INDEX idx_parking_event_reservation_id ON public.parking_event USING btree (reservation_id);


--
-- Name: idx_reservation_client_id; Type: INDEX; Schema: public; Owner: ssbd03admin
--

CREATE INDEX idx_reservation_client_id ON public.reservation USING btree (client_id);


--
-- Name: idx_reservation_sector_id; Type: INDEX; Schema: public; Owner: ssbd03admin
--

CREATE INDEX idx_reservation_sector_id ON public.reservation USING btree (sector_id);


--
-- Name: idx_sector_parking_id; Type: INDEX; Schema: public; Owner: ssbd03admin
--

CREATE INDEX idx_sector_parking_id ON public.sector USING btree (parking_id);


--
-- Name: idx_staff_data_user_level_id; Type: INDEX; Schema: public; Owner: ssbd03admin
--

CREATE INDEX idx_staff_data_user_level_id ON public.staff_data USING btree (id);


--
-- Name: idx_token_account_id; Type: INDEX; Schema: public; Owner: ssbd03admin
--

CREATE INDEX idx_token_account_id ON public.token USING btree (account_id);


--
-- Name: idx_user_level_account_id; Type: INDEX; Schema: public; Owner: ssbd03admin
--

CREATE INDEX idx_user_level_account_id ON public.user_level USING btree (account_id);


--
-- Name: account_attributes account_attribute_account_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.account_attributes
    ADD CONSTRAINT account_attribute_account_id_fk FOREIGN KEY (attribute_name_id) REFERENCES public.attribute_association(id);


--
-- Name: account_attributes account_attribute_attribute_name_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.account_attributes
    ADD CONSTRAINT account_attribute_attribute_name_id_fk FOREIGN KEY (account_id) REFERENCES public.account(id);


--
-- Name: account_history account_hist_account_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.account_history
    ADD CONSTRAINT account_hist_account_id_fk FOREIGN KEY (modified_by) REFERENCES public.account(id);


--
-- Name: admin_data admin_data_user_level_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.admin_data
    ADD CONSTRAINT admin_data_user_level_id_fk FOREIGN KEY (id) REFERENCES public.user_level(id);


--
-- Name: attribute_association attribute_record_attribute_name_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.attribute_association
    ADD CONSTRAINT attribute_record_attribute_name_id_fk FOREIGN KEY (attribute_name_id) REFERENCES public.attribute_name(id);


--
-- Name: attribute_association attribute_record_attribute_value_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.attribute_association
    ADD CONSTRAINT attribute_record_attribute_value_id_fk FOREIGN KEY (attribute_value_id) REFERENCES public.attribute_value(id);


--
-- Name: attribute_value attribute_value_attribute_name_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.attribute_value
    ADD CONSTRAINT attribute_value_attribute_name_id_fk FOREIGN KEY (attribute_name_id) REFERENCES public.attribute_name(id);


--
-- Name: client_data client_data_user_level_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.client_data
    ADD CONSTRAINT client_data_user_level_id_fk FOREIGN KEY (id) REFERENCES public.user_level(id);


--
-- Name: parking_event parking_event_reservation_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.parking_event
    ADD CONSTRAINT parking_event_reservation_id_fk FOREIGN KEY (reservation_id) REFERENCES public.reservation(id);


--
-- Name: past_password past_password_account_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.past_password
    ADD CONSTRAINT past_password_account_id_fk FOREIGN KEY (account_id) REFERENCES public.account(id);


--
-- Name: personal_data personal_data_account_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.personal_data
    ADD CONSTRAINT personal_data_account_id_fk FOREIGN KEY (id) REFERENCES public.account(id);


--
-- Name: reservation reservation_client_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.reservation
    ADD CONSTRAINT reservation_client_id_fk FOREIGN KEY (client_id) REFERENCES public.client_data(id);


--
-- Name: reservation reservation_sector_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.reservation
    ADD CONSTRAINT reservation_sector_id_fk FOREIGN KEY (sector_id) REFERENCES public.sector(id);


--
-- Name: sector sector_parking_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.sector
    ADD CONSTRAINT sector_parking_id_fk FOREIGN KEY (parking_id) REFERENCES public.parking(id);


--
-- Name: staff_data staff_data_user_level_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.staff_data
    ADD CONSTRAINT staff_data_user_level_id_fk FOREIGN KEY (id) REFERENCES public.user_level(id);


--
-- Name: token token_account_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.token
    ADD CONSTRAINT token_account_id_fk FOREIGN KEY (account_id) REFERENCES public.account(id);


--
-- Name: user_level user_level_account_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: ssbd03admin
--

ALTER TABLE ONLY public.user_level
    ADD CONSTRAINT user_level_account_id_fk FOREIGN KEY (account_id) REFERENCES public.account(id);


--
-- PostgreSQL database dump complete
--

