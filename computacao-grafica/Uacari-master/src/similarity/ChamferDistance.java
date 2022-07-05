package similarity;

import java.math.BigDecimal;

import distances.Distance;
import image.Image;

/**
 * Class to compute the Chamfer Distance. It is mainly used with binary images, but we have made it to work with grey images as well.
 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
 */
public class ChamferDistance extends SimilarityMeasure {
	public static final Distance EUCLIDEAN_DISTANCE = Distance.EUCLIDEAN_DISTANCE, MANHATTAN_DISTANCE = Distance.MANHATTAN_DISTANCE, 
			CHEBYSHEV_DISTANCE = Distance.CHEBYSHEV_DISTANCE;
	
	private double tau = 0;
	private Distance d = Distance.EUCLIDEAN_DISTANCE;
	
	public ChamferDistance(double tau, Distance d){
		this.d = d;
		this.tau = tau;
	}
	public ChamferDistance(Image img1, Image img2, double tau, Distance d){
		super(img1, img2);
		this.tau = tau;
		this.d = d;
	}
	

	@Override
	public void setParameters(Object... parameter) {
		this.tau = (Double) parameter[0];
		this.d = (Distance) parameter[1];
	}
	public void setTau(double tau){this.tau = tau;}
	public void setDistance(Distance d){this.d = d;}

	@Override
	public double compare(Image img1, Image img2, int band) {
		final int bandI = (band == ALL_BANDS) ? 0 : band, bandF = (band == ALL_BANDS) ? Math.min(img1.getNumBands(), img2.getNumBands()) : band + 1;
		final int width = Math.min(img1.getWidth(), img2.getWidth()), height = Math.min(img1.getHeight(), img2.getHeight());
		if (this.fastComputation()){//for commonplace images
			double total = 0; int it = 1; boolean reachedEnd = false, outBoundary = false;
			for (int b=bandI; b<bandF; b++){
				double min = Long.MAX_VALUE, compD = 0;
				
				for (int i=0; i<height; i++){
					for (int j=0; j<width; j++){
						min = Long.MAX_VALUE;
						reachedEnd = false; it = 1;
						//finding the min
						while(!reachedEnd){
							if (img1.getPixel(j, i, b) == img2.getPixel(j, i, b)){min = 0; break;}
							
							for (int j2=-it + j; j2<=it + j; j2+=2*it){
								for (int i2=-it + i; i2<=it + i; i2++){
									if (j2 < 0 || i2 < 0 || j2 >= width || i2 >= height) continue;
									if (img2.getPixel(j2, i2, b) == img1.getPixel(j, i, b)){
										compD = d.compute(j, i, j2, i2);
										if (compD < min) min = compD;
									}
								}
							}
							for (int i2=-it + i; i2<=it + i; i2+=2*it){
								for (int j2=-it + j; j2<=it + j; j2++){
									if (j2 < 0 || i2 < 0 || j2 >= width || i2 >= height) continue;
									if (img2.getPixel(j2, i2, b) == img1.getPixel(j, i, b)){
										compD = d.compute(j, i, j2, i2);
										if (compD < min) min = compD;
									}
								}
							}
							outBoundary = (it >= width && it >= height);
							if (outBoundary) {
								min = width*height*Math.sqrt(2) + 1; //diagonal plus one
							}
							reachedEnd = (min != Long.MAX_VALUE);
							it++;
						}
						if (min == Long.MAX_VALUE) continue;
						total += Math.max(min, tau);
					}
				}
			}
			return total/(double)bandF;
		}else{//for very big images
			BigDecimal total = BigDecimal.valueOf(0); int it = 1; boolean reachedEnd = false, outBoundary = false;
			for (int b=bandI; b<bandF; b++){
				double min = Long.MAX_VALUE, compD = 0;
				
				for (int i=0; i<height; i++){
					for (int j=0; j<width; j++){
						min = Long.MAX_VALUE;
						reachedEnd = false; it = 1;
						//finding the min
						while(!reachedEnd){
							if (img1.getPixel(j, i, b) == img2.getPixel(j, i, b)){min = 0; break;}
							
							for (int j2=-it + j; j2<=it + j; j2+=2*it){
								for (int i2=-it + i; i2<=it + i; i2++){
									if (j2 >= width || i2 >= height) continue;
									if (img2.getPixel(j2, i2, b) == img1.getPixel(j2, i2, b)){
										compD = d.compute(j, i, j2, i2);
										if (compD < min) min = compD;
									}
								}
							}
							for (int i2=-it + i; i2<=it + i; i2+=2*it){
								for (int j2=-it + j; j2<=it + j; j2++){
									if (j2 >= width || i2 >= height) continue;
									if (img2.getPixel(j2, i2, b) == img1.getPixel(j2, i2, b)){
										compD = d.compute(j, i, j2, i2);
										if (compD < min) min = compD;
									}
								}
							}
							outBoundary = (it >= width && it >= height);
							if (outBoundary) {
								min = width*height*Math.sqrt(2) + 1; //diagonal plus one
							}
							reachedEnd = (min != Long.MAX_VALUE);
							it++;
						}
						if (min == Long.MAX_VALUE) continue;
						total = total.add(BigDecimal.valueOf(Math.max(min, tau)));
					}
				}
			}
			return total.divide(BigDecimal.valueOf(bandF)).doubleValue();
		}
	}
	@Override
	public String getName() {
		return "Chamfer Distance";
	}
	@Override
	public boolean invert() {
		return false;
	}

}
