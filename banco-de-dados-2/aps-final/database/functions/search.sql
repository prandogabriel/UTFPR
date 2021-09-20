-- Função que realiza uma pesquisa entre dois textos sem considerar letras maíusculas ou acentos
CREATE OR REPLACE FUNCTION public.search(texto varchar) returns VARCHAR as $$
        BEGIN
                RETURN upper(unaccent(texto));
        END;
$$
LANGUAGE plpgsql
RETURNS NULL ON NULL INPUT; -- Retorna NULL caso a entrada seja NULL;

-- Exemplo
insert into products
values (uuid_generate_v4(),'pão_de_BATATA',null,'box','food',now(),now()); -- inserindo com acento e letras maiúsculas

select *
from products p 
where search(p.name) like search('pao_de_batata'); -- buscando sem acento e sem letras maiúsculas


