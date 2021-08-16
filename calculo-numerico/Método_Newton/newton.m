%Método de Newton para encontrar zero de funções
clc
clear

% xk+1 = xk - f(xk)/f'(xk) (formula de newton)
%x3+ 3x2−1 = 0, [−4,0]. (função a ser usada)
xk=2;

f=@(x) x^3+3*x^2-1;

i=1;
x(i)=2;
xk=2;
xk+1 = xk - f(xk)/(3*xk^2+6*x);
