# -*- coding: utf-8 -*-
"""
Created on Thu Feb 25 15:57:17 2021

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
num=[4]
den=[1, 0.5, 0]
G=tf(num,den) 
#Planta sem compensação em malha fechada
Gmf=feedback(G,1)
print(Gmf)
#Polos de MF sem compensação
print(pole(Gmf))

#Compensador de avanço
Gav=tf([6.26, 3.13],[1, 5.02])
#Planta compensada com avanço
Gmfcav=feedback(Gav*G,1)
Gmfcav=minreal(Gmfcav)
print(Gmfcav)
#Polos de malha fechada com compensação de avanço
print(pole(Gmfcav))
#Resposta ao degrau
t=np.linspace(0,10,1000)
y1, t1 = step(Gmfcav,t)
plt.figure()
plt.plot(t1,y1)
plt.legend(['Com compensação de avanço'])
plt.xlabel('t(s)')
plt.ylabel('Amplitude')
plt.grid()

#Compensador
numc=np.polymul([6.26,  3.13],[1, 0.2])
denc=np.polymul([1,  5.02],[1, 0.01247])
Gc=tf(numc,denc)
#Planta com compensação em malha fechada
Gmfc=feedback(Gc*G,1)
Gmfc=minreal(Gmfc)
print(Gmfc)
#Zero e Polos de malha fechada com compensação
print(zero(Gmfc))
print(pole(Gmfc))

#Resposta ao degrau
t=np.linspace(0,30,1000)
y2, t2 = step(Gmf,t)
y3, t3 = step(Gmfc,t)
plt.figure()
plt.plot(t2,y2,t3,y3)
plt.legend(('Sem compensação','Com compensação'))
plt.xlabel('t(s)')
plt.ylabel('Amplitude')
plt.grid()

#Resposta a rampa
t=np.linspace(0,15,1000)
Gi=tf(1,[1, 0])
yi, ti = step(Gi,t)
yr1, tr1 = step(Gi*Gmf,t)
yr2, tr2 = step(Gi*Gmfc,t)

plt.figure()
plt.plot(ti,yi,tr1,yr1,tr2,yr2)
plt.legend(('Referência','Sem compensação','Com compensação'))
plt.xlabel('t(s)')
plt.ylabel('Amplitude')
plt.title('Resposta a Rampa')
plt.grid()


#Erros
t=np.linspace(0,50,1000)
Yi, t=step(Gi,t);
Y1, T1=step(Gi*Gmf,t);
Y2, T2=step(Gi*Gmfc,t);
erro_MF=Yi-Y1;
erro_MF_C=Yi-Y2;

#Exibe erros
plt.figure();
plt.plot(T1,erro_MF,T2,erro_MF_C);
plt.legend(('Erro sem compensador','Erro com compensador'))
plt.xlabel('t(s)')
plt.ylabel('Amplitude')
plt.grid()


#Lugar das raízes de G(s)
rlocus(G)
#Lugar das raízes do sistema compensado
rlocus(Gc*G)
