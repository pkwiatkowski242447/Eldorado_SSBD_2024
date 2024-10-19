CREATE DATABASE ssbd03;
CREATE USER 'ssbd03admin'@'localhost' IDENTIFIED BY 'admin';
CREATE USER 'ssbd03auth'@'localhost' IDENTIFIED BY 'auth';
CREATE USER 'ssbd03mok'@'localhost' IDENTIFIED BY 'mok';
CREATE USER 'ssbd03mop'@'localhost' IDENTIFIED BY 'mop';

GRANT ALL PRIVILEGES ON ssbd03.* TO 'ssbd03admin'@'localhost' WITH GRANT OPTION;

--
-- Name: account; Type: TABLE; Schema: public
--

USE ssbd03;

CREATE TABLE account (
    active boolean NOT NULL,
    blocked boolean NOT NULL,
    suspended boolean NOT NULL,
    two_factor_auth boolean NOT NULL,
    unsuccessful_login_counter integer,
    activation_timestamp timestamp,
    blocked_timestamp timestamp,
    creation_timestamp timestamp NOT NULL,
    last_successful_login_time timestamp,
    last_unsuccessful_login_time timestamp,
    update_timestamp timestamp,
    version bigint NOT NULL,
    id binary(16) NOT NULL,
    language varchar(16) NOT NULL,
    last_successful_login_ip varchar(17),
    last_unsuccessful_login_ip varchar(17),
    login varchar(32) NOT NULL,
    phone_number varchar(32) NOT NULL,
    password varchar(60) NOT NULL,
    created_by varchar(255),
    updated_by varchar(255)
);

--
-- Name: account_attributes; Type: TABLE; Schema: public
--

CREATE TABLE account_attributes (
    account_id binary(16) NOT NULL,
    attribute_name_id binary(16) NOT NULL
);

--
-- Name: account_history; Type: TABLE; Schema: public
--

CREATE TABLE account_history (
    active boolean NOT NULL,
    blocked boolean NOT NULL,
    language character varying(2) NOT NULL,
    suspended boolean NOT NULL,
    two_factor_auth boolean NOT NULL,
    unsuccessful_login_counter integer NOT NULL,
    blocked_time timestamp,
    last_successful_login_time timestamp,
    last_unsuccessful_login_time timestamp,
    modification_time timestamp NOT NULL,
    version bigint NOT NULL,
    id binary(16) NOT NULL,
    modified_by binary(16),
    phone_number character varying(16) NOT NULL,
    last_successful_login_ip character varying(17),
    last_unsuccessful_login_ip character varying(17),
    login character varying(32) NOT NULL,
    password character varying(60) NOT NULL,
    email character varying(64) NOT NULL,
    first_name character varying(64) NOT NULL,
    last_name character varying(64) NOT NULL,
    operation_type enum('REGISTRATION', 'LOGIN', 'ACTIVATION', 'BLOCK', 'UNBLOCK', 'PASSWORD_CHANGE', 'EMAIL_CHANGE', 'SUSPEND', 'RESTORE_ACCESS', 'PERSONAL_DATA_MODIFICATION') NOT NULL
);

--
-- Name: admin_data; Type: TABLE; Schema: public
--

CREATE TABLE admin_data (
    id binary(16) NOT NULL
);

--
-- Name: attribute_association; Type: TABLE; Schema: public
--

CREATE TABLE attribute_association (
    version bigint NOT NULL,
    attribute_name_id binary(16) NOT NULL,
    attribute_value_id binary(16) NOT NULL,
    id binary(16) NOT NULL
);

--
-- Name: attribute_name; Type: TABLE; Schema: public
--

CREATE TABLE attribute_name (
    version bigint NOT NULL,
    id binary(16) NOT NULL,
    attribute_name character varying(255) NOT NULL
);

--
-- Name: attribute_value; Type: TABLE; Schema: public
--

CREATE TABLE attribute_value (
    version bigint NOT NULL,
    attribute_name_id binary(16) NOT NULL,
    id binary(16) NOT NULL,
    attribute_value character varying(255)
);

--
-- Name: client_data; Type: TABLE; Schema: public
--

