--
-- Name: unaccent; Type: EXTENSION; Schema: -; Owner:
--
CREATE EXTENSION IF NOT EXISTS unaccent WITH SCHEMA public;
--
-- Name: EXTENSION unaccent; Type: COMMENT; Schema: -; Owner:
--
COMMENT ON EXTENSION unaccent IS 'text search dictionary that removes accents';
--
-- Name: uuid-ossp; Type: EXTENSION; Schema: -; Owner:
--
CREATE EXTENSION IF NOT EXISTS "uuid-ossp" WITH SCHEMA public;
--
-- Name: EXTENSION "uuid-ossp"; Type: COMMENT; Schema: -; Owner:
--
COMMENT ON EXTENSION "uuid-ossp" IS 'generate universally unique identifiers (UUIDs)';
-- Name: orders_payment_type_enum; Type: TYPE; Schema: public; Owner: postgres