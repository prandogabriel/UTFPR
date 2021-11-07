from skimage import io, data
import numpy as np
from matplotlib import pyplot as plt

im = data.camera()
im = im.astype('float')

lin, col = im.shape

#im2 = im.copy()
im2 = np.zeros((lin,col))

filtro = np.array([[1/9, 1/9, 1/9],
                   [1/9, 1/9, 1/9],
                   [1/9, 1/9, 1/9]])

filtro = np.array([[1, 0, -1],
                   [1, 0, -1],
                   [1, 0, -1]]) / 6

for i in range(1, lin-1):
    for j in range(1, col-1):
#        soma = im[i-1,j-1]+im[i-1,j]+im[i-1,j+1]
#        soma = soma + im[i,j-1]+im[i,j]+im[i,j+1]
#        soma = soma + im[i+1,j-1]+im[i+1,j]+im[i+1,j+1]
#        media = soma / 9

        sub_im = im[i-1:i+2, j-1:j+2]

#        media = sub_im.mean()
#        media = (sub_im * (1/9)).sum()

        mult = sub_im * filtro
        media = mult.sum()

        im2[i,j] = media

plt.figure()
plt.subplot(121)
plt.imshow(im.astype('uint8'), cmap='gray')
plt.subplot(122)
plt.imshow(im2.astype('uint8'), cmap='gray')
