package image;


import java.awt.Color;
import java.awt.Font;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.FileImageOutputStream;
import javax.swing.filechooser.FileSystemView;

import filters.Filter;
import log.Logger;
import morphology.Morphology;
import morphology.MorphologyConstants;
import similarity.SimilarityMeasure;

import static image.Image.BoundaryOperationType.*;
/**
 * Class representing an image.
 * @author �rick Oliveira Rodrigues (erickr@id.uff.br)
 */
public class Image{
	public static MorphologyConstants MorphologyConstants;
	public static enum BoundaryOperationType{BOUNDARY_MODULE, BOUNDARY_REFLECT, BOUNDARY_AVERAGE}

	public static class InterpolationType{
		public static final Object BICUBIC = RenderingHints.VALUE_INTERPOLATION_BICUBIC,
				BILINEAR = RenderingHints.VALUE_INTERPOLATION_BILINEAR,
				NEAREST_NEIGHBOR = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
	
			private Object interpolationType;
			InterpolationType(Object interpolationType){
				this.interpolationType = interpolationType;
			}
			
			public Object getType(){
				return interpolationType;
			}
	}


	private List<TreeMap<Double, Integer>> intensities = null; //ascendingly ordered
	//private int[][] pixelValue;
	private PixelMap pixMap = null;
	private BufferedImage bufferedImage = null;
	private boolean updateBufferedImage = true, updateHistogram = true, updateMean = true;
	//private boolean[][] binaryImgs;
	private byte bands = 1;
	private int type = -1;
	//
	private ImageOperation op = new ImageOperation(this); //low memory instance
	private ImageDisplay display = null;
	private Morphology morphology = null;
	//
	private double[] backgroundColor = null;
	private BoundaryOperationType boundaryOperation = BOUNDARY_REFLECT;
	//
	private boolean updateHashCode = false;
	private int hashCodeShift = 0;
	//

	//auxiliary variables
	
	private void instantiateMorphology(){if (morphology == null) morphology = new Morphology(this);}
	
	public Image set(Image img){
		pixMap = new PixelMap(img.getWidth(), img.getHeight(), img.getNumBands(), img.getBitDepth(), img.containsFloatValues());
		for (int b=0; b<img.getNumBands(); b++){
			pixMap.setPixelData(img.pixMap.getPixelData(b).clone(), b, img.getBitDepth());
		}
		this.type = img.getType();
		this.bands = (byte) img.getNumBands();
		updateBufferedImage = true; updateHistogram = true; updateMean = true;
		display = null; morphology = null; bufferedImage = null; intensities = null; minMax = null;
		return this;
	}
	

	/**
	 * Creates a 8-bits depth image
	 * @param width
	 * @param height
	 * @throws Exception 
	 */
	public Image(int width, int height, int numBands){
		pixMap = new PixelMap(width, height, numBands, 8, false);
		this.bands = (byte) numBands;
	}

	public Image(int width, int height, int numBands, int bitDepth){
		pixMap = new PixelMap(width, height, numBands, bitDepth, false);
		this.bands = (byte) numBands;
	}

	public Image(int width, int height, int numBands, int bitDepth, boolean canAssumeFloatValues){
		pixMap = new PixelMap(width, height, numBands, bitDepth, canAssumeFloatValues);
		this.bands = (byte) numBands;
	}
	/**
	 * Assumes the image is grey and the image values are at most 32 bits depth and creates a new image based on the values of matrix img.
	 * @param img - input matrix
	 */
	public Image(int[][] img){
		pixMap = new PixelMap(img[0].length, img.length, 1, 32, false);
		pixMap.setPixelData(img, 0);
	}
	/**
	 * Instantiates a new 8-bits depth grey image
	 * @param width - width of the image
	 * @param height - height of the image
	 */
	public Image(int width, int height){
		pixMap = new PixelMap(width, height, 1, 8, false);
	}
	/**
	 * Creates a new image that is 16-bits depth with the values of the matrix img
	 * @param img - input matrix
	 */
	public Image(short[][] img){
		pixMap = new PixelMap(img[0].length, img.length, 1, 16, false);
		pixMap.setPixelData(img, 0);
	}
	/**
	 * Creates a new image that is 8-bits depth (from 0 to 255) with the values of the matrix img
	 * @param img - input matrix
	 */
	public Image(byte[][] img){
		pixMap = new PixelMap(img[0].length, img.length, 1, 8, false);
		pixMap.setPixelData(img, 0);
	}
	/**
	 * Clones a image of type Image, creates a new object that is identical to the previous image but is a different object
	 * @param img - the image to be cloned
	 */
	public Image(Image img){
		this.set(img);
	}
	/**
	 * If the image is RGB-layered it will be converted to one single layer as the mean of every layer.
	 * @param img - The image to be loaded
	 */
	public Image(BufferedImage img){
		setImageFromBufferedImage(img);
	}
	public Image(String imagePath) throws IOException{
		setImageFromBufferedImage(ImageIO.read(new File(imagePath)));
	}
	public Image(File imgFile) throws IOException{
		setImageFromBufferedImage(ImageIO.read(imgFile));
	}
	public void updateImage(BufferedImage img, int width, int height) throws Exception{
		if (width != this.getWidth() || height != this.getHeight()){
			setImageFromBufferedImage(updateBufferedImage(width, height));
		}else
			updateImage(img);
	}
	public void updateImage(BufferedImage img){
		//raster = img.getRaster();
		setImageFromBufferedImage(img);
		
		/*
		if (this.getType() == img.getType() && raster.getNumBands() == this.getNumBands()) {
			setImageFromBufferedImage(img);
			return;
		}
		
		
		for (int i=0; i<this.getHeight(); i++){
			for (int j=0; j<this.getWidth(); j++){
				for (int b=0; b<raster.getNumBands() && b < this.getNumBands(); b++){
					this.setPixel(j, i, b, raster.getSample(j, i, b));
				}
			}
		}
		
		this.setToUpdateBuffers();
		*/
	}

	
	/**
	 * Applis a filer passed as parameter to the current image.
	 * @param filter
	 * @return
	 * @author �rick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public Image applyFilter(Filter filter){
		this.set(filter.applyFilter(this));
		return this;
	}

	public void resize(int width, int height) throws Exception{
		this.scale(width/(float)this.getWidth(), height/(float)this.getHeight());
		updateImage(this.getBufferedImage(), width, height);
	}
	public void resize(final float scaleX, final float scaleY) throws Exception{
		this.scale(scaleX, scaleY);
		//updateImage(this.getBufferedImage(), (int)(this.getWidth()*scaleX), (int)(this.getHeight()*scaleY));
	}
	private BufferedImage updateBufferedImage(){
		return updateBufferedImage(this.getWidth(), this.getHeight());
	}
	private BufferedImage updateBufferedImage(int width, int height) {
		int type = (this.isGray()) ? BufferedImage.TYPE_BYTE_GRAY : BufferedImage.TYPE_INT_RGB;
		if (this.getNumBands() == 4) type = BufferedImage.TYPE_INT_ARGB;

		//check if binary
		/*
		boolean isBinary = false;
		if (this.isGray()) {
			isBinary = true;
			double v1 = this.getPixel(0, 0, 0), v2 = v1;
			Gray:
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					double v = this.getPixel(j, i, 0);
					if (v != v1 && v != v2) {
						if (v1 != v2) {
							isBinary = false;
							break Gray;
						}
						v2 = v;
					}

				}
			}
			if (isBinary) type = BufferedImage.TYPE_BYTE_BINARY;
		}
		final int MAX_V = isBinary ? 1 : 255;
		*/

