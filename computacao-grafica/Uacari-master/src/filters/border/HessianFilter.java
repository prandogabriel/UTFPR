package filters.border;

import filters.Filter;
import image.Image;


interface HessianFilterConstants{
	public static final HessianOperationType TYPE_ELEMENT0_0 = HessianOperationType.TYPE_ELEMENT0_0, TYPE_ELEMENT0_1 = HessianOperationType.TYPE_ELEMENT0_1, 
			TYPE_ELEMENT1_0 = HessianOperationType.TYPE_ELEMENT1_0, TYPE_ELEMENT1_1 = HessianOperationType.TYPE_ELEMENT1_1,
	TYPE_MODULE = HessianOperationType.TYPE_MODULE, TYPE_TRACE = HessianOperationType.TYPE_TRACE, 
	TYPE_DETERMINANT = HessianOperationType.TYPE_DETERMINANT, TYPE_FIRST_EIGENVALUE = HessianOperationType.TYPE_FIRST_EIGENVALUE, 
	TYPE_SECOND_EIGENVALUE = HessianOperationType.TYPE_SECOND_EIGENVALUE, TYPE_ORIENTATION = HessianOperationType.TYPE_ORIENTATION,
	TYPE_GAMMA_NORM_SQUARE_EIGENVALUE_DIFFERENCE = HessianOperationType.TYPE_GAMMA_NORM_SQUARE_EIGENVALUE_DIFFERENCE, 
	TYPE_SQUARE_OF_GAMMA_NORM_EINGENVALUE_DIFFERENCE = HessianOperationType.TYPE_GAMMA_NORM_SQUARE_EIGENVALUE_DIFFERENCE, 
	TYPE_FIRST_EIGENVALUE_2 = HessianOperationType.TYPE_FIRST_EIGENVALUE_2,	TYPE_SECOND_EIGENVALUE_2 = HessianOperationType.TYPE_SECOND_EIGENVALUE_2;
	public static enum HessianOperationType{ TYPE_ELEMENT0_0, TYPE_ELEMENT0_1, TYPE_ELEMENT1_0, TYPE_ELEMENT1_1,
		TYPE_MODULE, TYPE_TRACE, TYPE_DETERMINANT, TYPE_FIRST_EIGENVALUE, TYPE_SECOND_EIGENVALUE, TYPE_ORIENTATION,
		TYPE_GAMMA_NORM_SQUARE_EIGENVALUE_DIFFERENCE, TYPE_SQUARE_OF_GAMMA_NORM_EINGENVALUE_DIFFERENCE, TYPE_FIRST_EIGENVALUE_2,
		TYPE_SECOND_EIGENVALUE_2;}
}



/**
 * Filters the image according to properties of the Hessian Matrix (good to find and analyse borders)..
 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
 */
public class HessianFilter extends Filter implements HessianFilterConstants{

	
	private boolean useGaussianGradient = true; //if set to false it will use a linear one - Sobel-like (faster)11
	private int distance = 1;
	private Image firstDerivativeX = null,
			firstDerivativeY = null;
	Filter xDer2, xyDer, yDer2, yxDer;
	private float spreadX = 3f, spreadY = 3f;
	private float amplitude = 10;
	private int kernelWidth = 5, kernelHeight = 5;
	
	
	private HessianOperationType operation = TYPE_TRACE;
	
	public HessianFilter(){
		
	}
	public HessianFilter(final int distance, final HessianOperationType operation){
		this.setDistance(distance);
		this.setOperationType(operation);
	}
	public HessianFilter(final HessianOperationType operation){
		this.setOperationType(operation);
	}
	public HessianFilter(final Image image, final int distance, final HessianOperationType operation){
		super(image);
		this.setDistance(distance);
		this.setOperationType(operation);
	}
	public HessianFilter(final Image image, final HessianOperationType operation){
		super(image);
		this.setOperationType(operation);
	}
	public HessianFilter(final Image image){
		super(image);
	}
	
