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
#Polos de MF sem compensação
print(pole(Gmf))
#Lugar das raízes de G(s)
rlocus(G);

#Cálculo de Kp
s=complex(-2,2*math.sqrt(3))
Kp=1/abs((0.125*(s+8)*4)/(s*(s+2)))
print(Kp)

#Compensador
numc=[0.5,  4]  #0.5*[1 8]
denc=1
Gc=tf(numc,denc)
print(Gc)
#Planta com compensação em malha fechada
Gmfc=feedback(Gc*G,1)
#Polos de MF com compensação
print(pole(Gmfc))

#Resposta ao degrau
t=np.linspace(0,6,1000)
y1, t1 = step(Gmf,t)
y2, t2 = step(Gmfc,t)
plt.figure()
plt.plot(t1,y1,t2,y2)
plt.legend(('Gmf','Gmfc'))
plt.xlabel('t(s)')
plt.ylabel('Amplitude')
plt.grid()


#Lugar das raízes do sistema compensado
rlocus(Gc*G)


#Controlador PD com filtro passa-baixas (avanço)
numc2=[100, 800]   #200*0.5*[1 8];
denc2=[1, 200]
Gc2=tf(numc2,denc2)
#Planta com compensação PD como filtro passa-baixas em malha fechada
Gmfc2=feedback(Gc2*G,1)
#Polos de MF do sistema compensado por PD+filtro passa-baixas (avanço)
print(pole(Gmfc2))

#Comparação da compensação PD vs PD+filtro passa-baixas (avanço)
y3, t3 = step(Gmfc,t)
y4, t4 = step(Gmfc2,t)
plt.figure()
plt.plot(t3,y3,t4,y4)
plt.legend(('PD','Avanço'))
plt.xlabel('t(s)')
plt.ylabel('Amplitude')
plt.grid()

#Comparação entre a resposta em frequência do PD vs PD+filtro passa-baixas
#(avanço)
plt.figure() 
bode(Gc, Gc2)
plt.legend(('PD','Avanço'))
