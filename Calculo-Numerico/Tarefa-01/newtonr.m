%códigos disponiveis no github. "github.com/gprando/CN_2019-2"
%Algoritmo para calcular raiz pelo metodo de Newton
% a função tem 2 raizes, com o chute incial igual a 1 o valor encontrado é :2.9630
% com o chute inicial igual a -1, encontou a outra raiz = -0.90613
%------------------------------------------------------------
% para executar rode o comando a baixo na janela de comandos
%------------------------------------------------------------
%newtonr('x^4-3*x^3 + x - 2' ,'4*x^3-9*x^2+1',1,0.0001,1000)
%newtonr('x^4-3*x^3 + x - 2' ,'4*x^3-9*x^2+1',-1,0.0001,1000)

function Raiz = newtonr(funcao,derivada,x0,Toler,IterMax);


x = x0;
Fx = eval(funcao);
DFx = eval(derivada);
Iter = 0;
disp('interação');
disp(Iter);
disp('x');
disp(x);
disp('Valor na Função derivada');
disp(DFx);
disp('Valor na Função ');
disp(Fx);

while (1)
    DeltaX = -Fx/DFx;
    x = x+DeltaX;
    Fx = eval(funcao);
    DFx = eval(derivada);
    Iter = Iter+1;
    disp('interação');
    disp(Iter);
    disp('x');
    disp(x);
    disp('Valor na Função derivada');
    disp(DFx);
    disp('Valor na Função ');
    disp(Fx);
    disp('Valor DeltaX ');
    disp(DeltaX);    
    if(abs(DeltaX)<Toler && abs(Fx)<Toler)||abs(DFx)==0 || Iter>= IterMax
        break;
    end
    
end
Raiz = x;



    
    