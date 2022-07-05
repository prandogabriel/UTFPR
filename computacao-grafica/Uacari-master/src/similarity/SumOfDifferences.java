package similarity;

import java.math.BigDecimal;

import image.Image;

/**
 * Sum of differences of the errors. Works for both binary (in this case it will be the hamming distance) and also for grey images. Faster than {@link MeanDifference}.
 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
 */
public class SumOfDifferences extends SimilarityMeasure{
	private float g = 1;
	

	public SumOfDifferences(float expoentParameter){
		this.g = expoentParameter;
	}
	public SumOfDifferences(Image img1, Image img2, float expoentParameter){
		super(img1, img2);
		this.g = expoentParameter;
	}
	


	//set
	public void setExpoentParameter(float parameter){
		this.g = parameter;
	}

	@Override
	public void setParameters(Object... parameter) {
		g = (Float) parameter[0];
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
			long sum = 0;
			for (int b=bandI; b<bandF; b++){
				for (int i=0; i<height; i++){
					for (int j=0; j<width; j++){
						sum += Math.abs(Math.pow(img1.getPixel(j, i, b)-img2.getPixel(j, i, b), g));
					}
				}
			}
			return sum;
		}else{//for very big images
			BigDecimal bd = BigDecimal.valueOf(0);
			for (int b=bandI; b<bandF; b++){
				for (int i=0; i<height; i++){
					for (int j=0; j<width; j++){
						bd = bd.add(BigDecimal.valueOf(Math.abs(Math.pow(img1.getPixel(j, i, b)-img2.getPixel(j, i, b), g))));
					}
				}
			}
			return bd.doubleValue();
		}
	}
	@Override
	public String getName() {
		return "Sum Of Differences";
	}
	@Override
	public boolean invert() {
		return false;
	}
	
	
}
