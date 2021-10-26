# -*- coding: utf-8 -*-
"""
Created on Thu Sep 10 17:48:34 2020

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
print(G)

#Planta sem compensação em malha fechada
Gmf=feedback(G,1)
#Polos de MF sem compensação
print(pole(Gmf))

#Compensador
numc=[1.13, 6.4749, 1.2498]
denc=[1, 0]
Gc=tf(numc,denc)
print(Gc)
#Planta com compensação em malha fechada
Gmfc=feedback(Gc*G,1)
#Polos de MF do sistema compensado
print(pole(Gmfc))

#Resposta ao degrau
t=np.linspace(0,25,1000)
y1, t1 = step(Gmf,t)
y2, t2 = step(Gmfc,t)
plt.figure()
plt.plot(t1,y1,t2,y2)
plt.title('Resposta ao degrau')
plt.legend(('Sem compensação', 'Com compensação'))
plt.xlabel('t(s)')
plt.ylabel('Amplitude')
plt.grid()

#Lugar das raízes da planta e da planta compensada
rlocus(G)
rlocus(Gc*G)


#Resposta a rampa
Gi=tf([1],[1, 0]);
T=np.linspace(0,50,1000)
y3, t3 = step(Gi,T)
y4, t4 = step(Gi*Gmf,T)
y5, t5 = step(Gi*Gmfc,T)
plt.figure()
plt.plot(t3,y3,t4,y4,t5,y5)
plt.title('Resposta a rampa')
plt.legend(('Referência', 'Gmf','Gmfc'))
plt.xlabel('t(s)')
plt.ylabel('Amplitude')
plt.grid()

#Erros
y6, t6 = step(Gi,T)
y7, t7 = step(Gi*Gmf,T)
y8, t8 = step(Gi*Gmfc,T)
erro_MF=y3-y4
erro_MF_C=y3-y5

plt.figure()
plt.plot(T,erro_MF,T,erro_MF_C)
plt.title('Erro de rastreamento para referência do tipo rampa')
plt.legend(('Sem compensação', 'Com compensação'))
plt.xlabel('t(s)')
plt.ylabel('Amplitude')
plt.grid()