package filters;

import image.Image;

public abstract class Filter {
	protected boolean update = true;
	
	public Filter(){
		
	}
	public Filter(final Image image){
		this.setImage(image);
	}
	
	protected Image image = null;

	public Image applyFilter(){
		return applyFilter(image);
	}
	public Image applyFilter(final Image image) {
		Image out = new Image(image.getWidth(), image.getHeight(), image.getNumBands(), 32, true);
		this.setImage(image);
		for (int b=0; b<image.getNumBands(); b++){
			for(int i=0; i<image.getHeight(); i++){
				for (int j=0; j<image.getWidth(); j++){
					out.setPixel(j, i, b, getFilteredPixel(j, i, b));
					//System.out.println(getFilteredPixel(image, j, i, b));
				}
			}
		}
		return out;
	}
	
	/**
	 * Sets the image associated to the filter.
	 * @param image - the associated image.
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setImage(Image image){
		this.image = image;
		this.update = true;
	}
	public double getFilteredPixel(final int x, final int y, final int band){
		return (this.getFilteredPixel(this.image, x, y, band));
	}
	public double getFilteredPixelBoundaryMode(final int x, final int y, final int band){
		if (x < 0 || y < 0 || x >= this.image.getWidth() || y >= this.image.getHeight()) return this.image.getPixelBoundaryMode(x, y, band);
		return (this.getFilteredPixel(this.image, x, y, band));
	}
	
	
	public abstract double getFilteredPixel(final Image image, final int x, final int y, final int band);
	
	public Image getOriginalImage(){return this.image;}
	public Image getFilteredImage(){return this.applyFilter(this.image);}
}
