package filters.border;

import filters.Filter;
import image.Image;

/**
 * Classifical Sobel filter //TO BE FINISHED
 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
 */
public class Sobel extends Filter{
	private boolean vertical = true, horizontal = true;
	private float weight = 1;
	private Image kernelDirectionX = new Image(new short[][]{{-1,0,1},{-2,0,2},{-1,0,1}}), 
			kernelDirectionY = new Image(new short[][]{{1,2,1},{0,0,0},{-1,-2,-1}});
	
	
	public Sobel(Image image, boolean applyHorizontally, boolean applyVertically){
		this.setImage(image);
		this.horizontal = applyHorizontally;
		this.vertical = applyVertically;
	}
	public Sobel(){	}
	public Sobel(boolean applyHorizontally, boolean applyVertically){
		this.vertical = applyVertically;
		this.horizontal = applyHorizontally;
	}
	
	/**
	 * Sets the kernel for the computation of the Sobel filter in the x direction.
	 * @param filter
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setFilterX(Image filter){this.kernelDirectionX = filter;}
	/**
	 * Sets the kernel for the computation of the Sobel filter in the y direction.
	 * @param filter
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setFilterY(Image filter){this.kernelDirectionY = filter;}

	
	
	@Override
	public double getFilteredPixel(Image image, int x, int y, int band) {
		double result = 0;	
		final int halfX = kernelDirectionX.getWidth()/2, halfY = kernelDirectionY.getHeight()/2;
		for (int i=0; i<kernelDirectionX.getHeight(); i++){
			for (int j=0; j<kernelDirectionY.getWidth(); j++){
				if (vertical && horizontal) result += (image.getPixelBoundaryMode(x - halfX + j, y - halfY + i, band) * kernelDirectionY.getPixel(j, i) + 
						image.getPixelBoundaryMode(x - halfX + j, y - halfY + i, band) * kernelDirectionX.getPixel(j, i))/2f;
				else if (vertical) result += image.getPixelBoundaryMode(x - halfX + j, y - halfY + i, band) * kernelDirectionY.getPixel(j, i);
				else if (horizontal) result += image.getPixelBoundaryMode(x - halfX + j, y - halfY + i, band) * kernelDirectionX.getPixel(j, i);
			}
		}
		return result;
	}
	

	

}