	@Override
	public void setImage(Image image){
		super.setImage(image);
		update = true;
	}
	/**
	 * @param useGaussianGradient
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setToUseGaussianGradient(boolean useGaussianGradient){
		this.useGaussianGradient = useGaussianGradient;
	}
	/**
	 * Sets the spreading on the x direction of the Gaussian functions (derivatives).
	 * @param spreadX
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setSpreadX(final float spreadX){
		this.spreadX = spreadX;
	}
	/**
	 * Sets the spreading on the y direction of the Gaussian functions (used to compute the derivatives).
	 * @param spreadX
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setSpreadY(final float spreadY){
		this.spreadY = spreadY;
	}
	/**
	 * Sets the kernel width.
	 * @param kernelWidth
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setKernelWidth(final int kernelWidth){
		this.kernelWidth = kernelWidth;
	}
	/**
	 * Sets the kernel height.
	 * @param kernelHeight
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setKernelHeight(final int kernelHeight){
		this.kernelHeight = kernelHeight;
	}
	/**
	 * Sets the amplitude of the Gaussian functions (used to compute the derivatives).
	 * @param amplitude
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setAmplitude(final float amplitude){
		this.amplitude = amplitude;
	}
	/**
	 * Sets the scale or the magnitude of the detection. Increases the spreading on the x and y directions and the kernel size.
	 * @param scale
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setScale(float scale){
		//this.setAmplitude((float)(2*Math.PI*Math.pow(sigma,2)));
		
		this.setSpreadX(scale);
		this.setSpreadY(scale);
		
		if (3*scale < 3)
			scale = 1;
		this.setKernelHeight((int)(3*scale));
		this.setKernelWidth((int)(3*scale));
		
		
		//if (sigma < 3)
		//	sigma = 3;
		
	}
	
	/**
	 * Sets the distance of the linear gradient. It is a linear gradient parameter only, it does nothing if GaussianGradient is set to be used.
	 * @param distance
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setDistance(final int distance){this.distance = distance;}
	/**
	 * Sets the type of the operation for the filter. The selected option will define the value to be placed on the filtered image, based on the Hessian Matrix.
	 * @param operation
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setOperationType(final HessianOperationType operation){this.operation = operation;}
	
	private int lastBand = -1;
	
	@Override
	public double getFilteredPixel(Image image, int x, int y, int band) {
		if (firstDerivativeX == null || update || lastBand != band){
			lastBand = band;
			
			if (!useGaussianGradient){
				LinearGradient xDer = new LinearGradient(image, distance);
				xDer.setAxisOrientation(true, false);
				LinearGradient yDer = new LinearGradient(image, distance);
				yDer.setAxisOrientation(false, true);
				
				firstDerivativeX = xDer.getFilteredImage();
				firstDerivativeY = yDer.getFilteredImage();
				
				xDer2 = new LinearGradient(firstDerivativeX, distance);
				((LinearGradient)xDer2).setAxisOrientation(true, false);
				yDer2 = new LinearGradient(firstDerivativeY, distance);
				((LinearGradient)yDer2).setAxisOrientation(false, true);
				xyDer = new LinearGradient(firstDerivativeY, distance);
				((LinearGradient)xyDer).setAxisOrientation(true, false);
				yxDer = new LinearGradient(firstDerivativeX, distance);
				((LinearGradient)yxDer).setAxisOrientation(false, true);
			}else{
				
				
				GaussianGradient xDer = new GaussianGradient(image);
				xDer.setAxisOrientation(true, false);
				xDer.setSpreadX(spreadX);
				xDer.setSpreadY(spreadY);
				xDer.setAmplitude(amplitude);
				xDer.setKernelHeight(kernelHeight);
				xDer.setKernelWidth(kernelWidth);
				
				firstDerivativeX = xDer.getFilteredImage();
				
				GaussianGradient yDer = new GaussianGradient(image);
				yDer.setAxisOrientation(false, true);
				yDer.setSpreadX(spreadX);
				yDer.setSpreadY(spreadY);
				yDer.setAmplitude(amplitude);
				yDer.setKernelWidth(kernelWidth);
				yDer.setKernelHeight(kernelHeight);
				
				firstDerivativeY = yDer.getFilteredImage();
				
				
				xDer2 = new GaussianGradient(firstDerivativeX);
				((GaussianGradient)xDer2).setAxisOrientation(true, false);
				((GaussianGradient)xDer2).setSpreadX(spreadX); ((GaussianGradient)xDer2).setSpreadY(spreadY);
				((GaussianGradient)xDer2).setAmplitude(amplitude); ((GaussianGradient)xDer2).setKernelHeight(kernelHeight); ((GaussianGradient)xDer2).setKernelWidth(kernelWidth);
				yDer2 = new GaussianGradient(firstDerivativeY);
				((GaussianGradient)yDer2).setAxisOrientation(false, true);
				((GaussianGradient)yDer2).setSpreadX(spreadX); ((GaussianGradient)yDer2).setSpreadY(spreadY);
				((GaussianGradient)yDer2).setAmplitude(amplitude); ((GaussianGradient)yDer2).setKernelHeight(kernelHeight); ((GaussianGradient)yDer2).setKernelWidth(kernelWidth);
				xyDer = new GaussianGradient(firstDerivativeY);
				((GaussianGradient)xyDer).setAxisOrientation(true, false);
				((GaussianGradient)xyDer).setSpreadX(spreadX); ((GaussianGradient)xyDer).setSpreadY(spreadY);
				((GaussianGradient)xyDer).setAmplitude(amplitude); ((GaussianGradient)xyDer).setKernelHeight(kernelHeight); ((GaussianGradient)xyDer).setKernelWidth(kernelWidth);
				yxDer = new GaussianGradient(firstDerivativeX);
				((GaussianGradient)yxDer).setAxisOrientation(false, true);
				((GaussianGradient)yxDer).setSpreadX(spreadX); ((GaussianGradient)yxDer).setSpreadY(spreadY);
				((GaussianGradient)yxDer).setAmplitude(amplitude); ((GaussianGradient)yxDer).setKernelHeight(kernelHeight); ((GaussianGradient)yxDer).setKernelWidth(kernelWidth);
			}
		
			update = false;
		}
		
		double a = xDer2.getFilteredPixel(x, y, band),
				b = xyDer.getFilteredPixel(x, y, band),
				c = yxDer.getFilteredPixel(x, y, band),
				d = yDer2.getFilteredPixel(x, y, band);
		
		double result = 0;
		switch(operation){
		case TYPE_ELEMENT0_0:
			result = a;
			break;
		case TYPE_ELEMENT0_1:
			result = b;
			break;
		case TYPE_ELEMENT1_0:
			result = c;
			break;
		case TYPE_ELEMENT1_1:
			result = d;
			break;
		case TYPE_MODULE:
			result = Math.sqrt(Math.pow(a, 2) + b * c + Math.pow(d, 2));
			break;
		case TYPE_TRACE:
			result = a + d;
			break;
		case TYPE_DETERMINANT:
			result = a*d - c * b;
			break;
		case TYPE_FIRST_EIGENVALUE_2:
			result = (a + d)/2f + Math.sqrt((4*Math.pow(b, 2) + Math.pow(a - d, 2))/(2f));
			//result = 1/2f * (-Math.sqrt(a*a - (2*a*d) + (4*b*c) + d*d) + a + d);
			break;
		case TYPE_FIRST_EIGENVALUE:
			//result = (a + d)/2f + Math.sqrt((4*Math.pow(b, 2) + Math.pow(a - d, 2))/(2f));
			result = 1/2f * (-Math.sqrt(a*a - (2*a*d) + (4*b*c) + d*d) + a + d);
			break;
		case TYPE_SECOND_EIGENVALUE_2:
			result = (a + d)/2f - Math.sqrt((4*Math.pow(b, 2) + Math.pow(a - d, 2))/(2f));
			//result = 1/2f *(Math.sqrt(a*a - 2*a*d + 4*b*c + d*d) + a + d);
			break;
		case TYPE_SECOND_EIGENVALUE:
			//result = (a + d)/2f - Math.sqrt((4*Math.pow(b, 2) + Math.pow(a - d, 2))/(2f));
			result = 1/2f *(Math.sqrt(a*a - 2*a*d + 4*b*c + d*d) + a + d);
			break;
		case TYPE_ORIENTATION:
			result = 1/2f * Math.acos((4*Math.pow(b, 2) + Math.pow(a - d, 2))/(10000000f));
			break;
		case TYPE_GAMMA_NORM_SQUARE_EIGENVALUE_DIFFERENCE:
			result = /*Math.pow(Math.pow(1, 3/4f), 4) * */Math.pow(a - d, 2) * (Math.pow(a - d, 2) + 4*Math.pow(b, 2));
			break;
		case TYPE_SQUARE_OF_GAMMA_NORM_EINGENVALUE_DIFFERENCE:
			result = /*Math.pow(Math.pow(1, 3/4f), 4) * */(Math.pow(a - d, 2) + 4*Math.pow(b, 2));
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
