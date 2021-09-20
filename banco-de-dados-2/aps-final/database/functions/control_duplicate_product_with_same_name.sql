-- Garante que não seja cadastrado um produto com o mesmo nome no banco
-- Por exemplo: se existir um produto com nome 'Mamão' e tentarmos inserir um com o nome 'mamao'
-- Irá disparar um erro
CREATE extension IF NOT EXISTS unaccent;

CREATE OR REPLACE FUNCTION public.control_duplicate_product_with_same_name_trigger() RETURNS TRIGGER AS $control_duplicate_product_with_same_name_trigger$ BEGIN IF EXISTS(
        SELECT name
        FROM public.products
        WHERE search(name) = search(NEW.name) -- compara o nome de produtos na tabela com o produto a ser inserido com a função search
    ) THEN RAISE EXCEPTION 'product already registered.';
ELSE RETURN NEW;
END IF;
END;
$control_duplicate_product_with_same_name_trigger$ LANGUAGE plpgsql;

-- Função SEARCH utilizada criada em \functions\search.sql
-- Trigger utitlizada está criada na pasta \triggers

-- Exemplo
insert into products
values (uuid_generate_v4(),'pão_de_BATATA',null,'box','food',now(),now());

insert into products
values (uuid_generate_v4(),'pão_de_batatá',null,'box','food',now(),now());

-- ERROR: product already registered.
--  Where: função PL/pgSQL control_duplicate_product_with_same_name_trigger() linha 5 em RAISE
