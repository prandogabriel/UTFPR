package similarity;

import java.math.BigDecimal;

import image.Image;

/**
 * A class implementing a tweaked version of the Dice Index.
 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
 */
public class DiceIndex extends SimilarityMeasure{
	private float relevance = 0f;

	/**
	 * A slightly modified version of the Dice index.
	 * @param relevance - standard: 0. The higher this parameter, the greater will be the dice index, since the algorithm starts to be more 'acceptable' of errors.
	 */
	public DiceIndex(float relevance){
		this.relevance = relevance;
	}
	/**
 	 * A slightly modified version of the Dice index.
	 * @param img1
	 * @param img2
	 * @param relevance - standard: 0. The higher this parameter, the greater will be the dice index, since the algorithm starts to be more 'acceptable' of errors.
	 */
	public DiceIndex(Image img1, Image img2, float relevance){
		super(img1, img2);
		this.relevance = relevance;
	}


	//set
	public void setRelevance(float relevance){
		this.relevance = relevance;
	}

	@Override
	public void setParameters(Object... parameter) {
		this.relevance = (Float) parameter[0];
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
				int nS = 0;
				for (int i=0; i<height; i++){
					for (int j=0; j<width; j++){
						if ((img1.getPixel(j, i, b) >= img2.getPixel(j, i, b) - relevance 
								&& img1.getPixel(j, i, b) <= img2.getPixel(j, i, b) + relevance)){
							nS ++;
						}
					}
						
				}
				sum += nS/(double)(width*height);
			}
			return sum/(double)bandF;
		}else{//for very big images
			BigDecimal nS = BigDecimal.valueOf(0), sum = BigDecimal.valueOf(0);
			for (int b=bandI; b<bandF; b++){
				nS = BigDecimal.valueOf(0);
				for (int i=0; i<height; i++){
					for (int j=0; j<width; j++){
						if ((img1.getPixel(j, i, b) >= img2.getPixel(j, i, b) - relevance 
								&& img1.getPixel(j, i, b) <= img2.getPixel(j, i, b) + relevance)){
							nS = nS.add(BigDecimal.valueOf(1));
						}
					}
						
				}
				sum = sum.add(nS.divide(BigDecimal.valueOf(width*height)));
			}
			return sum.divide(BigDecimal.valueOf(bandF)).doubleValue();
		}
	}
	@Override
	public String getName() {
		return "Dice Index";
	}
	@Override
	public boolean invert() {
		return true;
	}
}
