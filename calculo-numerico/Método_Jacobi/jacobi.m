%-------------------------------------------------------------------------
% Método de Jacobi
%-------------------------------------------------------------------------
%Dados:
% Xº = chute inical
% tol = tolerancia 
% n_it = numero de iteraçoes
%
tol=0.00005;
n=5;
x=10;
%definir matriz

A(1,1) = -2.02;
A(1,2) = 1;
A(n,n) = -2.02;
A(n,n-1) = 1;

for i=2:n-1 %a primeira e ultima coluna sempre são definidas, o resto é genérico
  for j=i-1:i+1 %a cima e a baixo da diagonal
    if(i==j)
      A(i,j) = -2.02; %diagonal principal igual
    else
      A(i,j) = 1; %
    endif
  endfor
endfor

B(1)=1;
for i=2:n-1
  B(i)=0;
endfor
B(n-1)=1;
