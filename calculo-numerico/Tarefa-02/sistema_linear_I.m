n=5;

%definir matriz

A(1,1) = -2.02;
A(1,2) = 1;
A(n,n) = -2.02;
A(n,n-1) = 1;
%matriz 
for i=2:n-1 %a primeira e ultima coluna sempre são definidas, o resto é genérico
  for j=i-1:i+1 %a cima e a baixo da diagonal
    if(i==j)
      A(i,j) = -2.02; %diagonal principal igual
    else
      A(i,j) = 1; %
    endif
  endfor
endfor
A


B(1)=1;
B(2)=0;
B(3)=0;
B(4)=0;
B(5)=1;

% Escalonar a matriz

for k=1:n-1 
  for i=k+1:n
    M(i,k)=A(i,k)/A(k,k);
    A(i,k)=0;
    for j=k+1:n
      A(i,j)=A(i,j)-M(i,k)*A(k,j);
    endfor
    B(i)=B(i)-M(i,k)*B(k);
  endfor
endfor
A
B

for i=n:-1:1
  s = 0;
  for j=i+1:n
    s = s + A(i,j)*X(j);
  endfor
  X(i,1)=(B(i)-s)/A(i,i);
endfor
X