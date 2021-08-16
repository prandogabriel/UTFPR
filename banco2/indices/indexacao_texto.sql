create table teste(nome varchar);

do $$
begin
for i in 1..100000 loop
insert into teste values (texto(10));
end loop;
end; $$
language plpgsql;

analyze teste;

create index idxtext on teste(nome);

explain analyze
select * from teste where nome LIKE 'eQi%';

explain analyze
select * from teste where nome LIKE '%eQi%';


CREATE EXTENSION pg_trgm;


create index idxtextTrgm on teste using GIN(nome gin_trgm_ops);


