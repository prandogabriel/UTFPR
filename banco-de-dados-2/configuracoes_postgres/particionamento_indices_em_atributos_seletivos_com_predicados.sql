create table aluno (
	ra bigint primary key,
	nome varchar check (length(nome) < 60),
	cidade varchar check (length(cidade) < 30),
	idade integer check (idade > 0 and idade < 150)
);


do $$
begin
for i IN 1..500 LOOP
insert into aluno values (gera_id(), texto(5), 'Curitiba', numero(2))
                                                on conflict do nothing;
end loop;
end $$;

table aluno
analyse aluno

select idade, count(*) from aluno group by idade;

CREATE INDEX IdxRACapital ON Aluno (RA)
WHERE CIDADE='Curitiba';

explain
SELECT * FROM Aluno
WHERE CIDADE='Curitiba' AND Idade>20;

	explain
SELECT * FROM Aluno
WHERE CIDADE='Pato Branco' AND RA=117955;

explain
SELECT * FROM Aluno
WHERE CIDADE='Pato Branco' AND Idade>20;

CREATE INDEX IdxRAPatoB ON Aluno (RA)
WHERE CIDADE='Pato Branco';

explain
SELECT * FROM Aluno
WHERE Idade>20;




---funcoes uteis
create or replace function gera_id() returns integer as $$
declare
   h integer := 500000;
   l integer := 1;
begin
	return floor(random() * (h-l+1) + l)::int;
end
$$ language plpgsql;
select gera_id()
--SELECT floor(random() * (h-l+1) + l)::int;


create or replace function numero(digitos integer) returns integer as
$$
   begin
      return trunc(random()*power(10,digitos))+1;
   end;
$$language plpgsql;