CREATE TABLE client_data (
    total_reservation_hours bigint NOT NULL,
    id binary(16) NOT NULL,
    type enum('BASIC', 'STANDARD', 'PREMIUM') NOT NULL
);

--
-- Name: parking; Type: TABLE; Schema: public
--

CREATE TABLE parking (
    zip_code character varying(6) NOT NULL,
    creation_timestamp timestamp NOT NULL,
    update_timestamp timestamp,
    version bigint NOT NULL,
    id binary(16) NOT NULL,
    city character varying(50) NOT NULL,
    street character varying(50) NOT NULL,
    created_by character varying(255),
    sector_strategy enum('LEAST_OCCUPIED', 'MOST_OCCUPIED', 'LEAST_OCCUPIED_WEIGHTED') NOT NULL,
    updated_by character varying(255)
);

--
-- Name: parking_event; Type: TABLE; Schema: public
--

CREATE TABLE parking_event (
    date timestamp NOT NULL,
    version bigint NOT NULL,
    id binary(16) NOT NULL,
    reservation_id binary(16) NOT NULL,
    created_by character varying(255),
    type enum('ENTRY', 'EXIT') NOT NULL
);

--
-- Name: parking_history; Type: TABLE; Schema: public
--

CREATE TABLE parking_history (
    zip_code character varying(6) NOT NULL,
    modification_time timestamp NOT NULL,
    version bigint NOT NULL,
    id binary(16) NOT NULL,
    modified_by binary(16),
    city character varying(255) NOT NULL,
    strategy enum('LEAST_OCCUPIED', 'MOST_OCCUPIED', 'LEAST_OCCUPIED_WEIGHTED') NOT NULL,
    street character varying(255) NOT NULL
);

--
-- Name: past_password; Type: TABLE; Schema: public
--

CREATE TABLE past_password (
    account_id binary(16) NOT NULL,
    past_password character varying(255)
);

--
-- Name: personal_data; Type: TABLE; Schema: public
--

CREATE TABLE personal_data (
    id binary(16) NOT NULL,
    email character varying(32) NOT NULL,
    lastname character varying(32) NOT NULL,
    name character varying(32) NOT NULL
);

--
-- Name: reservation; Type: TABLE; Schema: public
--

CREATE TABLE reservation (
    begin_time timestamp,
    creation_timestamp timestamp NOT NULL,
    end_time timestamp,
    update_timestamp timestamp,
    version bigint NOT NULL,
    client_id binary(16),
    id binary(16) NOT NULL,
    sector_id binary(16) NOT NULL,
    created_by character varying(255),
    status enum('AWAITING', 'IN_PROGRESS', 'COMPLETED_MANUALLY', 'COMPLETED_AUTOMATICALLY', 'CANCELLED', 'TERMINATED') NOT NULL,
    updated_by character varying(255)
);

--
-- Name: sector; Type: TABLE; Schema: public
--

CREATE TABLE sector (
    max_places integer NOT NULL,
    occupied_places integer NOT NULL,
    weight integer NOT NULL,
    name character varying(5) NOT NULL,
    creation_timestamp timestamp NOT NULL,
    deactivation_time timestamp,
    update_timestamp timestamp,
    version bigint NOT NULL,
    id binary(16) NOT NULL,
    parking_id binary(16) NOT NULL,
    created_by character varying(255),
    type enum('COVERED', 'UNCOVERED', 'UNDERGROUND') NOT NULL,
    updated_by character varying(255),
    CONSTRAINT sector_max_places_check CHECK ((max_places <= 1000)),
    CONSTRAINT sector_weight_check CHECK (((weight <= 100) AND (weight >= 1)))
);

--
-- Name: staff_data; Type: TABLE; Schema: public
--

CREATE TABLE staff_data (
    id binary(16) NOT NULL
);

--
-- Name: token; Type: TABLE; Schema: public
--

CREATE TABLE token (
    creation_timestamp timestamp NOT NULL,
    version bigint NOT NULL,
    account_id binary(16) NOT NULL,
    id binary(16) NOT NULL,
    token_value character varying(512) NOT NULL,
    created_by character varying(255),
    type enum('REFRESH_TOKEN', 'MULTI_FACTOR_AUTHENTICATION_CODE', 'REGISTER', 'RESET_PASSWORD', 'CONFIRM_EMAIL', 'CHANGE_OVERWRITTEN_PASSWORD', 'RESTORE_ACCESS_TOKEN') NOT NULL
);

