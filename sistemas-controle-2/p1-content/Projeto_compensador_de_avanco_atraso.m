close all;
clear all;
clc;

%Planta
num=4;
den=[1 0.5 0];
G=tf(num,den);
%Planta sem compensação em malha fechada
Gmf=feedback(G,1)
%Polos de malha fechada sem compensação
pole(Gmf)

%Compensador de avanço
Gav=tf(6.26*[1 0.5],[1 5.02]);
%Planta compensada com avanço
Gmfcav=feedback(Gav*G,1);
Gmfcav=minreal(Gmfcav)
%Polos de malha fechada com compensação de avanço
pole(Gmfcav)
%Resposta ao degrau
step(Gmfcav,10);
legend('Com compensação de avanço');


%Compensador
numc=6.26*conv([1 0.5],[1 0.2]);
denc=conv([1 5.02],[1 0.01247]);
Gc=tf(numc,denc);
%Planta com compensação em malha fechada
Gmfc=feedback(Gc*G,1);
Gmfc=minreal(Gmfc)
zero(Gmfc)
pole(Gmfc)

%Resposta ao degrau
figure;
step(Gmf,30);
hold;
step(Gmfc,30);
legend('Sem compensação', 'Com compensação');

%Resposta a rampa
Gi=tf(1,[1 0]);
figure;
step(Gi,'b',15);
hold;
step(Gi*Gmf,'r',15);
step(Gi*Gmfc,'k',15);
legend('Referência','Sem compensação', 'Com compensação');
title('Ramp Response');

%Erros
[Yi t]=step(Gi,'b',linspace(0,50,1000));
[Y1 t]=step(Gi*Gmf,'r',linspace(0,50,1000));
[Y2 t]=step(Gi*Gmfc,'k',linspace(0,50,1000));
erro_MF=Yi-Y1;
erro_MF_C=Yi-Y2;

figure;
plot(t,erro_MF,'r');
hold;
plot(t,erro_MF_C,'b');
legend('Erro sem compensador','Erro com compensador');
xlabel('t(s)');
ylabel('Erro');

%Lugar das raízes da planta e planta compensada
figure;
rlocus(G);
legend('G(s)');
figure;
rlocus(Gc*G);
legend('Gc(s)G(s)');


