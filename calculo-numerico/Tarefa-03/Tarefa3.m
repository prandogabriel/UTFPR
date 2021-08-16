% minimos quadrados com integração por trapézios
clear
clc

f=@(x) x;
L=pi;
a=-L;   
b=L;     
n=100;     
m=500;     
h=(b-a)/m;  
x=linspace(a,b,m+1);     
                    
for i=1:2*n+1      
    for j=1:m+1
        if i<=(n+1)
            g(i,j)=cos((i-1)*pi*x(j)/L);
        else
            g(i,j)=sin((i-n-1)*pi*x(j)/L);
        end
    end
end
A=zeros(2*n+1);     
for i=1:2*n+1
    for j=1:2*n+1
        for k=1:m
            A(i,j)=A(i,j)+(g(i,k)*g(j,k)+g(i,k+1)*g(j,k+1));  
        end
    end
end
A=A.*h/2;    
B=zeros(2*n+1,1);    
for i=1:2*n+1
    for k=1:m
        B(i)=B(i)+f(x(k))*g(i,k)+f(x(k+1))*g(i,k+1);
    end
end
B=B.*h/2;   
Alpha=A\B;  

W=zeros(m+1,1);
for i=1:2*n+1  
    for j=1:m+1  
        W(j)=W(j)+g(i,j)*Alpha(i);  
    end
end
plot (x,f(x),x,W,'r')