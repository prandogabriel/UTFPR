package filters.blur;

import static distances.Distance.*;

import distances.Distance;
import image.Image;

/**
 * Box blur filter. Uses the Chebyshev Distance to compute the blur in a box-fashion.
 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
 */
public class BoxBlur extends MeanBlur{

	public BoxBlur(){
		super();
		super.setRadialDistance(CHEBYSHEV_DISTANCE);
	}
	
	public BoxBlur(final int kernelSize, final AverageType operationType){
		super(kernelSize, operationType);
	}
	public BoxBlur(final Image image, final int kernelSize, final AverageType operationType){
		super(image, kernelSize, operationType);
	}
	public BoxBlur(final Image image, final int kernelSize, final AverageType operationType, final Distance radialDistance){
		super(image, kernelSize, operationType, radialDistance);
	}
	public BoxBlur(final Image image, final int kernelWidth, final int kernelHeight, final AverageType operationType, final Distance radialDistance){
		super(image, kernelWidth, kernelHeight, operationType, radialDistance);
	}
	
}
