-- Função que da update no stock quando ocorre um pedido
CREATE FUNCTION public.update_stock() RETURNS trigger LANGUAGE plpgsql AS $$
declare var_stock_id uuid;
begin
    select s.id into var_stock_id
    from stock s
    where s.product_id = new.product_id;
    IF(TG_OP = 'INSERT') then
    update order_details
    set unit_price = (
            select s.price
            from stock s
            where s.id = var_stock_id
            limit 1
        )
    where id = new.id;
    return new;
    end if;
    IF(
        TG_OP = 'UPDATE'
        or TG_OP = 'INSERT'
    ) THEN
    update stock
    set quantity = (
            select quantity
            from stock s
            where s.id = var_stock_id
        ) - new.quantity
    where id = var_stock_id;
    return new;
    END IF;
    IF(TG_OP = 'DELETE') then
    update stock
    set quantity = (
            select quantity
            from stock s
            where s.id = var_stock_id
        ) + old.quantity
    where id = var_stock_id;
    return new;
    END IF;
END;
$$;
ALTER FUNCTION public.update_stock() OWNER TO postgres;
SET default_tablespace = '';
SET default_with_oids = false;

