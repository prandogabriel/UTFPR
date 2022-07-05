package similarity;

import java.math.BigDecimal;

import image.Image;

/**
 * Normalized cross correlation measure.
 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
 */
public class NormalizedCrossCorrelation extends SimilarityMeasure{

	public NormalizedCrossCorrelation(){}
	public NormalizedCrossCorrelation(Image img1, Image img2){
		super(img1, img2);
	}
	
	
	@Override
	public void setParameters(Object... parameter) {
	}

	/* (non-Javadoc)
	 * Assumes the images are of the same size
	 * @see similarity.SimilarityMeasure#compare(image.Image, image.Image)
	 */
	@Override
	public double compare(Image img1, Image img2, int band) {
		final int bandI = (band == ALL_BANDS) ? 0 : band, bandF = (band == ALL_BANDS) ? Math.min(img1.getNumBands(), img2.getNumBands()) : band + 1;
		final int width = Math.min(img1.getWidth(), img2.getWidth()), height = Math.min(img1.getHeight(), img2.getHeight());
		if (this.fastComputation()){//for commonplace images
			double numerator = 0, denominator1 = 0, denominator2 = 0;
			double img1MeanIntensity = 0, img2MeanIntensity = 0;
			double result = 0;
			
			for (int b=bandI; b<bandF; b++){
				img1MeanIntensity = img1.getAverageIntensity(b);
				img2MeanIntensity = img2.getAverageIntensity(b);
				for (int i=0; i<height; i++){
					for (int j=0; j<width; j++){
						numerator += (img1.getPixel(j, i, b)-img1MeanIntensity)*(img2.getPixel(j, i, b)-img2MeanIntensity);
						denominator1 += Math.pow((img1.getPixel(j, i, b)-img1MeanIntensity), 2);
						denominator2 += Math.pow((img2.getPixel(j, i, b)-img2MeanIntensity), 2);
					}
				}
				result += Math.abs(numerator/(Math.pow(denominator1*denominator2,1/2f)));
			}
			
			return result/(double)bandF;
		}else{//for very big images
			BigDecimal numerator = BigDecimal.valueOf(0), denominator1 = BigDecimal.valueOf(0), denominator2 = BigDecimal.valueOf(0);
			double img1MeanIntensity = 0, img2MeanIntensity = 0;
			
			BigDecimal result = BigDecimal.valueOf(0);
			
			for (int b=0; b<bandF; b++){
				img1MeanIntensity = img1.getAverageIntensity(b);
				img2MeanIntensity = img2.getAverageIntensity(b);
				for (int i=0; i<height; i++){
					for (int j=0; j<width; j++){
						numerator = numerator.add(BigDecimal.valueOf((img1.getPixel(j, i, b)-img1MeanIntensity)*(img2.getPixel(j, i, b)-img2MeanIntensity)));
						denominator1 = denominator1.add(BigDecimal.valueOf(Math.pow((img1.getPixel(j, i, b)-img1MeanIntensity), 2)));
						denominator2 = denominator2.add(BigDecimal.valueOf(Math.pow((img2.getPixel(j, i, b)-img2MeanIntensity), 2)));
					}
				}
				result = result.add(numerator.divide(BigDecimal.valueOf(Math.pow(denominator1.multiply(denominator2).doubleValue(), 1/2f))));
			}
			
			return result.divide(BigDecimal.valueOf(bandF)).doubleValue();
		}
	}
	@Override
	public String getName() {
		return "Normalized Cross Correlation";
	}
	@Override
	public boolean invert() {
		return true;
	}
	
	
}
