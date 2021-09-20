-- Indice criado para otimizar buscas em um método de pagamento especifico, nesse caso, cartao de credito
CREATE index idxValuePaymentType ON public.orders (value)  WHERE payment_type = 'credit_card';
analyze;

explain analyze 
SELECT *
FROM orders
WHERE value = 6735.0 and payment_type= 'credit_card';

--  Index Scan using batata on orders  (cost=0.12..8.14 rows=1 width=84) (actual time=0.008..0.010 rows=0 loops=1)
--   Index Cond: (value = '11'::double precision)
-- Planning Time: 0.394 ms
-- Execution Time: 0.062 ms
