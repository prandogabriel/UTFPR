package filters.border;

import image.Image;

interface HighOrderGradientConstants{
	public static HighOrderGradientType TYPE_LINEAR = HighOrderGradientType.LINEAR,
			TYPE_GAUSSIAN = HighOrderGradientType.GAUSSIAN;
	public static enum HighOrderGradientType{LINEAR, GAUSSIAN;}
}

/**
 * Computers high order gradients or derivatives from the image.
 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
 */
public class HighOrderGradient extends GaussianGradient implements HighOrderGradientConstants{
	
	
	private HighOrderGradientType type = TYPE_GAUSSIAN;
	
	private GaussianGradient gGradient = null;
	private LinearGradient lGradient = null;
	
	private int linearGradientDistance = 1;
	private int gradientOrder = 2;
	private boolean update = true;
	
	public HighOrderGradient(){
		
	}
	
	public HighOrderGradient(final Image image){
		this.setImage(image);
	}
	
	public HighOrderGradient(final Image image, final HighOrderGradientType type){
		this.setImage(image);
		this.setType(type);
	}
	
	public HighOrderGradient(final Image image, final HighOrderGradientType type, final int gradientOrder){
		this.setImage(image);
		this.setType(type);
		this.setGradientOrder(gradientOrder);
	}
	
	/**
	 * Sets the type of the high order gradient, it either can be linear or Gaussian-based.
	 * @param type
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setType(HighOrderGradientType type){
		if (this.type != type) update = true;
		this.type = type;
	}
	/**
	 * Sets the Gaussian Gradient if you have already instantiated it.
	 * @param gaussianGradient
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setGaussianGradient(GaussianGradient gaussianGradient){
		update = true;
		this.gGradient = gaussianGradient;
	}
	/**
	 * Sets the Linear Gradient if you have already instantiated it.
	 * @param lGradient
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setLinearGradient(LinearGradient lGradient){
		update = true;
		this.lGradient = lGradient;
	}
	/**
	 * Sets the distance for the computation of the linear gradient. Only applies to the linear gradient.
	 * @param distance
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setLinearGradientDistance(final int distance){
		update = true;
		this.linearGradientDistance = distance;
	}
	/**
	 * Sets the order of the derivatives or gradient. 
	 * @param order
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setGradientOrder(final int order){
		if (gradientOrder != order) update = true;
		this.gradientOrder = order;
	}


	private Image cachedGradient = null;
	
	@Override
	public double getFilteredPixel(Image image, int x, int y, int band) {
		synchronized(this){
			if ((type == TYPE_GAUSSIAN && gGradient == null) || (type == TYPE_LINEAR && lGradient == null) || update){
				update = false;
				
				System.out.println("bla");
				
				if (type == TYPE_GAUSSIAN){
					gGradient = new GaussianGradient();
					gGradient.setAmplitude(this.amplitude);
					gGradient.setAxisOrientation(computeXAxis, computeYAxis);
					gGradient.setKernelHeight(kernelSizeY);
					gGradient.setKernelWidth(kernelSizeX);
					gGradient.setSpreadX(spreadX);
					gGradient.setSpreadY(spreadY);
					gGradient.setImage(image);
					cachedGradient = gGradient.getFilteredImage();
				}else if (type == TYPE_LINEAR){
					lGradient = new LinearGradient();
					lGradient.setDistance(linearGradientDistance);
					lGradient.setImage(image);
					lGradient.setAxisOrientation(computeXAxis, computeYAxis);
					cachedGradient = lGradient.getFilteredImage();
				}
				
				for (int k=1; k<gradientOrder; k++){
					
					if (type == TYPE_GAUSSIAN){
						gGradient.setImage(cachedGradient);
						cachedGradient = gGradient.getFilteredImage();
					}else if (type == TYPE_LINEAR){
						lGradient.setImage(cachedGradient);
						cachedGradient = lGradient.getFilteredImage();
					}
					
				}
				
			}
		}
		return cachedGradient.getPixel(x, y, band);
	}
	
	
	

}
