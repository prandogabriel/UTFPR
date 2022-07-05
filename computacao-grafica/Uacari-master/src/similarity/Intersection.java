package similarity;

import java.util.Set;

import image.Image;

/**
 * Implements an intersection between images, counts how many intensities they have in common.
 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
 */
public class Intersection extends SimilarityMeasure {

	/**
	 * Counts the number of intensities that both image1 and image2 have, i.e., how many intensity values they have in common.
	 */
	public Intersection(){
	}
	/**
	 * Counts the number of intensities that both image1 and image2 have, i.e., how many intensity values they have in common.
	 * @param img1
	 * @param img2
	 */
	public Intersection(Image img1, Image img2){
		super(img1, img2);
	}
	
	@Override
	public void setParameters(Object... parameter) {
	}

	@Override
	public double compare(Image img1, Image img2, int band) {
		final int bandI = (band == ALL_BANDS) ? 0 : band, bandF = (band == ALL_BANDS) ? Math.min(img1.getNumBands(), img2.getNumBands()) : band + 1;
		
		Set<Double> i1 = null, i2 = null;
		
		long sum = 0;
		for (int b=bandI; b<bandF; b++){
			i1 = img1.getIntensities(b);
			i2 = img2.getIntensities(b);
			for (double k : i2){
				if (i1.contains(k)) sum++;
			}
		}
		return sum/(double)bandF;
	}
	@Override
	public String getName() {
		return "Intersection";
	}
	@Override
	public boolean invert() {
		return true;
	}

}
