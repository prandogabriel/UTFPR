package filters.blur;

/**
 *	Shake-effects the input image based on the Gaussian Blur.
 *
 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
 */
public class ShakeEffect extends GaussianBlur{
	
	public ShakeEffect(){
		this.setAmplitude(2);
	}
	public ShakeEffect(final float amplitude){
		this.setAmplitude(amplitude);
	}

	/* (non-Javadoc)
	 * @see filters.blur.GaussianBlur#setAmplitude(float)
	 */
	@Override
	public void setAmplitude(final float amplitude){
		this.amplitude = 1f/Math.abs(amplitude);
		this.update = true;
	}
	
}
