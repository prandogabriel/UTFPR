CREATE TABLE Aluno(
  Nome VARCHAR(50) NOT NULL,
  RA DECIMAL(8) NOT NULL,
  DataNasc DATE NOT NULL,
  Idade DECIMAL(3),
  NomeMae VARCHAR(50) NOT NULL,
  Cidade VARCHAR(30),
  Estado CHAR(2),
  Curso VARCHAR(50),
  periodo integer
);
CREATE TABLE Discip(
  Sigla CHAR(7) NOT NULL,
  Nome VARCHAR(25) NOT NULL,
  SiglaPreReq CHAR(7),
  NNCred DECIMAL(2) NOT NULL,
  Monitor DECIMAL(8),
  Depto CHAR(8)
);
CREATE TABLE Matricula(
  RA DECIMAL(8) NOT NULL,
  Sigla CHAR(7) NOT NULL,
  Ano CHAR(4) NOT NULL,
  Semestre CHAR(1) NOT NULL,
  CodTurma DECIMAL(4) NOT NULL,
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
ADD CONSTRAINT "PK_Matricula" PRIMARY KEY (RA, Sigla);
--
-- table: Discip PK_Discip; Type: CONSTRAINT; Owner: postgres
--
ALTER TABLE ONLY Discip
ADD CONSTRAINT "PK_Discip" PRIMARY KEY (Sigla);
--
-- table: Aluno PK_Aluno; Type: CONSTRAINT; Owner: postgres
--
ALTER TABLE ONLY Aluno
ADD CONSTRAINT "PK_Aluno" PRIMARY KEY (RA);
--
------------------------------ RELACIONAMENTOS ------------------------------
--
--
-- Name: Matricula MatriculaRA; Type: FK CONSTRAINT; Owner: postgres
--
ALTER TABLE ONLY Matricula
ADD CONSTRAINT "MatriculaRA" FOREIGN KEY (RA) REFERENCES Aluno(RA) ON UPDATE CASCADE ON DELETE CASCADE;
--
-- Name: Matricula MatriculaSigla; Type: FK CONSTRAINT; Owner: postgres
--
ALTER TABLE ONLY Matricula
ADD CONSTRAINT "MatriculaSigla" FOREIGN KEY (Sigla) REFERENCES Discip(Sigla) ON UPDATE CASCADE ON DELETE CASCADE;
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
Create or replace function texto(tamanho integer) returns text as $$
declare chars text [] := '{0,1,2,3,4,5,6,7,8,9,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z}';
result text := '';
i integer := 0;
begin if tamanho < 0 then raise exception 'Tamanho dado nao pode ser menor que zero';
end if;
for i in 1..tamanho loop result := result || chars [1+random()*(array_length(chars, 1)-1)];
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
begin for i in 1..10 loop for j in 1..4 loop
INSERT INTO Aluno (
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
VALUES(
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
begin for i in 1..10 loop for j in 1..4 loop
INSERT INTO Discip (
    Sigla,
    Nome,
    SiglaPreReq,
    NNCred,
    Monitor,
    Depto
  )
VALUES(
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
DO $$
DECLARE r record;
DECLARE r2 record;
BEGIN FOR r IN
SELECT *
FROM aluno LOOP RAISE NOTICE '%',
  r;
for r2 in
select *
from discip LOOP
INSERT INTO Matricula
VALUES (
    r.RA,
    r2.Sigla,
    numero(4)::char,
    numero(1)::char,
    numero(2),
    numero(2),
    numero(2),
    numero(2),
    numero(2),
    numero(2)
  );
end loop;
END LOOP;
END;
$$;

---------------- questâo 2
select  count(*) from aluno;
CREATE UNIQUE INDEX IdxAlunoNNI ON Aluno (Nome, NomeMae, Idade);
analyze;
--- usando index
explain select * from aluno  where nome = 'aaaa';
--
--- não usando index
-- quando usado a expressão do index, juntamente com or o index não acessado
explain select * from aluno  where nome = 'aaaa' or NomeMae = 'bbb';
--
---------------- FIM questâo 2
---------------- questâo 3
--- a) Sequential Scan -> consulta em qualquer tabela sem index retorna esse plano
explain select  * from aluno  where cidade = 'aa';
--- b) Bitmap Index Scan -> criado para estado por possuir 4 variações
create extension btree_gin;
create index idxEstadoBitmap on aluno using gin (Estado);
analyze aluno;
explain select  * from aluno  where estado = 'SP';

--- c) Index Scan -> busca em chave primária ou diretamento em outro index
--- chave primária
explain select  * from aluno  where ra = 185423;
---  em cima de um index criado
CREATE INDEX IdxCidade ON Aluno (Cidade);
explain select  * from aluno  where Cidade = 'cidade';

--- d) Index-Only Scan -> busca em chave cima do index projetando somento a a coluna do index
--  index chave primeira primária
explain select ra from aluno where  ra = 123;
--- em cima do index criado anteriormente
explain select  Cidade from aluno  where Cidade = 'cidade';
--- e) Multi-Index Scan -> (TODO) FALTA COMPLETAR
explain select nome, ra from aluno  where Nome = 'nome' or ra = 123;
---------------- FIM questâo 3
---- Exercı́cio 4)  Faça consultas com junções entre as tabelas e mostre o desempenho criando-se ı́ndices para cada chave estrangeira.
CREATE INDEX IdxFkSigla ON Matricula (Sigla);
CREATE INDEX IdxFkRA ON Matricula (RA);

analyze;

--------- com o uso do index da FK, consulta  muito mais mai rápida  ---------
explain analyze select * from matricula m join aluno a on a.ra = m.ra where m.ra = 123;

--------- Sem o uso do index da FK, consulta demora até muito mais  ---------
explain analyze select * from matricula m join aluno a on a.ra = m.ra where m.codturma = 123;
-------------
--- Exercı́cio 5) Utilize um ı́ndice bitmap para perı́odo e mostre-o em uso nas consultas.
create index idxPeriodBitmap on aluno using gin (Periodo);
explain analyze select  * from aluno  where periodo = 4;

explain analyze select periodo from aluno  where periodo = 4 and curso = 'abc';
-------------
