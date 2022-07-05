package matrices;

import static distances.Distance.*;

import java.util.ArrayList;
import java.util.TreeMap;

import distances.Distance;
import image.Image;
import log.Logger;

public class RunLengthMatrix {
	private Image image;
	private int lastHash = 0;
	private boolean update = true;
	private int orientation = 0;
	private int band = 0;
	private Distance distance = CHEBYSHEV_DISTANCE;
	private double kernelRadius = 7;
	
	public RunLengthMatrix(final Image image){
		this.setImage(image);
	}
	
	/**
	 * @param image
	 * @param orientation - the orientation in degrees (it can be 0, 45, 90 or 135).
	 * @param band
	 */
	public RunLengthMatrix(final Image image, final int orientation, final int band){
		this.setImage(image);
		this.setOrientation(orientation);
		this.setBand(band);
	}
	
	public RunLengthMatrix(final int orientation, final int band){
		this.setOrientation(orientation);
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
	
	/**
	 * Orientation degree (in degrees). It can be 0, 45, 90 or 135.
	 * @param delta
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setOrientation(final int delta){
		if (delta != this.orientation) this.update = true;
		this.orientation = delta;
	}
	
	public void setBand(final int band){
		if (band != this.band) this.update = true;
		this.band = band;
	}
	public void setKernelRadialDistanceMeasure(final Distance distance){
		if (!this.distance.equals(distance)) this.update = true;
		this.distance = distance;
	}
	
	private TreeMap<Double, TreeMap<Integer, Integer>> bufferedMatrix = null;
	/**
	 * Returns a TreeMap that contains the grey value as the first key and the number of consecutive occurrences of this value given a certain orientation
	 * as the second key. The result of the treemap is the number of consecutive occurrences. For instance, getMatrix().get(200).get(2). In this case we would
	 * be looking for how many times the grey value 200 occurred with length of 2, i.e., occurred two times consecutive (two pixels of 200, one after the other).
	 * @return
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public TreeMap<Double, TreeMap<Integer, Integer>> getMatrix(){
		if ((lastHash == this.image.hashCode() || update) && bufferedMatrix != null) return bufferedMatrix;
		update = false;
		
		TreeMap<Double, TreeMap<Integer, Integer>> matrix = new TreeMap<Double, TreeMap<Integer, Integer>>();
		
		int dx = 0, dy = 0;
		switch(orientation){
		case 0:
			dx = 1;
			break;
		case 45:
			dx = 1; dy = 1;
			break;
		case 90:
			dy = 1;
			break;
		case 135:
			dx = -1;
			dy = 1;
			break;
		}
		
		float centralX = ((image.getWidth() - 1)/2f),
				centralY = ((image.getHeight() - 1)/2f);
		
		int length = 0;
		double p1;
		int currentX = dx, currentY = dy;
		for (int i=0; i<image.getHeight(); i++){
			for (int j=0; j<image.getWidth(); j++){
				if (distance.compute(centralX, centralY, j, i) > kernelRadius) continue;
				
				p1 = image.getPixel(j, i, band);
				
				currentX = dx; currentY = dy;
				length = 1;
				
				//for length 1
				if (!matrix.containsKey(p1)) matrix.put(p1, new TreeMap<Integer, Integer>());
				
				if (matrix.get(p1).containsKey(length))
					matrix.get(p1).put(length, matrix.get(p1).remove(length) + 1);
				else
					matrix.get(p1).put(length, 1);
				//
				
				if (j + currentX >= 0 && j + currentX < image.getWidth() && i + currentY < image.getHeight()){
					while (p1 == image.getPixelBoundaryMode(j + currentX, i + currentY, band)){
						if (j + currentX < 0 || j + currentX >= image.getWidth() || i + currentY >= image.getHeight()) break;
						
						length++;
						
						if (!matrix.containsKey(p1)) matrix.put(p1, new TreeMap<Integer, Integer>());
						
						if (matrix.get(p1).containsKey(length))
							matrix.get(p1).put(length, matrix.get(p1).remove(length) + 1);
						else
							matrix.get(p1).put(length, 1);
							
						currentX += dx;
						currentY += dy;
					}
				}
				
			}
		}
		
		bufferedMatrix = matrix;
		return matrix;
	}

	private ArrayList<Double> values;
	private ArrayList<Integer> lengths;
	public int[][] getRawMatrix(){
		
		TreeMap<Double, TreeMap<Integer, Integer>> matrix = getMatrix();
		values = new ArrayList<Double>(matrix.size());
		lengths = new ArrayList<Integer>();
		
		for (double p1 : matrix.keySet()){
			if (!values.contains(p1)) values.add(p1);
			
			for (int l : matrix.get(p1).keySet()){
				if (!lengths.contains(l))
					lengths.add(l);
			}
		}
		
		int[][] pMatrix = new int[matrix.size()][lengths.size()];
		
		
		for (int k=0; k<values.size(); k++){
			for (int l=0; l<lengths.size(); l++){
				if (matrix.get(values.get(k)).containsKey(lengths.get(l)))
					pMatrix[k][l] = matrix.get(values.get(k)).get(lengths.get(l));
			}
		}
		return pMatrix;
	}
	
	public void printMatrix(){
		int[][] matrix = getRawMatrix();
		
		String beg = "";
		for (int k=0; k<values.size(); k++){
			while (beg.length() < (values.get(k) + "").length())
				beg = " " + beg;
		}
		Logger.log(beg + "|");
		
		for (int k=0; k<lengths.size(); k++){
			int maxLength = 0;
			for (int i2=0; i2<matrix.length; i2++) if ((matrix[i2][k] + "").length() > maxLength) maxLength = (matrix[i2][k] + "").length();
			String v = lengths.get(k) + "";
			while (v.length() < maxLength)
				v = " " + v;
			Logger.log(v + " ");
		}
		Logger.log("\n ------------------------------ \n");
		
		for (int i=0; i<matrix.length; i++){
			String value = values.get(i) + "";
			while (value.length() < (values.get(values.size() - 1)+"").length()){
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
	
	
	public double getGreyLevelNonUniformity(){
		TreeMap<Double, TreeMap<Integer, Integer>> matrix = getMatrix();
		
		double sum = 0, straightSum = 0;
		for (double p1 : matrix.keySet()){
			double innerSum = 0;
			for (int l : matrix.get(p1).keySet()){
				innerSum += matrix.get(p1).get(l);
				straightSum += matrix.get(p1).get(l);
			}
			sum += Math.pow(innerSum, 2);
		}
		
		return sum/(double)straightSum;
	}
	
	
	public double getRunPercentage(){
		TreeMap<Double, TreeMap<Integer, Integer>> matrix = getMatrix();
		
		double straightSum = 0;
		for (double p1 : matrix.keySet()){
			double innerSum = 0;
			for (int l : matrix.get(p1).keySet()){
				straightSum += matrix.get(p1).get(l);
			}
		}
		
		return straightSum/(double)(this.image.getHeight()*this.image.getWidth());
	}
}
