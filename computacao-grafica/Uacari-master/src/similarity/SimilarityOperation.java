package similarity;

import image.Image;
import image.PixelOperation;

public class SimilarityOperation extends SimilarityMeasure {
	private PixelOperation pOperation;

	public SimilarityOperation(PixelOperation pOperation){
		this.pOperation = pOperation;
	}
	
	public void setPixelOperation(PixelOperation pOperation){
		this.pOperation = pOperation;
	}
	
	@Override
	public void setParameters(Object... parameter) {
		this.pOperation = (PixelOperation) parameter[0];
	}

	@Override
	public double compare(Image img1, Image img2, int band) {
		final int bandI = (band == ALL_BANDS) ? 0 : band, bandF = (band == ALL_BANDS) ? Math.min(img1.getNumBands(), img2.getNumBands()) : band + 1;
		double[] p = new double[2];
		final int minHeight = img1.getHeight() < img2.getHeight() ? img1.getHeight() : img2.getHeight();
		final int minWidth = img1.getWidth() < img2.getWidth() ? img1.getWidth() : img2.getWidth();
		double total = 0;
		for (int b=bandI; b<bandF; b++){
			for (int i=0; i<minHeight; i++){
				for (int j=0; j<minWidth; j++){
					p[0] = img1.getPixel(j, i, b);
					p[1] = img2.getPixel(j, i, b);
					total += pOperation.compute(p);
				}
			}
		}
		return total;
	}

	@Override
	public String getName() {
		return this.getClass().getName();
	}

	@Override
	public boolean invert() {
		return false;
	}

	
	
}
