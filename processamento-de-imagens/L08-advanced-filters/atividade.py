# 1 - Do módulo filters, biblioteca skimage
# (i.e. https://scikit-image.org/docs/stable/api/skimage.filters.html)
# escolher 3 aleatórios filtros e aplicar em uma imagem qualquer
# 1.1 - Comentar sobre o resultado do filtro respondendo as seguintes perguntas
# 1.1.1 - O filtro é passa alta, passa banda ou passa baixa?
# 1.1.2 - Para que se destina o filtro?
# 1.1.3 - Qual a referência desse filtro? Por quem foi criado (artigo, livro, etc)

import numpy as np
from skimage import io, filters, feature
from matplotlib import pyplot as plt

im = io.imread('me.jpeg')
im = im[:,:,0]

print(im)

# filtro 1
# Este filtro pode ser usado para detectar bordas contínuas, por exemplo, vasos, rugas, rios. Ele pode ser usado para calcular a fração de toda a imagem que contém esses objetos.
# Definido apenas para imagens 2-D e 3-D. Quase igual ao filtro Frangi, mas usa um método alternativo de suavização. Consulte [1] para encontrar as diferenças entre os filtros Frangi e Hessian.
# Escrito por Marc Schrijver (novembro de 2001) Reescrito por DJ Kroon University of Twente (maio de 2009)
filter1 = filters.hessian(im, sigmas=range(1, 10, 2), scale_range=None, scale_step=None, alpha=0.5, beta=0.5, gamma=15, black_ridges=True, mode=None, cval=0)
plt.figure()
plt.subplot(121)
plt.imshow(im, cmap="gray")
plt.subplot(122)
plt.imshow(filter1, cmap="gray",)

# filtro 2
# filter2 = feature.
# Filtre uma imagem com o filtro de vasos Frangi.

# Este filtro pode ser usado para detectar cristas contínuas, por exemplo, vasos, rugas, rios. Ele pode ser usado para calcular a fração de toda a imagem que contém esses objetos.

# Definido apenas para imagens 2-D e 3-D. Calcula os vetores próprios do Hessian para calcular a similaridade de uma região da imagem com os vasos, de acordo com o método descrito em [1] .
# Notas

# Escrito por Marc Schrijver, novembro de 2001 Reescrito por DJ Kroon, Universidade de Twente, maio de 2009, [2] Adoção da versão 3D da DG Ellis, Januar 20017, [3]
filter2 = filters.frangi(
  im,
  sigmas=range(1, 20, 2),
  mode='constant',
)

plt.figure()
plt.subplot(121)
plt.imshow(im, cmap="gray")
plt.subplot(122)
plt.imshow(filter2, cmap='gray')


# filtro 3
# Encontre as bordas horizontais de uma imagem usando a transformação de Scharr.
# kernel utilizado
#  3   10   3
#  0    0   0
# -3  -10  -3

filter3 = filters.scharr_h(im)

plt.figure()
plt.subplot(121)
plt.imshow(im, cmap="gray")
plt.subplot(122)
plt.imshow(filter3, cmap='gray')
