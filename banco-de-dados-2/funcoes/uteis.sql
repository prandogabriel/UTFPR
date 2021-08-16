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