		bufferedImage = new BufferedImage(width, height, type);
		WritableRaster raster = bufferedImage.getRaster();


		int pixelValue = 0;

		for (int i=0; i<height; i++){
			for (int j=0; j<width; j++){
				for (int b=0; b<this.getNumBands() && b<raster.getNumBands(); b++){
					pixelValue = (int) this.getPixel(j, i, b);
					//if (pixelValue < 0) pixelValue = 0; if (pixelValue > MAX_V) pixelValue = MAX_V;
					raster.setSample(j, i, b, pixelValue);
				}
			}
		}

		this.setToUpdateBuffers();
		this.updateBufferedImage = false;
		return bufferedImage;
	}
	
	public void showHistogram() throws Exception{
		showHistogram(0);
	}
	public void showHistogram(final int band) throws Exception{
		getHistogramAsImage(band).showImage("Histogram");
	}
	
	public Image getHistogramAsImage(){
		return getHistogramAsImage(0);
	}
	public Image getHistogramAsImage(final int band){
		return getHistogramAsImage(256, 230, band);
	}
	public Image getHistogramAsImage(final int histogramWidth, final int histogramHeight, final int band){
		this.stretchOrShrinkRange(0, histogramWidth - 1);
		final int MAX_HIST_HEIGHT = histogramHeight, HIST_LENGTH = histogramWidth;
		Image histogram = new Image(HIST_LENGTH, MAX_HIST_HEIGHT);
		
		TreeMap<Double, Integer> hist = this.getHistogram(band);
		double max = Integer.MIN_VALUE;
		for (double intensity : hist.keySet()){
			if (hist.get(intensity) > max)
				max = hist.get(intensity);
		}
		
		float divisor = 1f;
		if (max > MAX_HIST_HEIGHT){
			divisor = (float) (MAX_HIST_HEIGHT/max);
		}
		
		for (int j=0; j<histogram.getWidth(); j++){
			for (int i=0; i<(int)(hist.get((double)j)*divisor); i++){
				histogram.setPixel(j, histogram.getHeight() - i - 1, 255);
			}
		}
		
		return histogram;
	}
	
	public TreeMap<Double, Integer> getHistogram(final int band){
		return this.getHistogram().get(band);
	}
	
	private ArrayList<TreeMap<Double,Integer>> getHistogram(){
		if (!this.updateHistogram && intensities != null) return (ArrayList<TreeMap<Double, Integer>>) intensities;

		intensities = new ArrayList<TreeMap<Double, Integer>>(this.getNumBands());
		for (int k=0; k<this.getNumBands(); k++) intensities.add(new TreeMap<Double, Integer>());
		for (int b=0; b<this.getNumBands(); b++){
			for (int i=0; i<this.getHeight(); i++){
				for (int j=0; j<this.getWidth(); j++){
					if (!intensities.get(b).containsKey(this.getPixel(j, i)))
						intensities.get(b).put(this.getPixel(j, i), 1);
					else{
						intensities.get(b).put(this.getPixel(j, i), intensities.get(b).remove(this.getPixel(j, i)) + 1);
					}
				}
			}
		}
		this.updateHistogram = false;
		return (ArrayList<TreeMap<Double, Integer>>) intensities;
	}
	
	
	//get
	public BoundaryOperationType getBoundaryOperation(){return this.boundaryOperation;}
	/**
	 * Returns the pixel at position (x,y).
	 * If the position is out of the image boundaries, then return the background color if set, otherwise returns 0 on that position.
	 * @return
	 * @author �rick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public double getPixelBoundaryMode(int x, int y){
		return getPixelBoundaryMode(x, y, 0);
	}
	/**
	 * Returns the pixel at position (x,y).
	 * If the position is out of the image boundaries, then return the background color if set, otherwise returns 0 on that position.
	 * @param x
	 * @param y
	 * @param band
	 * @return
	 * @author �rick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public double getPixelBoundaryMode(int x, int y, int band){
		final int WIDTH = this.getWidth(), HEIGHT = this.getHeight();
		if (band >= this.getNumBands()) return 0;
		if (x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT){
			if (this.backgroundColor != null){
				return this.backgroundColor[band];
			}else{
				if (this.getBoundaryOperation() == BOUNDARY_AVERAGE) return this.getAverageIntensity(band);

				int nX = 0, nY = 0;
				if (x < 0){
					switch(this.getBoundaryOperation()){
					case BOUNDARY_MODULE:
						nX = WIDTH - 1 - Math.abs(x % WIDTH);
						break;
					case BOUNDARY_REFLECT:
						nX = Math.abs(x % WIDTH);
						break;
					}
				}else if (x >= WIDTH){
					switch(this.getBoundaryOperation()){
					case BOUNDARY_MODULE:
						nX = Math.abs(x % WIDTH);
						break;
					case BOUNDARY_REFLECT:
						nX = WIDTH - 1 - Math.abs(x % WIDTH);
						break;
					}
				}
				if (y < 0){
					switch(this.getBoundaryOperation()){
					case BOUNDARY_MODULE:
						nY = HEIGHT - 1 - Math.abs(y % HEIGHT);
						break;
					case BOUNDARY_REFLECT:
						nY = Math.abs(y % HEIGHT);
						break;
					}
				}else if (y >= HEIGHT){
					switch(this.getBoundaryOperation()){
					case BOUNDARY_MODULE:
						nY = Math.abs(y % HEIGHT);
						break;
					case BOUNDARY_REFLECT:
						nY = HEIGHT - 1 - Math.abs(y % HEIGHT);
						break;
					}
				}
				return this.getPixel(nX, nY, band);
			}
		}
		return this.getPixel(x, y, band);
	}
	public double getPixel(int x, int y){
		return getPixel(x, y, 0);
	}
	public double getPixel(int x, int y, int band){
		return this.pixMap.get(x, y, band);
	}

	public int[][] getMatrixImage() throws Exception{
		return this.pixMap.getIntegerPixelData(0);
	}
	public Set<Double> getIntensities(int band){
		return getHistogram().get(band).keySet();
	}
	public int getHeight(){return this.pixMap.getHeight();}
	public int getWidth(){return this.pixMap.getWidth();}
	public float getDiagonalLength(){return (float) Math.pow(Math.pow(this.getWidth(), 2) + Math.pow(this.getHeight(), 2), 1/2d);}
	public boolean containsIntensity(double intensity, int band){
		if (this.getHistogram().get(band).containsKey(intensity)) 
			return this.getHistogram().get(band).get(intensity) >= 1; 
		return false;
	}
	public byte getNumBands(){return bands;}
	/**
	 * This method should be taken care because if the maximum value of the image exceeds 255, then it will be painted 255. If the minimal value is under 0, then it is also painted 0.
	 * @return
	 * @throws Exception
	 */
	public BufferedImage getBufferedImage() {
		if (!this.updateBufferedImage) return this.bufferedImage;

		this.bufferedImage = updateBufferedImage();
		return this.bufferedImage;
	}
	public int getType(){
		if (type == -1)
			switch (this.getNumBands()){
				case 1:
					return BufferedImage.TYPE_BYTE_GRAY;
				case 3:
					return BufferedImage.TYPE_INT_RGB;
				case 4:
					return BufferedImage.TYPE_INT_ARGB;
			}
		return type;
	}
	//private Vector minMax = null; 
	private HashMap<Integer, Vector> minMax = null;
	public Vector getMinMaxIntensity(int band){
		if (this.updateHistogram) {minMax = null; this.updateHistogram = false;}
		if (minMax == null){
			minMax = new HashMap<Integer, Vector>();
		}
		if (!minMax.containsKey(band)){
			double min = Long.MAX_VALUE, max = Long.MIN_VALUE;
			double value = 0;
			for (int i=0; i<this.getHeight(); i++){
				for (int j=0; j<this.getWidth(); j++){
					if (this.isGray()) value = this.getPixel(j, i);
					else value = this.getPixel(j, i, band);
					if (value < min)
						min = value;
					if (value > max)
						max = value;
				}
			}
			minMax.put(band,  new Vector(min, max));
		}
		return minMax.get(band);
	}
	public double getMinimalIntesity(int band){
		return this.getMinMaxIntensity(band).x;
	}
	public double getMaximalIntensity(int band){
		return this.getMinMaxIntensity(band).y;
	}
	public int getBitDepth(){return this.pixMap.getBitDepth();}//acertar dps
	public boolean containsFloatValues(){return this.pixMap.containsFloatValues();}
	private int getAssociatedNumBands(int imgType){return (imgType == BufferedImage.TYPE_BYTE_GRAY) ? 1 : (imgType == BufferedImage.TYPE_INT_RGB) ? 3 : 4;}
	private int getAssociatedType(int numBands){return (numBands == 1) ? BufferedImage.TYPE_BYTE_GRAY : (numBands == 3) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;}
	double meanIntensity = 0;
	byte meanBandBuffer = 0;
	public double getAverageIntensity(int band){
		if (!this.updateMean && meanBandBuffer == band) 
			return meanIntensity;
		
		BigDecimal bd = BigDecimal.valueOf(0);
		for (int i=0; i<this.getHeight(); i++){
			for (int j=0; j<this.getWidth(); j++){
				final double p = this.getPixel(j, i, band);
				//if (Double.isNaN(p)) continue;
				bd = bd.add(BigDecimal.valueOf(p));
			}
		}
		try{
			bd = bd.divide(BigDecimal.valueOf(this.getWidth()*this.getHeight()));
		}catch(Exception e){
			bd = bd.divide(BigDecimal.valueOf(this.getWidth()*this.getHeight()), 2, RoundingMode.HALF_UP); //not exact divisions
		}
		this.updateMean = false;
		this.meanBandBuffer = (byte) band;
		this.meanIntensity = bd.doubleValue();
		return meanIntensity;
	}
	
	/**
	 * Cluster binary images
	 * @return
	 * @throws Exception 
	 */
	public Image clusterImage() throws Exception{
		instantiateMorphology();
		return this.set(morphology.cluster(this, Morphology.STRUCT_PRIMARY));
	}
	/**
	 * Converts a binary image to an grey image, where at every white pixel the image receives the index of the pixel.
	 * @throws Exception 
	 */
	public Image convertToIndexedImage() throws Exception{
		if (!this.isBinary()) throw new Exception("Image must be binary in order to convert it to an indexed image.");
		
		boolean[][] img = new boolean [this.getHeight()][this.getWidth()];
		for (int i=0; i<img.length; i++) for (int j=0; j<img[0].length; j++) img[i][j] = this.getPixel(j, i) > 0;
		
		this.convertToGray(0, 64);
		for (int i=0; i<this.getHeight(); i++){
			for (int j=0; j<this.getWidth(); j++){
				if (img[i][j]) this.setPixel(j, i, i*this.getWidth() + j);
			}
		}
		img = null;
		return this;
	}
	
	/**
	 * Contrast the image according to a 8-bit depth image (from 0 to 255). In other words, stretches the image values to within this range: [0,255].
	 * @throws CloneNotSupportedException 
	 */
	public Image contrast(){
		this.stretchOrShrinkRange(0, 255);
		return this;
	}
	
	/**
	 * Linearly convert the image values to a new range. If your images goes from -10 to 80, and your pass as parameter newMinimum = 0 and newMaximum = 255, your
	 * image is stretched out to fir the range [0,255], for instance.
	 * @param newMinimum
	 * @param newMaximum
	 */
	public Image normalize(double newMinimum, double newMaximum){
		return stretchOrShrinkRange(newMinimum, newMaximum);
	}
	/**
	 * Linearly convert the image values to a new range. If your images goes from -10 to 80, and your pass as parameter newMinimum = 0 and newMaximum = 255, your
	 * image is stretched out to fir the range [0,255], for instance.
	 * @param newMinimum
	 * @param newMaximum
	 */
	public Image stretchOrShrinkRange(double newMinimum, double newMaximum){
		for (int b=0; b<this.getNumBands(); b++){
			final double max = this.getMaximalIntensity(b), min = this.getMinimalIntesity(b);
			for (int i=0; i<this.getHeight(); i++){
				for (int j=0; j<this.getWidth(); j++){
					final double r = ((newMinimum - newMaximum)/(min - max))*this.getPixel(j, i, b) +  newMaximum -(((newMinimum - newMaximum)/(min - max))*max);
					this.setPixel(j, i, b, r);
				}
			}
		}
		return this;
	}


	/**
	 * Reduces the background of the image iteratively
	 * @param backgroundValue
	 */
	public void reduceBackground(final int backgroundValue){
		double currentNotBackground = this.countValue(backgroundValue, true);
		double newNotBackground = currentNotBackground;

		while(newNotBackground == currentNotBackground) {
			double currentBackground = this.countValue(backgroundValue);
			double newBackground = currentBackground;

			//top left
			Image img = this.clone();
			img.subImage(1, 1, this.getWidth()-1, this.getHeight()-1);
			newBackground = img.countValue(backgroundValue);
			newNotBackground = img.countValue(backgroundValue, true);


			if (newBackground < currentBackground && newNotBackground == currentNotBackground) {
				this.subImage(1, 1, this.getWidth()-1, this.getHeight()-1);
			}

			//bottom right
			img = this.clone();
			img.subImage(0, 0, this.getWidth() - 1, this.getHeight() - 1);
			newBackground = img.countValue(backgroundValue);
			newNotBackground = img.countValue(backgroundValue, true);

			if (newBackground < currentBackground && newNotBackground == currentNotBackground) {
				this.subImage(0, 0, this.getWidth() - 1, this.getHeight() - 1);
			}

		}

	}


	/**
	 * Counts the amount of occurrences of value in band
	 * @param value
	 * @param band
	 * @param otherThan - true if it is to count the amount of values other than the one passed as parameter
	 * @return
	 */
	public double countValue(final double value, final int band, boolean otherThan){
		double counter = 0;
		for (int i=0; i<this.getHeight(); i++){
			for (int j=0; j<this.getWidth(); j++){
				if (!otherThan){
					if (this.getPixel(j, i, band) == value)
						counter++;
				}else{
					if (this.getPixel(j, i, band) != value)
						counter++;
				}
			}
		}
		return counter;
	}

	/**
	 * Counts the amount of occurrences of value in band
	 * @param value
	 * @return
	 */
	public double countValue(final double value, final int band){
		return countValue(value, band, false);
	}

	public double countValue(final double value, final boolean otherThan){
		double counter = 0;
		for (int b=0; b<this.getNumBands(); b++)
			counter += countValue(value, b, otherThan);
		return counter;
	}

	/**
	 * Counts the amount of occurrences of value in all bands
	 * @param value
	 * @return
	 */
	public double countValue(final double value){
		double counter = 0;
		for (int b=0; b<this.getNumBands(); b++)
			counter += countValue(value, b);
		return counter;
	}
	
	public int hashCode(){
		if (updateHashCode){
			Random r = new Random();
			hashCodeShift = 0;
			while(hashCodeShift == 0)
				hashCodeShift = r.nextInt(1000000000) - 500000000;
			this.updateHashCode = false;
		}
		return super.hashCode() + hashCodeShift;
	}
	
	
	//set
	public Image setTitle(final String title){
		if (display == null) display = new ImageDisplay();
		this.display.setTitle(title);
		return this;
	}
	public void setSize(final int width, final int height){
		Image nImg = new Image(width, height, this.getNumBands(), this.getBitDepth(), this.containsFloatValues());
		for (int b=0; b<this.getNumBands(); b++){
			for (int i=0; i<height && i<this.getHeight(); i++){
				for (int j=0; j<width && j<this.getWidth(); j++){
					nImg.setPixel(j, i, this.getPixelBoundaryMode(j, i, b));
				}
			}
		}
		this.set(nImg);
	}
	public void setWidth(final int width){
		this.setSize(width, this.getHeight());
	}
	public void setHeight(final int height){
		this.setSize(this.getWidth(), height);
	}
	/**
	 * Sets the boundary operation.
	 * @param boundaryOperation -
	 * {@link BoundaryOperationType#BOUNDARY_MODULE} if we want to take the module of the coordinate in respect to the size of the image, or
	 * {@link BoundaryOperationType#BOUNDARY_REFLECT} if we want to take the reflect the pixels near the boundary.
	 * @author �rick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void setBoundaryOperation(final BoundaryOperationType boundaryOperation){
		this.boundaryOperation = boundaryOperation;
	}
	public void setToUpdateBuffers(){this.updateBufferedImage = true; this.updateHistogram = true; this.updateMean = true; this.updateHashCode = true;}
	public void setPixelAllBands(int x, int y, double value){
		for (int b=0; b<this.getNumBands(); b++) this.pixMap.set(x, y, b, value); setToUpdateBuffers();
	}
	public void setPixel(int x, int y, double value){this.pixMap.set(x, y, value); setToUpdateBuffers();}
	public void setPixel(int x, int y, int band, double value){
		//this.updateBuffered = true;
		setToUpdateBuffers();
		this.pixMap.set(x, y, band, value);
	}
	public void setPixel(int x, int y, int band, Color color){
		setToUpdateBuffers();
		switch (band){
		case 0: this.pixMap.set(x, y, 0, color.getRed()); break;
		case 1: if (this.getNumBands() <= 1) break; this.pixMap.set(x, y, 1, color.getGreen()); break;
		case 2: if (this.getNumBands() <= 2) break; this.pixMap.set(x, y, 2, color.getBlue()); break;
		case 3: if (this.getNumBands() <= 3) break; this.pixMap.set(x, y, 3, color.getAlpha()); break;
		}
	}
	public void setPixel(int x, int y, Color color){
		for (int b=0; b<this.getNumBands(); b++)
			setPixel(x, y, b, color);
	}
	public void setPixelBoundaryMode(int x, int y, int band, double value){
		if (x < 0 || y < 0 || x >= this.getWidth() || y >= this.getHeight()) return;
		setPixel(x, y, band, value);
	}
	public void setNumBands(int num) throws Exception{this.bands = (byte) num; setConfiguration(num, this.getBitDepth());}
	public void setType(int type) throws Exception{this.type = type; setConfiguration(this.getNumBands(), getAssociatedNumBands(type));}
	
	public Image convertToRGB() throws Exception{
		return convertToRGB(8);
	}
	public Image convertToRGB(final int bitDepth) throws Exception{
		PixelMap pm = new PixelMap(this.getWidth(), this.getHeight(), 3, bitDepth, false);
		for (int b=0; b<3; b++){
			for (int i=0; i<this.getHeight(); i++){
				for (int j=0; j<this.getWidth(); j++){
					pm.set(j, i, b, this.getPixel(j, i, 0));
				}
			}
		}
		this.setNumBands(3);
		this.setType(BufferedImage.TYPE_INT_RGB);
		//this.updateBuffered = true;
		this.setToUpdateBuffers();
		this.pixMap = pm;
		return this;
	}
	public Image convertToGray() throws Exception{return convertToGray(0, 8);}
	public Image convertToGray(int band, int bitDepth) throws Exception{
		PixelMap pm = new PixelMap(this.getWidth(), this.getHeight(), 1, bitDepth, false);
		for (int i=0; i<this.getHeight(); i++){
			for (int j=0; j<this.getWidth(); j++){
				pm.set(j, i, this.getPixel(j, i, band));
			}
		}
		this.setConfiguration(1, BufferedImage.TYPE_BYTE_GRAY);

		this.pixMap = pm;
		return this;
	}
	public Image convertToBinary(float threshold) throws Exception{
		PixelMap pm = new PixelMap(this.getWidth(), this.getHeight(), 1, 1, false);
		for (int i=0; i<this.getHeight(); i++){
			for (int j=0; j<this.getWidth(); j++){
				pm.set(j, i, this.getPixel(j, i, 0) >= threshold ? 255 : 0);
			}
		}
		this.setNumBands(1);
		this.setType(BufferedImage.TYPE_BYTE_BINARY);
		//this.updateBuffered = true;
		this.setToUpdateBuffers();
		this.pixMap = pm;
		return this;
	}
	public void setRenderingAntiAliasing(boolean setOn){op.setAntiAliasing(setOn);} 
	/**
	 * Sets the interpolation when the image is rendered using Java's Graphics2D
	 * @param interpolationType - the types can be Image.BICUBIC, Image.BILINEAR or Image.NEAREST_NEIGHBOR
	 */
	public void setInterpolation(Object interpolationType){this.op.setInterpolation(interpolationType);}
	public void setBitDepth(final int bitDepth) throws Exception{this.setConfiguration(this.getNumBands(), bitDepth);}
	public void setConfiguration(int numBands, int bitDepth) throws Exception{setConfiguration(numBands, bitDepth, false);}
	public void setConfiguration(int numBands) throws Exception{setConfiguration(numBands, 8, false);}
	public void setConfiguration(int numBands, int bitDepth, boolean containsFloatValues) throws Exception{ //TERMINAR
		
		if (bitDepth < 32 && containsFloatValues){Logger.log("The minimum bit-depth for float values is 32, resetting it to 32.\n");}
		PixelMap previousMap = this.pixMap;
		this.bands = (byte) numBands;
		this.type = getAssociatedType(numBands);
		this.pixMap = new PixelMap(this.getWidth(), this.getHeight(), numBands, bitDepth, containsFloatValues);
		for (int i=0; i<this.getHeight(); i++){
			for (int j=0; j<this.getWidth(); j++){
				for (int b=0; b<numBands; b++){
					if (previousMap.getNumBands() <= b)
						continue;

					this.setPixel(j, i, b, previousMap.get(j, i, b));
				}
			}
		}
		previousMap.dispose();
		previousMap = null;


		this.updateBufferedImage();

		//if (numBands == 1) aux = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.);
		if (this.getBitDepth() != bitDepth){
			Logger.log("The bit depth has not changed.\n");
		}
	}
	public void setImageFromBufferedImage(BufferedImage img){
		if (pixMap != null) pixMap.dispose();

		if (img.getType() == BufferedImage.TYPE_BYTE_INDEXED) {
			BufferedImage rgb = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
			rgb.createGraphics().drawImage(img, 0, 0, null);
			img = rgb;
		}

		//update objects
		this.setToUpdateBuffers(); this.updateBufferedImage = false;
		this.bufferedImage = img;
		this.type = img.getType();
		
		WritableRaster raster = img.getRaster();
		this.bands = (byte) raster.getNumBands();
		pixMap = new PixelMap(img.getWidth(), img.getHeight(), raster.getNumBands(), 8, false);


		//fill the image
		for (int i=0; i<img.getHeight(); i++) {
			for (int j=0; j<img.getWidth(); j++) {
				for (int b=0; b<raster.getNumBands(); b++) {
					this.pixMap.set(j, i, b, raster.getSample(j, i, b));
				}
			}
		}
		
	}

	/**
	 * Paints the background of the image given an alpha threshold
	 * @param bgColor
	 * @param alphaThreshold
	 * @return
	 */
	public Image paintBackground(final double[] bgColor, float alphaThreshold){
		if (this.getNumBands() < 4) return this;

		for (int i=0; i<this.getHeight(); i++){
			for (int j=0; j<this.getWidth(); j++){

				if (this.getPixel(j, i, 3) <= alphaThreshold){
					this.setPixel(j, i, 0, bgColor[0]);
					this.setPixel(j, i, 1, bgColor[1]);
					this.setPixel(j, i, 2, bgColor[2]);
					if (bgColor.length == 4) this.setPixel(j, i, 3, bgColor[3]);
				}

			}
		}
		return this;
	}

	public void setBackgroundColor(final double[] bgColor){this.backgroundColor = bgColor;}
	public void setBackgroundColor(double value, int numOfBands){
		this.backgroundColor = new double[numOfBands];
		for (int k=0; k<numOfBands; k++){
			this.backgroundColor[k] = value;
		}
	}

	/**
	 * Transforms this image in a cropped image, starting at position (x, y), with width and height as set in the parameters.
	 * This method also centers the content of the image.
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return
	 */
	public Image setCanvas(int x, int y, int width, int height){
		return this.setCanvas(x, y,  width, height, 0, 0);
	}

	/**
	 * Transforms this image in a cropped image, starting at position (x, y), with width and height as set in the parameters.
	 * This method also centers the content of the image.
	 * @param width
	 * @param height
	 * @return
	 */
	public Image setCanvas(int width, int height){
		return this.setCanvas(0,0,width,height);
	}

	/**
	 * Transforms this image in a cropped image, starting at position (x, y), with width and height as set in the parameters.
	 * This method also centers the content of the image.
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param xShift - shifts the content of the image (x axis)
	 * @param yShift - shifts the content of the image (y axis)
	 * @return
	 */
	public Image setCanvas(int x, int y, int width, int height, int xShift, int yShift){
		int displaceY = (this.getHeight() - height)/2, displaceX = (this.getWidth() - width)/2;
		subImage(x, y, displaceX + xShift, displaceY + yShift, width, height);
		return this;
	}


	/**
	 * Transforms this image in a cropped image, starting at position (x, y), with width and height as set in the parameters.
	 * This method also centers the content of the image.
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param centroidX - this is the x point that will be centered in the new canvas resolution
	 * @param centroidY - this is the y point that will be centered in the new canvas resolution
	 * @return
	 */
	public Image setCanvas(int x, int y, int width, int height, float centroidX, float centroidY){
		int displaceY = (this.getHeight() - height)/2, displaceX = (this.getWidth() - width)/2;
		displaceX += centroidX -this.getWidth()/2; displaceY += centroidY - this.getHeight()/2;

		subImage(x, y, displaceX, displaceY, width, height);
		return this;
	}

	
	//is
	public boolean isGray(){return bands == 1;}
	public boolean isBinary(){return this.getBitDepth() == 1;}
	
	//has
	public boolean hasBufferedImage(){return this.bufferedImage != null;}
	
	//others
	public void dispose(){if (this.intensities != null) this.intensities.clear(); this.intensities = null; this.pixMap = null;}
	public void disposeBufferedImage(){this.bufferedImage = null;}
	
	
	public Image clone() {
		return new Image(this);
	}
	
	public boolean equals(Image comparedImg, SimilarityMeasure sm) throws Exception{
		sm.setImages(this, comparedImg);
		return sm.isEqual(this, comparedImg);
	}
	/**
	 * Assumes you are comparing the images using the mean difference measure. May not be threadsafe if multiple Images are accessing this method.
	 * @param comparedImg
	 * @return
	 * @throws Exception 
	 */
	public boolean equals(Image comparedImg) throws Exception{
		SimilarityMeasure.MEAN_DIFFERENCE.setImages(this, comparedImg);
		return SimilarityMeasure.MEAN_DIFFERENCE.isEqual(this, comparedImg);
	}
	
	
	/**
	 * Compares this image with an imaged passed as parameter according to a certain similarity measure, returns a double value representing the similarity.
	 * The correspondence of the value depends on the employed measure, but usually, the lower the returned result the better.
	 * @param comparedImg
	 * @param sm
	 * @return
	 * @throws Exception 
	 */
	public double compare(Image comparedImg, SimilarityMeasure sm) throws Exception{
		sm.setImages(this, comparedImg);
		return sm.compare();
	}
	
	public void print(){
		for (int b=0; b<this.getNumBands(); b++){
			System.out.printf("Band (layer): %d \n", b);
			for (int i=0; i<this.getHeight(); i++){
				for (int j=0; j<this.getWidth(); j++){
					if (!this.isGray())
						System.out.printf("%d ", this.getPixel(j, i, b));
					else
						System.out.printf("%d ", this.getPixel(j, i));
				}
				System.out.printf("\n");
			}
		}
	}
	
	/**
	 * Saves the image on the path provided and with the format provided (png, jpg, etc)
	 * @param outputPath
	 * @param formatName
	 * @throws Exception
	 * @author �rick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void exportImage(String outputPath, String formatName) throws Exception{
		ImageIO.write(this.getBufferedImage(), formatName.toUpperCase(), new File(outputPath));
	}


	/**
	 * Saves a JPG compressed image
	 * @param outputPath - the output path of the image
	 * @param compresisonQuality - compression quality (0 worst, 1 best)
	 * @throws Exception
	 */
	public void exportCompressedImage(String outputPath, final float compresisonQuality) throws Exception{
		JPEGImageWriteParam jpegParams = new JPEGImageWriteParam(null);
		jpegParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		jpegParams.setCompressionQuality(compresisonQuality);
		final ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
		// specifies where the jpg image has to be written
		writer.setOutput(new FileImageOutputStream(
				new File(outputPath)));
		// writes the file with given compression level
		// from your JPEGImageWriteParam instance
		writer.write(null, new IIOImage(this.getBufferedImage(), null, null), jpegParams);
	}
	
	/**
	 * Saves the image as png on the path provided
	 * @param outputPath
	 * @throws Exception
	 * @author �rick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void exportImage(String outputPath) throws Exception{
		ImageIO.write(this.getBufferedImage(), "PNG", new File(outputPath));
	}
	
	/**
	 * Saves the image as png in a standard folder
	 * @throws Exception
	 * @author �rick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public void exportImage() throws Exception{
		File home = FileSystemView.getFileSystemView().getHomeDirectory(),
				newFolder = new File(home.getAbsolutePath() + "/Uacari/");
		SecureRandom random = new SecureRandom();
		String output = newFolder.getAbsolutePath() + "/" + new BigInteger(80, random).toString(32) + ".png";

		System.out.printf("The images are being exported to the folder: " + output);
		exportImage(output);
	}
	
	
	/**
	 * Opening and closing operations. Dilation + erosion - original image.
	 * @param structuringElement
	 * @param times - times to dilate and erode
	 * @return
	 * @throws IOException
	 * @throws CloneNotSupportedException
	 */
	public Image skeletonize(Image structuringElement, int times) throws IOException, CloneNotSupportedException{
		Image aux = new Image(this);
		for (int k=0; k<times; k++) this.op.dilateOrErode(structuringElement, true);
		for (int k=0; k<times; k++) this.op.dilateOrErode(structuringElement, false);
		this.subtract(aux);
		this.contrast();
		return this;
	}
	/**
	 * Opening and closing operations. Dilation + erosion - original image.
	 * @param times
	 * @return
	 * @throws IOException
	 * @throws CloneNotSupportedException
	 */
	public Image skeletonize(int times) throws IOException, CloneNotSupportedException{
		if (this.isBinary()) return this.skeletonize(Morphology.STRUCT_SIMPLE_BINARY_RECT, times);
		else return this.skeletonize(Morphology.STRUCT_PRIMARY, times);
	}
	
	/**
	 * Operates the image according to the operation in PixelOperation
	 * @param pixelOperation
	 * @return
	 * @author �rick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public Image operate(final PixelOperation pixelOperation){
		for (int b=0; b<this.getNumBands(); b++){
			operate(pixelOperation, b);
		}
		return this;
	}
	
	/**
	 * Operates the image according to the operation in PixelOperation
	 * @param pixelOperation
	 * @param band
	 * @return
	 * @author �rick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public Image operate(final PixelOperation pixelOperation, final int band){
		double[] pixels = new double[1];
		for (int i=0; i<this.getHeight(); i++){
			for (int j=0; j<this.getWidth(); j++){
				pixels[0] = this.getPixel(j, i, band);
				this.setPixel(j, i, band, pixelOperation.compute(pixels));
			}
		}
		return this;
	}
	
	/**
	 * Operates the image along with the images passed as parameter according to the operation defined in PixelOperation
	 * @author �rick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public Image operate(final PixelOperation pixelOperation, final Image image){
		Image[] imgArray = new Image[1];
		imgArray[0] = image;
		for (int b=0; b<this.getNumBands(); b++)
			operate(pixelOperation, b, imgArray);
		return this;
	}
	
	/**
	 * Operates the image along with the images passed as parameter according to the operation defined in PixelOperation
	 * @author �rick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public Image operate(final PixelOperation pixelOperation, final int band, final Image image){
		Image[] imgArray = new Image[1];
		imgArray[0] = image;
		return operate(pixelOperation, band, imgArray);
	}

	/**
	 * Operates the image along with the images passed as parameter according to the operation defined in PixelOperation
	 * @param pixelOperation
	 * @param images
	 * @return
	 * @author �rick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public Image operate(final PixelOperation pixelOperation, final Image... images){
		for (int b=0; b<this.getNumBands(); b++)
			operate(pixelOperation, b, images);
		return this;
	}
	
	/**
	 * Operates the image along with the images passed as parameter according to the operation defined in PixelOperation
	 * @param pixelOperation
	 * @param band
	 * @param images
	 * @return
	 * @author �rick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	public Image operate(final PixelOperation pixelOperation, final int band, final Image... images){
		double[] pixels = new double[images.length + 1];
		for (int i=0; i<this.getHeight(); i++){
			for (int j=0; j<this.getWidth(); j++){
				pixels[0] = this.getPixel(j, i, band);
				for (int k=0; k<images.length; k++){
					pixels[k + 1] = images[k].getPixelBoundaryMode(j, i, band);
				}
				this.setPixel(j, i, band, pixelOperation.compute(pixels));
			}
		}
		return this;
	}
	
	/**
	 * Transforms this image in a cropped image, starting at position (x, y), with width and height as set in the parameters.
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return
	 */
	public Image subImage(int x, int y, int xShift, int yShift, int width, int height){
		//if (width > this.getWidth() - x) width = this.getWidth() - x; if (height > this.getHeight() - y) height = this.getHeight() - y;
		Image out = new Image(width, height, this.getNumBands(), this.getBitDepth(), this.containsFloatValues());
		for (int i=0; i<height; i++)
			for (int j=0; j<width; j++)
				for (int b=0; b<out.getNumBands(); b++)
					out.setPixel(j, i, b, this.getPixelBoundaryMode(j + x + xShift, i + y + yShift, b));

		this.set(out);
		return this;
	}

	/**
	 * Transforms this image in a cropped image, starting at position (x, y), with width and height as set in the parameters.
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return
	 */
	public Image subImage(int x, int y, int width, int height){
		return subImage(x, y, 0, 0, width, height);
	}
	
	public Image insertImage(Image imageToInsert, final int x, final int y){
		for (int i=0; i<imageToInsert.getHeight(); i++){
			for (int j=0; j<imageToInsert.getWidth(); j++){
				final int numBands = this.getNumBands() > imageToInsert.getNumBands() ? this.getNumBands() : imageToInsert.getNumBands();
				for (int b=0; b<numBands; b++){
					if (j + x >= this.getWidth() || i + y >=this.getHeight()) continue;
					this.setPixel(j + x, i + y, b, imageToInsert.getPixel(j, i, b));
				}
			}
		}
		return this;
	}
	
	public Image drawInnerBorder(final Color borderColor, final int size){
		for (int i=0; i<this.getHeight(); i++){
			for (int j=0; j<size; j++){
				if (this.getNumBands() > 0) this.setPixel(j, i, 0, borderColor.getRed());
				if (this.getNumBands() > 1) this.setPixel(j, i, 1, borderColor.getGreen());
				if (this.getNumBands() > 2) this.setPixel(j, i, 2, borderColor.getBlue());
				if (this.getNumBands() > 3) this.setPixel(j, i, 3, borderColor.getAlpha());
				
				if (this.getNumBands() > 0) this.setPixel(this.getWidth() - j - 1, i, 0, borderColor.getRed());
				if (this.getNumBands() > 1) this.setPixel(this.getWidth() - j - 1, i, 1, borderColor.getGreen());
				if (this.getNumBands() > 2) this.setPixel(this.getWidth() - j - 1, i, 2, borderColor.getBlue());
				if (this.getNumBands() > 3) this.setPixel(this.getWidth() - j - 1, i, 3, borderColor.getAlpha());
			}
		}
		for (int j=0; j<this.getWidth(); j++){
			for (int i=0; i<size; i++){
				if (this.getNumBands() > 0) this.setPixel(j, i, 0, borderColor.getRed());
				if (this.getNumBands() > 1) this.setPixel(j, i, 1, borderColor.getGreen());
				if (this.getNumBands() > 2) this.setPixel(j, i, 2, borderColor.getBlue());
				if (this.getNumBands() > 3) this.setPixel(j, i, 3, borderColor.getAlpha());
				
				if (this.getNumBands() > 0) this.setPixel(j, this.getHeight() - i - 1, 0, borderColor.getRed());
				if (this.getNumBands() > 1) this.setPixel(j, this.getHeight() - i - 1, 1, borderColor.getGreen());
				if (this.getNumBands() > 2) this.setPixel(j, this.getHeight() - i - 1, 2, borderColor.getBlue());
				if (this.getNumBands() > 3) this.setPixel(j, this.getHeight() - i - 1, 3, borderColor.getAlpha());
			}
		}
		return this;
	}
	
	public Image drawInnerBorder(final int borderValue, final int borderSize){
		for (int b=0; b<this.getNumBands(); b++){
			drawInnerBorder(borderValue, b, borderSize);
		}
		return this;
	}
	
	public Image drawInnerBorder(final int borderValue, final int band, final int borderSize){
		for (int i=0; i<this.getHeight(); i++){
			for (int j=0; j<borderSize; j++){
				this.setPixel(j, i, band, borderValue);
				this.setPixel(this.getWidth() - j - 1, i, band, borderValue);
			}
		}
		for (int j=0; j<this.getWidth(); j++){
			for (int i=0; i<borderSize; j++){
				this.setPixel(j, this.getHeight() - i - 1, band, borderValue);
				this.setPixel(j, i, band, borderValue);
			}
		}
		return this;
	}
	
	//
	public Image multiply(final float factor){for (int b=0; b<this.getNumBands(); b++) multiply(factor, b); return this;}
	public Image multiply(final float factor, final int band){
		for (int i=0; i<this.getHeight(); i++){
			for (int j=0; j<this.getWidth(); j++){
				this.setPixel(j, i, band, this.getPixel(j, i, band)*factor);
			}
		}
		return this;
	}
	public Image sum(final float parcel){for (int b=0; b<this.getNumBands(); b++) sum(parcel, b); return this;}
	public Image sum(final float parcel, final int band){
		for (int i=0; i<this.getHeight(); i++){
			for (int j=0; j<this.getWidth(); j++){
				this.setPixel(j, i, band, this.getPixel(j, i, band) + parcel);
			}
		}
		return this;
	}
	public Image subtract(final float parcel){sum(-parcel); return this;}
	public Image subtract(final float parcel, final int band){sum(-parcel, band); return this;}
	public Image divide(final float factor, final int band){multiply(1f/factor, band); return this;}
	public Image divide(final float factor){multiply(1f/factor); return this;}
	
	//transforms
	private Vector auxVec1 = new Vector(0, 0), auxVec2 = new Vector(0, 0);
	public Image translate(int x, int y) throws Exception{op.translate(x, y); return this;}
	public Image scale(double x, double y) throws Exception{op.scale(x, y); return this;}
	public Image scaleWithFixedSize(double x, double y) throws Exception{op.scaleWithFixedSize(x, y); return this;}
	public Image transform(AffineTransform at) {op.transform(at); return this;}
	public Image rotate(double theta)throws Exception{op.rotate(theta, true); return this;}
	public Image rotateWithoutCentralizing(double theta)throws Exception{op.rotate(theta, false); return this;}
	
	//
	public Image drawLine(int x1, int y1, int x2, int y2, Color lineColor) throws Exception{auxVec1.x = x1; auxVec1.y = y1; auxVec2.x = x2; auxVec2.y = y2; op.drawLine(auxVec1, auxVec2, lineColor); return this;}
	public Image drawLine(Vector p1, Vector p2, Color lineColor) throws Exception{op.drawLine(p1, p2, lineColor); return this;}
	public Image drawRectangle(int x, int y, int width, int height, Color color) throws Exception{op.drawRectangle(x, y, width, height, color); return this;}
	public Image drawRectangleOutline(int x, int y, int width, int height, Color color) throws Exception{op.drawRectangleOutline(x, y, width, height, color); return this;}
	/**
	 * Renders the text of the specified String, using the current text attribute state in the Graphics2D context. The baseline of the first character is at position (x, y) in the User Space.
	 * @param x
	 * @param y
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public Image drawString(int x, int y, String str) throws Exception{op.drawString(x, y, str, null, null); return this;}
	public Image drawString(int x, int y, String str, Color color, Font font) throws Exception{op.drawString(x, y, str, color, font); return this;}
	public Image blendImages(Image topImg, int posX, int posY) throws Exception{op.blendImages(topImg, posX, posY); return this;}
	public Image invert(){op.invert(); return this;}
	public Image intersect(Image imgToIntersect){op.intersect(imgToIntersect); return this;}
	public Image maskImage(Image mask){op.getMaskedImage(mask); return this;}
	public Image addBrightness(int valueToAdd){op.addBrightness(valueToAdd); return this;}
	public Image subtract(Image imgToSubtract){op.subtract(imgToSubtract); return this;}
	public Image add(Image imgToSum){op.add(imgToSum); return this;}
	public Image threshold(int thresholdLevel){op.threshold(thresholdLevel); return this;}
	public Image threshold(int lowerThresholdLevel, int upperThresholdLevel){op.threshold(lowerThresholdLevel, upperThresholdLevel); return this;}
	public Image smoothThreshold(int lowerThresholdLevel, int upperThresholdLevel){op.smoothThreshold(lowerThresholdLevel, upperThresholdLevel); return this;}
	//public Image toGray(){op.toGray(); return this;}
	public Image antiAlias(){op.antiAlias(); return this;}
	/**
	 * Dilates the image based on the structuring element passed as parameter. You can create a new structuring element or use one of the Morphology class ({@link Morphology}). 
	 * E.g., {@link Morphology.STRUCT_PRIMARY} for grey images, {@link Morphology.SIMPLE_BINARY_RECT} for binary images, etc.
	 * If the desired dilation is a binary dilation, do not forget to convert the image to binary first. Otherwise that could introduce problems related to the upper and lower limits of the image.
	 * @param structuringElement - the structuring element
	 * @param times - how many times the dilation will be performed
	 * @param contrast - whether or not to contrast the image (if not enabled, the image may very easily exceed the 255 value (max of 8bits depth images)
	 * @return - the dilated image
	 * @throws Exception
	 */
	public Image dilate(Image structuringElement, int times, boolean contrast) {for (int k=0; k<times; k++) op.dilateOrErode(structuringElement, true); if(contrast) this.contrast(); return this;}
	/**
	 * Dilates the image based on the structuring element passed as parameter. You can create a new structuring element or use one of the Morphology class ({@link Morphology}).
	 * E.g., {@link Morphology.STRUCT_PRIMARY} for grey images, {@link Morphology.SIMPLE_BINARY_RECT} for binary images, etc.
	 * If the desired dilation is a binary dilation, do not forget to convert the image to binary first. Otherwise that could introduce problems related to the upper and lower limits of the image.
	 * @param structuringElement - the structuring element
	 * @param times - how many times the dilation will be performed
	 * @return - the dilated image
	 * @throws Exception
	 */
	public Image dilate(Image structuringElement, int times){return dilate(structuringElement, times, true);}
	/**
	 * Erodes the image based on the structuring element passed as parameter. You can create a new structuring element or use one of the Morphology class ({@link Morphology}). .
	 * E.g., {@link Morphology.STRUCT_PRIMARY} for grey images, {@link Morphology.SIMPLE_BINARY_RECT} for binary images, etc.
	 * If the desired dilation is a binary dilation, do not forget to convert the image to binary first. Otherwise that could introduce problems related to the upper and lower limits of the image.
	 * @param structuringElement - the structuring element
	 * @param times - how many times the dilation will be performed
	 * @return - the dilated image
	 * @throws Exception
	 */
	public Image erode(Image structuringElement, int times) {return erode(structuringElement, times, true);}
	/**
	 * Erodes the image based on the structuring element passed as parameter. You can create a new structuring element or use one of the Morphology class ({@link Morphology}). 
	 * E.g., {@link Morphology.STRUCT_PRIMARY} for grey images, {@link Morphology.SIMPLE_BINARY_RECT} for binary images, etc.
	 * If the desired dilation is a binary dilation, do not forget to convert the image to binary first. Otherwise that could introduce problems related to the upper and lower limits of the image.
	 * @param structuringElement - the structuring element
	 * @param times - how many times the dilation will be performed
	 * @param contrast - whether or not to contrast the image (if not enabled, the image may very easily exceed the 255 value (max of 8bits depth images)
	 * @return - the dilated image
	 * @throws Exception
	 */
	public Image erode(Image structuringElement, int times, boolean contrast) {for (int k=0; k<times; k++) op.dilateOrErode(structuringElement, false); if(contrast) this.contrast(); return this;}
	
	
	
	
	public Image showImage() throws Exception{
		if (display == null) display = new ImageDisplay();
		//if (this.hasBufferedImage()) createBufferedImage();
		display.setImage(this);
		return this;
	}
	public Image showImage(ImageDisplay display) throws Exception{
		display.setImage(this);
		return this;
	}
	public Image showImage(String title) throws Exception{
		if (display == null) display = new ImageDisplay();
		display.setTitle(title);
		display.setImage(this);
		return this;
	}

}
