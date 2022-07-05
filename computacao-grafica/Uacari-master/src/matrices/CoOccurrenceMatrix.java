package matrices;

import static distances.Distance.*;

import java.util.ArrayList;
import java.util.TreeMap;

import distances.Distance;
import image.Image;
import log.Logger;

public class CoOccurrenceMatrix {
	private Image image;
	private float kernelRadius = 5;
	private Distance distance = CHEBYSHEV_DISTANCE;
	private boolean computeBothOrientations = false;
	private boolean update = true;
	
	private int deltaX = 1, deltaY = 0;
	private int band = 0;
	
	private int lastHash = 0;
	
	public CoOccurrenceMatrix(final Image image){
		this.setImage(image);
	}
	public CoOccurrenceMatrix(final Image image, final float kernelRadius, final boolean computeBothOrientations){
		this.setImage(image);
		this.setKernelRadius(kernelRadius);
		this.setToComputeBothOrientations(computeBothOrientations);
	}
	public CoOccurrenceMatrix(final Image image, final float kernelRadius, final Distance kernelRadiusDistance, final boolean computeBothOrientations){
		this.setImage(image);
		this.setKernelRadius(kernelRadius);
		this.setKernelRadialDistanceMeasure(kernelRadiusDistance);
		this.setToComputeBothOrientations(computeBothOrientations);
	}
	public CoOccurrenceMatrix(final Image image, final float kernelRadius, final Distance kernelRadiusDistance, final int deltaX, final int deltaY, final boolean computeBothOrientations){
		this.setImage(image);
		this.setKernelRadius(kernelRadius);
		this.setKernelRadialDistanceMeasure(kernelRadiusDistance);
		this.setToComputeBothOrientations(computeBothOrientations);
		this.setDelta(deltaX, deltaY);
	}
	public CoOccurrenceMatrix(final Image image, final float kernelRadius, final Distance kernelRadiusDistance, final int deltaX, final int deltaY, final int band, final boolean computeBothOrientations){
		this.setImage(image);
		this.setKernelRadius(kernelRadius);
		this.setKernelRadialDistanceMeasure(kernelRadiusDistance);
		this.setToComputeBothOrientations(computeBothOrientations);
		this.setDelta(deltaX, deltaY);
		this.setBand(band);
	}
	
	public CoOccurrenceMatrix(final Image image, final int deltaX, final int deltaY, final int band, final boolean computeBothOrientations){
		this.setImage(image);
		this.setKernelRadius(kernelRadius);
		this.setToComputeBothOrientations(computeBothOrientations);
		this.setDelta(deltaX, deltaY);
		this.setBand(band);
	}
	
	public CoOccurrenceMatrix(final Image image, final int deltaX, final int deltaY, final int band){
		this.setImage(image);
		this.setKernelRadius(kernelRadius);
		this.setDelta(deltaX, deltaY);
		this.setBand(band);
	}
	
	
	public void setImage(final Image image){
		this.image = image;
		this.lastHash = image.hashCode();
		this.setKernelRadius(image.getHeight() > image.getWidth() ? ((image.getHeight() - 1)/2f) : (image.getWidth() - 1)/2f);
	}
	
	public void setKernelRadius(final float kernelRadius){
		if (kernelRadius != this.kernelRadius) this.update = true;
		this.kernelRadius = kernelRadius;
	}
	public void setToComputeBothOrientations(final boolean computeBoth){
		if (this.computeBothOrientations != computeBoth) this.update = true;
		this.computeBothOrientations = computeBoth;
	}
	