--
-- Name: user_level; Type: TABLE; Schema: public
--

CREATE TABLE user_level (
    creation_timestamp timestamp NOT NULL,
    update_timestamp timestamp,
    version bigint NOT NULL,
    account_id binary(16) NOT NULL,
    id binary(16) NOT NULL,
    level character varying(31) NOT NULL,
    created_by character varying(255)
);

--
-- Name: account_attributes account_attributes_pkey; Type: CONSTRAINT; Schema: public
--

ALTER TABLE account_attributes
    ADD CONSTRAINT account_attributes_pkey PRIMARY KEY (account_id, attribute_name_id);


--
-- Name: account_history account_history_pkey; Type: CONSTRAINT; Schema: public
--

ALTER TABLE account_history
    ADD CONSTRAINT account_history_pkey PRIMARY KEY (id, version);


--
-- Name: account account_login_key; Type: CONSTRAINT; Schema: public
--

ALTER TABLE account
    ADD CONSTRAINT account_login_key UNIQUE (login);


--
-- Name: account account_pkey; Type: CONSTRAINT; Schema: public
--

ALTER TABLE account
    ADD CONSTRAINT account_pkey PRIMARY KEY (id);


--
-- Name: admin_data admin_data_pkey; Type: CONSTRAINT; Schema: public
--

ALTER TABLE admin_data
    ADD CONSTRAINT admin_data_pkey PRIMARY KEY (id);


--
-- Name: attribute_association attribute_association_pkey; Type: CONSTRAINT; Schema: public
--

ALTER TABLE attribute_association
    ADD CONSTRAINT attribute_association_pkey PRIMARY KEY (id);


--
-- Name: attribute_name attribute_name_attribute_name_key; Type: CONSTRAINT; Schema: public
--

ALTER TABLE attribute_name
    ADD CONSTRAINT attribute_name_attribute_name_key UNIQUE (attribute_name);


--
-- Name: attribute_name attribute_name_pkey; Type: CONSTRAINT; Schema: public
--

ALTER TABLE attribute_name
    ADD CONSTRAINT attribute_name_pkey PRIMARY KEY (id);


--
-- Name: attribute_value attribute_value_attribute_value_attribute_name_id_key; Type: CONSTRAINT; Schema: public
--

ALTER TABLE attribute_value
    ADD CONSTRAINT attribute_value_attribute_value_attribute_name_id_key UNIQUE (attribute_value, attribute_name_id);


--
-- Name: attribute_value attribute_value_pkey; Type: CONSTRAINT; Schema: public
--

ALTER TABLE attribute_value
    ADD CONSTRAINT attribute_value_pkey PRIMARY KEY (id);


--
-- Name: client_data client_data_pkey; Type: CONSTRAINT; Schema: public
--

ALTER TABLE client_data
    ADD CONSTRAINT client_data_pkey PRIMARY KEY (id);


--
-- Name: parking parking_city_zip_code_street_key; Type: CONSTRAINT; Schema: public
--

ALTER TABLE parking
    ADD CONSTRAINT parking_city_zip_code_street_key UNIQUE (city, zip_code, street);


--
-- Name: parking_event parking_event_pkey; Type: CONSTRAINT; Schema: public
--

ALTER TABLE parking_event
    ADD CONSTRAINT parking_event_pkey PRIMARY KEY (id);


--
-- Name: parking_history parking_history_pkey; Type: CONSTRAINT; Schema: public
--

ALTER TABLE parking_history
    ADD CONSTRAINT parking_history_pkey PRIMARY KEY (id, version);


--
-- Name: parking parking_pkey; Type: CONSTRAINT; Schema: public
--

ALTER TABLE parking
    ADD CONSTRAINT parking_pkey PRIMARY KEY (id);


--
-- Name: past_password past_password_account_id_past_password_key; Type: CONSTRAINT; Schema: public
--

