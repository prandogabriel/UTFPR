# Para as imagens dourado.jpeg e everton.jpeg faça uma
# transformação geométrica a fim de verificar com
# exatidão se os jogadores estão ou não impedidos
# nos lances.
# Adicione na imagem transformada uma linha vermelha
# de impedimento conforme exemplo da imagem
#  linha.jpeg. A posição (coordenadas) dessa linha
#   na imagem vc pode fazer manualmente (tentativa e
# erro). Procure na biblioteca matplotlib como
# criar uma linha no plot da imagem.

from skimage import io, transform
import numpy as np
import matplotlib.pyplot as plt

arq = 'everton.jpeg'
# [X,Y]
c = [[360,260], [530,250], [535,556], [320,560]]
linha = 228

# Load source image
img = io.imread(arq)

plt.figure()
# Adjust the 4 coordinates below: they must lie on the same plane. Use pitch patterns to axis-align.
#plt.rcParams["figure.figsize"] = (20, 20)
plt.subplot(121)
plt.imshow(img)
pts = np.array(c)

plt.scatter(pts[:,0], pts[:,1])

pts_norm = np.array([[0, 0], [300, 0], [300, 800], [0, 800]])
# Calculate Homography
tform = transform.ProjectiveTransform()
tform.estimate(pts_norm, pts)

## Straighten out image
img_norm =  transform.warp(img, tform, output_shape=(800, 300))

plt.subplot(122)
plt.imshow(img_norm)

x1, y1 = [25, 25], [790, 0]
plt.plot(x1, y1, color='red',linewidth=2 )
plt.show()
