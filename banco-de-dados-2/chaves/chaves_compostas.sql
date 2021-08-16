create table tab (campo1 varchar, campo2 integer,
campo3 numeric, constraint chave primary key
(campo1,campo2));


do $$
begin
for i in 1..6000 loop
insert into tab values (texto(3), i, i*3.3);
end loop;
end
$$ language plpgsql


explain select * from tab where campo1 = 'aaa' and campo2 = 1;

explain select * from tab where campo2 = 1;

explain select * from tab where campo1 = 'aaa';
