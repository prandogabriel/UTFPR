-- Trigger utilizada na função update_stock
CREATE TRIGGER tg_update_stock AFTER INSERT OR DELETE OR UPDATE
ON public.order_details
FOR EACH ROW EXECUTE PROCEDURE public.update_stock(); 
