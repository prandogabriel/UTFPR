import numpy as np
from skimage import io, filters, feature
from matplotlib import pyplot as plt

im = io.imread('Lenna.png')
im = im[:,:,0]

mask = np.ones( (3,3) )

# im2 = filters.median(im, mask)
im2 = filters.gaussian(im, sigma=2)

im2 = (im2*255).astype('uint8')

im3 = feature.canny(im, 2, 10, 30)

plt.figure()
plt.subplot(221)
plt.imshow(im, cmap="gray")
plt.subplot(222)
plt.imshow(im2, cmap="gray")
plt.subplot(223)
plt.imshow(im3, cmap='gray')



