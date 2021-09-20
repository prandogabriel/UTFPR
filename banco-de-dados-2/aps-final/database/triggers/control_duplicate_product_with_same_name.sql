-- Trigger utilizada na função control_duplicate_product_with_same_name()
CREATE TRIGGER tg_control_duplicate_product_with_same_name BEFORE INSERT ON public.products
FOR EACH ROW EXECUTE PROCEDURE public.control_duplicate_product_with_same_name_trigger(); 
