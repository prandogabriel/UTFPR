package filters.noise;

import distances.Distance;
import filters.Filter;
import image.Image;

/**
 * Reduces the noise from binary or grey-scale images.
 * Looks the neighbourhood of each pixel and check if there are at least a certain number of pixels there. If there are, then keep the central pixel. Otherwise, erase it.
 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
 */
public class BinaryNoiseReduction extends Filter{
	private float threshold = 0.2f;
	private Distance distance = Distance.EUCLIDEAN_DISTANCE;
	private int radius = 3;
	private double[] min, max;
	
	/**
	 * Sets the threshold for the noise removal. The lesser it is, less noise is removed.
	 * @param threshold
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setThreshold(final float threshold){
		this.threshold = threshold;
	}
	/**
	 * Sets the distance type from a central iterated pixel. 
	 * @param distance
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setDistance(final Distance distance){
		this.distance = distance;
	}
	/**
	 * Sets the maximum distance from the central iterated pixel.
	 * @param radius
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setRadius(final int radius){
		this.radius = radius;
	}

	@Override
	public double getFilteredPixel(Image image, int x, int y, int band) {
		
		int counter = 0,
				rightCounter = 0;
		
		final float mean = (float) ((max[band] - min[band])/2f);
		
		for (int i=y - radius; i<= y + radius; i++){
			for (int j=x - radius; j<= x + radius; j++){
				
				if (distance.compute(x, y, j, i) > radius) continue;
				
				if (image.getPixelBoundaryMode(j, i, band) >= mean){
					rightCounter++;
				}
				
				counter ++;
			}
		}
		
		if (counter*threshold <= rightCounter)
			return image.getPixel(x, y, band);
		else
			return min[band];
	}

	
	
	public Image applyFilter(final Image image) {
		min = new double[image.getNumBands()];
		max = new double[image.getNumBands()];
		
		for (int b=0; b<image.getNumBands(); b++){
			min[b] = image.getMinimalIntesity(b);
			max[b] = image.getMaximalIntensity(b);
		}
		
		Image out = super.applyFilter(image);
		return out;
	}
	
}