ALTER TABLE past_password
    ADD CONSTRAINT past_password_account_id_past_password_key UNIQUE (account_id, past_password);


--
-- Name: personal_data personal_data_email_key; Type: CONSTRAINT; Schema: public
--

ALTER TABLE personal_data
    ADD CONSTRAINT personal_data_email_key UNIQUE (email);


--
-- Name: personal_data personal_data_pkey; Type: CONSTRAINT; Schema: public
--

ALTER TABLE personal_data
    ADD CONSTRAINT personal_data_pkey PRIMARY KEY (id);


--
-- Name: reservation reservation_pkey; Type: CONSTRAINT; Schema: public
--

ALTER TABLE reservation
    ADD CONSTRAINT reservation_pkey PRIMARY KEY (id);


--
-- Name: sector sector_name_parking_id_key; Type: CONSTRAINT; Schema: public
--

ALTER TABLE sector
    ADD CONSTRAINT sector_name_parking_id_key UNIQUE (name, parking_id);


--
-- Name: sector sector_pkey; Type: CONSTRAINT; Schema: public
--

ALTER TABLE sector
    ADD CONSTRAINT sector_pkey PRIMARY KEY (id);


--
-- Name: staff_data staff_data_pkey; Type: CONSTRAINT; Schema: public
--

ALTER TABLE staff_data
    ADD CONSTRAINT staff_data_pkey PRIMARY KEY (id);


--
-- Name: token token_pkey; Type: CONSTRAINT; Schema: public
--

ALTER TABLE token
    ADD CONSTRAINT token_pkey PRIMARY KEY (id);


--
-- Name: token token_token_value_key; Type: CONSTRAINT; Schema: public
--

ALTER TABLE token
    ADD CONSTRAINT token_token_value_key UNIQUE (token_value);


--
-- Name: user_level user_level_account_id_level_key; Type: CONSTRAINT; Schema: public
--

ALTER TABLE user_level
    ADD CONSTRAINT user_level_account_id_level_key UNIQUE (account_id, level);


--
-- Name: user_level user_level_pkey; Type: CONSTRAINT; Schema: public
--

ALTER TABLE user_level
    ADD CONSTRAINT user_level_pkey PRIMARY KEY (id);


--
-- Name: idx_account_attribute_account_id; Type: INDEX; Schema: public
--

CREATE INDEX idx_account_attribute_account_id ON account_attributes (account_id);


--
-- Name: idx_account_attribute_attribute_name_id; Type: INDEX; Schema: public
--

CREATE INDEX idx_account_attribute_attribute_name_id ON account_attributes (attribute_name_id);


--
-- Name: idx_account_hist_account_id; Type: INDEX; Schema: public
--

CREATE INDEX idx_account_hist_account_id ON account_history (modified_by);


--
-- Name: idx_account_id; Type: INDEX; Schema: public
--

CREATE INDEX idx_account_id ON past_password (account_id);


--
-- Name: idx_admin_data_user_level_id; Type: INDEX; Schema: public
--

CREATE INDEX idx_admin_data_user_level_id ON admin_data (id);


--
-- Name: idx_attribute_record_attribute_name_id; Type: INDEX; Schema: public
--

CREATE INDEX idx_attribute_record_attribute_name_id ON attribute_association (attribute_name_id);


--
-- Name: idx_attribute_record_attribute_value_id; Type: INDEX; Schema: public;
--

CREATE INDEX idx_attribute_record_attribute_value_id ON attribute_association (attribute_value_id);


--
-- Name: idx_attribute_value_attribute_name_id; Type: INDEX; Schema: public
--

CREATE INDEX idx_attribute_value_attribute_name_id ON attribute_value (attribute_name_id);


--
-- Name: idx_client_data_user_level_id; Type: INDEX; Schema: public
--

CREATE INDEX idx_client_data_user_level_id ON client_data (id);


--
-- Name: idx_parking_event_reservation_id; Type: INDEX; Schema: public
--

CREATE INDEX idx_parking_event_reservation_id ON parking_event (reservation_id);


--
-- Name: idx_parking_history_account_id; Type: INDEX; Schema: public
--

