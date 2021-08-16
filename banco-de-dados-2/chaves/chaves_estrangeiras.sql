create table cliente (
	idCliente serial primary key,
	nome varchar, datanasc date);

create table carro(
	id serial primary key,
	ano integer, modelo varchar,
	idCliente integer references cliente(idCliente)
);

do $$
begin
for i in 1..100000 loop
insert into cliente (nome,datanasc)
values (texto(6), data());
end loop;
end
$$ language plpgsql

do $$
begin
for i in 1..100000 loop
insert into carro (ano,modelo,idCliente) values
((random()*100)::integer, texto(6), i);
end loop;
end
$$ language plpgsql

analyze cliente;
analyze carro;


EXPLAIN ANALYZE
SELECT NOME, MODELO
FROM CLIENTE NATURAL JOIN CARRO
WHERE IDCLIENTE=30

create index idxCliCarro on carro(idCliente);
drop index idxCliCarro



