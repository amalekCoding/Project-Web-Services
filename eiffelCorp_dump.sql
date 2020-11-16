--
-- PostgreSQL database dump
--

-- Dumped from database version 12.1
-- Dumped by pg_dump version 13.0

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: Clients; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Clients" (
    id integer NOT NULL,
    bank_balance integer DEFAULT 0,
    login character varying(30) NOT NULL,
    password character varying(30) NOT NULL,
    firstname character varying(20) NOT NULL,
    lastname character varying(20) NOT NULL
);


ALTER TABLE public."Clients" OWNER TO postgres;

--
-- Name: COLUMN "Clients".bank_balance; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public."Clients".bank_balance IS 'Solde bancaire';


--
-- Name: COLUMN "Clients".firstname; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public."Clients".firstname IS 'Prénom';


--
-- Name: COLUMN "Clients".lastname; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public."Clients".lastname IS 'Nom';


--
-- Name: Clients_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."Clients_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public."Clients_id_seq" OWNER TO postgres;

--
-- Name: Clients_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."Clients_id_seq" OWNED BY public."Clients".id;


--
-- Name: Employees; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Employees" (
    id integer NOT NULL,
    login character varying(30) NOT NULL,
    password character varying(30) NOT NULL,
    firstname character varying(20) NOT NULL,
    lastname character varying(20) NOT NULL
);


ALTER TABLE public."Employees" OWNER TO postgres;

--
-- Name: COLUMN "Employees".firstname; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public."Employees".firstname IS 'Prénom';


--
-- Name: COLUMN "Employees".lastname; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public."Employees".lastname IS 'Nom';


--
-- Name: Employees_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."Employees_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public."Employees_id_seq" OWNER TO postgres;

--
-- Name: Employees_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."Employees_id_seq" OWNED BY public."Employees".id;


--
-- Name: Purchases; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Purchases" (
    date timestamp with time zone NOT NULL,
    vehicle_id integer NOT NULL,
    client_id integer NOT NULL
);


ALTER TABLE public."Purchases" OWNER TO postgres;

--
-- Name: Rentals; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Rentals" (
    date timestamp with time zone NOT NULL,
    vehicle_id integer NOT NULL,
    employee_id integer NOT NULL,
    vehicle_grade integer,
    condition_grade integer
);


ALTER TABLE public."Rentals" OWNER TO postgres;

--
-- Name: Vehicles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Vehicles" (
    id integer NOT NULL,
    buying_price double precision DEFAULT 0,
    rental_price double precision DEFAULT 0,
    brand character varying(20),
    model character varying(20)
);


ALTER TABLE public."Vehicles" OWNER TO postgres;

--
-- Name: COLUMN "Vehicles".buying_price; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public."Vehicles".buying_price IS 'Prix d''achat du véhicule (en euros)';


--
-- Name: COLUMN "Vehicles".rental_price; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public."Vehicles".rental_price IS 'Prix de location du véhicule (en euros)';


--
-- Name: COLUMN "Vehicles".brand; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public."Vehicles".brand IS 'Marque du véhicule';


--
-- Name: COLUMN "Vehicles".model; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public."Vehicles".model IS 'Modèle du véhicule';


--
-- Name: Vehicles_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."Vehicles_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public."Vehicles_id_seq" OWNER TO postgres;

--
-- Name: Vehicles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."Vehicles_id_seq" OWNED BY public."Vehicles".id;


--
-- Name: Clients id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Clients" ALTER COLUMN id SET DEFAULT nextval('public."Clients_id_seq"'::regclass);


--
-- Name: Employees id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Employees" ALTER COLUMN id SET DEFAULT nextval('public."Employees_id_seq"'::regclass);


--
-- Name: Vehicles id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Vehicles" ALTER COLUMN id SET DEFAULT nextval('public."Vehicles_id_seq"'::regclass);


--
-- Data for Name: Clients; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: Employees; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: Purchases; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: Rentals; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: Vehicles; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public."Vehicles" VALUES (1, 5000, 0, NULL, NULL);


--
-- Name: Clients_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Clients_id_seq"', 1, true);


--
-- Name: Employees_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Employees_id_seq"', 3, true);


--
-- Name: Vehicles_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Vehicles_id_seq"', 1, true);


--
-- Name: Clients Clients_login_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Clients"
    ADD CONSTRAINT "Clients_login_key" UNIQUE (login);


--
-- Name: Clients Clients_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Clients"
    ADD CONSTRAINT "Clients_pkey" PRIMARY KEY (id);


--
-- Name: Employees Employees_login_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Employees"
    ADD CONSTRAINT "Employees_login_key" UNIQUE (login);


--
-- Name: Employees Employees_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Employees"
    ADD CONSTRAINT "Employees_pkey" PRIMARY KEY (id);


--
-- Name: Purchases Purchases_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Purchases"
    ADD CONSTRAINT "Purchases_pkey" PRIMARY KEY (date, vehicle_id, client_id);


--
-- Name: Rentals Rentals_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Rentals"
    ADD CONSTRAINT "Rentals_pkey" PRIMARY KEY (date, vehicle_id, employee_id);


--
-- Name: Vehicles Vehicles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Vehicles"
    ADD CONSTRAINT "Vehicles_pkey" PRIMARY KEY (id);


--
-- Name: Purchases idClient_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Purchases"
    ADD CONSTRAINT "idClient_fk" FOREIGN KEY (client_id) REFERENCES public."Clients"(id);


--
-- Name: Rentals idEmployee_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Rentals"
    ADD CONSTRAINT "idEmployee_fk" FOREIGN KEY (employee_id) REFERENCES public."Employees"(id);


--
-- Name: Rentals idVehicle_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Rentals"
    ADD CONSTRAINT "idVehicle_fk" FOREIGN KEY (vehicle_id) REFERENCES public."Vehicles"(id);


--
-- Name: Purchases idVehicle_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Purchases"
    ADD CONSTRAINT "idVehicle_fk" FOREIGN KEY (vehicle_id) REFERENCES public."Vehicles"(id);


--
-- PostgreSQL database dump complete
--

