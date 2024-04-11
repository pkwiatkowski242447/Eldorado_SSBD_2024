-- -- GRAND PRIVILEGES
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.testtable      TO ssbd03mok;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.testtable2     TO ssbd03mop;

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.parking TO ssbd03mop;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.sector TO ssbd03mop;

GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.account        TO ssbd03mok;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.personal_data  TO ssbd03mok;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.user_level     TO ssbd03mok;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.client_data    TO ssbd03mok;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.staff_data     TO ssbd03mok;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.admin_data     TO ssbd03mok;

GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.reservation    TO ssbd03mop;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.parking        TO ssbd03mop;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.sector         TO ssbd03mop;
GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE public.parking_event  TO ssbd03mop;
GRANT SELECT                         ON TABLE public.account        TO ssbd03mop;
GRANT SELECT                         ON TABLE public.personal_data  TO ssbd03mop;
GRANT SELECT                         ON TABLE public.user_level     TO ssbd03mop;
GRANT SELECT                         ON TABLE public.client_data    TO ssbd03mop;
