package similarity;

import java.util.TreeMap;
import java.util.Map.Entry;

import image.Image;

/**
 * A weighted mutual information combing the mutual information and mean difference measures. 
 * More details on: http://www.cmpbjournal.com/article/S0169-2607(15)00244-8/abstract , be advised that the WMI equation in this paper is inverted (the higher the better). 
 * Here, the lower the better.
 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
 */
public class WeightedMutualInformation extends SimilarityMeasure {

	private String logBase = "2";
	protected boolean weighted = true, weight2 = false /*tirar dps*/;
	
	public WeightedMutualInformation(String logBase){
		this.logBase = logBase;
	}
	public WeightedMutualInformation(Image img1, Image img2, String logBase){
		super(img1, img2);
		this.logBase = logBase;
	}


	@Override
	public void setParameters(Object... parameter) {
		this.logBase = (String) parameter[0];
	}
	public void setLogBase(String logBase){
		this.logBase = logBase;
	}

	private static TreeMap<Double, TreeMap<Double, Double>> mutualOccurrence = null;
	private static TreeMap<Double, Double> fixedOccurrence = null, movingOccurrence = null;
	/* (non-Javadoc)
	 * Assumes the images are of the same size
	 * @see similarity.SimilarityMeasure#compare(image.Image, image.Image)
	 */
	@Override
	public double compare(Image img1, Image img2, int band) {
		final int bandI = (band == ALL_BANDS) ? 0 : band, bandF = (band == ALL_BANDS) ? Math.min(img1.getNumBands(), img2.getNumBands()) : band + 1;
		final int width = Math.min(img1.getWidth(), img2.getWidth()), height = Math.min(img1.getHeight(), img2.getHeight());


		double mutualInf = 0;
		
		for (int b=bandI; b<bandF; b++){
			mutualOccurrence = new TreeMap<Double, TreeMap<Double, Double>>();
			fixedOccurrence = new TreeMap<Double, Double>();
			movingOccurrence = new TreeMap<Double, Double>();
			TreeMap<Double, Double> aux = new TreeMap<Double, Double>();
			
			double count = 0, value = 0, result, resultAux;
			for (int i=0; i<height; i++){//initializing co-occurrences
				for (int j=0; j<width; j++){
					//mutual occurrence
					if (!mutualOccurrence.containsKey(img1.getPixel(j, i, b))){
						aux = new TreeMap<Double, Double>();
						mutualOccurrence.put(img1.getPixel(j, i, b), aux);
					}
					count = 0;
					aux = mutualOccurrence.get(img1.getPixel(j, i, b));
					if (aux.containsKey(img2.getPixel(j, i, b))){
						count = aux.get(img2.getPixel(j, i, b));
						aux.remove(img2.getPixel(j, i, b));
					}
					aux.put(img2.getPixel(j, i, b), (count + 1));
					
					
					//fixed (img1) occurrence
					if (!fixedOccurrence.containsKey(img1.getPixel(j, i, b))){
						fixedOccurrence.put(img1.getPixel(j, i, b), (double) 0);
					}
					value = fixedOccurrence.get(img1.getPixel(j, i, b));
					fixedOccurrence.remove(img1.getPixel(j, i, b));
					fixedOccurrence.put(img1.getPixel(j, i, b), (value + 1));
					
					//moving (img2) occurrence
					if (!movingOccurrence.containsKey(img2.getPixel(j, i, b))){
						movingOccurrence.put(img2.getPixel(j, i, b), (double) 0);
					}
					value = movingOccurrence.get(img2.getPixel(j, i, b));
					movingOccurrence.remove(img2.getPixel(j, i, b));
					movingOccurrence.put(img2.getPixel(j, i, b), (value + 1));
					
				}
			}
			
	
			
			
			//computing result
			for (Entry<Double, Double> fixed : fixedOccurrence.entrySet()){
				for (Entry<Double, Double> moving : movingOccurrence.entrySet()){
					if (mutualOccurrence.get(fixed.getKey()).containsKey(moving.getKey())){
						double weight = 1;
						if (weight2)
							weight = (double) (Math.abs(moving.getKey() - fixed.getKey()));
						else if (weighted)
							weight = (double) 1/(Math.abs(moving.getKey() - fixed.getKey()) + 1);
						result = mutualOccurrence.get(fixed.getKey()).get(moving.getKey())/(double)(width*height);
						resultAux = (double)result/( ((fixedOccurrence.get(fixed.getKey()))/(double)(width*height))
								* (movingOccurrence.get(moving.getKey())/(double)(width*height)) );
						if (logBase.equals("e"))
							result *= Math.log(resultAux);
						else if (logBase.equals("2"))
							result *= Math.log(2)/Math.log(resultAux);
						else if (logBase.equals("10"))
							result *= Math.log10(resultAux);
						result *= weight;
						mutualInf += result;
					}
				}
			}
		}

		return mutualInf/(double)bandF;
	}
	@Override
	public String getName() {
		return "Weighted Mutual Information";
	}
	@Override
	public boolean invert() {
		return true;
	}
	
	
}
