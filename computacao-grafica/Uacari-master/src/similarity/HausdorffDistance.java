package similarity;


import distances.Distance;
import image.Image;

/**
 * A class implementing the Hausdorff distance. Brute force, which is very slow. Works with binary images only.
 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
 */
public class HausdorffDistance extends SimilarityMeasure{
	public static final Distance EUCLIDEAN_DISTANCE = Distance.EUCLIDEAN_DISTANCE, MANHATTAN_DISTANCE = Distance.MANHATTAN_DISTANCE, 
			CHEBYSHEV_DISTANCE = Distance.CHEBYSHEV_DISTANCE;
	
	private Distance d = Distance.EUCLIDEAN_DISTANCE;
	
	
	public HausdorffDistance(Distance d){
		this.d = d;
	}
	public HausdorffDistance(Image img1, Image img2, Distance d){
		super(img1, img2);
		this.d = d;
	}


	//set
	public void setDistance(Distance d){
		this.d = d;
	}


	@Override
	public void setParameters(Object... parameter) {
		d = (Distance) parameter[0];
	}
	@Override
	public void setImages(Image img1, Image img2) throws Exception{
		if (!img1.isBinary() || !img2.isBinary()) throw new Exception("Image 1 or image 2 must be binary to compute the hausdorff distance");
		else super.setImages(img1, img2);
	}
	@Override
	public void setImage1(Image img1) throws Exception{
		if (!img1.isBinary()) throw new Exception("Image 1 must be binary to compute the hausdorff distance");
		else super.setImage1(img1);
	}
	@Override
	public void setImage2(Image img1) throws Exception{
		if (!img1.isBinary()) throw new Exception("Image 2 must be binary to compute the hausdorff distance");
		else super.setImage2(img2);
	}

	/* (non-Javadoc)
	 * Assumes the images are of the same size
	 * @see similarity.SimilarityMeasure#compare(image.Image, image.Image)
	 */
	@Override
	public double compare(Image img1, Image img2, int band) {
		final int bandI = (band == ALL_BANDS) ? 0 : band, bandF = (band == ALL_BANDS) ? Math.min(img1.getNumBands(), img2.getNumBands()) : band + 1;
		final int width = Math.min(img1.getWidth(), img2.getWidth()), height = Math.min(img1.getHeight(), img2.getHeight());
		
		double min1 = Long.MAX_VALUE, min2 = Long.MAX_VALUE, 
				max1 = Long.MIN_VALUE, max2 = Long.MIN_VALUE,
				sumBands = 0, aux;
		
		for (int b=bandI; b<bandF; b++){
			//first img
			for (int i=0; i<height; i++){
				for (int j=0; j<width; j++){
					if (img1.getPixel(j, i, b) <= 0) continue;
					min1 = Long.MAX_VALUE;
					//second img
					for (int i2=0; i2<height; i2++){
						for (int j2=0; j2<width; j2++){
							if (img2.getPixel(j2, i2, b) <= 0) continue;
							aux = this.d.compute(j, i, j2, i2);
							if (aux < min1)
								min1 = aux;
						}
					}
					if (max1 < min1) max1 = min1;
				}
			}
			//performing switching the images
			//first img
			for (int i=0; i<height; i++){
				for (int j=0; j<width; j++){
					if (img2.getPixel(j, i, b) <= 0) continue;
					min2 = Long.MAX_VALUE;
					//second img
					for (int i2=0; i2<height; i2++){
						for (int j2=0; j2<width; j2++){
							if (img1.getPixel(j2, i2, b) <= 0) continue;
							aux = this.d.compute(j, i, j2, i2);
							if (aux < min2)
								min2 = aux;
						}
					}
					if (max2 < min2) max2 = min2;
				}
			}
			sumBands += Math.max(max1, max2);
		}
		return sumBands/(float)bandF;
	}
	
	@Override
	public String getName() {
		return null;
	}
	@Override
	public boolean invert() {
		return false;
	}
	
	
}
