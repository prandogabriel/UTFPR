package registration;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import image.Image;
import log.Logger;
import similarity.SimilarityMeasure;

public class RegistrationParameters {

	private ArrayList<AffineTransform> affineTransforms;
	private ArrayList<SimilarityMeasure> measures;
	private ArrayList<RegistrationParameterStruct> params;
	

	public RegistrationParameters(){
		
	}
	
	/**
	 * Instantiates the parameters for a rigid transform.
	 * @param tx - translation on x
	 * @param ty - translation on y
	 * @param tetha - rotation degree
	 * @param measure - a similarity measure to compare the images being registered
	 */
	public RegistrationParameters(double tx, double ty, double theta, SimilarityMeasure measure, Image sensibleImage){
		this.set(tx, ty, theta, measure, sensibleImage);
	}
	
	private void set(double tx, double ty, double theta, SimilarityMeasure measure, Image sensibleImage){
		affineTransforms = new ArrayList<AffineTransform>(1);
		affineTransforms.add(new AffineTransform());
		affineTransforms.get(0).setToRotation(1.57*theta/90, sensibleImage.getWidth()/2, sensibleImage.getHeight()/2);
		double[] m = new double[6];
		affineTransforms.get(0).getMatrix(m);
		affineTransforms.remove(0);
		affineTransforms.add(new AffineTransform(m[0], m[1], m[2], m[3], m[4] + tx, m[5] + ty));
		//affineTransforms[0].translate(tx, ty);
		
		
		this.params = new ArrayList<RegistrationParameterStruct>(1);
		RegistrationParameterStruct params = new RegistrationParameterStruct();
		params.theta = theta;
		params.tx = tx;
		params.ty = ty;
		this.params.add(params);

		this.measures = new ArrayList<SimilarityMeasure>(1);
		this.measures.add(measure);
	}
	
	/**
	 * Instantiates the parameters for a rigid transform.
	 * This constructor receives lists, which are used to run the registration tx.length = ty.length = theta.length times, 
	 * where the best parameter set of parameters is chosen (e.g., tx[0], ty[0], theta[0] - the best set of parameters is at index 0).
	 * The size of the measure array can be different from tx, ty and theta.
	 * @param tx - a list of translations on x
	 * @param ty - a list of translations on y
	 * @param theta - a list of rotation degrees
	 * @param measure - a similarity measure to compare the images being registered
	 */
	public RegistrationParameters(double[] tx, double[] ty, double[] theta, SimilarityMeasure measure, final Image sensibleImage){
		if (!(tx.length == ty.length && tx.length == theta.length))
			Logger.log(String.format("The array sizes of the parameters must be the same! [tx: %d != ty: %d != theta: %d] \n", tx.length, ty.length, theta.length));
		
		params = new ArrayList<RegistrationParameterStruct>(theta.length);
		affineTransforms = new ArrayList<AffineTransform>(tx.length);
		
		for (int k=0; k<tx.length; k++){
			affineTransforms.add(k, new AffineTransform());
			affineTransforms.get(k).setToRotation(1.57*theta[k]/90, sensibleImage.getWidth()/2, sensibleImage.getHeight()/2);
			//affineTransforms[k].concatenate(AffineTransform.getTranslateInstance(tx[k], ty[k]));
			double[] m = new double[6];
			affineTransforms.get(k).getMatrix(m);
			affineTransforms.remove(k);
			affineTransforms.add(k, new AffineTransform(m[0], m[1], m[2], m[3], m[4] + tx[k], m[5] + ty[k]));
			
			RegistrationParameterStruct params = new RegistrationParameterStruct();
			params.theta = theta[k];
			params.tx = tx[k];
			params.ty = ty[k];
			this.params.add(params);
		}
		
		this.measures = new ArrayList<SimilarityMeasure>(1);
		this.measures.add(measure);
	}
	
	/**
	 * Instantiates the parameters for a rigid transform.
	 * This constructor receives lists, which are used to run the registration tx.length = ty.length = theta.length times, 
	 * where the best parameter set of parameters is chosen (e.g., tx[0], ty[0], theta[0] - the best set of parameters is at index 0).
	 * The size of the measure array can be different from tx, ty and theta.
	 * @param tx - a list of translations on x
	 * @param ty - a list of translations on y
	 * @param theta - a list of rotation degrees
	 * @param measure - a list of similarity measures to compare the images being registered
	 */
	public RegistrationParameters(double[] tx, double[] ty, double[] theta, SimilarityMeasure[] measure, final Image sensibleImage){
		if (!(tx.length == ty.length && tx.length == theta.length))
			Logger.log(String.format("The array sizes of the parameters must be the same! [tx: %d != ty: %d != theta: %d] \n", tx.length, ty.length, theta.length));
		
		this.params = new ArrayList<RegistrationParameterStruct>(theta.length);
		affineTransforms = new ArrayList<AffineTransform>(tx.length);
		
		this.measures = new ArrayList<SimilarityMeasure>(measure.length);
		
		for (int k=0; k<tx.length; k++){
			affineTransforms.add(k, new AffineTransform());
			affineTransforms.get(k).setToRotation(1.57*theta[k]/90, sensibleImage.getWidth()/2, sensibleImage.getHeight()/2);
			//affineTransforms[k].concatenate(AffineTransform.getTranslateInstance(tx[k], ty[k]));
			double[] m = new double[6];
			affineTransforms.get(k).getMatrix(m);
			affineTransforms.remove(k);
			affineTransforms.add(k, new AffineTransform(m[0], m[1], m[2], m[3], m[4] + tx[k], m[5] + ty[k]));
			
			RegistrationParameterStruct params = new RegistrationParameterStruct();
			params.theta = theta[k];
			params.tx = tx[k];
			params.ty = ty[k];
			this.params.add(params);
			
			this.measures.add(k, measure[k]);
		}
		
	}
	
	
	/**
	 * Instantiates the parameters for an affine transform.
	 * @param transform - the affine transform
	 * @param measure - a similarity measure to compare the images being registered
	 */
	public RegistrationParameters(AffineTransform transform, SimilarityMeasure measure){
		affineTransforms = new ArrayList<AffineTransform>(1);
		affineTransforms.add(transform);
		
		this.params = new ArrayList<RegistrationParameterStruct>(1);
		RegistrationParameterStruct params = new RegistrationParameterStruct();
		params.theta = Double.MIN_VALUE;
		params.tx = Double.MIN_VALUE;
		params.ty = Double.MIN_VALUE;
		this.params.add(params);
		
		this.measures = new ArrayList<SimilarityMeasure>(1);
		this.measures.add(measure);
		
	}
	