CREATE INDEX idx_parking_history_account_id ON parking_history (modified_by);


--
-- Name: idx_reservation_client_id; Type: INDEX; Schema: public
--

CREATE INDEX idx_reservation_client_id ON reservation (client_id);


--
-- Name: idx_reservation_sector_id; Type: INDEX; Schema: public
--

CREATE INDEX idx_sector_parking_id ON sector (parking_id);


--
-- Name: idx_sector_parking_id; Type: INDEX; Schema: public
--

CREATE INDEX idx_reservation_sector_id ON reservation (sector_id);


--
-- Name: idx_staff_data_user_level_id; Type: INDEX; Schema: public
--

CREATE INDEX idx_staff_data_user_level_id ON staff_data (id);


--
-- Name: idx_token_account_id; Type: INDEX; Schema: public
--

CREATE INDEX idx_token_account_id ON token (account_id);


--
-- Name: idx_user_level_account_id; Type: INDEX; Schema: public
--

CREATE INDEX idx_user_level_account_id ON user_level (account_id);


--
-- Name: account_attributes account_attribute_account_id_fk; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE account_attributes
    ADD CONSTRAINT account_attribute_account_id_fk FOREIGN KEY (attribute_name_id) REFERENCES attribute_association(id);


--
-- Name: account_attributes account_attribute_attribute_name_id_fk; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE account_attributes
    ADD CONSTRAINT account_attribute_attribute_name_id_fk FOREIGN KEY (account_id) REFERENCES account(id);


--
-- Name: account_history account_hist_account_id_fk; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE account_history
    ADD CONSTRAINT account_hist_account_id_fk FOREIGN KEY (modified_by) REFERENCES account(id);


--
-- Name: admin_data admin_data_user_level_id_fk; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE admin_data
    ADD CONSTRAINT admin_data_user_level_id_fk FOREIGN KEY (id) REFERENCES user_level(id);


--
-- Name: attribute_association attribute_record_attribute_name_id_fk; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE attribute_association
    ADD CONSTRAINT attribute_record_attribute_name_id_fk FOREIGN KEY (attribute_name_id) REFERENCES attribute_name(id);


--
-- Name: attribute_association attribute_record_attribute_value_id_fk; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE attribute_association
    ADD CONSTRAINT attribute_record_attribute_value_id_fk FOREIGN KEY (attribute_value_id) REFERENCES attribute_value(id);


--
-- Name: attribute_value attribute_value_attribute_name_id_fk; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE attribute_value
    ADD CONSTRAINT attribute_value_attribute_name_id_fk FOREIGN KEY (attribute_name_id) REFERENCES attribute_name(id);


--
-- Name: client_data client_data_user_level_id_fk; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE client_data
    ADD CONSTRAINT client_data_user_level_id_fk FOREIGN KEY (id) REFERENCES user_level(id);


--
-- Name: parking_event parking_event_reservation_id_fk; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE parking_event
    ADD CONSTRAINT parking_event_reservation_id_fk FOREIGN KEY (reservation_id) REFERENCES reservation(id);


--
-- Name: parking_history parking_history_account_id_fk; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE parking_history
    ADD CONSTRAINT parking_history_account_id_fk FOREIGN KEY (modified_by) REFERENCES account(id);


--
-- Name: past_password past_password_account_id_fk; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE past_password
    ADD CONSTRAINT past_password_account_id_fk FOREIGN KEY (account_id) REFERENCES account(id);


--
-- Name: personal_data personal_data_account_id_fk; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE personal_data
    ADD CONSTRAINT personal_data_account_id_fk FOREIGN KEY (id) REFERENCES account(id);


--
-- Name: reservation reservation_client_id_fk; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE reservation
    ADD CONSTRAINT reservation_client_id_fk FOREIGN KEY (client_id) REFERENCES client_data(id);


--
-- Name: reservation reservation_sector_id_fk; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE reservation
    ADD CONSTRAINT reservation_sector_id_fk FOREIGN KEY (sector_id) REFERENCES sector(id);


--
-- Name: sector sector_parking_id_fk; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE sector
    ADD CONSTRAINT sector_parking_id_fk FOREIGN KEY (parking_id) REFERENCES parking(id);


