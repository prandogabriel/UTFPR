-- Gabriel Prando, RA: 2039869
-- Prova 1 Banco de dados 2, semestre 2021.1
-- Professor Ives Pola
-- código disponível também em -> https://github.com/gprando/UTFPR/tree/master/banco-de-dados-2/prova-1
-- Para o desenvolvimento, foi utilizado docker para rodar o banco e postgres na versão  13.4 (LTS) sem nenhuma configuração extra do banco

create table Aluno(
  Nome VARCHAR(50) not null,
  RA DECIMAL(8) not null,
  DataNasc DATE not null,
  Idade DECIMAL(3),
  NomeMae VARCHAR(50) not null,
  Cidade VARCHAR(30),
  Estado CHAR(2),
  Curso VARCHAR(50),
  periodo integer
);
create table Discip(
  Sigla CHAR(7) not null,
  Nome VARCHAR(25) not null,
  SiglaPreReq CHAR(7),
  NNCred DECIMAL(2) not null,
  Monitor DECIMAL(8),
  Depto CHAR(8)
);
create table Matricula(
  RA DECIMAL(8) not null,
  Sigla CHAR(7) not null,
  Ano CHAR(4) not null,
  Semestre CHAR(1) not null,
  CodTurma DECIMAL(4) not null,
  NotaP1 DECIMAL(3),
  NotaP2 DECIMAL(3),
  NotaTrab DECIMAL(3),
  NotaFIM DECIMAL(3),
  Frequencia DECIMAL(3)
);
--
-- table: Matricula PK_Matricula; Type: CONSTRAINT; Owner: postgres
--
ALTER TABLE ONLY Matricula
add constraint "PK_Matricula" primary key (RA, Sigla);
--
-- table: Discip PK_Discip; Type: CONSTRAINT; Owner: postgres
--
ALTER TABLE ONLY Discip
add constraint "PK_Discip" primary key (Sigla);
--
-- table: Aluno PK_Aluno; Type: CONSTRAINT; Owner: postgres
--
ALTER TABLE ONLY Aluno
add constraint "PK_Aluno" primary key (RA);
--
------------------------------ RELACIONAMENTOS ------------------------------
--
--
-- Name: Matricula MatriculaRA; Type: FK CONSTRAINT; Owner: postgres
--
ALTER TABLE ONLY Matricula
add constraint "MatriculaRA" foreign key (RA) references Aluno(RA) on
update cascade on
	delete cascade;
--
-- Name: Matricula MatriculaSigla; Type: FK CONSTRAINT; Owner: postgres
--
ALTER TABLE ONLY Matricula
add constraint "MatriculaSigla" foreign key (Sigla) references Discip(Sigla) on
update cascade on
	delete cascade;
------
-------------------- Funções Uteis ---------------------
------
--Numero aleatorio (Quantidade max de digitos por parametro)
create or replace function numero(digitos integer) returns integer as $$ begin return trunc(random() * power(10, digitos));
end;
$$language plpgsql;
--select numero(3);
--Data aleatoria (indicar periodo na funcao se quiser alterar)
create or replace function data() returns date as $$ begin return date(
    timestamp '1980-01-01 00:00:00' + random() * (
      timestamp '2017-01-30 00:00:00' - timestamp '1990-01-01 00:00:00'
    )
  );
end;
$$language plpgsql;
--select data()
--Texto aleatorio
create or replace function texto(tamanho integer) returns text as $$
declare chars text [] := '{0,1,2,3,4,5,6,7,8,9,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z}';
result text := '';
i integer := 0;
begin if tamanho < 0 then raise exception 'Tamanho dado nao pode ser menor que zero';
end if;
for i in 1..tamanho loop result := result || chars [1 + random()*(array_length(chars, 1)-1)];
end loop;
return result;
end;
$$ language plpgsql;
--select texto(5)
------
-------------- Popular Aluno
------
do $$
declare varEstado varchar [] = '{"PR", "SC", "RS", "SP"}';
begin for i in 1..10000 loop for j in 1..4 loop
insert into Aluno (
    Nome,
    RA,
    DataNasc,
    Idade,
    NomeMae,
    Cidade,
    Estado,
    Curso,
    periodo
  )
