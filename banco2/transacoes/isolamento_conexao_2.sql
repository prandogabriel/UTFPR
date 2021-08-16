--read committed
begin;
set transaction isolation level read committed;
select * from cliente;
commit;


--repeatable read
begin;
set transaction isolation level repeatable read;
insert into cliente values (3,'Cliente1',4000);
commit;
rollback;

select * from cliente;


--serializable
begin;
set transaction isolation level serializable;
update cliente set renda = renda * 2 where id = 1;
commit;
rollback;


--read committed -- Suspensao
begin;
set transaction isolation level read committed;
update cliente set renda = renda * 2 where id = 1;
commit;

select * from cliente;


--read committed -- Deadlock
begin;
set transaction isolation level read committed;
update cliente set renda = renda * 2 where id = 2;
update cliente set renda = renda * 2 where id = 1;
commit;
rollback;