--
-- Name: staff_data staff_data_user_level_id_fk; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE staff_data
    ADD CONSTRAINT staff_data_user_level_id_fk FOREIGN KEY (id) REFERENCES user_level(id);


--
-- Name: token token_account_id_fk; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE token
    ADD CONSTRAINT token_account_id_fk FOREIGN KEY (account_id) REFERENCES account(id);


--
-- Name: user_level user_level_account_id_fk; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE user_level
    ADD CONSTRAINT user_level_account_id_fk FOREIGN KEY (account_id) REFERENCES account(id);

-- GRAND PRIVILEGES
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE account                TO 'ssbd03mok'@'localhost';
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE past_password          TO 'ssbd03mok'@'localhost';
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE personal_data          TO 'ssbd03mok'@'localhost';
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE user_level             TO 'ssbd03mok'@'localhost';
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE client_data            TO 'ssbd03mok'@'localhost';
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE staff_data             TO 'ssbd03mok'@'localhost';
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE admin_data             TO 'ssbd03mok'@'localhost';
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE token                  TO 'ssbd03mok'@'localhost';
GRANT SELECT, INSERT                 ON TABLE account_history        TO 'ssbd03mok'@'localhost';

GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE account_attributes     TO 'ssbd03mok'@'localhost';
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE attribute_association  TO 'ssbd03mok'@'localhost';
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE attribute_name         TO 'ssbd03mok'@'localhost';
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE attribute_value        TO 'ssbd03mok'@'localhost';

GRANT SELECT                         ON TABLE account_attributes     TO 'ssbd03auth'@'localhost';
GRANT SELECT                         ON TABLE attribute_association  TO 'ssbd03auth'@'localhost';
GRANT SELECT                         ON TABLE attribute_name         TO 'ssbd03auth'@'localhost';
GRANT SELECT                         ON TABLE attribute_value        TO 'ssbd03auth'@'localhost';

GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE reservation            TO 'ssbd03mop'@'localhost';
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE parking                TO 'ssbd03mop'@'localhost';
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE sector                 TO 'ssbd03mop'@'localhost';
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE parking_event          TO 'ssbd03mop'@'localhost';
GRANT SELECT                         ON TABLE account                TO 'ssbd03mop'@'localhost';
GRANT SELECT                         ON TABLE personal_data          TO 'ssbd03mop'@'localhost';
GRANT SELECT                , UPDATE ON TABLE user_level             TO 'ssbd03mop'@'localhost';
GRANT SELECT                , UPDATE ON TABLE client_data            TO 'ssbd03mop'@'localhost';
GRANT SELECT                         ON TABLE admin_data             TO 'ssbd03mop'@'localhost';
GRANT SELECT                         ON TABLE staff_data             TO 'ssbd03mop'@'localhost';
GRANT SELECT, INSERT                 ON TABLE parking_history        TO 'ssbd03mop'@'localhost';

GRANT SELECT                         ON TABLE account_attributes     TO 'ssbd03mop'@'localhost';
GRANT SELECT                         ON TABLE attribute_association  TO 'ssbd03mop'@'localhost';
GRANT SELECT                         ON TABLE attribute_name         TO 'ssbd03mop'@'localhost';
GRANT SELECT                         ON TABLE attribute_value        TO 'ssbd03mop'@'localhost';


GRANT SELECT, UPDATE                 ON TABLE account                TO 'ssbd03auth'@'localhost';
GRANT SELECT                         ON TABLE personal_data          TO 'ssbd03auth'@'localhost';
GRANT SELECT                         ON TABLE user_level             TO 'ssbd03auth'@'localhost';
GRANT SELECT                         ON TABLE client_data            TO 'ssbd03auth'@'localhost';
GRANT SELECT                         ON TABLE staff_data             TO 'ssbd03auth'@'localhost';
GRANT SELECT                         ON TABLE admin_data             TO 'ssbd03auth'@'localhost';
GRANT SELECT, INSERT, DELETE         ON TABLE token                  TO 'ssbd03auth'@'localhost';
GRANT SELECT, INSERT                 ON TABLE account_history        TO 'ssbd03auth'@'localhost';