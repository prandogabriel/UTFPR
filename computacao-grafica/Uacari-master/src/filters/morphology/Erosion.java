package filters.morphology;


import filters.Filter;
import image.Image;
import morphology.Morphology;
import morphology.MorphologyConstants;

/**
 * The erosion operation from mathematical morphology.
 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
 */
public class Erosion extends Filter implements MorphologyConstants{
	private int timesToErode = 2;
	
	private Morphology morphology = null;
	private Image structuringElement = STRUCT_FILLED_RING;
	private Image resultImage = null;
	
	
	public Erosion(){
		
	}
	public Erosion(final Image image, final Image structuringElement, final int timesToErode){
		this.setImage(image);
		this.setStructuringElement(structuringElement);
		this.setTimesToErode(timesToErode);
	}
	
	public Erosion(final Image structuringElement, final int timesToErode){
		this.setStructuringElement(structuringElement);
		this.setTimesToErode(timesToErode);
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
	 * Sets how many times the image should be eroded.
	 * @param timesToErode
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setTimesToErode(final int timesToErode){
		this.timesToErode = timesToErode;
	}

	
	@Override
	public synchronized double getFilteredPixel(Image image, int x, int y, int band) {
		if (morphology == null){
			morphology = new Morphology();
			
			resultImage = morphology.erode(image, structuringElement, timesToErode);
			
		}
		return resultImage.getPixelBoundaryMode(x, y, band);
	}

	public Image applyFilter(final Image image) {
		Image out = super.applyFilter(image);
		this.morphology = null;
		return out;
	}

}