	/**
	 * Sets the distance measurement regarding the kernel radius. If the distance of the central pixel of the image to the one being computed is greater than the
	 * radius, respecting this set distance measure, then the pixel is ignored. The default distance measure is Chebyshev Distance (so, we consider a squared region around the processed pixel).
	 * @param distance
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setKernelRadialDistanceMeasure(final Distance distance){
		if (!this.distance.equals(distance)) this.update = true;
		this.distance = distance;
	}
	
	
	public void setDelta(final int deltaX, final int deltaY){
		if (deltaX != this.deltaX || deltaY != this.deltaY) update = true;
		this.deltaX = deltaX;
		this.deltaY = deltaY;
	}
	
	/**
	 * Sets the image band to compute the co-occurrence matrix from.
	 * @param band
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setBand(final int band){
		if (band != this.band) update = true;
		this.band = band;
	}
	
	private TreeMap<Double, TreeMap<Double, Integer>> bufferedMatrix = null;
	
	/**
	 * Returns a tree map that contains another tree map. 
	 * The keys of the tree maps represent the grey values we want to compare. The returned result corresponds to the number of co-occurrences.
	 * For instance, f we want to check how many times 120 co-occurred with 150, then that would be equal to: getMatrix(band, deltaX, deltaY).get(120).get(150).
	 * @return
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public TreeMap<Double, TreeMap<Double, Integer>> getMatrix(){
		if (band >= image.getNumBands()) band = 0;
		
		if ((lastHash == this.image.hashCode() || update) && bufferedMatrix != null) return bufferedMatrix;
		
		update = false;
		
		TreeMap<Double, TreeMap<Double, Integer>> matrix = new TreeMap<Double, TreeMap<Double, Integer>>();
		
		float centralX = ((image.getWidth() - 1)/2f),
				centralY = ((image.getHeight() - 1)/2f);
		
		for (int i=0; i<image.getHeight(); i++){
			for (int j=0; j<image.getWidth(); j++){
				
				if (distance.compute(centralX, centralY, j, i) > kernelRadius) continue;
				
				if (i + deltaY >= image.getHeight() || j + deltaX >= image.getWidth()) continue;
				
				final double p1 = image.getPixel(j, i, band),
						p2 = image.getPixel(j + deltaX, i + deltaY, band);
				
				if (!matrix.containsKey(p1)) matrix.put(p1, new TreeMap<Double, Integer>());
				if (computeBothOrientations) if (!matrix.containsKey(p2)) matrix.put(p2, new TreeMap<Double, Integer>());
				
				if (!matrix.get(p1).containsKey(p2)) matrix.get(p1).put(p2, 1);
				else matrix.get(p1).put(p2, matrix.get(p1).remove(p2) + 1);
				
				if (computeBothOrientations){
					if (!matrix.get(p2).containsKey(p1)) matrix.get(p2).put(p1, 1);
					else matrix.get(p2).put(p1, matrix.get(p2).remove(p1) + 1);
				}
				
			}
		}
		
		if (computeBothOrientations){
			ArrayList<Double> checkedValues = new ArrayList<Double>();
			for (int i=0; i<image.getHeight(); i++){
				for (int j=0; j<image.getWidth(); j++){
					
					if (distance.compute(centralX, centralY, j, i) > kernelRadius) continue;
					if (i + deltaY >= image.getHeight() || j + deltaX >= image.getWidth()) continue;
					
					final double p1 = image.getPixel(j, i, band),
							p2 = image.getPixel(j + deltaX, i + deltaY, band);
					
					if (p1 == p2 && !checkedValues.contains(p1)){
						matrix.get(p1).put(p2, (int)(matrix.get(p1).remove(p2)/2));
						checkedValues.add(p1);
					}
				}
			}
			checkedValues.clear();
		}
		
		this.bufferedMatrix = matrix;
		return matrix;
	}
	
	
	private ArrayList<Double> orderedValues;
	/**
	 * Returns the co-occurrence matrix in the form of a real matrix.
	 * The matrix is n x n where n is the number of different grey values within the image.
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public int[][] getRawMatrix(){
		
		TreeMap<Double, TreeMap<Double, Integer>> matrix = getMatrix();
		
		ArrayList<Double> values = new ArrayList<Double>(matrix.size());
		
		for (double p1 : matrix.keySet()){
			
			if (!values.contains(p1)) values.add(p1);
			
			for (double p2 : matrix.get(p1).keySet()){
				
				if (!values.contains(p2)) values.add(p2);
				
			}
			
		}
		
		orderedValues = new ArrayList<Double>(values.size());
		
		while(values.size() > 0){
			double min = Integer.MAX_VALUE;
			int rightK = 0;
			for (int k=0; k<values.size(); k++){
				if (values.get(k) < min){
					min = values.get(k);
					rightK = k;
				}
			}
			
			orderedValues.add(values.remove(rightK));
		}
		
		int[][] pMatrix = new int[orderedValues.size()][orderedValues.size()];
		
		
		for (int k=0; k<orderedValues.size(); k++){
			for (int k2=0; k2<orderedValues.size(); k2++){
				if (matrix.containsKey(orderedValues.get(k))){
					if (matrix.get(orderedValues.get(k)).containsKey(orderedValues.get(k2))){
						pMatrix[k][k2] = matrix.get(orderedValues.get(k)).get(orderedValues.get(k2));
					}
				}
			}
		}
		
		return pMatrix;
	}
	
	
	
	public void printMatrix(){
		int[][] matrix = getRawMatrix();
		
		String beg = "";
		for (int k=0; k<orderedValues.size(); k++){
			while (beg.length() < (orderedValues.get(k) + "").length())
				beg = " " + beg;
		}
		Logger.log(beg + "|");
		
		for (int k=0; k<orderedValues.size(); k++){
			int maxLength = 0;
			for (int i2=0; i2<matrix.length; i2++) if ((matrix[i2][k] + "").length() > maxLength) maxLength = (matrix[i2][k] + "").length();
			String v = orderedValues.get(k) + "";
			while (v.length() < maxLength)
				v = " " + v;
			Logger.log(v + " ");
		}
		Logger.log("\n ------------------------------ \n");
		
		for (int i=0; i<matrix.length; i++){
			String value = orderedValues.get(i) + "";
			while (value.length() < (orderedValues.get(orderedValues.size() - 1)+"").length()){
				value = " " + value;
			}
			Logger.log(value + "| ");
			
			for (int j=0; j<matrix[0].length; j++){
				int maxLength = 0;
				for (int i2=0; i2<matrix.length; i2++) if ((matrix[i2][j] + "").length() > maxLength) maxLength = (matrix[i2][j] + "").length();
				String cValue = matrix[i][j] + "";
				while (cValue.length() < maxLength)
					cValue = " " + cValue;
				Logger.log(cValue + " ");
			}
			Logger.log("\n");
		}
		
	}
	
	
	/**
	 * Returns the squared sum of the co-occurrences in the matrix. Equivalent to the Haralick's energy.
	 * In other words, the greater the number of co-occurrences, the greater the returned value.
	 * @return
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public double getEnergy(){
		TreeMap<Double, TreeMap<Double, Integer>> matrix = getMatrix();
		
		double sum = 0;
		for (double p1 : matrix.keySet()){
			for (double p2: matrix.get(p1).keySet()){
				sum += Math.pow(matrix.get(p1).get(p2), 2);
			}
		}
		return sum;
	}
	
	/**
	 * Returns the equivalent of the Haralick contrast.
	 * @return
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public double getContrast(){
		TreeMap<Double, TreeMap<Double, Integer>> matrix = getMatrix();
		
		double sum = 0;
		for (double p1 : matrix.keySet()){
			for (double p2: matrix.get(p1).keySet()){
				sum += Math.pow(Math.abs(p1 - p2), 2) * matrix.get(p1).get(p2);
			}
		}
		return sum;
	}
	
	
	/**
	 * Returns the equivalent of Haralick's Homogeneity
	 * @return
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public double getHomogeneity(){
		TreeMap<Double, TreeMap<Double, Integer>> matrix = getMatrix();
		
		double sum = 0;
		for (double p1 : matrix.keySet()){
			for (double p2: matrix.get(p1).keySet()){
				sum += matrix.get(p1).get(p2)/(1 + Math.abs(p1 - p2));
			}
		}
		return sum;
	}
	
	
	/**
	 * Returns the equivalent of Haralick's Entropy
	 * @return
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public double getEntropy(){
		TreeMap<Double, TreeMap<Double, Integer>> matrix = getMatrix();
		
		double sum = 0;
		for (double p1 : matrix.keySet()){
			for (double p2: matrix.get(p1).keySet()){
				sum += matrix.get(p1).get(p2) * Math.log(matrix.get(p1).get(p2));
			}
		}
		return sum;
	}
	
	
	/**
	 * Returns the moment of the co-occurrence matrix
	 * @param g
	 * @return
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public double getMoment(final float g){
		TreeMap<Double, TreeMap<Double, Integer>> matrix = getMatrix();
		
		double sum = 0;
		for (double p1 : matrix.keySet()){
			for (double p2: matrix.get(p1).keySet()){
				sum += matrix.get(p1).get(p2) * Math.pow((p1 - p2), g);
			}
		}
		return sum;
	}
}
