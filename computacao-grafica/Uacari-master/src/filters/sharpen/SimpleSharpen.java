package filters.sharpen;

import filters.Filter;
import image.Image;

/**
 * A simple sharpen that uses a simple kernel to sharpen the image.
 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
 */
public class SimpleSharpen extends Filter{
	
	public static final Image SMALL_KERNEL = new Image(new short[][]{{-1,-1,-1},{-1,9,-1},{-1,-1,-1}}),
			SUBTLE_KERNEL = new Image(new short[][]{{-1,-1,-1,-1,-1},{-1,2,2,2,-1},{-1,2,8,2,-1},{-1,2,2,2,-1},{-1,-1,-1,-1,-1}});
	
	private Image kernel = SMALL_KERNEL;
	
	public SimpleSharpen(){
		
	}
	public SimpleSharpen(final Image kernel){
		this.kernel = kernel;
	}

	@Override
	public double getFilteredPixel(Image image, int x, int y, int band) {
		double result = 0;
		final int xS = (int)(kernel.getWidth()/2),
				yS = (int)(kernel.getHeight()/2);
		for (int i=0; i<kernel.getHeight(); i++){
			for (int j=0; j<kernel.getWidth(); j++){
				result += image.getPixelBoundaryMode(x - xS + j, y - yS) * kernel.getPixel(j, i);
			}
		}
		if (result < 0)result = 0;
		return result;
	}

	
	
}
