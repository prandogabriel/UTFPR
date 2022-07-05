package filters.border;

import filters.blur.GaussianBlur;
import image.Image;
import log.Logger;


/**
 * Computes a Gaussian-function based Gradient of the image on both or single directions.
 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
 */
public class GaussianGradient extends GaussianBlur implements GradientConstants{
	protected boolean computeXAxis = true, computeYAxis = true;
	private boolean suppressPrint = false;
	private double[][] kernelX, kernelY;
	private AverageType operationType = TYPE_MEAN;
	
	
	public GaussianGradient(final Image image){
		super(image);
	}
	
	
	/**
	 * Instantiates a Gaussian Gradient filter.
	 */
	public GaussianGradient(){
		this.setToSupressKernelPrint(false);
	}
	
	/**
	 * Instantiates a Gaussian Gradient filter.
	 * @param computeXAxis - compute on the x axis.
	 * @param computeYAxis - compute on the y axis.
	 */
	public GaussianGradient(final boolean computeXAxis, final boolean computeYAxis){
		this.setAxisOrientation(computeXAxis, computeYAxis);
	}
	
	
	/**
	 * Suppress the print of the kernel.
	 * @param suppress
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setToSupressKernelPrint(final boolean suppress){
		this.suppressPrint = suppress;
		super.supressPrint = true;
	}
	
	/**
	 * Sets the orientation of the computation of the gradient.
	 * @param computeXAxis - compute on the x direction.
	 * @param computeYAxis - compute on the y direction.
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setAxisOrientation(final boolean computeXAxis, final boolean computeYAxis){
		if (computeXAxis != this.computeXAxis || computeYAxis != this.computeYAxis) update = true;
		this.computeXAxis = computeXAxis;
		this.computeYAxis = computeYAxis;
	}
	
	protected void updateKernel(){
		if (update){
			super.updateKernel();
			
			final int halfSizeX = (int) Math.floor(kernelSizeX/2d),
					halfSizeY = (int) Math.floor(kernelSizeY/2d);
			
			if (computeXAxis){
				kernelX = new double[kernel.length][kernel[0].length];
				
				for (int i=0; i<kernel.length; i++){
					this.kernelX[i][halfSizeX] = 0;
					
					for (int j=0; j<halfSizeX; j++)
						this.kernelX[i][j] = kernel[i][j];
					
					for (int j= halfSizeX + 1; j<kernel[0].length; j++){
						this.kernelX[i][j] = -1 * kernel[i][j];
					}
				}
				
				
				if (!suppressPrint) Logger.log("Gaussian gradient kernel on the X direction: \n");
				for (int i=0; i<kernelX.length; i++){
					for (int j=0; j<kernelX[0].length; j++){
						if (!suppressPrint) Logger.log(kernelX[i][j] + " ");
					}
					if (!suppressPrint) Logger.log("\n");
				}
				if (!suppressPrint) Logger.log("-----------------\n");
			}
			
			if (computeYAxis){
				kernelY = new double[kernel.length][kernel[0].length];
				
				for (int j=0; j<kernel[0].length; j++){
					this.kernelY[halfSizeY][j] = 0;
					
					for (int i=0; i<halfSizeY; i++)
						this.kernelY[i][j] = kernel[i][j];
					
					for (int i= halfSizeX + 1; i<kernel[0].length; i++){
						this.kernelY[i][j] = -1 * kernel[i][j];
					}
				}
				
				
				if (!suppressPrint) Logger.log("Gaussian gradient kernel on the Y direction: \n");
				for (int i=0; i<kernelY.length; i++){
					for (int j=0; j<kernelY[0].length; j++){
						if (!suppressPrint) Logger.log(kernelY[i][j] + " ");
					}
					if (!suppressPrint) Logger.log("\n");
				}
				if (!suppressPrint) Logger.log("-----------------\n");
			}
			
		}
	}

	@Override
	public double getFilteredPixel(Image image, int x, int y, int band) {
		updateKernel();

		double resultX = 0, resultY = 0, result = 0;
		final int halfSizeX = (int) Math.floor(kernelSizeX/2d),
				halfSizeY = (int) Math.floor(kernelSizeY/2d);
		
		//kernelSum = 0;
		for (int i=y - halfSizeY; i<= y + halfSizeY; i++){
			for (int j=x - halfSizeX; j<= x + halfSizeX; j++){
				final int kerX = j - (x - halfSizeX), kerY = i - (y - halfSizeY);
				final double p = image.getPixelBoundaryMode(j, i, band);
				
				if (computeXAxis){
					resultX += (p * kernelX[kerY][kerX]);
				}
				if (computeYAxis){
					resultY += (p * kernelY[kerY][kerX]);
				}
			}
		}
		
		float divisor = 0;
		result = (computeXAxis ? resultX : resultY);
		if (computeXAxis && computeYAxis){
			switch(operationType){
			case TYPE_MEAN:
				result = (resultX + resultY)/2f;
				break;
			case TYPE_SUM:
				result = resultX + resultY;
				break;
			case TYPE_DIFFERENCE:
				result = Math.abs(resultX - resultY);
				break;
			case TYPE_PRODUCT:
				result = resultX * resultY;
				break;
			case TYPE_DIVISION_Y:
				divisor = (float) ((resultY != 0) ? resultY : 1);
				result = resultX/divisor;
				break;
			case TYPE_DIVISION_X:
				divisor = (float) ((resultX != 0) ? resultX : 1);
				result = resultY/divisor;
				break;
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