values(
    texto(20),
    numero(8),
    data(),
    numero(2),
    texto(20),
    texto(20),
    varEstado [j],
    texto(5),
    numero(1)
  ) on conflict do nothing;
end loop;
end loop;
end;
$$ language plpgsql;
analyze Aluno;
------
-------------- Popular Discip
------
do $$
declare varDepartamento varchar [] = '{"DAEELE", "DAINF", "DAMAT", "DALET"}';
begin for i in 1..10000 loop for j in 1..4 loop
insert into Discip (
    Sigla,
    Nome,
    SiglaPreReq,
    NNCred,
    Monitor,
    Depto
  )
values(
    texto(7),
    texto(20),
    texto(7),
    numero(1),
    numero(8),
    varDepartamento [j]
  ) on conflict do nothing;
end loop;
end loop;
end;
$$ language plpgsql;
analyze Discip;
------
-------------- Popular Matricula
------
do $$
declare varRA int;
declare varSigla char(20);
begin for i in 1..1500 loop
select ra into varRA
from aluno
order by random()
limit 1;
select Sigla into varSigla
from Discip
order by random()
limit 1;
insert into Matricula
values (
    varRA,
    varSigla,
    numero(4)::char,
    numero(1)::char,
    numero(2),
    numero(2),
    numero(2),
    numero(2),
    numero(2),
    numero(2)
  ) on conflict do nothing;
end loop;
end;
$$ language plpgsql;
analyze Matricula;
---------------- questâo 2
select count(*)
from aluno;
create unique index IdxAlunoNNI on Aluno (Nome, NomeMae, Idade);
analyze;
--- usando index
explain
select *
from aluno
where nome = 'aaaa';
--
--- não usando index
-- quando usado a expressão do index, juntamente com or o index não acessado
explain
select *
from aluno
where nome = 'aaaa'
  or NomeMae = 'bbb';
--
---------------- FIM questâo 2
---------------- questâo 3
--  a Sequential Scan->consulta em qualquer tabela sem index retorna esse plano
explain select *
from aluno
where cidade = 'aa';
--  b Bitmap Index Scan->criado para estado por possuir 4 variações create extension btree_gin;
create extension btree_gin;
create index idxEstadoBitmap on aluno using gin (Estado);
analyze aluno;
explain
select *
from aluno
where estado = 'SP';
--  c Index Scan->busca em chave primária ou diretamento em outro index --- chave primária
explain analyze
select *
from aluno
where ra = 185423;
---  em cima de um index criado
create index IdxCidade on Aluno (Cidade);
explain
select *
from aluno
where Cidade = 'cidade';
--  d Index - Only Scan->busca em chave cima do index projetando somento a a coluna do index --  index chave primeira primária
explain analyze
select ra
from aluno
where ra = 123;
--- em cima do index criado anteriormente
explain analyze
select Cidade
from aluno
where Cidade = 'cidade';
--  e Multi - Index Scan
explain analyze
select nome,
  ra
from aluno
where Nome = 'nome'
  and ra = 123;
---------------- FIM questâo 3
---- Exercı́cio 4)  Faça consultas com junções entre as tabelas e mostre o desempenho criando-se ı́ndices para cada chave estrangeira.
create index IdxFkSigla on Matricula (Sigla);
create index IdxFkRA on Matricula (RA);
analyze;
--------- com o uso do index da FK, consulta  muito mais mai rápida  ---------
explain analyze
select *
from matricula m
  join aluno a on a.ra = m.ra
where m.ra = 123;
--Nested Loop  (cost=0.57..16.61 rows=1 width=168) (actual time=0.016..0.017 rows=0 loops=1)
--  ->  Index Scan using idxfkra on matricula m  (cost=0.28..8.29 rows=1 width=46) (actual time=0.015..0.016 rows=0 loops=1)
--        Index Cond: (ra = '123'::numeric)
--  ->  Index Scan using "PK_Aluno" on aluno a  (cost=0.29..8.31 rows=1 width=122) (never executed)
--        Index Cond: (ra = '123'::numeric)
--Planning Time: 0.185 ms
--Execution Time: 0.043 ms
--------- Sem o uso do index da FK, consulta demora muito mais  ---------
explain analyze
select *
from matricula m
  join aluno a on a.ra = m.ra
