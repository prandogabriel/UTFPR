from skimage import io, filters
import numpy as np
from matplotlib import pyplot as plt
from scipy import ndimage

im = io.imread('me.jpeg')
im = im[:,:,0]
im = im.astype('float')

lin, col = im.shape

im2 = np.zeros((lin,col))

filtro = np.array([[1, 0, -1],
                   [1, 0, -1],
                   [1, 0, -1]]) / 6

for i in range(1, lin-1):
    for j in range(1, col-1):

        sub_im = im[i-1:i+2, j-1:j+2]

        mult = sub_im * filtro
        media = mult.sum()

        im2[i,j] = media


# Filtro passa baixa, onde temos o realce das bordas da imagem
plt.figure()
plt.subplot(121)
plt.imshow(im.astype('uint8'), cmap='gray')
plt.subplot(122)
plt.imshow(im2.astype('uint8'), cmap='gray')

# ------------------------------------

# filtro mediana, deixamos a imagem mais clara e nítida

im2 = np.zeros((lin,col))

for i in range(1, (lin - 1)):
  for j in range(1, (col - 1)):
    subPic = im[i-1 : i+2, j-1 : j+2]
    median = np.median(subPic)
    if(not((im[i][j] < (median-5)) or (im[i][j] > (median+5)))):
      im2[i][j] = median
    else:
      im2[i][j] = im[i][j]

plt.figure()
plt.subplot(121)
plt.imshow(im.astype('uint8'), cmap='gray')
plt.subplot(122)
plt.imshow(im2.astype('uint8'), cmap='gray')


# ---------------------
# Recorte de 10 pixels da imagem inicial na área central

nim = im[800:810,440:450].astype('uint8')
plt.figure()
plt.imshow(nim)


# ---------------------------------------
# reflect, constant, nearest, mirror, wrap passa baixa no recorte da imagem 10x10

shape = nim.shape

lin = shape[0]
col = shape[1]
print(im.shape)

k = np.array([[0,1,0], [0,1,0], [0,1,0]])

reflect = ndimage.filters.convolve(
  nim, k,
  mode='reflect'
)

constant = ndimage.filters.convolve(
  nim, k,
  mode='constant'
)
nearest = ndimage.filters.convolve(
  nim, k,
  mode='nearest'
)
mirror = ndimage.filters.convolve(
  nim, k,
  mode='mirror'
)
wrap = ndimage.filters.convolve(
  nim, k,
  mode='wrap'
)

plt.figure()
plt.subplot(231)
plt.imshow(reflect, cmap='gray')
plt.subplot(232)
plt.imshow(constant, cmap='gray')
plt.subplot(233)
plt.imshow(nearest, cmap='gray')
plt.subplot(234)
plt.imshow(mirror, cmap='gray')
plt.subplot(235)
plt.imshow(wrap, cmap='gray')



# ------------------------
# filtro passa baixa scharr_h
im2 = filters.scharr_h(im)

plt.figure()
plt.subplot(121)
plt.imshow(im.astype('uint8'), cmap='gray')
plt.subplot(122)
plt.imshow(im2, cmap='gray')


#
def plot(data, title):
    plot.i += 1
    plt.subplot(2,2,plot.i)
    plt.imshow(data)
    plt.gray()
    plt.title(title)

plot.i = 0


im = io.imread('me.jpeg')
plot(im, 'Original')

data = np.array(im, dtype=float)


# Outra maneira de fazer um filtro passa-alto é simplesmente subtrair um filtro passa-baixo
# imagem filtrada do original. Aqui, usaremos um filtro gaussiano simples
# para "desfocar" (ou seja, um filtro passa-baixa) o original.

lowpass = ndimage.gaussian_filter(data, 3)
gauss_highpass = data - lowpass
plot(gauss_highpass, r'Gaussian Highpass, $\sigma = 3 pixels$')

plt.show()
