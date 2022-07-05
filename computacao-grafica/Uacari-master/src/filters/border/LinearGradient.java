package filters.border;

import filters.Filter;
import image.Image;


/**
 * Linear gradient that combines the derivatives in the x and y directions.
 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
 */
public class LinearGradient extends Filter implements GradientConstants{
	/**
	 * OPERATION INDEXES
	 */


	
	private int distance = 1;
	private AverageType operation = TYPE_MEAN;
	private boolean computeXAxis = true, computeYAxis = true;
	
	public LinearGradient(){
		
	}
	public LinearGradient(final int distance, final AverageType operation){
		this.setDistance(distance);
		this.setOperation(operation);
	}
	public LinearGradient(final Image image, final int distance, final AverageType operation){
		this.setImage(image);
		this.setDistance(distance);
		this.setOperation(operation);
	}
	
	public LinearGradient(final Image image, final int distance){
		this.setImage(image);
		this.setDistance(distance);
	}
	
	public void setImage(final Image image){
		super.setImage(image);
	}
	
	public void setDistance(final int distance){this.distance = distance;}
	/**
	 * Sets the operation when averaging both derivatives (x and y axes).
	 * @param operation
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setOperation(final AverageType operation){this.operation = operation;}
	public void setAxisOrientation(final boolean computeXAxis, final boolean computeYAxis){
		this.computeXAxis = computeXAxis;
		this.computeYAxis = computeYAxis;
	}
	

	@Override
	public double getFilteredPixel(Image image, int x, int y, int band) {
		
		final double xDer = image.getPixelBoundaryMode(x - distance, y, band) - image.getPixelBoundaryMode(x + distance, y, band),
				yDer = image.getPixelBoundaryMode(x, y - distance, band) - image.getPixelBoundaryMode(x, y + distance, band);
		
	
		
		double result = (computeXAxis ? xDer : yDer);
		double divisor = 0;
		if (computeXAxis && computeYAxis){
			switch(operation){
			case TYPE_MEAN:
				result = (xDer + yDer)/2d;
				break;
			case TYPE_SUM:
				result = (xDer + yDer);
				break;
			case TYPE_DIFFERENCE:
				result = Math.abs(xDer - yDer);
				break;
			case TYPE_PRODUCT:
				result = (xDer * yDer);
				break;
			case TYPE_DIVISION_Y:
				divisor = (yDer != 0d) ? yDer : 1d;
				result = (xDer / divisor);
				break;
			case TYPE_DIVISION_X:
				divisor = (xDer != 0d) ? xDer : 1d;
				result = (yDer / divisor);
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
