package similarity;

import image.Image;

/**
 * Class implementing a simple mean difference measure. Works for binary and grey images.
 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
 */
public class MeanDifference extends SumOfDifferences{
	
	public MeanDifference(float expoentParameter) {
		super(expoentParameter);
	}
	public MeanDifference(Image img1, Image img2, float expoentParameter){
		super(img1, img2, expoentParameter);
	}



	/* (non-Javadoc)
	 * Assumes the images are of the same size
	 * @see similarity.SimilarityMeasure#compare(image.Image, image.Image)
	 */
	@Override
	public double compare(Image img1, Image img2, int band) {
		final int bandF = (band == ALL_BANDS) ? Math.min(img1.getNumBands(), img2.getNumBands()) : band + 1;
		final int width = Math.min(img1.getWidth(), img2.getWidth()), height = Math.min(img1.getHeight(), img2.getHeight());
		return super.compare(img1, img2, band)/((double)width*height*bandF);
	}
	
	
	@Override
	public String getName(){return "Mean Difference";}
}
