# 1 - Faça upload para o colab de uma fotografia de rosto sua e converta para escala de cinza
# 2 - Utilize segmentação de otsu global e segmentação adaptativa
# 3 - Verifique os resultados, comente os achados utilizando markdown as respostas as perguntas abaixo
# 3.1 - Com essa técnica de segmentação vc consegue isolar os olhos ou cabelo da sua fotografia?
# 3.2 - Qual método teve melhor resultado na remoção do fundo?
import numpy as np
from skimage import io, filters
from matplotlib import pyplot as plt


def plotMany(figures, cmap):
    plt.figure(figsize=(10, 10))
    subplotNumber = 241
    for figure in (figures):
        plt.subplot(subplotNumber)
        plt.imshow(figure, cmap=cmap)
        subplotNumber += 1


im = io.imread('me.jpeg')
im = im[:, :, 0]

lin, col = np.shape(im)

t1 = filters.threshold_otsu(im)
seg1 = np.where(im > t1, 1, 0)

t2 = filters.rank.otsu(im, np.ones((25, 25)))
seg2 = np.where(im > t2, 1, 0)

plotMany([im, seg1, seg2], cmap='gray')
