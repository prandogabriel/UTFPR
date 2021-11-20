from matplotlib import pyplot as plt
import numpy as np
from skimage import io, color
from math import sqrt, exp

def distance(point1,point2):
    return sqrt((point1[0]-point2[0])**2 + (point1[1]-point2[1])**2)

def butterworthLP(lin, col, D0, n):
    base = np.zeros((lin,col))
    center = (lin/2,col/2)
    for x in range(lin):
        for y in range(col):
            base[y,x] = 1/(1+(distance((y,x),center)/D0)**(2*n))
    return base

def butterworthHP(lin, col, D0, n):
    base = butterworthLP(lin, col, D0, n)
    base = 1 - base
    return base

def gaussianLP(lin, col, D0):
    base = np.zeros((lin,col))
    center = (lin/2,col/2)
    for x in range(col):
        for y in range(lin):
            base[y,x] = exp(((-distance((y,x),center)**2)/(2*(D0**2))))
    return base

def gaussianHP(lin, col, D0):
    base = gaussianLP(lin, col, D0)
    base = 1 - base
    return base    

im = io.imread('Lenna.png')

im2 = color.rgb2gray(im)
im2 = (im2 * 255).astype('uint8')

lin,col = im2.shape

#f1 = butterworthLP(51,51,7,2)
#f2 = butterworthHP(51,51,7,2)
#f3 = gaussianLP(51,51,7)
#f4 = gaussianHP(51,51,7)

f1 = butterworthLP(lin, col, 20, 2)
f2 = butterworthHP(lin, col, 120, 2)
f3 = gaussianLP(lin, col, 20)
f4 = gaussianHP(lin, col, 120)

plt.figure()
plt.subplot(221)
plt.imshow(f1)
plt.subplot(222)
plt.imshow(f2)
plt.subplot(223)
plt.imshow(f3)
plt.subplot(224)
plt.imshow(f4).

imft2 = np.fft.fft2(im2)
fshift2 = np.fft.fftshift(imft2)

blur1 = fshift2 * f1
bordas1 = fshift2 * f2
blur2 = fshift2 * f3
bordas2 = fshift2 * f4

blur1 = np.fft.ifft2(np.fft.ifftshift(blur1))
bordas1 = np.fft.ifft2(np.fft.ifftshift(bordas1))
blur2 = np.fft.ifft2(np.fft.ifftshift(blur2))
bordas2 = np.fft.ifft2(np.fft.ifftshift(bordas2))

plt.figure()
plt.subplot(221)
plt.imshow(np.abs(blur1), cmap='gray')
plt.subplot(222)
plt.imshow(np.abs(bordas1), cmap='gray')
plt.subplot(223)
plt.imshow(np.abs(blur2), cmap='gray')
plt.subplot(224)
plt.imshow(np.abs(bordas2), cmap='gray')