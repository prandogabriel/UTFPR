from matplotlib import pyplot as plt
from skimage import filters, data
import numpy as np

im = data.coins()

lin,col = np.shape(im)

t1 = filters.threshold_otsu(im)
seg1 = np.where(im>t1, 1, 0)

t2 = filters.rank.otsu(im, np.ones((lin,col)))
seg2 = np.where(im>t2, 1, 0)

plt.figure()
plt.subplot(131)
plt.imshow(im, cmap='gray')
plt.subplot(132)
plt.imshow(seg1, cmap='gray')
plt.subplot(133)
plt.imshow(seg2, cmap='gray')