create table access_log (
    client_ip inet,
	url varchar check (length(url) < 255)
);

do $$
begin
for i IN 1..5000 LOOP
insert into access_log values (gera_ip(), texto(5)|| '.html');
end loop;
end $$;

analyze access_log
table access_log


CREATE INDEX access_log_client_ip_ix ON access_log (client_ip)
WHERE NOT (client_ip > inet '192.168.100.0' AND
client_ip < inet '192.168.100.255');

explain
SELECT *
FROM access_log
WHERE url = '/index.html' AND client_ip = inet '212.78.10.32';

explain
SELECT *
FROM access_log
WHERE client_ip = inet '192.168.100.23';


CREATE INDEX access_log_client_ip_ix2 ON access_log (client_ip)
WHERE (client_ip > inet '192.168.100.0' AND
client_ip < inet '192.168.100.255');



--funcoes uteis
create function gera_IP() returns inet as
$$
begin
	return CONCAT(
	  TRUNC(RANDOM() * 250 + 2), '.' ,
	  TRUNC(RANDOM() * 250 + 2), '.',
	  TRUNC(RANDOM() * 250 + 2), '.',
	  TRUNC(RANDOM() * 250 + 2)
	)::INET;
end
$$ language plpgsql



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


create or replace function data() returns date as
$$
   begin
   return date(timestamp '1980-01-01 00:00:00' +
       random() * (timestamp '2020-07-18 00:00:00' -
                   timestamp '1980-01-01 00:00:00'));
   end;
$$language plpgsql;

set datestyle to "DMY,SQL"
select data()

