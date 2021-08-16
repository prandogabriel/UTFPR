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
  NotaP1 NUMERIC(3, 1),
  NotaP2 NUMERIC(3, 1),
  NotaTrab NUMERIC(3, 1),
  NotaFIM NUMERIC(3, 1),
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
create or replace function numero(digitos integer) returns integer as
$$
   begin
      return trunc(random()*power(10,digitos));
   end;
$$language plpgsql;
--select numero(3);

--Data aleatoria (indicar periodo na funcao se quiser alterar)
create or replace function data() returns date as
$$
   begin
   return date(timestamp '1980-01-01 00:00:00' +
       random() * (timestamp '2017-01-30 00:00:00' -
                   timestamp '1990-01-01 00:00:00'));
   end;
$$language plpgsql;
--select data()

--Texto aleatorio
Create or replace function texto(tamanho integer) returns text as
$$
declare
  chars text[] := '{0,1,2,3,4,5,6,7,8,9,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z}';
  result text := '';
  i integer := 0;
begin
  if tamanho < 0 then
    raise exception 'Tamanho dado nao pode ser menor que zero';
  end if;
  for i in 1..tamanho loop
    result := result || chars[1+random()*(array_length(chars, 1)-1)];
  end loop;
  return result;
end;
$$ language plpgsql;

--select texto(5)


------
--------------------
------
do $$
declare varEstado varchar [] = '{"PR", "SC", "RS", "SP"}';
begin for i in 1..1000000 loop for j in 1..4 loop
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
  numero(60),
  texto(20),
  texto(20),
  varEstado [j],
  texto(20),
  numero(10)
  ) on conflict do nothing;;

end loop;
end loop;
end;
$$ language plpgsql;
