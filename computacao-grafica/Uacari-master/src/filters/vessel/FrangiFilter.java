package filters.vessel;

import java.util.ArrayList;

import filters.Filter;
import image.Image;

/**
 * Implementation inspired by the work of Frangi et al.: Multiscale vessel enhancement filtering and on the matlab implementation.
 * @author �rick Oliveira Rodrigues (erickr@id.uff.br)
 */
public class FrangiFilter extends Filter{
	
	private float beta1 = 1f, beta2 = 2f;
	private float sigmaMin = 1, sigmaMax = 1, sigmaStep = 1;
	private boolean blackWhite = true;
	
	public FrangiFilter(){
		
	}
	public FrangiFilter(final Image image){
		this.setImage(image);
	}
	public FrangiFilter(final Image image, final float beta1, final float beta2){
		this.setImage(image);
		this.setBeta(beta1, beta2);
	}
	public FrangiFilter(final Image image, final float beta1, final float beta2, final int sigmaMin, final int sigmaMax, final int sigmaStep){
		this.setImage(image);
		this.setBeta(beta1, beta2);
		this.setMaxSigma(sigmaMax);
		this.setMinSigma(sigmaMin);
		this.setSigmaStep(sigmaStep);
	}
	
	public void setBeta(final float beta1, final float beta2){
		this.beta1 = beta1;
		this.beta2 = beta2;
	}
	public void setMinSigma(final float sigmaStart){
		this.sigmaMin = sigmaStart;
	}
	public void setMaxSigma(final float sigmaStop){
		this.sigmaMax = sigmaStop;
	}
	public void setSigmaStep(final float sigmaStep){
		this.sigmaStep = sigmaStep;
	}

	public void setMinMaxSigma(final float sigmaStart, final float sigmaStop){
		this.setMinSigma(sigmaStart);
		this.setMaxSigma(sigmaStop);
	}

	private int lastBand = -1;
	
