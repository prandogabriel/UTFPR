package registration;

import java.awt.geom.AffineTransform;

import image.Image;
import log.Logger;
import similarity.SimilarityMeasure;

public class Registration {
	private Image reference, sensible;
	private RegistrationParameters params, bestParams;
	private boolean printResult = true;
	private String resultLog = null;
	private AffineTransform lastAffineTransform = null;
	private double bestScore = 0;

	public Registration(){
		
	}
	
	public Registration(Image reference, Image sensible, RegistrationParameters params){
		this.setImages(reference, sensible);
		this.params = params;
	}
	
	
	public void setParameters(RegistrationParameters params){this.params = params;}
	public void setToPrintResults(boolean print){this.printResult = print;}
	
	public void setImages(Image referenceImage, Image sensibleImage){
		sensibleImage = sensibleImage.clone();
		
		final int maxWidth = referenceImage.getWidth() > sensibleImage.getWidth() ? referenceImage.getWidth() : sensibleImage.getWidth();
		final int maxHeight = referenceImage.getHeight() > sensibleImage.getHeight() ? referenceImage.getHeight() : sensibleImage.getHeight();
		
		if (maxWidth == referenceImage.getWidth()){
			sensibleImage.setWidth(maxWidth);
		}else if (maxWidth != referenceImage.getWidth()){
			referenceImage.setWidth(maxWidth);
		}

		if (maxHeight == referenceImage.getHeight()){
			sensibleImage.setHeight(maxHeight);
		}else if (maxHeight != referenceImage.getHeight()){
			referenceImage.setHeight(maxHeight);
		}
		
		
		this.reference = referenceImage; 
		this.sensible = sensibleImage;
	}
	
	
	public String getResultLog(){
		return this.resultLog;
	}
	public AffineTransform getBestAffineTransform(){
		return lastAffineTransform;
	}
	public RegistrationParameters getBestParametersList(){
		return this.bestParams;
	}
	public double getBestScore(){return this.bestScore;}

	/**
	 * Processes the registration, the registration parameters and images must have been set already
	 * @return - the best affine transform of the registration
	 * @throws Exception
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public Image process() {
		String log = "", bestName = "", bestParams = "";
		AffineTransform t, bestT = null; SimilarityMeasure m;
		Image senClone;
		double rawScore = 0, score = 0, minScore = Long.MAX_VALUE;
		int counter = 1;
		while((m = params.getNextSimilarityMeasure()) != null){
			log = String.format("+=== %s ===+ \nEvaluations: \n", m.getName());
			while ((t = params.getNextAffineTransform()) != null){
				senClone = sensible.clone();
				senClone.transform(t);
				//senClone.showImage();
				rawScore = m.compare(reference, senClone);
				if (m.invert()) score = -rawScore; else score = rawScore;
				if (score < minScore) {
					this.bestScore = score;
					minScore = score;
					bestT = t;
					bestParams = String.format("[Similarity Score: %f]%s \n", rawScore, params.getParametersAsString());
					this.bestParams = params;
					bestName = m.getName();
				}
				log += String.format("%d: ", counter);
				counter ++;
				log += String.format("[Similarity Score: %f]%s \n", rawScore, params.getParametersAsString());
			}
			//log += "\n";
			if (printResult) Logger.log(log);
			resultLog += log;
			
			log = String.format("+=== Best Transformation for %s ===+ \n%s", bestName, bestParams);
			if (printResult) Logger.log(log);
			resultLog += log;
		}

		lastAffineTransform = bestT;
		return sensible.clone().transform(bestT);
	}
	

	/**
	 * Sets the images and parameters and performs the registration
	 * @param referenceImage
	 * @param sensibleImage
	 * @param params
	 * @return - the best affine transform of the registration
	 * @throws Exception
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public Image process(Image referenceImage, Image sensibleImage, RegistrationParameters params) throws Exception{
		this.setParameters(params);
		this.setImages(referenceImage, sensibleImage);
		return process();
	}
}
