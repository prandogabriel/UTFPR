create table pessoa (
	id serial primary key,
	nome varchar,
	genero varchar(1) check (genero in ('M','F'))
);

do $$
begin
for i in 1..100000 loop
insert into pessoa(nome, genero) values (texto(10),'M');
end loop;
end; $$
language plpgsql;

do $$
begin
for i in 1..100000 loop
insert into pessoa(nome, genero) values (texto(10),'F');
end loop;
end; $$
language plpgsql;

analyze pessoa

explain analyze
select * from pessoa where genero = 'M';

create extension btree_gin;

create index idxgeneroBitmap on pessoa using gin (genero);


