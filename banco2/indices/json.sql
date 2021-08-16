create table students (info jsonb);


do $$
declare
vartype varchar[] = '{"quiz", "exam", "homework"}';
begin
for i in 1..1000000 loop
for j in 1..3 loop
insert into students values (('
{
"student" : ' || i || ',
"type" : "' || vartype[j] || '",
"score" : ' || round(random()*100) || '
}')::json);
end loop;
end loop;
end; $$
language plpgsql;


explain analyze
Select info
FROM students
where info->>'type' = 'quiz'

create index idxtype on students
using BTREE ((info->>'type'));

explain analyze
Select *
FROM students
WHERE info @> '{"type": "quiz"}';

create index idxJSON on students using GIN (info);

Select info->>'student'
FROM students
where info->>'score' = '10'
group by info->>'student'
explain analyze

Select *
FROM students
WHERE info @> '{"student": 3212}' and info->>'type' = 'exam';

explain analyze
Select info->>'score'
FROM students
WHERE info->>'student' = '3212'
