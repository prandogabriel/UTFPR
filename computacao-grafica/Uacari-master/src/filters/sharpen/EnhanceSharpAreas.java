package filters.sharpen;

import filters.Filter;
import filters.blur.GaussianBlur;
import image.Image;

/**
 * Enchance sharpen areas based on a "laplacian-gaussian" filter.
 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
 */
public class EnhanceSharpAreas extends Filter{
	private float kernelWidth = 10, kernelHeight = 10, amplitude = 2, differenceX = 0.1f, differenceY = 0.1f,
			spreadX = 1, spreadY = 1;
	
	private GaussianBlur g1 = null, g2 = null;
	
	public EnhanceSharpAreas(){
		
	}
	/**
	 * 
	 * @param kernelSize
	 * @param amplitude
	 * @param differenceX - the lower the difference, the less pixels are selected, it should be lower than 1
	 * @param differenceY - the lower the difference, the less pixels are selected, it should be lower than 1
	 */
	public EnhanceSharpAreas(final int kernelSize, final float amplitude, final float differenceX, final float differenceY){
		this.setKernelSize(kernelSize);
		this.setAmplitude(amplitude);
		this.setDifferenceX(differenceX);
		this.setDifferenceY(differenceY);
	}
	
	public void setSpreadX(final float spreadX){this.spreadX = spreadX;}
	public void setSpreadY(final float spreadY){this.spreadY = spreadY;}
	public void setKernelSize(final int kernelSize){this.setKernelWidth(kernelSize); this.setKernelHeight(kernelSize);}
	public void setKernelWidth(final int kernelWidth){this.kernelWidth = kernelWidth; this.update = true;}
	public void setKernelHeight(final int kernelHeight){this.kernelHeight = kernelHeight; this.update = true;}
	public void setAmplitude(final float amplitude){this.amplitude = amplitude; this.update = true;}
	/**
	 * The lower the difference the less pixels are selected, the difference should be lower than 1.
	 * @param differenceX
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setDifferenceX(final float differenceX){this.differenceX = differenceX; this.update = true;}
	/**
	 * The lower the difference the less pixels are selected, the difference should be lower than 1.
	 * @param differenceY
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setDifferenceY(final float differenceY){this.differenceY = differenceY; this.update = true;}
	

	@Override
	public double getFilteredPixel(Image image, int x, int y, int band) {
		if (g1 == null || update){
			g1 = new GaussianBlur((int)kernelWidth, (int)kernelHeight, amplitude, spreadX - differenceX, spreadY - differenceY);
			g2 = new GaussianBlur((int)kernelWidth, (int)kernelHeight, amplitude, spreadX, spreadY);
			update = false;
		}
		
		double result = g1.getFilteredPixel(image, x, y, band) - 
				g2.getFilteredPixel(image, x, y, band);
		
		return result;
	}
	
	public Image applyFilter(final Image image) {
		Image out = super.applyFilter(image);
		out.stretchOrShrinkRange(0, 255);
		return out;
	}

}
