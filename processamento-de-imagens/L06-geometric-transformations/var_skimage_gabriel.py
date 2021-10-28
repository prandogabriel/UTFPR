from skimage import io, transform
import numpy as np
import matplotlib.pyplot as plt

arq = 'gabriel.jpeg'
c = [[276,188], [432,188], [301,488], [74,488]]
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

