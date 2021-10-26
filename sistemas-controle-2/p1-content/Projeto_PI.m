close all;
clear all;
clc;

%Planta
num=1.06;
den=conv([1 1 0],[1 2]);
G=tf(num,den);
%Planta sem compensação em malha fechada
Gmf=feedback(G,1);
%Polos de MF do sistema
pole(Gmf)

%Lugar das raizes de G(s)
figure;
rlocus(G);

%Lugar das raizes de Gc(s)G(s)para projeto do ganho Kp
numc=[1 0.05];
denc=[1 0];
Gcp=tf(numc,denc);
figure;
rlocus(G);
hold;
rlocus(Gcp*G);

%Compensador
numc=0.97*[1 0.05];
denc=[1 0];
Gc=tf(numc,denc);
%Planta com compensação em malha fechada
Gmfc=feedback(Gc*G,1);
%Polos de MF do sistema compensado
pole(Gmfc)

%Resposta ao degrau
figure;
step(Gmf);
hold;
step(Gmfc);
legend('Sem compensação', 'Com compensação');

%Lugar das raízes da planta e planta compensada
figure;
rlocus(G);
legend('G(s)');
figure;
rlocus(Gc*G);
legend('Gc(s)G(s)');

%Resposta a rampa
Gi=tf(1,[1 0]);
figure;
step(Gi,'b',50);
hold;
step(Gi*Gmf,'r',50);
step(Gi*Gmfc,'k',50);
title('Ramp Response');
legend('Referência','Gmf','Gmfc');

%Erros
[Yi t]=step(Gi,'b',linspace(0,200,2000));
[Y1 t]=step(Gi*Gmf,'r',linspace(0,200,2000));
[Y2 t]=step(Gi*Gmfc,'k',linspace(0,200,2000));

erro_MF=Yi-Y1;
erro_MF_C=Yi-Y2;

figure;
plot(t,erro_MF,'r');
hold;
plot(t,erro_MF_C,'b');
legend('Erro sem compensador','Erro com compensador');
xlabel('t (s)');
ylabel('Amplitude');