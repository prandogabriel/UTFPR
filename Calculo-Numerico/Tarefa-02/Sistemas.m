clear
clc

n=5;
for i=1:n
    for j=1:n
        A(i,j)=1/(i+j-1);
    end
    B(i)=1/i;
end
A
B = B(:)
y=A\B
for k=1:(n-1)
    for i=(k+1):n
        aux = A(i,k)/A(k,k);
        B(i) = B(i)-aux*B(k);
        for j=k:n
            if j==k
                A(i,j)=0;
            else
                A(i,j)=A(i,j)-aux*A(k,j);
            end
        end
    end
end
A
C=B;
for i=n:-1:1
    for j=n:-1:i
        
        if j>i
            C(i)=C(i)-A(i,j)*C(j);
        else
            
            C(i)=C(i)/A(i,j);
        end
    end
end
C