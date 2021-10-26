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
num=[4]
den=[1,2,0]
G=tf(num,den) 
print(G)

#Planta sem compensação em malha fechada
Gmf=feedback(G,1)
print(Gmf)
#Polos de MF sem compensação
print(pole(Gmf))
#Lugar das raízes de G(s)
rlocus(G)

#Verificação da condição de ângulo
s=complex(-2,2*math.sqrt(3))
angle=np.angle(4/(s*(s+2)))*180/np.pi #O resultado é em rad que é convertido para graus.
print(angle)

#Cálculo de Kc
Kc=1/abs(4/(s*(s+4)))
print(Kc)

#Compensador
numc=[4,  8]  #4*[1 2]
denc=[1, 4]
Gc=tf(numc,denc)
print(Gc)

#Planta com compensação em malha fechada
Gmfc=feedback(Gc*G,1)
Gmfc=minreal(Gmfc)
print(Gmfc)
#Polos de MF com compensação
print(pole(Gmfc))

#Resposta ao degrau
t=np.linspace(0,6,1000)
y1, t1 = step(Gmf,t)
y2, t2 = step(Gmfc,t)
plt.figure()
plt.plot(t1,y1,t2,y2)
plt.legend(('Sem compensação','Com compensação'))
plt.xlabel('t(s)')
plt.ylabel('Amplitude')
plt.grid()

#Lugar das raízes do sistema compensado
rlocus(Gc*G)
