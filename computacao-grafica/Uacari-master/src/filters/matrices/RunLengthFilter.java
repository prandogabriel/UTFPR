package filters.matrices;

import static distances.Distance.CHEBYSHEV_DISTANCE;

import distances.Distance;
import filters.Filter;
import image.Image;
import matrices.RunLengthMatrix;

interface RunLengthConstants{
	public static final RunLengthType TYPE_RUN_PERCENTAGE = RunLengthType.TYPE_RUN_PERCENTAGE,
			TYPE_GREY_LEVEL_NON_UNIFORMITY = RunLengthType.TYPE_GREY_LEVEL_NON_UNIFORMITY;
	public static enum RunLengthType{ TYPE_RUN_PERCENTAGE, TYPE_GREY_LEVEL_NON_UNIFORMITY;}
}

/**
 * A filter based on the run length matrix.
 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
 */
public class RunLengthFilter extends Filter implements RunLengthConstants{

	private int kernelSizeX = 7, kernelSizeY = 7;
	private Distance kernelRadialDistance = CHEBYSHEV_DISTANCE;
	private int orientation = 0;
	
	private RunLengthType operation = TYPE_GREY_LEVEL_NON_UNIFORMITY;
	
	public RunLengthFilter(){
		
	}
	public RunLengthFilter(final Image image){
		this.setImage(image);
	}
	
	public RunLengthFilter(final Image image, final int orientation){
		this.setImage(image);
		this.setOrientation(orientation);
	}
	
	public RunLengthFilter(final Image image, final int orientation, final Distance kernelRadialDistance){
		this.setImage(image);
		this.setOrientation(orientation);
		this.setKernelRadialDistanceMeasure(kernelRadialDistance);
	}
	
	public RunLengthFilter(final Image image, final int orientation, final int kernelSize){
		this.setImage(image);
		this.setOrientation(orientation);
		this.setKernelSize(kernelSize);
	}
	
	public RunLengthFilter(final Image image, final int orientation, final int kernelSize, final RunLengthType operationType){
		this.setImage(image);
		this.setOrientation(orientation);
		this.setKernelSize(kernelSize);
		this.setOperationType(operationType);
	}
	
	/**
	 * Sets the equation to be computed from the local co-occurrence matrices.
	 * @param type
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setOperationType(final RunLengthType type){
		this.operation = type;
	}
	/**
	 * Sets the neighbourhood size around the processed pixel.
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
		this.kernelSizeX = kernelWidth % 2 == 0 ? kernelWidth + 1 : kernelWidth;
	}
	/**
	 * Sets the height of the kernel.
	 * @param kernelHeight
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setKernelHeight(final int kernelHeight){
		this.kernelSizeX = kernelHeight % 2 == 0 ? kernelHeight + 1 : kernelHeight;
	}
	/**
	 * Sets the distance type of the computation. If the euclidean distance is set, then a circular region around the iterated pixel is regarded.
	 * If the chebyshev distance is chosen, then a squared region would be regarded instead.
	 * @param distance
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setKernelRadialDistanceMeasure(final Distance distance){
		this.kernelRadialDistance = distance;
	}
	
	/**
	 * Sets the orientation of the run length computation. It can be either 0, 45, 90 or 135.
	 * @param orientation
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setOrientation(final int orientation){
		this.orientation = orientation;
	}

	
	
	@Override
	public double getFilteredPixel(Image image, int x, int y, int band) {
		Image neighImage = new Image(kernelSizeX, kernelSizeY, 1, 32, true);
		
		final int sX = kernelSizeX/2, sY = kernelSizeY/2;
		
		for (int i=y - sY; i <= y + sY; i++){
			for (int j=x - sX; j <= x + sX; j++){
				neighImage.setPixel(j - x + sX, i - y + sY, band, image.getPixelBoundaryMode(j, i, band));
			}
		}
		
		RunLengthMatrix rMatrix = new RunLengthMatrix(neighImage);
		rMatrix.setBand(band);
		rMatrix.setOrientation(orientation);
		rMatrix.setKernelRadialDistanceMeasure(kernelRadialDistance);
		
		double result = 0;
		
		switch(operation){
		case TYPE_RUN_PERCENTAGE:
			result = rMatrix.getRunPercentage();
			break;
		case TYPE_GREY_LEVEL_NON_UNIFORMITY:
			result = rMatrix.getGreyLevelNonUniformity();
			break;
		}
		
		return result;
	}
	
	public Image applyFilter(final Image image) {
		Image out = super.applyFilter(image);
		out.stretchOrShrinkRange(0, 255);
		return out;
	}
	
}
