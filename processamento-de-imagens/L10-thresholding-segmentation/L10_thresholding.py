from matplotlib import pyplot as plt
from skimage import io
import numpy as np
import time

im = io.imread('cameraman.tif')

lin, col = im.shape

t1 = time.time()
seg = np.zeros((lin,col))
for i in range(lin):
    for j in range(col):
        if im[i,j] < 50:
            seg[i,j] = 1
t2 = time.time()
print(t2-t1)

t1 = time.time()
seg = np.zeros((lin,col))
seg[im < 50] = 1
t2 = time.time()
print(t2-t1)

t1 = time.time()
seg = np.where(im<50, 1, 0)
t2 = time.time()
print(t2-t1)

plt.figure()
plt.subplot(131)
plt.imshow(im, cmap='gray')
plt.subplot(132)
plt.imshow(seg, cmap='gray')
plt.subplot(133)
plt.imshow(im * seg, cmap='gray')
