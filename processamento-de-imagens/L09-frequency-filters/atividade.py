from matplotlib import pyplot as plt
import numpy as np
from skimage import io, color
from math import sqrt, exp


def distance(point1, point2):
    return sqrt((point1[0]-point2[0])**2 + (point1[1]-point2[1])**2)


def gaussianLP(lin, col, D0):
    base = np.zeros((lin, col))
    center = (lin/2, col/2)
    for x in range(col):
        for y in range(lin):
            base[y, x] = exp(((-distance((y, x), center)**2)/(2*(D0**2))))
    return base


def plotMany(figures, cmap):
    plt.figure(figsize=(10, 10))
    subplotNumber = 241
    for figure in (figures):
        plt.subplot(subplotNumber)
        plt.imshow(figure, cmap=cmap)
        subplotNumber += 1


im = io.imread('Lenna.png')

im2 = color.rgb2gray(im)
im2 = (im2 * 255).astype('uint8')

lin, col = im2.shape
# Filtro variando o sigma do filtro gaussiano
f1 = gaussianLP(lin, col, 2)
f2 = gaussianLP(lin, col, 20)
f3 = gaussianLP(lin, col, 40)
f4 = gaussianLP(lin, col, 500)

# mostra os filtros
plotMany([f1, f2, f3, f4], cmap=None)

# realiza a transformada
imft2 = np.fft.fft2(im2)
fshift2 = np.fft.fftshift(imft2)

# aplica os filtros na imagem com a transformada
blur1 = fshift2 * f1
blur2 = fshift2 * f2
blur3 = fshift2 * f3
blur4 = fshift2 * f4

# realiza a transformada inversa
blur1 = np.fft.ifft2(np.fft.ifftshift(blur1))
blur2 = np.fft.ifft2(np.fft.ifftshift(blur2))
blur3 = np.fft.ifft2(np.fft.ifftshift(blur3))
blur4 = np.fft.ifft2(np.fft.ifftshift(blur4))


plotMany([np.abs(blur1), np.abs(blur2), np.abs(
    blur3), np.abs(blur4)], cmap='gray')

# Ao aumentar o sigma no filtro gaussiano passa baixa, é possível que a imagem se torna mais nítida
# A transformada de uma gaussiana é outra gaussiana
# Após a transformada, a gaussiana continua com o mesmo tamanho
