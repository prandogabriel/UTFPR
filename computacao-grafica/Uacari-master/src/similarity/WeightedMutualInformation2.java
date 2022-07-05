package similarity;


import image.Image;

/**
 * Implements a mutual information measure. The lower the better.
 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
 */
public class WeightedMutualInformation2 extends WeightedMutualInformation{
	

	public WeightedMutualInformation2(String logBase){
		super(logBase);
		this.weighted = false;
		this.weight2 = true;
	}
	public WeightedMutualInformation2(Image img1, Image img2, String logBase){
		super(img1, img2, logBase);
		this.weight2 = true;
		this.weighted = false;
	}
	


	@Override
	public void setParameters(Object... parameter) {
		super.setParameters(parameter);
	}

	/* (non-Javadoc)
	 * Assumes the images are of the same size
	 * @see similarity.SimilarityMeasure#compare(image.Image, image.Image)
	 */
	@Override
	public double compare(Image img1, Image img2, int band) {
		return super.compare(img1, img2, band);
	}
	
	@Override
	public String getName(){return "Weighted Mutual Information 2";}
	

}