	@Override
	public double getFilteredPixel(Image image, int x, int y, int band) {
		synchronized(this){
			if (maxVals == null || lastBand != band) {
				lastBand = band;
				frangi(image, band);
			}
		}
		return maxVals.getPixel(x, y, band);
	}
	
	
	private Image Dxx, Dxy, Dyy;
	private void frangiToHessian(final Image image, float sigma, final int band){
		//construct Hessian kernels
		int n_kern_x = 2*Math.round(3*sigma) + 1;
		int n_kern_y = n_kern_x;
		double[] kern_xx_f = new double[n_kern_x*n_kern_y];
		double[] kern_xy_f = new double[n_kern_x*n_kern_y];
		double[] kern_yy_f = new double[n_kern_x*n_kern_y];
		int i=0, j=0;
		for (int x = -Math.round(3*sigma); x <= Math.round(3*sigma); x++){
			j=0;
			for (int y = -Math.round(3*sigma); y <= Math.round(3*sigma); y++){
				kern_xx_f[i*n_kern_y + j] = 1.0f/(2.0f*Math.PI*sigma*sigma*sigma*sigma) * (x*x/(sigma*sigma) - 1) * Math.exp(-(x*x + y*y)/(2.0f*sigma*sigma));
				kern_xy_f[i*n_kern_y + j] = 1.0f/(2.0f*Math.PI*sigma*sigma*sigma*sigma*sigma*sigma)*(x*y)*Math.exp(-(x*x + y*y)/(2.0f*sigma*sigma));
				j++;
			}
			i++;
		}
		for (j=0; j < n_kern_y; j++){
			for (i=0; i < n_kern_x; i++){
				kern_yy_f[j*n_kern_x + i] = kern_xx_f[i*n_kern_x + j];
			}
		}
		
		Dxx = new Image(image.getWidth(), image.getHeight(), image.getNumBands(), 32, true);
		Dyy = new Image(image.getWidth(), image.getHeight(), image.getNumBands(), 32, true);
		Dxy = new Image(image.getWidth(), image.getHeight(), image.getNumBands(), 32, true);

		for (i=0; i<Dxx.getHeight(); i++){
			for (j=0; j<Dxx.getWidth(); j++){
				
				double sumXX = 0, sumXY = 0, sumYY = 0;
				int i2=0, j2=0;
				for (int x = j -Math.round(3*sigma); x <= j + Math.round(3*sigma); x++){
					j2=0;
					for (int y = i -Math.round(3*sigma); y <= i + Math.round(3*sigma); y++){
						
						double p = image.getPixelBoundaryMode(x, y, band);
						
						sumXX += p*kern_xx_f[i2*n_kern_y + j2];
						sumXY += p*kern_xy_f[i2*n_kern_y + j2];
						sumYY += p*kern_yy_f[i2*n_kern_y + j2];
						
						j2++;
					}
					i2++;
				}
				
				Dxx.setPixel(j, i, band, sumXX);
				Dxy.setPixel(j, i, band, sumXY);
				Dyy.setPixel(j, i, band, sumYY);
				
			}
		}
		
		/*
		try {
			Dxx.stretchOrShrinkRange(0, 255);
			Dxx.showImage();
			Dxy.stretchOrShrinkRange(0, 255);
			Dxy.showImage();
			Dyy.stretchOrShrinkRange(0, 255);
			Dyy.showImage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
	
	Image lambda1, lambda2, Ix, Iy;
	
	private void frangiToEigenToImage(final Image image, final int band){
		lambda1 = new Image(image.getWidth(), image.getHeight(), image.getNumBands(), 32, true);
		lambda2 = new Image(image.getWidth(), image.getHeight(), image.getNumBands(), 32, true);
		Ix = new Image(image.getWidth(), image.getHeight(), image.getNumBands(), 32, true);
		Iy = new Image(image.getWidth(), image.getHeight(), image.getNumBands(), 32, true);
		
		//calculate eigenvectors of J, v1 and v2
		for (int i=0; i<image.getHeight(); i++){
			for (int j=0; j<image.getWidth(); j++){
				final double dxxV = Dxx.getPixel(j, i, band), dyyV = Dyy.getPixel(j, i, band), s = dxxV - dyyV,
						dxyV = Dxy.getPixel(j, i, band),
						tmpV = Math.sqrt(s*s + 4*dxyV*dxyV);
				
				double v2xV = 2*dxxV,
						v2yV = dyyV - dxxV + tmpV;
				
				//normalize
				final double magV = Math.sqrt(v2xV*v2xV + v2yV*v2yV);
				
				//v2xtmp.setPixel(j, i, b, v2xV*(1f/magP));
				if (magV != 0) v2xV = v2xV*(1f/magV);
				if (magV != 0) v2yV = v2yV*(1f/magV);
				
				//eigenvectors are orthogonal
				double v1xV = -1*v2yV,
						v1yV = v2xV;
				
				//compute eigenvalues
				double mu1V = 0.5f*(dxxV + dyyV + tmpV),
						mu2V = 0.5f*(dxxV + dyyV - tmpV);
	
				
				//sort eigenvalues by absolute value abs(Lambda1) < abs(Lamda2)
				lambda1.setPixel(j, i, band, mu1V);
				lambda2.setPixel(j, i, band, mu2V);
				
				Ix.setPixel(j, i, band, v1xV);
				Iy.setPixel(j, i, band, v1yV);
				
				if (Math.abs(mu1V) > Math.abs(mu2V)){
					lambda1.setPixel(j, i, band, mu2V);
					lambda2.setPixel(j, i, band, mu1V);
					
					Ix.setPixel(j, i, band, v2xV);
					Iy.setPixel(j, i, band, v2yV);
				}
			}
		}
		
		/*
		try {
			lambda1.stretchOrShrinkRange(0, 255);
			lambda1.showImage();
			lambda2.stretchOrShrinkRange(0, 255);
			lambda2.showImage();
			Ix.stretchOrShrinkRange(0, 255);
			Ix.showImage();
			Iy.stretchOrShrinkRange(0, 255);
			Iy.showImage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}

	private Image maxVals = null, outAngles;
	private float whatScale;
	

	private void frangi(final Image image, final int band){
		Image angles = new Image(image.getWidth(), image.getHeight(), image.getNumBands(), 32, true);
		Image Rb = new Image(image.getWidth(), image.getHeight(), image.getNumBands(), 32, true);
		Image S2 = new Image(image.getWidth(), image.getHeight(), image.getNumBands(), 32, true);
		Image iFiltered = new Image(image.getWidth(), image.getHeight(), image.getNumBands(), 32, true);
		maxVals = new Image(image.getWidth(), image.getHeight(), image.getNumBands(), 32, true);
		outAngles = new Image(image.getWidth(), image.getHeight(), image.getNumBands(), 32, true);
		
		ArrayList<Image> allFiltered = new ArrayList<Image>();
		ArrayList<Image> allAngles = new ArrayList<Image>();
		float beta = 2*beta1*beta1;
		float c = 2*beta2*beta2;

		for (float sigma = this.sigmaMin; sigma <= this.sigmaMax; sigma += this.sigmaStep){
			//create 2D hessians
			frangiToHessian(image, sigma, band);

			//correct for scale
			Dxx.multiply(sigma*sigma);
			Dyy.multiply(sigma*sigma);
			Dxy.multiply(sigma*sigma);
		
			//calculate (abs sorted) eigenvalues and vectors
			frangiToEigenToImage(image, band);
			
			//compute direction of the minor eigenvector
			for (int i=0; i<image.getHeight(); i++){
				for (int j=0; j<image.getWidth(); j++){
					angles.setPixel(j, i, band, Math.atan2(Iy.getPixel(j, i, band), Ix.getPixel(j, i, band)));
					
					final double lambda1V = lambda1.getPixel(j, i, band),
							lambda2V = lambda2.getPixel(j, i, band);
					
					double RbV = Math.pow(lambda1V * (1f/lambda2V), 2),
							S2V = lambda1V*lambda1V + lambda2V*lambda2V;
					
					RbV = Math.exp(-RbV/beta);
					S2V = Math.exp(-S2V/c);
					
					Rb.setPixel(j, i, band, RbV);
					S2.setPixel(j, i, band, S2V);
					
					double IfilteredV = RbV*(1 - S2V);
					if (blackWhite){
						if (lambda2V < 0) IfilteredV = 0;
					}else{
						if (lambda2V > 0) IfilteredV = 0;
					}
					
					iFiltered.setPixel(j, i, band, IfilteredV);
				}
			}
			allAngles.add(angles);
			

			//store results
			allFiltered.add(iFiltered);
		}

		float sigma = sigmaMin;
		maxVals = allFiltered.get(0);
		outAngles = allFiltered.get(0);
		whatScale = sigma;

		//find element-wise maximum across all accumulated filter results
		for (int k=1; k < allFiltered.size(); k++){
			
			boolean same = true;
			for (int i=0; i<image.getHeight(); i++){
				for (int j=0; j<image.getWidth(); j++){
					final double p = maxVals.getPixel(j, i, band);
					same &= allFiltered.get(k).getPixel(j, i, band) == p;
					if (allFiltered.get(k).getPixel(j, i, band) > p){
						maxVals.setPixel(j, i, band, allFiltered.get(k).getPixel(j, i, band) );
					}
				}
			}
			if (same){
				whatScale = sigma;
				outAngles = allAngles.get(k);
			}
			sigma += sigmaStep;
		}
		
		/*
		try {
			outAngles.stretchOrShrinkRange(0, 255);
			outAngles.showImage();
			maxVals.stretchOrShrinkRange(0, 255);
			maxVals.showImage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
	}

	
	public Image applyFilter(final Image image) {
		Image out = super.applyFilter(image);
		out.stretchOrShrinkRange(0, 255);
		return out;
	}

}
