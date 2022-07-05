package filters.blur;

import filters.Filter;
import image.Image;

import static distances.Distance.*;

import java.util.ArrayList;

import distances.Distance;


/**
 * An average blur.
 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
 */
interface MeanBlurConstants {
	public static enum AverageType{TYPE_ARITHMETIC_MEAN, TYPE_GEOMETRIC_MEAN, TYPE_MEDIAN, TYPE_MAX, TYPE_MIN;}
	public static final AverageType TYPE_MAX = AverageType.TYPE_MAX, TYPE_ARITHMETIC_MEAN = AverageType.TYPE_ARITHMETIC_MEAN,
			TYPE_GEOMETRIC_MEAN = AverageType.TYPE_GEOMETRIC_MEAN, TYPE_MEDIAN = AverageType.TYPE_MEDIAN,
			TYPE_MIN = AverageType.TYPE_MIN;
}

public class MeanBlur extends Filter implements MeanBlurConstants{
	private int kernelWidth = 7, kernelHeight = 7;
	
	private AverageType operationType = TYPE_MAX;
	private Distance distance = EUCLIDEAN_DISTANCE;
	
	public MeanBlur(){
		
	}
	public MeanBlur(final int kernelSize, final AverageType operationType){
		this.setKernelSize(kernelSize);
		this.setOperationType(operationType);
	}
	public MeanBlur(final Image image, final int kernelSize, final AverageType operationType){
		this.setKernelSize(kernelSize);
		this.setOperationType(operationType);
		this.setImage(image);
	}
	public MeanBlur(final Image image, final int kernelSize, final AverageType operationType, final Distance radialDistance){
		this.setKernelSize(kernelSize);
		this.setOperationType(operationType);
		this.setImage(image);
		this.setRadialDistance(radialDistance);
	}
	public MeanBlur(final Image image, final int kernelWidth, final int kernelHeight, final AverageType operationType, final Distance radialDistance){
		this.setKernelWidth(kernelWidth);
		this.setKernelHeight(kernelHeight);
		this.setOperationType(operationType);
		this.setImage(image);
		this.setRadialDistance(radialDistance);
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
	 * Sets the width of the kernel.
	 * @param kernelWidth
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setKernelWidth(final int kernelWidth){
		this.kernelWidth = kernelWidth;
		if (kernelWidth % 2 == 0) this.kernelWidth ++;
	}
	
	/**
	 * Sets the height of the kernel.
	 * @param kernelHeight
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setKernelHeight(final int kernelHeight){
		this.kernelHeight = kernelHeight;
		if (kernelHeight % 2 == 0) this.kernelHeight ++;
	}
	
	/**
	 * Sets the distance for each pixel. If Euclidean Distance is selected, then a circular region around each pixel is regarded.
	 * If Chebyshev Distance is selected, for instance, a squared region around each pixel is regarded instead.
	 * @param distance
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setRadialDistance(final Distance distance){
		this.distance = distance;
	}

	/**
	 * Sets the operation type for the mean blur, arithmetic, geometric, etc.
	 * @param operationType
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setOperationType(final AverageType operationType){
		this.operationType = operationType;
	}
	
	
	@Override
	public double getFilteredPixel(Image image, int x, int y, int band) {
		final int sX = kernelWidth/2,
				sY = kernelHeight/2;
		
		ArrayList<Double> values = null;
		if (operationType == TYPE_MEDIAN) values = new ArrayList<Double>(kernelWidth*kernelHeight);
		
		double result = 0;
		int counter = 0;
		
		boolean firstTime = true;
		
		for (int i= y - sY; i<= y + sY; i++){
			for (int j= x - sX; j<= x + sX; j++){
				
				if (distance.compute(x, y, j, i) > (kernelWidth/2f > kernelHeight/2f ? kernelWidth/2f : kernelHeight/2f)) continue;
				
				switch(operationType){
				case TYPE_ARITHMETIC_MEAN:
					result += image.getPixelBoundaryMode(j, i, band);
					break;
				case TYPE_GEOMETRIC_MEAN:
					if (firstTime) {result = 1; firstTime = false;}
					result *= image.getPixelBoundaryMode(j, i, band);
					break;
				case TYPE_MAX:
					if (firstTime) {result = Integer.MIN_VALUE; firstTime = false;}
					result = (image.getPixelBoundaryMode(j, i, band) > result) ? image.getPixelBoundaryMode(j, i, band) : result;
					break;
				case TYPE_MIN:
					if (firstTime) {result = Integer.MAX_VALUE; firstTime = false;}
					result = (image.getPixelBoundaryMode(j, i, band) < result) ? image.getPixelBoundaryMode(j, i, band) : result;
					break;
				case TYPE_MEDIAN:
					if (values.size() == 0) values.add(image.getPixelBoundaryMode(j, i, band));
					else{
						For:
						for (int k=0; k<values.size(); k++){
							if (values.get(k) > image.getPixelBoundaryMode(j, i, band)){
								values.add(k, image.getPixelBoundaryMode(j, i, band));
								break For;
							}
						}
					}
					break;
				}
				
				counter++;
			}
		}
		
		switch(operationType){
		case TYPE_ARITHMETIC_MEAN:
			result /= counter;
			break;
		case TYPE_GEOMETRIC_MEAN:
			result = Math.pow(result, 1f/(counter));
			break;
		case TYPE_MEDIAN:
			result = values.get(values.size()/2);
			break;
		}
		
		return result;
	}
	
	public Image applyFilter(final Image image) {
		return super.applyFilter(image);
	}
}
