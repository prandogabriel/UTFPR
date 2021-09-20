--
-- Name: delivery_points; Type: TABLE; Schema: public; Owner: postgres
--
CREATE TABLE public.delivery_points (
    id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    city character varying NOT NULL,
    state character varying NOT NULL,
    suburb character varying NOT NULL,
    street character varying NOT NULL,
    number integer NOT NULL,
    cep character varying NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    updated_at timestamp without time zone DEFAULT now() NOT NULL
);
ALTER TABLE public.delivery_points OWNER TO postgres;
--
-- Name: order_details; Type: TABLE; Schema: public; Owner: postgres
--
CREATE TABLE public.order_details (
    id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    order_id uuid,
    product_id uuid NOT NULL,
    unit_price double precision DEFAULT 0,
    quantity double precision NOT NULL,
    discount double precision NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    updated_at timestamp without time zone DEFAULT now() NOT NULL
);
ALTER TABLE public.order_details OWNER TO postgres;
--
-- Name: orders; Type: TABLE; Schema: public; Owner: postgres
--
CREATE TABLE public.orders (
    id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    date timestamp with time zone NOT NULL,
    value double precision NOT NULL,
    payment_type public.orders_payment_type_enum NOT NULL,
    delivery_point_id uuid NOT NULL,
    user_id uuid NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    updated_at timestamp without time zone DEFAULT now() NOT NULL
);
ALTER TABLE public.orders OWNER TO postgres;
--
-- Name: products; Type: TABLE; Schema: public; Owner: postgres
--
CREATE TABLE public.products (
    id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    name character varying NOT NULL,
    image character varying,
    unit public.products_unit_sale_enum NOT NULL,
    category public.products_category_enum NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    updated_at timestamp without time zone DEFAULT now() NOT NULL
);
ALTER TABLE public.products OWNER TO postgres;
--
-- Name: user_tokens; Type: TABLE; Schema: public; Owner: postgres
--
CREATE TABLE public.user_tokens (
    id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    token uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    user_id uuid NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    updated_at timestamp without time zone DEFAULT now() NOT NULL
);
ALTER TABLE public.user_tokens OWNER TO postgres;
--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--
CREATE TABLE public.users (
    id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    name character varying NOT NULL,
    email character varying,
    phone character varying NOT NULL,
    password character varying,
    cpf character varying NOT NULL,
    role character varying DEFAULT 'b'::character varying NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    updated_at timestamp without time zone DEFAULT now() NOT NULL
);
ALTER TABLE public.users OWNER TO postgres;
--
-- Name: stock; Type: TABLE; Schema: public; Owner: postgres
--
CREATE TABLE public.stock (
    id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    product_id uuid NOT NULL,
    price double precision NOT NULL,
    batch character varying,
    quantity double precision NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    updated_at timestamp without time zone DEFAULT now() NOT NULL
);
ALTER TABLE public.stock OWNER TO postgres;
--
-- Name: log_stock; Type: TABLE; Schema: public; Owner: postgres
--
CREATE TABLE public.log_stock (
    id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    user_name character varying not null,
    operation CHAR(10) not null,
    old_record text,
    new_record text,
    created_at timestamp without time zone DEFAULT now() NOT NULL
);
ALTER TABLE public.log_stock OWNER TO postgres;

--
-- Name: log_stock PK_LogStock; Type: CONSTRAINT; Schema: public; Owner: postgres
--
ALTER TABLE ONLY public.log_stock
ADD CONSTRAINT "PK_LogStock" PRIMARY KEY (id);
--
-- Name: stock PK_Stock; Type: CONSTRAINT; Schema: public; Owner: postgres
--
ALTER TABLE ONLY public.stock
ADD CONSTRAINT "PK_Stock" PRIMARY KEY (id);
--
-- Name: products PK_Products; Type: CONSTRAINT; Schema: public; Owner: postgres
--
ALTER TABLE ONLY public.products
ADD CONSTRAINT "PK_Products" PRIMARY KEY (id);
--
-- Name: order_details PK_OrderDetails; Type: CONSTRAINT; Schema: public; Owner: postgres
--
ALTER TABLE ONLY public.order_details
ADD CONSTRAINT "PK_OrderDetails" PRIMARY KEY (id);
--
-- Name: user_tokens PK_UserTokens; Type: CONSTRAINT; Schema: public; Owner: postgres
--
ALTER TABLE ONLY public.user_tokens
ADD CONSTRAINT "PK_UserTokens" PRIMARY KEY (id);
--
-- Name: orders PK_Orders; Type: CONSTRAINT; Schema: public; Owner: postgres
--
ALTER TABLE ONLY public.orders
ADD CONSTRAINT "PK_Orders" PRIMARY KEY (id);
--
-- Name: users PK_Users; Type: CONSTRAINT; Schema: public; Owner: postgres
--
ALTER TABLE ONLY public.users
ADD CONSTRAINT "PK_Users" PRIMARY KEY (id);
--
-- Name: delivery_points PK_DeliveryPoints; Type: CONSTRAINT; Schema: public; Owner: postgres
--
ALTER TABLE ONLY public.delivery_points
ADD CONSTRAINT "PK_DeliveryPoints" PRIMARY KEY (id);
--
-- Name: users UQ_CPFUsers; Type: CONSTRAINT; Schema: public; Owner: postgres
--
ALTER TABLE ONLY public.users
ADD CONSTRAINT "UQ_CPFUsers" UNIQUE (cpf);
--
-- Name: users UQ_EmailUsers; Type: CONSTRAINT; Schema: public; Owner: postgres
--
ALTER TABLE ONLY public.users
ADD CONSTRAINT "UQ_EmailUsers" UNIQUE (email);