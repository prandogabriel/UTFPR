package filters.vessel;

import filters.Filter;
import filters.border.HessianFilter;
import image.Image;


/**
 * Implementation inspired by the work of Frangi et al.: Multiscale vessel enhancement filtering
 * @author �rick Oliveira Rodrigues (erickr@id.uff.br)
 */
public class VesselnessFilter extends Filter{
	
	private HessianFilter hessian = null;
	private float beta = 0.5f, c = 40;
	private float scale = 0.6f;
	
	public VesselnessFilter(){
		
	}
	public VesselnessFilter(final Image image){
		this.setImage(image);
	}
	public VesselnessFilter(final Image image, final float beta, final float c, final float scale){
		this.setImage(image);
		this.setBeta(beta);
		this.setC(c);
		this.setScale(scale);
	}
	
	
	public void setBeta(final float beta){
		this.beta = beta;
	}
	public void setC(final float c){
		this.c = c;
	}
	public void setScale(final float scale){
		this.scale = scale;
	}
	
	private int lastBand = -1;

	@Override
	public double getFilteredPixel(Image image, int x, int y, int band) {
		if (hessian == null || lastBand != band){
			lastBand = band;
			hessian = new HessianFilter(image);
			hessian.setScale(scale);
		}
		
		hessian.setOperationType(HessianFilter.TYPE_FIRST_EIGENVALUE);
		double firstEigen = hessian.getFilteredPixel(x, y, band);
		
		hessian.setOperationType(HessianFilter.TYPE_SECOND_EIGENVALUE);
		double secondEigen = hessian.getFilteredPixel(x, y, band);
		
		final double aux = firstEigen;
		if (secondEigen < firstEigen) {
			firstEigen = secondEigen;
			secondEigen = aux;
		}
		
		if (secondEigen < 0) return 0;
		
		final double rb = Math.pow(Math.abs(firstEigen)/Math.abs(secondEigen), 2);
		
		final double s = Math.sqrt(firstEigen*firstEigen + secondEigen*secondEigen);
		
		return Math.exp(- (rb*rb)/(2f*beta*beta)) * (1 - Math.exp(- (s*s)/(2f*c*c)));
	}

	public Image applyFilter(final Image image) {
		Image out = super.applyFilter(image);
		out.stretchOrShrinkRange(0, 255);
		return out;
	}

	
}
