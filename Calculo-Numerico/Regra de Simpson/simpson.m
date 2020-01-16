%          I N T E G R A Ç Ã O    N U M E R I C A    P E L A    R E G R A    D E    S I M P S O N   ! ! !
%                                    INTEGRAÇÃO F(X) = E^X NO INTERVALO [0,1]

% Xi = a+ih, onde i=0,1,2,3 ... n
% h = b-a/n
% som h/3 [f(x2i - 2) + 4f(x2i-1) + f(x2i)]

clc;
clear all;

a = 0; % intervalo a
b = 1; % intervalo b
n = 10; % numero de partições
% n = 100

h = (b - a) / n% h seria o tamanho de cada espaçamento

for i = 1:n
    x(i) = a + (i * h); % valor de cada espaçamento contido no vetor x
endfor

simpson = 0; % vai guardar o resultado das somatoria (soma)
simpson = simpson + (exp(a) + 4 * exp(x(1)) + exp(x(2))); % fazendo um laço fora

for i = 2:n / 2% i=2 pois tem um laço fora ... e vai até metade por causa dos numeros pares
    simpson = simpson + (exp(x(2 * i - 2)) + 4 * exp(x(2 * i - 1)) + exp(x(2 * i)));
endfor

disp("Comparando o resultado obtido pela regra de simpson e pela integral")
simpson = simpson * (h / 3)% concluindo o resultado fazendo soma * h/3 da formula ...
integral = e - 1% resolvendo por integral ... ficaria e^1 - e^0 ...

% comparando o metodo de simpson com a integral ...
% podemos perceber que quanto maior o numero de intervalos (n) tiver mais preciso sera pois estara dividindo a area em mais regioes ...

% END
