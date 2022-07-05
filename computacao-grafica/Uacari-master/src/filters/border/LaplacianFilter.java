package filters.border;

import filters.blur.GaussianBlur;
import image.Image;
import log.Logger;

/**
 * A Laplacian filter.
 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
 */
public class LaplacianFilter extends GaussianBlur{
	
	private double negativeSum = 0;
	
	public LaplacianFilter(){
		this.setKernelSize(5);
		this.setAmplitude(3);
		this.setSpreadX(2);
		this.setSpreadY(2);
	}
	
	public LaplacianFilter(final Image image, final int kernelSize, final float amplitude, final float spreadX, final float spreadY){
		this.setImage(image);
		this.setKernelSize(kernelSize);
		this.setAmplitude(amplitude);
		this.setSpreadX(spreadX);
		this.setSpreadY(spreadY);
	}
	
	public LaplacianFilter(final Image image, final int kernelWidth, final int kernelHeight, final float amplitude, final float spreadX, final float spreadY){
		this.setImage(image);
		this.setKernelWidth(kernelWidth);
		this.setKernelHeight(kernelHeight);
		this.setAmplitude(amplitude);
		this.setSpreadX(spreadX);
		this.setSpreadY(spreadY);
	}
	

	protected void updateKernel(){
		if (update){
			negativeSum = 0;
			super.supressPrint = true;
			super.updateKernel();
			
			final int halfX = kernel[0].length/2,
					halfY = kernel.length/2;
			
			Logger.log("Laplacian Kernel:\n");
			for (int i=0; i<this.kernel.length; i++){
				for (int j=0; j<this.kernel[0].length; j++){
					
					if (!(j == halfX && i == halfY)){
						kernel[i][j] *= -1;
						negativeSum += kernel[i][j];
					}else{
						kernelSum += -2*kernel[i][j];
					}
					
					Logger.log(kernel[i][j] + " ");
				}
				Logger.log("\n");
			}
		}
	}
	
	@Override
	public double getFilteredPixel(Image image, int x, int y, int band) {
		updateKernel();
		
		final int halfX = kernel[0].length/2,
				halfY = kernel.length/2;
		
		final double positiveFactor = (-1*negativeSum) / kernel[halfY][halfX];
 
		double result = 0;
		final int halfSizeX = (int) Math.floor(kernelSizeX/2d),
				halfSizeY = (int) Math.floor(kernelSizeY/2d);
		for (int i=y - halfSizeY; i<= y + halfSizeY; i++){
			for (int j=x - halfSizeX; j<= x + halfSizeX; j++){
				final int kerX = j - (x - halfSizeX), kerY = i - (y - halfSizeY);
				
				if (kerX == halfX && kerY == halfY){
					result += (image.getPixelBoundaryMode(j, i, band) * kernel[kerY][kerX])*positiveFactor;
				}else
					result += image.getPixelBoundaryMode(j, i, band) * kernel[kerY][kerX];
				
			}
		}
		
		return result;
	}
	
	public Image applyFilter(final Image image) {
		Image out = super.applyFilter(image);
		out.stretchOrShrinkRange(0, 255);
		return out;
	}
	
}