where m.codturma = 123;
--Nested Loop  (cost=0.29..43.06 rows=1 width=168) (actual time=0.289..0.290 rows=0 loops=1)
--  ->  Seq Scan on matricula m  (cost=0.00..34.75 rows=1 width=46) (actual time=0.289..0.289 rows=0 loops=1)
--        Filter: (codturma = '123'::numeric)
--        Rows Removed by Filter: 1500
--  ->  Index Scan using "PK_Aluno" on aluno a  (cost=0.29..8.31 rows=1 width=122) (never executed)
--        Index Cond: (ra = m.ra)
--Planning Time: 0.399 ms
--Execution Time: 0.313 ms
-------------
--- Exercı́cio 5) Utilize um ı́ndice bitmap para perı́odo e mostre-o em uso nas consultas.
create index idxPeriodBitmap on aluno using gin (Periodo);
explain analyze
select *
from aluno
where periodo = 4;
explain analyze
select periodo
from aluno
where periodo = 4
  and curso = 'abc';
-------------
--Exercı́cio 6) Compare na prática o custo de executar uma consulta com e sem um ı́ndice clusterizado
--na tabela aluno. Ou seja, faça uma consulta sobre algum dado indexado, clusterize a tabela naquele
--ı́ndice e refaça a consulta. Mostre os comandos e os resultados do explain analyze.
explain analyze
select *
from aluno
where nome = 'aaaa';
--Index Scan using idxalunonni on aluno  (cost=0.42..8.44 rows=1 width=146) (actual time=0.016..0.016 rows=0 loops=1)
--  Index Cond: ((nome)::text = 'aaaa'::text)
--Planning Time: 0.370 ms
--Execution Time: 0.036 ms
alter table Aluno cluster on idxalunonni;
cluster Aluno;
analyze aluno;
--
explain analyze
select *
from aluno
where nome = 'aaaa';
-- Index Scan using idxalunonni on aluno  (cost=0.42..8.44 rows=1 width=146) (actual time=0.018..0.018 rows=0 loops=1)
--  Index Cond: ((nome)::text = 'aaaa'::text)
--Planning Time: 0.353 ms
--Execution Time: 0.030 ms
--
--
-------------
--- Exercı́cio 7) Acrescente um campo adicional na tabela de Aluno, chamado de informacoesExtras, do
--tipo JSON. Insira dados diferentes telefônicos e de times de futebol que o aluno torce para cada aluno
--neste JSON. Crie ı́ndices para o JSON e mostre consultas que o utiliza (explain analyze). Exemplo:
--retorne os alunos que torcem para o Internacional.
--------- Adicionar coluna JSON
alter table Aluno
add informacoesExtras jsonb;
--
-------- update aluno com jsons
do $$
declare varRA int;
declare varTimes varchar [] = '{"internacional", "são paulo", "grêmio", "palmeiras"}';
begin for i in 1..250 loop for j in 1..4 loop
select ra into varRA
from aluno
order by random()
limit 1;
update Aluno
set informacoesExtras = (
    (
      '
{
"telefone" : ' || numero(9) || ',
"time" : "' || varTimes [j] || '"
}'
    )::json
  )
where RA = varRA;
end loop;
end loop;
end;
$$ language plpgsql;
analyze aluno;
--
-- irá fazer um seq scan
explain analyze
select informacoesextras
from aluno
where informacoesextras->>'time' = 'grêmio';
create index idxTimeJson on Aluno using BTREE ((informacoesextras->>'time'));
--
-- irá utilizar o index idxTimeJson
explain analyze
select informacoesextras
from aluno
where informacoesextras->>'time' = 'grêmio';
create index idxTimeInter on Aluno using BTREE ((informacoesextras->>'time'))
where informacoesextras->>'time' = 'internacional';
--
-- irá utilizar o index idxTimeInter primeiramente e após o idxTimeJson em um bitmap heap scan
explain analyze
select informacoesextras
from aluno
where informacoesextras->>'time' = 'internacional';
