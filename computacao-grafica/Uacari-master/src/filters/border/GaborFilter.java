package filters.border;

import filters.Filter;
import image.Image;
import log.Logger;

/**
 * A Gabor filter implementation (Sinusoidal Wave + Gaussian Function).
 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
 */
public class GaborFilter extends Filter{
	private int kernelWidth = 10, kernelHeight = 10;
	private float spreadX = 2f, spreadY = 2f, delta = (float) (0.25f*Math.PI);
	private double offSet = 0;
	private double[][] kernel;
	
	
	/**
	 * Set the orientation of the function in degrees.
	 * @param delta - the degree of rotation.
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setOrientation(final float delta){
		this.delta = (float) (delta*Math.PI/360);
		update = true;
	}
	
	/**
	 * Sets the offset of the function.
	 * @param offset
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setOffset(final float offset){
		this.offSet = offset;
		update = true;
	}
	
	/**
	 * Sets the width of the kernel.
	 * @param kernelSizeX
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setKernelWidth(final int kernelSizeX){
		this.kernelWidth = kernelSizeX;
		update = true;
	}
	
	/**
	 * Sets the size of the kernel.
	 * @param kernelSizeY
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setKernelHeight(final int kernelSizeY){
		this.kernelHeight = kernelSizeY;
		update = true;
	}
	
	/**
	 * Sets the size of the kernel (width and height).
	 * @param kernelSize
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setKernelSize(final int kernelSize){
		this.setKernelWidth(kernelSize);
		this.setKernelHeight(kernelSize);
	}
	
	/**
	 * Sets the spread on the x direction of the Gaussian function.
	 * @param spreadX
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setSpreadX(final float spreadX){
		this.spreadX = spreadX;
		update = true;
	}
	
	/**
	 * Sets the spread on the y direction of the Gaussian function.
	 * @param spreadY
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setSpreadY(final float spreadY){
		this.spreadY = spreadY;
		update = true;
	}
	
	/**
	 * Sets the spread on both x and y directions of the Gaussian function.
	 * @param spread
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setSpread(final float spread){
		this.setSpreadX(spread);
		this.setSpreadY(spread);
	}
	
	protected void updateKernel(){
		if (update){
			
			final int halfSizeX = (int) Math.floor(kernelWidth/2d),
					halfSizeY = (int) Math.floor(kernelWidth/2d);
			kernel = new double[halfSizeY*2 + 1][halfSizeX*2 + 1];
			//Image kernelImg = new Image(kernel[0].length, kernel.length, 1, 32);
			final int x0 = halfSizeX, y0 = halfSizeY;
			Logger.log("Gabor Kernel: \n");
			for (int i=0; i<kernel.length; i++){
				for (int j=0; j<kernel[0].length; j++){
					final int x = j - x0, y = i - y0;
		
					 kernel[i][j] += Math.exp(- (Math.pow(x, 2)/(2*Math.pow(spreadX, 2)) + Math.pow(y, 2)/(2*Math.pow(spreadY, 2)))) /*gaussian*/ *
						 2*Math.PI*2*(y*Math.cos(delta) + x*Math.sin(delta) + offSet) /*stripes*/;

					//kernelImg.setPixel(j, i, kernel[i][j]);
					Logger.log(kernel[i][j] + " ");
				}
				Logger.log("\n");
			}
			//kernelImg.stretchOrShrinkRange(0, 255);
			update = false;
		}
	}

	@Override
	public double getFilteredPixel(Image image, int x, int y, int band) {
		updateKernel();
		final int halfX = kernelWidth/2, halfY = kernelHeight/2;
		
		delta = (float) (delta * Math.PI / 180);
		
		double result = 0;
		for (int i=y - halfY; i<=y + halfY; i++){
			for (int j=x - halfX; j<=x + halfX; j++){
				final int kerX = j - (x - halfX), kerY = i - (y - halfY);
				
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
