
docker run -d --name p3-banco -e POSTGRES_PASSWORD=pass -p 5432:5432 postgres

-- ------------------------------------------- PROVA DE BANCO ---------------------------------------------------------------------


drop table Measure;
drop table Point;
drop table TypePoint;
drop table Source;
drop table Protocol;

CREATE TABLE Protocol (
id_protocol integer not null PRIMARY KEY,
desc_protocol varchar(50) not null,
format varchar(20)
);


CREATE TABLE TypePoint (
id_type integer not null PRIMARY KEY,
desc_type varchar(25) not null
);


CREATE TABLE Source (
id_source integer not null PRIMARY KEY,
boud_rate integer,
source_name varchar(50) not null,
id_protocol integer,
FOREIGN KEY(id_protocol) REFERENCES Protocol (id_protocol)
);


CREATE TABLE Point (
id_point integer not null PRIMARY KEY,
desc_point varchar(50) not null,
location varchar(50),
id_type integer not null,
id_source integer not null,
FOREIGN KEY(id_source) REFERENCES Source (id_source),
FOREIGN KEY(id_type) REFERENCES TypePoint (id_type)
);
commit;

CREATE TABLE Measure (
id_measure integer not null PRIMARY KEY,
measured_data integer not null,
time_collect date not null,
id_point integer not null,
FOREIGN KEY(id_point) REFERENCES Point (id_point)
);


insert into Protocol values (1,'Modbus','Serial');
insert into Protocol values (2,'Fieldbus','RTU');
insert into Protocol values (3,'POOR Field','assynchronous');
insert into Protocol values (4,'FieldLog','Parallel');
insert into Protocol values (5,'Logger','transactional');
insert into Protocol values (6,'HiLog','script');
insert into Protocol values (7,'RipHop','standard');

insert into TYPEPOINT values (1,'Integer');
insert into TYPEPOINT values (2,'Bynary');
insert into TYPEPOINT values (3,'Interval');
insert into TYPEPOINT values (4,'Boolean');
insert into TYPEPOINT values (5,'Char');
insert into TYPEPOINT values (6,'Float');
insert into TYPEPOINT values (7,'Numeric');

insert into SOURCE values (1, 1200, 'Assembly line',1);
insert into SOURCE values (2, 1300, 'Painting room',2);
insert into SOURCE values (3, 1500, 'Renewal room',1);
insert into SOURCE values (4, 1600, 'Engine production',3);
insert into SOURCE values (5, 1500, 'Ties planning',3);
insert into SOURCE values (6, 600, 'Shell manufacturing',4);
insert into SOURCE values (7, 800, 'Seat production',6);
insert into SOURCE values (8, 2000, 'Interior improvement',5);
insert into SOURCE values (9, 1800, 'Safety test',4);
insert into SOURCE values (10,1700, 'Safety design',4);
insert into SOURCE (ID_SOURCE, BOUD_RATE, SOURCE_NAME) values (11,1650, 'Safety planning');
insert into SOURCE (ID_SOURCE, BOUD_RATE, SOURCE_NAME) values (12,1555, 'Safety prooving');
insert into SOURCE (ID_SOURCE, BOUD_RATE, SOURCE_NAME) values (13,1300, 'Safety ensurance');

insert into POINT values (1, 'Pressure sensor','floor',2,3);
insert into POINT values (2, 'Start drawing','machine',4,2);
insert into POINT values (3, 'Light sensor','office',3,3);
insert into POINT values (4, 'Temperature sensor','floor',4,1);
insert into POINT values (5, 'Stop production','conveyor',4,3);
insert into POINT values (6, 'Flow sensor','floor',7,5);
insert into POINT values (7, 'Start drilling','machine',6,6);
insert into POINT values (8, 'Distinction sensor','conveyor',7,6);
insert into POINT values (9, 'Humidity','floor',5,10);
insert into POINT values (10, 'Cognition Sensor','machine',5,8);


insert into MEASURE values (1, 55,'02/03/2021',1);
insert into MEASURE values (2, 1,'02/03/2021',2);
insert into MEASURE values (3, 56,'03/03/2021',1);
insert into MEASURE values (4, 5455,'03/03/2021',3);
insert into MEASURE values (5, 30,'03/03/2021',4);
insert into MEASURE values (6, 32,'03/03/2021',4);
insert into MEASURE values (7, 60,'04/03/2021',1);
insert into MEASURE values (8, 34,'04/03/2021',4);
insert into MEASURE values (9, 35,'04/03/2021',4);
insert into MEASURE values (10,13,'04/03/2021',8);
insert into MEASURE values (11, 433,'05/03/2021',5);
insert into MEASURE values (12, 133,'05/03/2021',6);
insert into MEASURE values (13, 456,'05/03/2021',6);
insert into MEASURE values (14, 55,'05/03/2021',6);
insert into MEASURE values (15, 30,'05/03/2021',7);
insert into MEASURE values (16, 32,'05/03/2021',7);
insert into MEASURE values (17, 60,'06/03/2021',9);
insert into MEASURE values (18, 34,'06/03/2021',9);
insert into MEASURE values (19, 35,'06/03/2021',9);
insert into MEASURE values (20, 44,'06/03/2021',9);

--
--1) (1.5 pt) Retorne o ID (dê o nome ID FONTE) e a descrição (dê o nome FONTE) de todas as fontes de
--dados da fábrica. Caso a fonte esteja relacionada a um determinado protocolo, mostre também o
--nome desse protocolo e o seu formato. Ordene o resultado alfabeticamente pelo nome da fonte.
--Entregue 2 respostas: uma usando JOIN e a outra usando a operação de produto cartesiano.

