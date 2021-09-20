CREATE OR REPLACE FUNCTION public.log_stock_trigger() RETURNS TRIGGER AS $log_stock_trigger$ -- VARIAVEL QUE SERÁ UTILIZADA NA TRIGGER
DECLARE var_old_record text;
var_new_record text;
BEGIN -- VERIFICA SE FOI FEITA ALGUMA ALTERAÇÃO (UPDATE)
IF (
  NEW.id IS NOT NULL
  AND OLD.id IS NOT NULL
) THEN --TG_OP = 'UPDATE'
var_old_record = OLD.product_id ||','|| OLD.price || ',' || OLD.batch || ',' || OLD.quantity;
var_new_record = NEW.product_id ||','|| NEW.price || ',' || NEW.batch || ',' || NEW.quantity;
INSERT INTO public.log_stock (
    id,
    user_name,
    operation,
    old_record,
    new_record,
    created_at
  )
VALUES(
    uuid_generate_v4(),
    CURRENT_USER,
    'update',
    var_old_record,
    var_new_record,
    now()
  );
RAISE NOTICE 'update log persist.';
RETURN NEW;
END IF;
-- VERIFICA SE FOI FEITA ALGUMA INSERÇÃO
IF (
  NEW.id IS NOT NULL
  AND OLD.id ISNULL
) THEN --TG_OP = 'INSERT'
var_old_record = NULL;
var_new_record = NEW.product_id ||','|| NEW.price || ',' || NEW.batch || ',' || NEW.quantity;
INSERT INTO public.log_stock (
    id,
    user_name,
    operation,
    old_record,
    new_record,
    created_at
  )
VALUES(
    uuid_generate_v4(),
    CURRENT_USER,
    'insert',
    var_old_record,
    var_new_record,
    now()
  );
RAISE NOTICE 'insert log persist.';
RETURN NEW;
END IF;
--VERIFICA SE FOI FEITA ALGUMA DELEÇÃO
IF (
  NEW.id ISNULL
  AND OLD.id IS NOT NULL
) THEN --TG_OP = 'DELETE'
var_old_record = OLD.product_id ||','|| OLD.price || ',' || OLD.batch || ',' || OLD.quantity;
var_new_record = NULL;
INSERT INTO public.log_stock (
    id,
    user_name,
    operation,
    old_record,
    new_record,
    created_at
  )
VALUES(
    uuid_generate_v4(),
    CURRENT_USER,
    'delete',
    var_old_record,
    var_new_record,
    now()
  );
RAISE NOTICE 'delete log persist.';
RETURN OLD;
END IF;
END;
$log_stock_trigger$ LANGUAGE plpgsql;
CREATE TRIGGER tg_log_stock BEFORE
INSERT
  OR
UPDATE
  OR DELETE ON public.stock FOR EACH ROW EXECUTE PROCEDURE public.log_stock_trigger();