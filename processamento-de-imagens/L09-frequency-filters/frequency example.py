from matplotlib import pyplot as plt
import numpy as np

plt.figure()
#f = np.zeros((30,30), 'uint8')
#f[5:24,13:17] = 255
#f[13:17,5:24] = 255
f = np.random.randint(0, 255, (30,30))
plt.imshow(f, cmap='gray', vmin=0, vmax=255)

plt.figure()
F = np.fft.fft2(f+1)
F2 = np.fft.fftshift(F)
F2 = np.abs(F2)
F2 = np.log(F2 + 0.00001)

plt.imshow(F2)
plt.colorbar()