-- View para selecionar a quantidade total vendida de cada produto
CREATE or REPLACE VIEW public.view_total_products_sold_report as
select distinct p.name as NomeProd,
    (
        select sum(od2.quantity) as quantity_total_sold
        from public.order_details od2
        where p.id = od2.product_id
    ) as QTD_Vendida
from public.products p join public.order_details od on p.id = od.product_id
order by QTD_Vendida DESC;

-- Teste da View
select * from public.view_total_products_sold_report;
