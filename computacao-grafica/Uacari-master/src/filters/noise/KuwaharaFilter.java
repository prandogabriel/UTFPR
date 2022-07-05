package filters.noise;

import filters.Filter;
import image.Image;

/**
 * Implementation of the Kuwahara Filter. It reduces noise from the image but also turns the image into a "water painting" picture.
 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
 */
public class KuwaharaFilter extends Filter{
	private int kernelSizeX = 7, kernelSizeY = 7;
	
	/**
	 * Sets the kernel size on the x and y directions.
	 * @param kernelSize
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setKernelSize(final int kernelSize){
		this.setKernelWidth(kernelSize);
		this.setKernelHeight(kernelSize);
	}
	
	/**
	 * Sets the kernel size on the x direction (width)
	 * @param kernelSizeX
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setKernelWidth(final int kernelSizeX){
		this.kernelSizeX = kernelSizeX;
	}
	/**
	 * Sets the kernel height.
	 * @param kernelSizeY
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setKernelHeight(final int kernelSizeY){
		this.kernelSizeY = kernelSizeY;
	}

	@Override
	public double getFilteredPixel(Image image, int x, int y, int band) {
		final int halfX = kernelSizeX/2, halfY = kernelSizeY/2;
		
		//region 1
		float mean1 = 0;
		int counter = 0;
		for (int i=y - halfY; i<=y; i++){
			for (int j=x - halfX; j<=x; j++){
				mean1 += image.getPixelBoundaryMode(j, i, band);
				counter ++;
			}
		}
		mean1 /= counter;
		
		float stdDev1 = 0;
		for (int i=y - halfY; i<=y; i++){
			for (int j=x - halfX; j<=x; j++){
				stdDev1 += (float) Math.pow(image.getPixelBoundaryMode(j, i, band) - mean1, 2);
			}
		}
		stdDev1 /= counter;
		stdDev1 = (float) Math.sqrt(stdDev1);
		
		//region2
		float mean2 = 0;
		int counter2 = 0;
		for (int i=y - halfY; i<=y; i++){
			for (int j=x; j<=x + halfX; j++){
				mean2 += image.getPixelBoundaryMode(j, i, band);
				counter2 ++;
			}
		}
		mean2 /= counter2;
		
		float stdDev2 = 0;
		for (int i=y - halfY; i<=y; i++){
			for (int j=x; j<=x + halfX; j++){
				stdDev2 += (float) Math.pow(image.getPixelBoundaryMode(j, i, band) - mean2, 2);
			}
		}
		stdDev2 /= counter2;
		stdDev2 = (float) Math.sqrt(stdDev2);
		
		//region 3
		float mean3 = 0;
		int counter3 = 0;
		for (int i=y; i<=y + halfY; i++){
			for (int j=x - halfY; j<=x; j++){
				mean3 += image.getPixelBoundaryMode(j, i, band);
				counter3 ++;
			}
		}
		mean3 /= counter3;
		
		float stdDev3 = 0;
		for (int i=y; i<=y + halfY; i++){
			for (int j=x - halfY; j<=x; j++){
				stdDev3 += Math.pow(image.getPixelBoundaryMode(j, i, band) - mean3, 2);
			}
		}
		stdDev3 /= counter3;
		stdDev3 = (float) Math.sqrt(stdDev3);
		
		
		//region 4
		float mean4 = 0;
		int counter4 = 0;
		for (int i=y; i<=y + halfY; i++){
			for (int j=x; j<=x + halfX; j++){
				mean4 += image.getPixelBoundaryMode(j, i, band);
				counter4++;
			}
		}
		mean4 /= counter;
		
		float stdDev4 = 0;
		for (int i=y; i<=y + halfY; i++){
			for (int j=x; j<=x + halfX; j++){
				stdDev4 += Math.pow(image.getPixelBoundaryMode(j, i, band) - mean4, 2);
			}
		}
		stdDev4 /= counter4;
		stdDev4 = (float) Math.sqrt(stdDev4);
		
		
		final float minDev = Math.min(Math.min(Math.min(stdDev1, stdDev2), stdDev3), stdDev4);
		
		
		
		if (minDev == stdDev1) return mean1;
		else if (minDev == stdDev2) return mean2;
		else if (minDev == stdDev3) return mean3;
		else return mean4;
	}

	
	
}