--  trazendo os dados a esquerda, mesmo que o protocol seja nullo
select id_source as id_fonte, source_name as descricao_fonte, p.desc_protocol as nome_protocolo, p.format as formato_protocolo from "source" s
left join protocol p on p.id_protocol = s.id_protocol 
order by s.source_name asc;

--  trazendo somente os dados que possuem um protocolo
select id_source as id_fonte, source_name as descricao_fonte, p.desc_protocol as nome_protocolo, p.format as formato_protocolo from "source" s
join protocol p on p.id_protocol = s.id_protocol 
order by s.source_name asc;

--  produto cartesiano trazendo somente os dados que possuem um protocolo
select s.id_source as id_fonte, s.source_name as descricao_fonte, p.desc_protocol as nome_protocolo, p.format as formato_protocolo from "source" s, protocol p 
where s.id_protocol = p.id_protocol
order by s.source_name asc;




--2) (2 pts) Faça uma consulta que retorne 3 valores médios:
--(i) a média da temperatura (ponto de dados 4);
--(ii) a média da luminosidade (ponto de dados 8); e
--(iii) a média de humidade (ponto de dados 9).
--A consulta deve incidir sobre o período de 02 a 05 de março de 2021. Junto aos cálculos da função,
--identifique de que se trata o valor, ou seja, a descrição do ponto e o tipo do ponto medido. Finalmente,
--implemente uma restrição para garantir que só sejam apresentados resultados cujo retorno de função
--seja maior do que 20.



select avg(measured_data), p.desc_point as descricao_ponto, t.desc_type as tipo_ponto, 
(select avg(measured_data) from measure m where time_collect between '2021-03-02' and '2021-03-05' and m.id_point=4) as media 
from  measure m
join point p on p.id_point = m.id_point and m.id_point=4
join typepoint t on t.id_type = p.id_type 
where time_collect between '2021-03-02' and '2021-03-05'
group by p.desc_point , t.desc_type
having avg(measured_data) > 20

union 


select avg(measured_data), p.desc_point as descricao_ponto, t.desc_type as tipo_ponto, 
(select avg(measured_data) from measure m where time_collect between '2021-03-02' and '2021-03-05' and m.id_point=8) as media 
from  measure m
join point p on p.id_point = m.id_point and m.id_point=8
join typepoint t on t.id_type = p.id_type 
where time_collect between '2021-03-02' and '2021-03-05'
group by p.desc_point , t.desc_type
having avg(measured_data) > 20

union 

select avg(measured_data), p.desc_point as descricao_ponto, t.desc_type as tipo_ponto, 
(select avg(measured_data) from measure m where time_collect between '2021-03-02' and '2021-03-05' and m.id_point=9) as media 
from  measure m
join point p on p.id_point = m.id_point and m.id_point=9
join typepoint t on t.id_type = p.id_type 
where time_collect between '2021-03-02' and '2021-03-05'
group by p.desc_point , t.desc_type
having avg(measured_data) > 20




--3) (2 pts) Faça uma consulta que retorne: média, mínima, máxima e quantidade de coletas para o
--índice de temperatura (ponto 4) do setor de montagem (fonte 1) da pequena fábrica. Complemente
--com uma descrição (DADOS DE) junto ao nome do ponto, como mostra o exemplo de saída abaixo.
select 'Dados de ' || p.desc_point as descricao, avg(measured_data) as media, max(measured_data) as maxima, min(measured_data) as minima, count(*) as quantidade from measure m 
join point p on p.id_point = m.id_point
where m.id_point=4 and p.id_source=1
group by p.desc_point




--4) (1.5 pt) Implemente uma consulta que retorne quantas vezes cada protocolo já foi utilizado para
--configurar pontos de dados da pequena fábrica. Ordene por aqueles que mais foram utilizados.
--Juntamente, informe o nome do protocolo. Caso um protocolo nunca tenha sido utilizado, ainda
--assim ele deve constar na lista, mesmo que a contagem seja 0.

select p.desc_protocol as nome_protocolo, count(s.id_protocol) as quantidade
from protocol p 
left join "source" s on s.id_protocol=p.id_protocol
group by nome_protocolo
order by 2 desc

--5) (3.0 pts) Implemente uma consulta que retorne o nome de cada ponto de dados, acompanhado da
--quantidade total de medições já obtidas desse ponto e do valor médio histórico dessas medições.
--Desse total histórico de medições do ponto, informe quantas delas foram feitas no dia D (escolha um
--dia). Maiores detalhes:
-- A consulta deve retornar, para cada ponto:
--o 2 linhas: uma com os dados históricos e outra com os dados do dia D, caso haja;
--o 1 linha: somente com os dados históricos, caso não haja dados do dia D.
-- Mesmo que um ponto não tenha medição registrada (nem no dia D e nem histórica), mostre sua
--descrição, ainda que as funções retornem 0.
-- Adicione uma descrição constante para uma coluna chamada “Tipo_Medição”. Sua função é
--identificar o perfil de cada linha, ou seja, se é dado histórico ou se é dado do dia D.
-- Ordene primeiro pelo nome do ponto e, depois, pela quantidade DESCENDENTE de medições.
--Exemplo de retorno:


select desc_point as nome_ponto, count(p.id_point) as qtd, avg(m.measured_data) as media, 'historico' as periodo 
from point p
join measure m on m.id_point = p.id_point 
group by nome_ponto
union
select desc_point as nome_ponto, count(p.id_point) as qtd, avg(m.measured_data) as media, 'dia'
from point p
join measure m on m.id_point = p.id_point 
where m.time_collect='2021-03-03'
group by nome_ponto
order by nome_ponto

