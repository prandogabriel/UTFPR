
% considere 0 <= x <= 5
% y'(x) = (1/(1+x^2) - 2y^2
% y(0) = 0

% Sol Exata: y(x) = x/(1+x^2)

% ---------------------------------------
clc;
clear all;
close all;

f=@(x,y) ((1/(1+x^2)) - 2*(y^2));

h = 0.01;
a=0;
b=5;
N = (b-a)/h;
x(1) = 0; % x_0 ... intervalo 
y(1) = 0; % y_0
z(1) = 0; % valor de y_0 ... armazenar

for k=1 : (N+1)
  x(k+1) = x(k) + h;
  z(k+1) = z(k) + h*f(x(k),z(k)); % euler
  u(k+1) = y(k) + h*f(x(k),y(k));
  y(k+1) = y(k) + h/2 * (f(x(k),y(k)) + f(x(k+1), u(k+1))); % euler modificado
endfor

t = a : 0.01 : b;
sol_exata = t./(1+t.^2);
plot(x,y,'g*', x, z, 'm*', t, sol_exata, 'b')
title('Implementação Método de Euler e Runge-Kutta 2nd Order')
legend('Euler Modificado','Euler','Solucao Exata')
grid on

% podemos concluir pela analise do grafico que o euler modificado apresenta uma aproximação mais exata comparado com o euler ...
