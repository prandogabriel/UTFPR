n=5;

for i=1:n
  for j=1:n
    A(i,j) = 1/(i+j-1);
  endfor
endfor

%matriz B
for i=1:n
  B(i,1) = 1/i;  
endfor

% Escalonar a matriz

for k=1:n-1 %coluna, pq a ultima não precisa "organizar"
  for i=k+1:n %linha começa em k+1 pq a primeira não precisa escalonar
    M(i,k)=A(i,k)/A(k,k); % definindo o multiplicador
    A(i,k)=0; %atribui 0 em baixo do pivo
    for j=k+1:n
      A(i,j)=A(i,j)-M(i,k)*A(k,j); %substitui o valor do multp, atribui os novos valores aos indices da matriz
    endfor
    B(i)=B(i)-M(i,k)*B(k); %arrumando os novos valors para a matriz b após escalonar
  endfor
endfor
A
B

for i=n:-1:1
  s = 0;
  for j=i+1:n
    s = s + A(i,j)*X(j);
  endfor
  X(i,1)=(B(i)-s)/A(i,i); %começa substituindo de baixo para cima
endfor
X