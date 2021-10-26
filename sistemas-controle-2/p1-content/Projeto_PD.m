close all;
clear all;
clc;

%Planta
num=4;
den=[1 2 0];
G=tf(num,den);
%Planta sem compensação em malha fechada
Gmf=feedback(G,1);
%Polos de MF sem compensação
pole(Gmf)
%Lugar das raízes de G(s)
rlocus(G);

%Cálculo de Kp
s=-2+j*2*sqrt(3);
Kp=1/abs((0.125*(s+8)*4)/(s*(s+2)))

%Compensador
numc=0.5*[1 8];
denc=1;
Gc=tf(numc,denc)
%Planta com compensação em malha fechada
Gmfc=feedback(Gc*G,1);
%Polos de MF com compensação
pole(Gmfc)

%Resposta ao degrau
figure;
step(Gmf);
hold;
step(Gmfc);
legend('Sem compensação','PD');

%Lugar das raízes do sistema compensado
figure;
rlocus(Gc*G)

%Controlador PD com filtro passa-baixas (avanço)
numc2=200*0.5*[1 8];
denc2=[1 200];
Gc2=tf(numc2,denc2)

%Planta com compensação PD como filtro passa-baixas em malha fechada
Gmfc2=feedback(Gc2*G,1);

%Polos de MF do sistema compensado por PD+filtro passa-baixas (avanço)
pole(Gmfc2)

%Comparação da compensação PD vs PD+filtro passa-baixas (avanço)
figure;
step(Gmfc,linspace(0,6,1000));
hold;
step(Gmfc2,linspace(0,6,1000));
legend('PD','Avanço');

%Comparação entre a resposta em frequência do PD vs PD+filtro passa-baixas
%(avanço)
figure;
bode(Gc);
hold;
bode(Gc2);
legend('PD','Avanço');