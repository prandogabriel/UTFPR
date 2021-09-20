-- Indice criado para melhorar as buscas em nome de produtos
CREATE INDEX idxProductName ON public.products using gin(to_tsvector('portuguese', name));
analyze;
-- Na query da busca:
explain analyze SELECT * FROM public.products p WHERE to_tsvector('portuguese', p.name) @@ to_tsquery('portuguese', 'Breads');
--Bitmap Heap Scan on products p  (cost=12.01..16.27 rows=1 width=82) (actual time=0.016..0.017 rows=0 loops=1)
--  Recheck Cond: (to_tsvector('portuguese'::regconfig, (name)::text) @@ '''jos'''::tsquery)
--  ->  Bitmap Index Scan on idxProductName  (cost=0.00..12.01 rows=1 width=0) (actual time=0.014..0.014 rows=0 loops=1)
--        Index Cond: (to_tsvector('portuguese'::regconfig, (name)::text) @@ '''jos'''::tsquery)
--Planning Time: 42.571 ms
--Execution Time: 0.071 ms
