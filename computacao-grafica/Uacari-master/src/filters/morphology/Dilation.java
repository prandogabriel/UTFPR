package filters.morphology;

import filters.Filter;
import image.Image;
import morphology.Morphology;
import morphology.MorphologyConstants;


/**
 * A dilation operation from mathematical morphology.
 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
 */
public class Dilation extends Filter implements MorphologyConstants{
	private int timesToDilate = 2;
	
	private Morphology morphology = null;
	private Image structuringElement = STRUCT_FILLED_RING;
	private Image resultImage = null;
	
	
	public Dilation(){
		
	}
	public Dilation(final Image image, final Image structuringElement, final int timesToDilate){
		this.setImage(image);
		this.setStructuringElement(structuringElement);
		this.setTimesToDilate(timesToDilate);
	}
	
	public Dilation(final Image structuringElement, final int timesToDilate){
		this.setStructuringElement(structuringElement);
		this.setTimesToDilate(timesToDilate);
	}
	
	
	
	/**
	 * Sets the structuring element to be used.
	 * @param structuringElement
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setStructuringElement(final Image structuringElement){
		this.structuringElement = structuringElement;
	}
	/**
	 * Sets how many times the image should be dilated.
	 * @param timesToDilate
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setTimesToDilate(final int timesToDilate){
		this.timesToDilate = timesToDilate;
	}

	@Override
	public synchronized double getFilteredPixel(Image image, int x, int y, int band) {
		if (morphology == null){
			morphology = new Morphology();
			
			resultImage = morphology.dilate(image, structuringElement, timesToDilate);
			
		}
		
		return resultImage.getPixelBoundaryMode(x, y, band);
	}
	
	public Image applyFilter(final Image image) {
		Image out = super.applyFilter(image);
		this.morphology = null;
		return out;
	}

}