	/**
	 * Instantiates the parameters for an affine transform.
	 * The length of the measure array can be different from the transform array.
	 * @param transform - the affine transform array
	 * @param measure - a similarity measure array to compare the images being registered
	 */
	public RegistrationParameters(AffineTransform[] transform, SimilarityMeasure[] measure){
		this.affineTransforms = new ArrayList<AffineTransform>(transform.length);
		this.measures = new ArrayList<SimilarityMeasure>(measure.length);
		for (int k=0; k<this.affineTransforms.size(); k++){
			this.affineTransforms.add(k, transform[k]);
			this.measures.add(k, measure[k]);
		}
		
		
		this.params = new ArrayList<RegistrationParameterStruct>(transform.length);
		
		RegistrationParameterStruct rp = new RegistrationParameterStruct();
		rp.tx = Double.MIN_VALUE;
		rp.ty = Double.MIN_VALUE;
		rp.theta = Double.MIN_VALUE;
		for (int k=0; k<transform.length; k++) {this.params.add(k, rp);}
	}
	
	//add
	public void addSimilarityMeasure(final SimilarityMeasure measure){
		if (measures == null) measures = new ArrayList<SimilarityMeasure>();
		this.measures.add(measure);
	}
	public void addTransformationParameters(final double tx, final double ty, final double theta, final Image sensibleImage){
		addTransformationParameters(tx, ty, theta, sensibleImage.getWidth()/2, sensibleImage.getHeight()/2);
	}
	public void addTransformationParameters(final double tx, final double ty, final double theta, final int rotationAnchorPointX, final int rotationAnchorPointY){
		if (affineTransforms == null) affineTransforms = new ArrayList<AffineTransform>(1);
		//affineTransforms.add(new AffineTransform());
		AffineTransform transform = new AffineTransform();
		transform.setToRotation(1.57*theta/90, rotationAnchorPointX, rotationAnchorPointY);
		double[] m = new double[6];
		transform.getMatrix(m);
		affineTransforms.add(new AffineTransform(m[0], m[1], m[2], m[3], m[4] + tx, m[5] + ty));
		
		if (this.params == null){
			this.params = new ArrayList<RegistrationParameterStruct>(1);
		}
		
		RegistrationParameterStruct params = new RegistrationParameterStruct();
		params.theta = theta;
		params.tx = tx;
		params.ty = ty;
		this.params.add(params);
	}
	
	public void clearTransformationParameters(){
		if (this.affineTransforms != null) this.affineTransforms.clear();
		if (this.params != null) this.params.clear();
	}
	public void clearSimilarityMeasures(){
		if (this.measures != null) this.measures.clear();
	}
	public void clear(){
		this.clearSimilarityMeasures();
		this.clearTransformationParameters();
	}

	
	private int tCounter = 0;
	protected AffineTransform getNextAffineTransform(){
		tCounter ++;
		if (tCounter > affineTransforms.size()) {
			tCounter = 0;
			return null;
		}
		return affineTransforms.get(tCounter - 1);
	}
	
	private int rpCounter = 0;
	protected RegistrationParameterStruct getNextRegistrationParameters(){
		rpCounter ++;
		if (rpCounter > params.size()) {
			rpCounter = 0;
			return null;
		}
		return params.get(rpCounter - 1);
	}
	
	private int pCounter = 0;
	protected SimilarityMeasure getNextSimilarityMeasure(){
		pCounter ++;
		if (pCounter > measures.size()){
			pCounter = 0;
			return null;
		}
		return measures.get(pCounter - 1);
	}
	
	public String getParametersAsString(int paramIndex){
		
		String params = "[Input Parameters: translationOnX: " + 
		((this.params.get(0).tx == Double.MIN_VALUE) ? "?" : this.params.get(paramIndex).tx) + 
		", translationOnY: " +
		((this.params.get(0).ty == Double.MIN_VALUE) ? "?" : this.params.get(paramIndex).ty) + 
		", degree of rotation: " +
		((this.params.get(0).theta == Double.MIN_VALUE) ? "?" : this.params.get(paramIndex).theta) + "]";
		
		params += "[" + affineTransforms.get(paramIndex) + "]";
		return params;
 	}
	
	/**
	 * Returns the parameters of the affine transformation that is currently being iterated
	 * @return
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public String getParametersAsString(){
		int idx = (tCounter - 1 < 0) ? 0 : tCounter - 1;
		return getParametersAsString(idx);
	}
	
	protected SimilarityMeasure[] getSimilarityMeasures(){return (SimilarityMeasure[]) this.measures.toArray();}
	protected AffineTransform[] getAffineTransforms(){return (AffineTransform[]) this.affineTransforms.toArray();}
	
	
}
