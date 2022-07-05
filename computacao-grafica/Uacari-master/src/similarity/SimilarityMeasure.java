package similarity;

import distances.Distance;
import image.Image;

public abstract class SimilarityMeasure {
	public static final SimilarityMeasure MEAN_DIFFERENCE = new MeanDifference(1), SUM_OF_DIFFERENCES = new SumOfDifferences(1),
			HAUSDORFF_DISTANCE = new HausdorffDistance(Distance.EUCLIDEAN_DISTANCE), NORMALIZED_CROSS_CORRELATION = new NormalizedCrossCorrelation(),
			MUTUAL_INFORMATION = new MutualInformation("2"), INTERSECTION = new Intersection(), HYBRID_SUM_OF_DIFFERENCES = new HybridSumOfDifferences(3, 0),
			DICE_INDEX = new DiceIndex(0), CHAMFER_DISTANCE = new ChamferDistance(2, Distance.EUCLIDEAN_DISTANCE), WEIGHTED_MUTUAL_INFORMATION = new WeightedMutualInformation("2"),
			WEIGHTED_MUTUAL_INFORMATION2 = new WeightedMutualInformation2("2");
	
	
	protected boolean boost = true;
	
	public SimilarityMeasure(Image img1, Image img2){
		this.img1 = img1;
		this.img2 = img2;
	}
	public SimilarityMeasure(){}
	
	
	protected static final int ALL_BANDS = Integer.MAX_VALUE;
	
	protected Image img1 = null, img2 = null;
	private int threshold = 0; //0 = exactly equal

	/**
	 * Sets the parameters of the similarity measure (as many as are necessary for each measure)
	 * @param parameter 
	 */
	public abstract void setParameters(Object... parameter);
	public void setImage1(Image img) throws Exception {this.img1 = img;}
	public void setImage2(Image img) throws Exception {this.img2 = img;}
	public void setImages(Image img1, Image img2) throws Exception{this.img1 = img1; this.img2 = img2;}
	/**
	 * Sets whether the image comparison should be computed faster or not. If set to true the computation is faster but it may not function properly for big images with high bit depths.
	 * @param activate
	 */
	public void setBoost(boolean activate){this.boost = activate;}
	
	public boolean fastComputation(){return boost;}
	
	/**
	 * Compares images img1 and img2, regarding just the 'band' passed as parameter
	 * @param img1
	 * @param img2
	 * @param band
	 * @return
	 */
	public abstract double compare(Image img1, Image img2, int band);
	/**
	 * Compares images img1 and img2, regarding all bands
	 * @param img1
	 * @param img2
	 * @return
	 */
	public double compare(Image img1, Image img2){
		return compare(img1, img2, ALL_BANDS);
	}
	
	/**
	 * Compares img1 and img2, with the result unified so that the lower the value returned by this function the better, no matter which
	 * similarity is regarded
	 * @param img1
	 * @param img2
	 * @return
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public double compareUnified(Image img1, Image img2){
		if (invert()) return Long.MAX_VALUE - compare(img1, img2);
		else return compare(img1, img2);
	}

	/**
	 * Compares img1 and img2, with the result unified so that the lower the value returned by this function the better, no matter which
	 * similarity is regarded
	 * @param img1
	 * @param img2
	 * @param band
	 * @return
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public double compareUnified(Image img1, Image img2, int band){
		if (invert()) return Long.MAX_VALUE - compare(img1, img2, band);
		else return compare(img1, img2, band);
	}
	
	public double compare() throws Exception{
		if (img1 == null || img2 == null) throw new Exception("One or both of the images is/are null. Please set them with the method setImages(img1, img2) before using compare.");
		else return compare(this.img1, this.img2);
	}
	public double compareUnified() throws Exception{
		if (img1 == null || img2 == null) throw new Exception("One or both of the images is/are null. Please set them with the method setImages(img1, img2) before using compare.");
		else return compareUnified(this.img1, this.img2);
	}
	public double compare(int band) throws Exception{
		if (img1 == null || img2 == null) throw new Exception("One or both of the images is/are null. Please set them with the method setImages(img1, img2) before using compare.");
		else return compare(this.img1, this.img2, band);
	}
	public double compareUnified(int band) throws Exception{
		if (img1 == null || img2 == null) throw new Exception("One or both of the images is/are null. Please set them with the method setImages(img1, img2) before using compare.");
		else return compareUnified(this.img1, this.img2, band);
	}

	/**
	 * Checks if the images are equal or not according to a set threshold regarding all bands (all bands are compared).
	 * The comparison done is defined as: compare(img1, img2) <= threshold, where the compare function gives the score of similarity between images 1 and 2
	 * @param img1
	 * @param img2
	 * @param threshold - default is 0, in this case the image will be equal when they are exactly the same in most similarity measures
	 * @return
	 */
	public boolean isEqual(Image img1, Image img2, int threshold) {
		return compare(img1, img2) <= threshold;
	}
	/**
	 * Checks if the images are equal or not according to a set threshold regarding the band passed as parameter (just one band is compared).
	 * The comparison done is defined as: compare(img1, img2) <= threshold, where the compare function gives the score of similarity between images 1 and 2
	 * @param img1
	 * @param img2
	 * @param band
	 * @param threshold - default is 0, in this case the image will be equal when they are exactly the same in most similarity measures
	 * @return
	 */
	public boolean isEqual(Image img1, Image img2, int band, int threshold) {
		return compare(img1, img2, band) <= threshold;
	}
	public boolean isEqual(Image img1, Image img2){
		return compare(img1, img2) <= threshold;
	}
	
	
	public boolean isEqual() throws Exception{
		if (img1 == null || img2 == null) throw new Exception("One or both of the images is/are null. Please set them with the method setImages(img1, img2) before using isEqual.");
		else return isEqual(this.img1, this.img2, threshold);
	}
	public boolean isEqual(int threshold) throws Exception{
		if (img1 == null || img2 == null) throw new Exception("One or both of the images is/are null. Please set them with the method setImages(img1, img2) before using isEqual.");
		else return isEqual(this.img1, this.img2, threshold);
	}
	
	
	public abstract String getName();
	/**
	 * Returns false if it is a measure like Mean Differences, Hausdorff Distance, etc, where the lower the number returned the better
	 * and returns true otherwise
	 * @return
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public abstract boolean invert();
}
