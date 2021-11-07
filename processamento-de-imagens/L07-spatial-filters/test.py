import cv2
import numpy as np
from matplotlib import pyplot as plt
from numpy.fft import fft2, ifft2, fftshift, ifftshift


def lowPassButterWorth(im, cutOff, n):
    h = im.shape[0]
    w = im.shape[1]
    #img_float32 = np.float32(img)
    fftim = fftshift(fft2(im))
    x = np.linspace(-0.5, 0.5, w) * 2 * w
    y = np.linspace(-0.5, 0.5, h) * 2 * h
    u, v = np.meshgrid(x, y)
    dis = np.sqrt(u ** 2 + v ** 2)
    hhp = 1 / (1 + ((dis / cutOff) ** (2 * n)))
    out_spec_centre = fftim * hhp
    out_spec = ifftshift(out_spec_centre)
    out = np.abs(ifft2(out_spec))
    # out = (out - np.min(out)) / (np.max(out) - np.min(out))
    # out = np.uint8(255 * out)
    return out


def highPassGaussian(im, cutOff):
    h = im.shape[0]
    w = im.shape[1]
    #img_float32 = np.float32(img)
    fftim = fftshift(fft2(im))
    x = np.linspace(-1, 1, w) * 2 * w
    y = np.linspace(-1, 1, h) * 2 * h
    u, v = np.meshgrid(x, y)
    dis = np.sqrt(u ** 2 + v ** 2)
    hhp = 1 - (np.exp(-(dis ** 2) / (2 * (cutOff ** 2))))
    out_spec_centre = fftim * hhp
    out_spec = ifftshift(out_spec_centre)
    out = np.real(ifft2(out_spec))
    # out = (out - np.min(out)) / (np.max(out) - np.min(out))
    # out = np.uint8(255 * out)
    return out


def bandPass(image, low_cutOff, degree, high_cutOff):
    output = lowPassButterWorth(image, low_cutOff, degree)
    final_img = highPassGaussian(output, high_cutOff)
    return final_img


if __name__ == '__main__':
    img = cv2.imread("Cameraman.PNG", 0)
    camLow = lowPassButterWorth(img, 50, 1)
    plt.imshow(camLow, cmap='gray')
    plt.savefig("CameraManLow.png")
    camHigh = highPassGaussian(img, 50)
    plt.imshow(camHigh, cmap='gray')
    plt.savefig("CameraManHigh.png")
    camBand = bandPass(img, 50, 2, 50)
    plt.imshow(camBand, cmap='gray')
    plt.savefig("CameraManBand.png")


    plt.subplot(221), plt.imshow(img, cmap='gray')
    plt.title('Input Image'), plt.xticks([]), plt.yticks([])

    plt.subplot(222), plt.imshow(camLow, cmap='gray')
    plt.title('CameraManLow'), plt.xticks([]), plt.yticks([])


    plt.subplot(223), plt.imshow(camHigh, cmap='gray')
    plt.title('CameraManHigh'), plt.xticks([]), plt.yticks([])


    plt.subplot(224), plt.imshow(camBand, cmap='gray')
    plt.title('CameraManBand'), plt.xticks([]), plt.yticks([])

    plt.savefig("me.jpeg")
    plt.show()
    # plt.savefig("cameramanlow.png")
    # camHigh =
