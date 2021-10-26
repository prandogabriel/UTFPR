# -*- coding: utf-8 -*-
"""
Created on Tue Sep  1 12:13:56 2020

@author: rcard
"""
#As próximas 3 linhas são para selecionar entre plot inline ou em nova janela
#Útil para rlocus
from IPython import get_ipython
get_ipython().run_line_magic('matplotlib', 'qt')
#get_ipython().run_line_magic('matplotlib', 'inline')

import numpy as np                #Biblioteca para cálculo numérico
import math                       #Funções matemáticas
import matplotlib.pyplot as plt   # Funções de plot similares as do MATLAB
import control as ctrl            # Biblioteca para controle
from control.matlab import *      # Funções para controle similares as do MATLAB

#Planta
num=[1.06]
den=[1, 3, 2, 0]
G=tf(num,den) 
print(G)
#Planta sem compensação em malha fechada
Gmf=feedback(G,1)
#Polos de MF sem compensação
print(pole(Gmf))

#Lugar das raízes de Gc(s)G(s) para projeto do ganho Kc
numc=[1,  0.05]
denc=[1, 0.005]
Gcp=tf(numc,denc)
rlocus(G)
rlocus(Gcp*G)

#Cálculo analítico de Kc
s=complex(-0.31,0.55)
Kc=1/abs(((s+0.05)*1.06)/((s+0.005)*(s**3+3*s**2+2*s)))
print(Kc)

#Compensador
numc=[0.97,  0.0485]   #0.97*[1, 0.05]
denc=[1, 0.005]
Gc=tf(numc,denc)
#Planta com compensação em malha fechada
Gmfc=feedback(Gc*G,1)
#Polos de malha fechada com compensação
print(pole(Gmfc))

#Resposta ao degrau
t=np.linspace(0,70,1000)
y1, t1 = step(Gmf,t)
y2, t2 = step(Gmfc,t)
plt.figure()
plt.plot(t1,y1,t2,y2)
plt.legend(('Sem compensação','Com compensação'))
plt.xlabel('t(s)')
plt.ylabel('Amplitude')
plt.title('Resposta ao Degrau')
plt.grid()



#Resposta a rampa
t=np.linspace(0,50,1000)
Gi=tf(1,[1, 0])
yi, ti = step(Gi,t)
yr1, tr1 = step(Gi*Gmf,t)
yr2, tr2 = step(Gi*Gmfc,t)

plt.figure()
plt.plot(ti,yi,tr1,yr1,tr2,yr2)
plt.legend(('Referência','Sem compensação','Com compensação'))
plt.xlabel('t(s)')
plt.ylabel('Amplitude')
plt.title('Resposta à Rampa')
plt.grid()


#Erros em regime permanente para entrada rampa
t=np.linspace(0,200,2000)
Yi, t=step(Gi,t);
Y1, T1=step(Gi*Gmf,t);
Y2, T2=step(Gi*Gmfc,t);
erro_MF=Yi-Y1;
erro_MF_C=Yi-Y2;

plt.figure();
plt.plot(T1,erro_MF,T2,erro_MF_C);
plt.legend(('Erro sem compensador','Erro com compensador'))
plt.xlabel('t(s)')
plt.ylabel('Amplitude')
plt.grid()