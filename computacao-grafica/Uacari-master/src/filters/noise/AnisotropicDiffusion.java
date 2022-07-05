package filters.noise;

import filters.Filter;
import filters.border.LinearGradient;
import image.Image;

/**
 * Anisotropic diffusion is a blurring algorithm that preserve edges.
 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
 */
public class AnisotropicDiffusion extends Filter{
	
	private float kappa = 3f, lambda = 0.5f;
	private int totalIterations = 30;
	
	private Image resultImage = null;
	
	public AnisotropicDiffusion(final Image image, final float kappa, final float lambda, final int iterations){
		this.setImage(image);
		this.setKappa(kappa);
		this.setLambda(lambda);
		this.setIterations(iterations);
	}
	
	public AnisotropicDiffusion(){
		
	}
	
	public void setKappa(final float kappa){this.kappa = kappa;}
	public void setLambda(final float lambda){this.lambda = lambda;}
	/**
	 * Sets the number of iterations for the anisotropic diffusion.
	 * @param iterations
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setIterations(final int iterations){this.totalIterations = iterations;}

	
	@Override
	public synchronized double getFilteredPixel(Image image, int x, int y, int band) {
		
		if (resultImage == null){
			Image currImage = image;
			Image newImage;
			
			int counter = 0;
			while (counter < totalIterations){
				newImage = currImage.clone();
				for (int i=0; i<image.getHeight(); i++){
					for (int j=0; j<image.getWidth(); j++){
						
						double north  = currImage.getPixelBoundaryMode(j, i - 1, band);
						double south  = currImage.getPixelBoundaryMode(j, i + 1, band);
						double west   = currImage.getPixelBoundaryMode(j - 1, i, band);
						double east   = currImage.getPixelBoundaryMode(j + 1, i, band);
						double center = currImage.getPixelBoundaryMode(j, i, band);
						
						/// red diffs
						double redN  = north - center;
						double redW  = west - center;
						double redS  = south - center;
						double redE  = east - center; 
						
		
						double red_new = (center + lambda*(fluxDerivative(redN)*redN + fluxDerivative(redS)*redS
							+fluxDerivative(redE)*redE + fluxDerivative(redW)*redW));
					
						
						newImage.setPixel(j, i, band, red_new);
						
					}
				}
				currImage = newImage;
				counter++;
			}
			
			/*
			Image currImage = image;
			
			int counter = 0;
			while (counter < totalTime){
				
				Gradient currGrad = new Gradient();
				currGrad.setImage(currImage);
				Image newImage = currImage.clone();
				for (int i=0; i<currImage.getHeight(); i++){
					for (int j=0; j<currImage.getWidth(); j++){
						
						double north = currImage.getPixelBoundaryMode(j, i - 1, band),
								east = currImage.getPixelBoundaryMode(j + 1, i, band),
								west = currImage.getPixelBoundaryMode(j - 1, i, band),
								south = currImage.getPixelBoundaryMode(j, i + 1, band);

						double total = currImage.getPixelBoundaryMode(j, i, band) +
								lambda * 
								(currGrad.getFilteredPixel(currImage, j, i - 1, band)*north + 
								currGrad.getFilteredPixel(currImage, j, i + 1, band)*south +
								currGrad.getFilteredPixel(currImage, j + 1, i, band)*east + 
								currGrad.getFilteredPixel(currImage, j - 1, i, band)*west);
						
						newImage.setPixel(j, i, band, total);
						
					}
				}
				currImage = newImage;
				
				counter++;
				
			}*/
			
			resultImage = currImage;
		}
		
		return resultImage.getPixelBoundaryMode(x, y, band);
	}
	
	private double fluxDerivative(final double intensity){
		return Math.exp(-Math.pow(intensity/this.kappa, 2));
	}
	
	
	public Image applyFilter(final Image image) {
		Image out = super.applyFilter(image);
		out.stretchOrShrinkRange(0, 255);
		return out;
	}
	

}
