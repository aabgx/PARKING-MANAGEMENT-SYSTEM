--
-- PostgreSQL database dump
--

-- Dumped from database version 14.9 (Ubuntu 14.9-0ubuntu0.22.04.1)
-- Dumped by pg_dump version 14.9 (Ubuntu 14.9-0ubuntu0.22.04.1)

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
-- Name: bookingtable; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.bookingtable (
    id integer NOT NULL,
    userid integer,
    parkingspaceid integer,
    date date
);


ALTER TABLE public.bookingtable OWNER TO postgres;

--
-- Name: booking_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.bookingtable ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.booking_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: griditemtable; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.griditemtable (
    id integer NOT NULL,
    x integer,
    y integer,
    width integer,
    height integer,
    sectionid integer,
    orientation character varying
);


ALTER TABLE public.griditemtable OWNER TO postgres;

--
-- Name: griditem_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.griditemtable ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.griditem_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: parkingspacetable; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.parkingspacetable (
    id integer NOT NULL,
    griditemid integer,
    type character varying
);


ALTER TABLE public.parkingspacetable OWNER TO postgres;

--
-- Name: parkingspace_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.parkingspacetable ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.parkingspace_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: roadtable; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.roadtable (
    id integer NOT NULL,
    griditemid integer,
    name character varying
);


ALTER TABLE public.roadtable OWNER TO postgres;

--
-- Name: road_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.roadtable ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.road_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: sectiontable; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sectiontable (
    id integer NOT NULL,
    name character varying,
    height integer,
    width integer
);


ALTER TABLE public.sectiontable OWNER TO postgres;

--
-- Name: section_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.sectiontable ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.section_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: usertable; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.usertable (
    id integer NOT NULL,
    username character varying,
    password character varying,
    phone character varying,
    email character varying,
    isadmin boolean,
    userrole character varying
);


ALTER TABLE public.usertable OWNER TO postgres;

--
-- Name: user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.usertable ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Data for Name: bookingtable; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.bookingtable (id, userid, parkingspaceid, date) FROM stdin;
\.


--
-- Data for Name: griditemtable; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.griditemtable (id, x, y, width, height, sectionid, orientation) FROM stdin;
3	5	6	7	8	1	NORTH
\.


--
-- Data for Name: parkingspacetable; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.parkingspacetable (id, griditemid, type) FROM stdin;
2	3	NORMAL
4	3	ELECTRIC
5	3	NORMAL
\.


--
-- Data for Name: roadtable; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.roadtable (id, griditemid, name) FROM stdin;
\.


--
-- Data for Name: sectiontable; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sectiontable (id, name, height, width) FROM stdin;
1	0	0	0
\.


--
-- Data for Name: usertable; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.usertable (id, username, password, phone, email, isadmin, userrole) FROM stdin;
1	MariusAnd	cioco	+40770871913	marius.andreiasi2002@gmail.com	f	user
\.


--
-- Name: booking_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.booking_id_seq', 1, false);


--
-- Name: griditem_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.griditem_id_seq', 3, true);


--
-- Name: parkingspace_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.parkingspace_id_seq', 5, true);


--
-- Name: road_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.road_id_seq', 1, false);


--
-- Name: section_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.section_id_seq', 1, true);


--
-- Name: user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.user_id_seq', 1, true);


--
-- Name: bookingtable booking_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bookingtable
    ADD CONSTRAINT booking_pk PRIMARY KEY (id);


--
-- Name: griditemtable griditem_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.griditemtable
    ADD CONSTRAINT griditem_pk PRIMARY KEY (id);


--
-- Name: parkingspacetable parkingspace_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.parkingspacetable
    ADD CONSTRAINT parkingspace_pk PRIMARY KEY (id);


--
-- Name: roadtable road_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roadtable
    ADD CONSTRAINT road_pk PRIMARY KEY (id);


--
-- Name: sectiontable section_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sectiontable
    ADD CONSTRAINT section_pk PRIMARY KEY (id);


--
-- Name: usertable usertable_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usertable
    ADD CONSTRAINT usertable_pk PRIMARY KEY (id);


--
-- Name: bookingtable booking_parkingspace_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bookingtable
    ADD CONSTRAINT booking_parkingspace_id_fk FOREIGN KEY (parkingspaceid) REFERENCES public.parkingspacetable(id);


--
-- Name: bookingtable booking_user_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bookingtable
    ADD CONSTRAINT booking_user_id_fk FOREIGN KEY (userid) REFERENCES public.usertable(id);


--
-- Name: griditemtable griditem_section_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.griditemtable
    ADD CONSTRAINT griditem_section_id_fk FOREIGN KEY (sectionid) REFERENCES public.sectiontable(id);


--
-- Name: parkingspacetable parkingspace_griditem_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.parkingspacetable
    ADD CONSTRAINT parkingspace_griditem_id_fk FOREIGN KEY (griditemid) REFERENCES public.griditemtable(id);


--
-- Name: roadtable road_griditem_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roadtable
    ADD CONSTRAINT road_griditem_id_fk FOREIGN KEY (griditemid) REFERENCES public.griditemtable(id);


--
-- PostgreSQL database dump complete
--

