% Método da Bissecção para encontrar Zero de Funções
clear
clc

a=1;
b=2;

f=@(x) x^2-2;

tol=0.0005;
%tol = 5*10^-4
nit = 1000;
for k=1 : nit
  x(k)=(a+b)/2;
  if f(a)*f(x(k))<0
    b=x(k);
  else
    a=x(k);
  endif
  if abs(b-a)<tol
    break
  endif
endfor