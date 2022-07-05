package similarity;

import java.math.BigDecimal;

import image.Image;

/**
 * Implements a hybrid sum of differences. More details on: http://www.cmpbjournal.com/article/S0169-2607(15)00244-8/abstract
 * Works with grey images.
 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
 */
public class HybridSumOfDifferences extends SimilarityMeasure {
	
	private double threshold = 0;
	private float g = 3;
	
	/**
	 * A hybrid similarity measure. The expoentParameter is the same as in the MeanDifference and SumOfDifferences measures. The threshold is used to compute differently two parts of the image.
	 * @param expoentParameter - standard: 3
	 * @param threshold - standard: 0
	 */
	public HybridSumOfDifferences(float expoentParameter, double threshold){
		this.g = expoentParameter;
		this.threshold = threshold;
	}
	
	/**
	 * A hybrid similarity measure. The expoentParameter is the same as in the MeanDifference and SumOfDifferences measures. The threshold is used to compute differently two parts of the image.
	 * @param img1
	 * @param img2
	 * @param expoentParameter - standard: 3
	 * @param threshold - standard: 0
	 */
	public HybridSumOfDifferences(Image img1, Image img2, float expoentParameter, double threshold){
		super(img1, img2);
		this.g = expoentParameter;
		this.threshold = threshold;
	}

	public void setThreshold(double threshold){
		this.threshold = threshold;
	}
	public void setExpoentParameter(float expoentParameter){
		this.g = expoentParameter;
	}
	
	@Override
	public void setParameters(Object... parameter) {
		this.g = (Float) parameter[0];
		this.threshold = (Double) parameter[1];
	}

	@Override
	public double compare(Image img1, Image img2, int band) {
		final int bandI = (band == ALL_BANDS) ? 0 : band, bandF = (band == ALL_BANDS) ? Math.min(img1.getNumBands(), img2.getNumBands()) : band + 1;
		final int width = Math.min(img1.getWidth(), img2.getWidth()), height = Math.min(img1.getHeight(), img2.getHeight());
		if (this.fastComputation()){//for commonplace images
			double img1MaxIntensity = 0, img2MaxIntensity = 0;

			long fScore = 0, fCounter = 1;
			double delta = 0, mSample = 0;
			for (int b=bandI; b<bandF; b++){
				img1MaxIntensity = img1.getMaximalIntensity(b);
				img2MaxIntensity = img2.getMaximalIntensity(b);
				for (int i=0; i<height; i++){
					for (int j=0; j<width; j++){
						delta = img1.getPixel(j, i, b);
						mSample = (img2.getPixel(j, i, b)*(img1MaxIntensity/img2MaxIntensity));
						fCounter = 1;
						//if (img1.getPixel(j, i, b) >= 0){
							if (mSample > threshold){
								delta -= (1 + img1MaxIntensity)/(1 + mSample);
								if (delta < 0) delta = 0;
								for (byte a=0; a<g; a++) fCounter *= delta;
								fCounter = -Math.abs(fCounter);
							}else{
								delta -= mSample;
								for (byte a=0; a<g; a++) fCounter *= delta;
								fCounter = Math.abs(fCounter);
							}
						/*}else{
							for (byte a=0; a<g; a++) fCounter *= img1MaxIntensity;
							fCounter = Math.abs(fCounter);
						}*/
						
						//hybric mean error
						fScore += fCounter;
					}
				}
			}
			
			return fScore/(double)bandF;
		}else{//for very big images
			double img1MaxIntensity = 0, img2MaxIntensity = 0;

			BigDecimal fScore = BigDecimal.valueOf(0), fCounter = BigDecimal.valueOf(1);
			double delta = 0, mSample = 0;
			for (int b=bandI; b<bandF; b++){
				img1MaxIntensity = img1.getMaximalIntensity(b);
				img2MaxIntensity = img2.getMaximalIntensity(b);
				for (int i=0; i<height; i++){
					for (int j=0; j<width; j++){
						delta = img1.getPixel(j, i, b);
						mSample = (img2.getPixel(j, i, b)*(img1MaxIntensity/(double)img2MaxIntensity));
						fCounter = BigDecimal.valueOf(1);
						//if (img1.getPixel(j, i, b) >= 0){
							if (mSample > threshold){
								delta -= (1 + img1MaxIntensity)/(1 + mSample);
								if (delta < 0) delta = 0;
								for (byte a=0; a<g; a++) fCounter = fCounter.multiply(BigDecimal.valueOf(delta));
								fCounter = fCounter.abs().negate();
							}else{
								delta -= mSample;
								for (byte a=0; a<g; a++) fCounter = fCounter.multiply(BigDecimal.valueOf(delta));
								fCounter = fCounter.abs();
							}
						/*}else{
							for (byte a=0; a<g; a++) fCounter *= img1MaxIntensity;
							fCounter = Math.abs(fCounter);
						}*/
						
						//hybric mean error
						fScore = fScore.add(fCounter);
					}
				}
			}
			
			return fScore.divide(BigDecimal.valueOf(bandF)).doubleValue();
		}
	}

	@Override
	public String getName() {
		return "Hybrid Sum Of Differences";
	}

	@Override
	public boolean invert() {
		return false;
	}

	
	
}
