package registration;

import java.util.ArrayList;
import java.util.Random;

import image.Image;
import similarity.SimilarityMeasure;

public class GeneticRegistrationOptimization {
	private Image sensible, reference;
	private SimilarityMeasure simMeasure;
	
	private Random r = new Random();
	
	//genetic
	private int iterationsLimit = 1000;
	private double similarityThreshold = Long.MIN_VALUE;
	private int populationSize = 20;
	private double translationRate = 100, thetaRate = 360;
	private double maxTx = Long.MAX_VALUE, minTx = Long.MIN_VALUE, 
			maxTy = Long.MAX_VALUE, minTy = Long.MIN_VALUE,
			maxTheta = 360, minTheta = 0;
	//
	
	private RegistrationParameters bestParams;
	private double bestSim = Integer.MAX_VALUE;
	
	
	public GeneticRegistrationOptimization(final Image reference, final Image sensible, final SimilarityMeasure simMeasure){
		this.sensible = sensible;
		this.reference = reference;
		this.simMeasure = simMeasure;
		this.translationRate = reference.getWidth() > reference.getHeight() ? reference.getWidth() : reference.getHeight();
	}
	
	//set
	public void setIterationsLimit(final int iterationsLimit){this.iterationsLimit = iterationsLimit;}
	public void setSimilarityThreshold(final double similarityThreshold){this.similarityThreshold = similarityThreshold;}
	public void setPopulationSize(final int populationSize){this.populationSize = populationSize;}
	public void setMaxTranslationOnX(final int maxTranslationOnX){this.maxTx = maxTranslationOnX;}
	public void setMaxTranslationOnY(final int maxTranslationOnY){this.maxTy = maxTranslationOnY;}
	public void setMaxTranslantion(final int maxTranslationOnX, final int maxTranslationOnY){this.setMaxTranslationOnX(maxTranslationOnX); this.setMaxTranslationOnY(maxTranslationOnY);}
	public void setMinTranslantionOnX(final int minTranslationOnX){this.minTx = minTranslationOnX;}
	public void setMinTranslationOnY(final int minTranslationOnY){this.minTy = minTranslationOnY;}
	public void setMinTranslation(final int minTranslationOnX, final int minTranslationOnY){this.setMinTranslantionOnX(minTranslationOnX); this.setMinTranslationOnY(minTranslationOnY);}
	public void setMinRotation(final double minRotation){this.minTheta = minRotation;}
	public void setMaxRotation(final double maxRotation){this.maxTheta = maxRotation;}
	
	
	
	
	private Registration registration;
	private ArrayList<RegistrationParameterStruct> population = new ArrayList<RegistrationParameterStruct>();
	private ArrayList<Double> scores = new ArrayList<Double>();
	public RegistrationParameters process() throws Exception{
		if (registration == null) registration = new Registration(reference, sensible, null);
		
		
		population.clear();
		scores.clear();
		
		RegistrationParameters rp = new RegistrationParameters(), bestRp = null;
		rp.addSimilarityMeasure(simMeasure);
		
		RegistrationParameterStruct p;
		double score = 0, bestScore = Long.MAX_VALUE;
		
		//populate first
		for (int k=0; k<populationSize; k++){
			p = new RegistrationParameterStruct();
			p.tx = genTx(0);
			p.ty = genTy(0);
			p.theta = genTheta(0);
			
			
			rp.clearTransformationParameters();
			rp.addTransformationParameters(p.tx, p.ty, p.theta, sensible);
			
			//registration.setParameters(rp);
			
			registration.process(reference, sensible, rp);
			score = registration.getBestScore();
			
			
			//if (score < bestScore){
			//bestScore = score;
			
			if (population.size() == 0){
				population.add(p);
				scores.add(score);
				bestRp = rp;
				bestScore = score;
			}else{
				boolean added = false;
				for (int k2=0; k2<population.size(); k2++){
					if (scores.get(k2) > score){
						scores.add(k2, score);
						population.add(k2, p);
						added = true;
						if (k2==0) {
							bestRp = rp;
							bestScore = score;
						}
						break;
					}
				}
				if (!added){
					population.add(p);
					scores.add(score);
				}
			}
			//}
			
		}
		
		//iterate then
		int it = 0;
		RegistrationParameterStruct p2;
		while (it < iterationsLimit){
			RegistrationParameterStruct ind1 = population.get((int)(r.nextInt(population.size()))),
					ind2 = population.get((int)(r.nextInt(population.size()) * r.nextFloat()));
			
			//cross-over
			p = new RegistrationParameterStruct();
			p.tx = r.nextBoolean() ? ind1.tx : ind2.tx;
			p.ty = r.nextBoolean() ? ind1.ty : ind2.ty;
			p.theta = r.nextBoolean() ? ind1.theta : ind2.theta;
			
			//mutations
			p2 = new RegistrationParameterStruct();
			p2.tx = genTx(p.tx);
			p2.ty = genTy(p.ty);
			p2.theta = genTheta(p.theta);
			
			
			rp.clearTransformationParameters();
			rp.addTransformationParameters(p2.tx, p2.ty, p2.theta, sensible);
			
			//registration.setParameters(rp);
			
			registration.process(reference, sensible, rp);
			score = registration.getBestScore();
			
			if (score < bestScore){
				bestRp = rp;
				bestScore = score;
				
				//insert
				boolean added = false;
				for (int k2=0; k2<population.size(); k2++){
					if (scores.get(k2) > bestScore){
						scores.add(k2, score);
						population.add(k2, p2);
						added = true;
						break;
					}
				}
				if (!added){
					population.add(p2);
					scores.add(score);
				}
				population.remove(population.size() - 1); //remove the last one (least fit)
				scores.remove(scores.size() - 1);
				
				//update increasing rates
				double newTranslationRate = (p2.tx / p.tx + p2.ty / p.ty)/2;
				if (newTranslationRate >= 1)
					translationRate += newTranslationRate;
				else
					translationRate += 1f/newTranslationRate;
				
				
				double newThetaRate = (p2.theta / p.theta);
				if (newThetaRate >= 1)
					thetaRate += newThetaRate;
				else
					thetaRate += 1f/newThetaRate;
			}
			
			if (bestScore < similarityThreshold)
				break;
			
			it++;
		}
		
		//return the best one
		return bestRp;
	}
	
	
	private double genTx(final double tx){
		double outTx = 0;
		do{
			outTx = tx + r.nextInt((int)translationRate*2) - translationRate;
		}while (outTx > maxTx || outTx < minTx);
		return outTx;
	}
	private double genTy(final double ty){
		double outTy = 0;
		do{
			outTy = ty + r.nextInt((int)translationRate*2) - translationRate;
		}while (outTy > maxTy || outTy < minTy);
		return outTy;
	}
	private double genTheta(final double theta){
		double outTheta = 0;
		do{
			outTheta = theta + (r.nextFloat()*thetaRate*2) - thetaRate;
		}while (outTheta > maxTheta || outTheta < minTheta);
		return outTheta;
	}
}
