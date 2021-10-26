close all;
clear all;
clc;

%Planta
num=4;
den=[1 2 0];
G=tf(num,den)
%Planta sem compensação em malha fechada
Gmf=feedback(G,1)
%Polos de MF sem compensação
pole(Gmf)

%Lugar das raízes de G(s)
rlocus(G)

%Verificação da condição de ângulo
s=-2+j*2*sqrt(3);
angle(4/(s*(s+2)))*180/pi %O resultado é em rad que é convertido para graus.

%Cálculo de Kc
Kc=1/abs(4/(s*(s+4)))

%Compensador
numc=4*[1 2];
denc=[1 4];
Gc=tf(numc,denc)

%Planta com compensação em malha fechada
Gmfc=feedback(Gc*G,1);
Gmfc=minreal(Gmfc)
%Polos de MF com compensação
pole(Gmfc)

%Resposta ao degrau
figure;
step(Gmf);
hold;
step(Gmfc);
legend('Sem compensação', 'Com compensação');

%Lugar das raízes da planta compensada
figure;
rlocus(Gc*G);