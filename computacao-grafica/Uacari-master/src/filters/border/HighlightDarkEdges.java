package filters.border;

import filters.blur.GaussianBlur;
import image.Image;
import log.Logger;

/**
 * Highlights the dark edges biased by a Gaussian function.
 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
 */
public class HighlightDarkEdges extends GaussianBlur{
	//private double negativeSum = 0;
	
	public HighlightDarkEdges(){
		this.setAmplitude(5);
		this.setSpreadX(4f);
		this.setSpreadY(4f);
	}
	
	protected void updateKernel(){
		if (update){
			//negativeSum = 0;
			super.supressPrint = true;
			super.updateKernel();
			
			final int halfX = kernel[0].length/2,
					halfY = kernel.length/2;
			
			Logger.log("Laplacian Kernel:\n");
			for (int i=0; i<this.kernel.length; i++){
				for (int j=0; j<this.kernel[0].length; j++){
					
					if (!(j == halfX && i == halfY)){
						kernel[i][j] *= -1;
						//negativeSum += kernel[i][j];
					}else{
						//kernelSum += -2*kernel[i][j];
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
		
		final double positiveParcel = image.getPixelBoundaryMode(x, y, band) / kernel[halfY][halfX];
		double negativeParcel = 0;

		double result = 0;
		final int halfSizeX = (int) Math.floor(kernelSizeX/2d),
				halfSizeY = (int) Math.floor(kernelSizeY/2d);
		for (int i=y - halfSizeY; i<= y + halfSizeY; i++){
			for (int j=x - halfSizeX; j<= x + halfSizeX; j++){
				final int kerX = j - (x - halfSizeX), kerY = i - (y - halfSizeY);
				
				if (!(kerX == halfX && kerY == halfY))
					negativeParcel += image.getPixelBoundaryMode(j, i, band) * kernel[kerY][kerX];
				
			}
		}
		
		result = Math.abs(negativeParcel/positiveParcel);
		
		return result;
	}
}
