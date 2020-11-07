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
    bank_balance integer DEFAULT 0
);


ALTER TABLE public."Clients" OWNER TO postgres;

--
-- Name: COLUMN "Clients".bank_balance; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public."Clients".bank_balance IS 'Solde bancaire';


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
    id integer NOT NULL
);


ALTER TABLE public."Employees" OWNER TO postgres;

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
-- Name: Grades; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Grades" (
    vehicle_id integer NOT NULL,
    employee_id integer NOT NULL,
    vehicle_grade integer,
    condition_grade integer
);


ALTER TABLE public."Grades" OWNER TO postgres;

--
-- Name: Grades_employee_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."Grades_employee_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public."Grades_employee_id_seq" OWNER TO postgres;

--
-- Name: Grades_employee_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."Grades_employee_id_seq" OWNED BY public."Grades".employee_id;


--
-- Name: Grades_vehicle_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."Grades_vehicle_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public."Grades_vehicle_id_seq" OWNER TO postgres;

--
-- Name: Grades_vehicle_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."Grades_vehicle_id_seq" OWNED BY public."Grades".vehicle_id;


--
-- Name: Vehicles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Vehicles" (
    id integer NOT NULL,
    price integer DEFAULT 0,
    nb_rented integer DEFAULT 0
);


ALTER TABLE public."Vehicles" OWNER TO postgres;

--
-- Name: COLUMN "Vehicles".price; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public."Vehicles".price IS 'Prix du véhicule (en euros)';


--
-- Name: COLUMN "Vehicles".nb_rented; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public."Vehicles".nb_rented IS 'Nombre de fois que le véhicule a été loué';


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
-- Name: Grades vehicle_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Grades" ALTER COLUMN vehicle_id SET DEFAULT nextval('public."Grades_vehicle_id_seq"'::regclass);


--
-- Name: Grades employee_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Grades" ALTER COLUMN employee_id SET DEFAULT nextval('public."Grades_employee_id_seq"'::regclass);


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

INSERT INTO public."Employees" VALUES (1);
INSERT INTO public."Employees" VALUES (2);
INSERT INTO public."Employees" VALUES (3);


--
-- Data for Name: Grades; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: Vehicles; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public."Vehicles" VALUES (1, 5000, 0);


--
-- Name: Clients_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Clients_id_seq"', 1, false);


--
-- Name: Employees_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Employees_id_seq"', 3, true);


--
-- Name: Grades_employee_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Grades_employee_id_seq"', 1, false);


--
-- Name: Grades_vehicle_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Grades_vehicle_id_seq"', 1, false);


--
-- Name: Vehicles_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Vehicles_id_seq"', 1, true);


--
-- Name: Clients Clients_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Clients"
    ADD CONSTRAINT "Clients_pkey" PRIMARY KEY (id);


--
-- Name: Employees Employees_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Employees"
    ADD CONSTRAINT "Employees_pkey" PRIMARY KEY (id);


--
-- Name: Grades Grades_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Grades"
    ADD CONSTRAINT "Grades_pkey" PRIMARY KEY (vehicle_id, employee_id);


--
-- Name: Vehicles Vehicles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Vehicles"
    ADD CONSTRAINT "Vehicles_pkey" PRIMARY KEY (id);


--
-- Name: Grades employee_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Grades"
    ADD CONSTRAINT employee_fk FOREIGN KEY (employee_id) REFERENCES public."Employees"(id);


--
-- Name: Grades vehicle_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Grades"
    ADD CONSTRAINT vehicle_fk FOREIGN KEY (vehicle_id) REFERENCES public."Vehicles"(id);


--
-- PostgreSQL database dump complete
--

