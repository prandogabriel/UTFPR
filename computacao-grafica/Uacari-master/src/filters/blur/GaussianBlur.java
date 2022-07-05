package filters.blur;

import filters.Filter;
import image.Image;
import log.Logger;

/**
 * Uses a 2D-Gaussian Function to blur an image.
 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
 */
public class GaussianBlur extends Filter{
	protected int kernelSizeX = 3, kernelSizeY = 3;
	protected float amplitude = 0.8f;
	protected float spreadX = 1;
	protected float spreadY = 1;
	protected boolean update = true;
	protected double kernelSum = 0;
	protected double[][] kernel = null;
	protected boolean supressPrint = false;
	
	/**
	 * Instantiates a Gaussian Blur filter.
	 * @param image - The image for the filter to be applied to.
	 */
	public GaussianBlur(final Image image){
		this.setImage(image);
	}
	
	/**
	 * Instantiates a Gaussian Blur filter.
	 */
	public GaussianBlur(){
		
	}

	/**
	 * Instantiates a Gaussian Blur filter.
	 * @param kernelSize - the size of the kernel (width and height).
	 * @param amplitude - the amplitude of the gaussian function.
	 * @param spreadX - the spread on the x direction of the gaussian function.
	 * @param spreadY - the spread on the y direction of the gaussian function.
	 */
	public GaussianBlur(final int kernelSize, final float amplitude, final float spreadX, final float spreadY){
		this.setKernelSize(kernelSize);
		this.setAmplitude(amplitude);
		this.setSpreadX(spreadX);
		this.setSpreadY(spreadY);
	}
	/**
	 * Instantiates a Gaussian Blur filter.
	 * @param image - the image for the filter to be applied to.
	 * @param kernelWidth - the width of the gaussian kernel.
	 * @param kernelHeight - the height of the gaussian kernel.
	 * @param amplitude - the amplitude of the gaussian function.
	 * @param spreadX - the spread on the x direction of the gaussian function.
	 * @param spreadY - the spread on the y direction of the gaussian function.
	 */
	public GaussianBlur(final Image image, final int kernelWidth, final int kernelHeight, final float amplitude, final float spreadX, final float spreadY){
		this.setImage(image);
		this.setKernelWidth(kernelWidth);
		this.setKernelHeight(kernelHeight);
		this.setAmplitude(amplitude);
		this.setSpreadX(spreadX);
		this.setSpreadY(spreadY);
	}
	/**
	 * Instantiates a Gaussian Blur filter.
	 * @param kernelWidth - the width of the gaussian kernel.
	 * @param kernelHeight - the height of the gaussian kernel.
	 * @param amplitude - the amplitude of the gaussian function.
	 * @param spreadX - the spread on the x direction of the gaussian function.
	 * @param spreadY - the spread on the y direction of the gaussian function.
	 */
	public GaussianBlur(final int kernelWidth, final int kernelHeight, final float amplitude, final float spreadX, final float spreadY){
		this.setKernelWidth(kernelWidth);
		this.setKernelHeight(kernelHeight);
		this.setAmplitude(amplitude);
		this.setSpreadX(spreadX);
		this.setSpreadY(spreadY);
	}
	/**
	 * Instantiates a Gaussian Blur filter.
	 * @param kernelSize - the kernel size (width and height).
	 * @param amplitude - the amplitude of the gaussian function.
	 */
	public GaussianBlur(final int kernelSize, final float amplitude){
		this.setKernelSize(kernelSize);
		this.setAmplitude(amplitude);
	}
	
	protected void updateKernel(){
		if (update){
			final int halfSizeX = (int) Math.floor(kernelSizeX/2d),
					halfSizeY = (int) Math.floor(kernelSizeX/2d);
			kernel = new double[halfSizeY*2 + 1][halfSizeX*2 + 1];
			final int x0 = halfSizeX, y0 = halfSizeY;
			if (!supressPrint) Logger.log("Gaussian Kernel: \n");
			kernelSum = 0;
			for (int i=0; i<kernel.length; i++){
				for (int j=0; j<kernel[0].length; j++){
					
					kernel[i][j] = amplitude*Math.exp( -( (Math.pow(j - x0, 2)/(2*Math.pow(spreadX,2))) + (Math.pow(i - y0, 2)/(2*Math.pow(spreadY, 2))) ));
					kernelSum += kernel[i][j];
					
					if (!supressPrint) Logger.log(kernel[i][j] + " ");
				}
				if (!supressPrint) Logger.log("\n");
			}
			if (!supressPrint) Logger.log("-----------------\n");
			update = false;
		}
	}
	
	/**
	 * Sets the amplitude of the Gaussian function.
	 * @param amplitude
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setAmplitude(final float amplitude){
		this.amplitude = amplitude;
		this.update = true;
	}
	/**
	 * Sets the sigma x, i.e., the spread of the gaussian function in the x direction.
	 * @param spreadX
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setSpreadX(final float spreadX){
		this.spreadX = spreadX;
		this.update = true;
	}
	/**
	 * Sets the sigma y, i.e., the spread of the gaussian function in the y direction.
	 * @param spreadY
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setSpreadY(final float spreadY){
		this.spreadY = spreadY;
		this.update = true;
	}
	/**
	 * Sets the width of the Gaussian kernel.
	 * @param kernelSizeX
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setKernelWidth(final int kernelSizeX){
		this.kernelSizeX = kernelSizeX;
		this.update = true;
	}
	/**
	 * Sets the height of the Gaussian kernel.
	 * @param kernelSizeY
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setKernelHeight(final int kernelSizeY){
		this.kernelSizeY = kernelSizeY;
		this.update = true;
	}
	/**
	 * Sets the width and height of the Gaussian kernel.
	 * @param kernelSize
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setKernelSize(final int kernelSize){
		this.setKernelWidth(kernelSize);
		this.setKernelHeight(kernelSize);
	}
	
	@Override
	public double getFilteredPixel(Image image, int x, int y, int band) {
		updateKernel();

		double result = 0;
		final int halfSizeX = (int) Math.floor(kernelSizeX/2d),
				halfSizeY = (int) Math.floor(kernelSizeY/2d);
		for (int i=y - halfSizeY; i<= y + halfSizeY; i++){
			for (int j=x - halfSizeX; j<= x + halfSizeX; j++){
				final int kerX = j - (x - halfSizeX), kerY = i - (y - halfSizeY);
				
				result += image.getPixelBoundaryMode(j, i, band) * kernel[kerY][kerX];
				
			}
		}
		
		return result/kernelSum;
	}

}
