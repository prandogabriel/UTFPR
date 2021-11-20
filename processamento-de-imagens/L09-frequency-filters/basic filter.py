from skimage import io, color
from matplotlib import pyplot as plt
import numpy as np
#from mpl_toolkits.mplot3d import Axes3D 

im = io.imread('Lenna.png')

im2 = color.rgb2gray(im)
im2 = (im2 * 255).astype('uint8')
plt.figure()
plt.imshow(im2, cmap='gray')

filtro = np.zeros((512,512))
filtro[220:292, 220:292] = 1
plt.figure()
plt.imshow(filtro, cmap='gray')

#filtro = np.ones((512,512))
#filtro[220:292, 220:292] = 0

f2 = np.fft.fft2(im2)
plt.figure()
plt.imshow(np.log(np.abs(f2) + 0.00001))

fshift2 = np.fft.fftshift(f2)
plt.figure()
plt.imshow(np.log(np.abs(fshift2) + 0.00001))

m = fshift2 * filtro

plt.figure()
plt.imshow(np.log(np.abs(m) + 0.00001 ), cmap='gray')
plt.colorbar()

plt.figure()
f_ishift = np.fft.ifftshift(m)
plt.imshow(np.log(np.abs(f_ishift) + 0.00001), cmap='gray')


img_back = np.fft.ifft2(f_ishift)
img_back = np.abs(img_back)
plt.figure()
plt.imshow(img_back, cmap='gray')
