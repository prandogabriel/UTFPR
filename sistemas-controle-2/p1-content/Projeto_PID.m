close all;
clear all;
clc;

%Planta
num=4;
den=[1 0.5 0];
G=tf(num,den);
%Planta sem compensação em malha fechada
Gmf=feedback(G,1);

%Compensador
numc=1.13*[1 5.73 1.106];
denc=[1 0];
Gc=tf(numc,denc);
%Planta com compensação em malha fechada
Gmfc=feedback(Gc*G,1);
%Polos de MF do sistema compensado
pole(Gmfc)

%Resposta ao degrau
figure;
step(Gmf,30);
hold;
step(Gmfc,30);
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
step(Gi,'b',15);
hold;
step(Gi*Gmf,'r',15);
step(Gi*Gmfc,'k',15);
title('Ramp Response');
legend('Referência','Gmf','Gmfc');

%Erros
[Yi t]=step(Gi,'b',linspace(0,50,1000));
[Y1 t]=step(Gi*Gmf,'r',linspace(0,50,1000));
[Y2 t]=step(Gi*Gmfc,'k',linspace(0,50,1000));

erro_MF=Yi-Y1;
erro_MF_C=Yi-Y2;

%Exibe erros
figure;
plot(t,erro_MF,'r');
hold;
plot(t,erro_MF_C,'b');
legend('Erro sem compensador','Erro com compensador');
xlabel('t (s)');
ylabel('Amplitude');