-- View para mostrar a quantidade de pedidos retirados por cidade
CREATE or REPLACE VIEW view_orders_by_city AS
select d.city as Cidade, count(o.delivery_point_id) as qtd_pedidos
from orders o join delivery_points d ON o.delivery_point_id=d.id
group by d.city
order by qtd_pedidos desc;

-- Testar a View
SELECT * from view_orders_by_city

