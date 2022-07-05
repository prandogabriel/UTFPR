package image;

class PixelMap {

	private PixelData[] layers = null;
	private int bitDepth = 0;
	private int width, height;
	private boolean floatValues = false;
	
	PixelMap(int width, int height, int numBands, int bitDepth, boolean floatValues){
		this.bitDepth = bitDepth;
		this.width = width; this.height = height;
		layers = new PixelData[numBands];
		this.floatValues = floatValues;
		if (floatValues){
			if (bitDepth <= 32){
				for (int b=0; b<numBands; b++)
					layers[b] = new FloatPixelData(width, height);
			}
			else if (bitDepth <= 64){
				for (int b=0; b<numBands; b++)
					layers[b] = new DoublePixelData(width, height);
			}
		}else{
			if (bitDepth == 1){
				for (int b=0; b<numBands; b++)
					layers[b] = new BooleanPixelData(width, height);
			}
			else if (bitDepth <= 8){
				for (int b=0; b<numBands; b++)
					layers[b] = new BytePixelData(width, height);
			}
			else if (bitDepth <= 16){
				for (int b=0; b<numBands; b++)
					layers[b] = new ShortPixelData(width, height);
			}
			else if (bitDepth <= 32){
				for (int b=0; b<numBands; b++)
					layers[b] = new IntegerPixelData(width, height);
			}
			else if (bitDepth > 32){
				for (int b=0; b<numBands; b++)
					layers[b] = new DoublePixelData(width, height);
			}
		}

	}
	
	//get
	public double get(int x, int y, int band){
		return layers[band].get(x, y);
	}
	public double get(int x, int y){return layers[0].get(x, y);}
	public PixelData getPixelData(int band){return layers[band];}
	public int[][] getIntegerPixelData(int band) throws Exception{if (this.getBitDepth() != 32) throw new Exception("The bit depth of the image is not 32."); return ((IntegerPixelData)layers[band]).pixelData;}
	public int getNumBands(){return this.layers.length;}
	public int getBitDepth(){return this.bitDepth;}
	public int getWidth(){return width;}
	public int getHeight(){return height;}
	

	
	
	//set
	public void set(int x, int y, int band, double value){
		layers[band].set(x, y, value);
	}
	public void set(int x, int y, double value){layers[0].set(x, y, value);}
	public void set(int x, int y, boolean value){((BooleanPixelData)layers[0]).set(x, y, value);}
	
	
	//set
	public void setPixelData(int[][] pData, int band){
		((IntegerPixelData)layers[band]).pixelData = pData;
	}
	public void setPixelData(short[][] pData, int band){
		((ShortPixelData)layers[band]).pixelData = pData;
	}
	public void setPixelData(byte[][] pData, int band){
		((BytePixelData)layers[band]).pixelData = pData;
	}
	public void setPixelData(boolean[][] pData, int band){
		((BooleanPixelData)layers[band]).pixelData = pData;
	}
	public void setPixelData(float[][] pData, int band){
		((FloatPixelData)layers[band]).pixelData = pData;
	}
	public void setPixelData(double[][] pData, int band){
		((DoublePixelData)layers[band]).pixelData = pData;
	}
	public void setPixelData(PixelData pData, int band, int bitDepth){
		if (floatValues){
			if (bitDepth <= 32)
				setPixelData(((FloatPixelData)pData).pixelData, band);
			else if (bitDepth <= 64)
				setPixelData(((DoublePixelData)pData).pixelData, band);
		}else{
			if (bitDepth == 1)
				setPixelData(((BooleanPixelData)pData).pixelData, band);
			else if (bitDepth <= 8)
				setPixelData(((BytePixelData)pData).pixelData, band);
			else if (bitDepth <= 16)
				setPixelData(((ShortPixelData)pData).pixelData, band);
			else if (bitDepth <= 32)
				setPixelData(((IntegerPixelData)pData).pixelData, band);
		}
	}
	
	public boolean containsFloatValues(){return floatValues;}
	
	
	abstract class PixelData{
		public abstract double get(int x, int y);
		public abstract void set(int x, int y, double value);
		public abstract PixelData clone();
	}
	private class BooleanPixelData extends PixelData{
		boolean[][] pixelData;
		
		BooleanPixelData(int width, int height){pixelData = new boolean[height][width];}
		
		@Override
		public double get(int x, int y) {
			return ((pixelData[y][x]) ? 255 : 0);
		}
		public boolean getBoolean(int x, int y){
			return pixelData[y][x];
		}

		@Override
		public void set(int x, int y, double value) {
			pixelData[y][x] = (value != 0);
		}
		public void set(int x, int y, boolean value) {
			pixelData[y][x] = value;
		}
		
		@Override
		public PixelData clone() {
			BooleanPixelData n = new BooleanPixelData(this.pixelData[0].length, this.pixelData.length);
			for (int i=0; i<pixelData.length; i++)
				for (int j=0; j<pixelData[0].length; j++)
					n.set(j, i, this.get(j, i));					
			return n;
		}
		
	}
	/**
	 * Byte images cannot receive negative values!!! Just short or higher ones are able to do this.
	 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
	 */
	private class BytePixelData extends PixelData{
		byte[][] pixelData;
		final static byte SHIFT = Byte.MIN_VALUE;
		
		BytePixelData(int width, int height){pixelData = new byte[height][width]; for (int i=0; i<pixelData.length; i++) for (int j=0; j<pixelData[0].length; j++) pixelData[i][j] = SHIFT;}
		
		@Override
		public double get(int x, int y) {
			return pixelData[y][x] - SHIFT;
		}

		@Override
		public void set(int x, int y, double value) {
			pixelData[y][x] = (byte) (value + SHIFT);
		}
		
		@Override
		public PixelData clone() {
			BytePixelData n = new BytePixelData(this.pixelData[0].length, this.pixelData.length);
			for (int i=0; i<pixelData.length; i++)
				for (int j=0; j<pixelData[0].length; j++)
					n.set(j, i, this.get(j, i));					
			return n;
		}

	}
	private class ShortPixelData extends PixelData{
		short[][] pixelData;
		//final static short SHIFT = 128;
		
		ShortPixelData(int width, int height){pixelData = new short[height][width]; }
		
		@Override
		public double get(int x, int y) {
			return pixelData[y][x];
		}

		@Override
		public void set(int x, int y, double value) {
			pixelData[y][x] = (short) (value);
		}
		
		@Override
		public PixelData clone() {
			ShortPixelData n = new ShortPixelData(this.pixelData[0].length, this.pixelData.length);
			for (int i=0; i<pixelData.length; i++)
				for (int j=0; j<pixelData[0].length; j++)
					n.set(j, i, this.get(j, i));					
			return n;
		}

	}
	private class IntegerPixelData extends PixelData{
		int[][] pixelData;
		
		IntegerPixelData(int width, int height){pixelData = new int[height][width]; }
		
		@Override
		public double get(int x, int y) {
			return pixelData[y][x];
		}

		@Override
		public void set(int x, int y, double value) {
			pixelData[y][x] = (int) value;
		}
		
		@Override
		public PixelData clone() {
			IntegerPixelData n = new IntegerPixelData(this.pixelData[0].length, this.pixelData.length);
			for (int i=0; i<pixelData.length; i++)
				for (int j=0; j<pixelData[0].length; j++)
					n.set(j, i, this.get(j, i));					
			return n;
		}
	}
	private class FloatPixelData extends PixelData{
		float[][] pixelData;
		
		FloatPixelData(int width, int height){pixelData = new float[height][width];}
		
		@Override
		public double get(int x, int y) {
			return pixelData[y][x];
		}

		@Override
		public void set(int x, int y, double value) {
			pixelData[y][x] = (float) value;
		}
		
		@Override
		public PixelData clone() {
			FloatPixelData n = new FloatPixelData(this.pixelData[0].length, this.pixelData.length);
			for (int i=0; i<pixelData.length; i++)
				for (int j=0; j<pixelData[0].length; j++)
					n.set(j, i, this.get(j, i));					
			return n;
		}
		
	}
	private class DoublePixelData extends PixelData{
		double[][] pixelData;
		
		DoublePixelData(int width, int height){pixelData = new double[height][width];}
		
		@Override
		public double get(int x, int y) {
			return pixelData[y][x];
		}

		@Override
		public void set(int x, int y, double value) {
			pixelData[y][x] = value;
		}
		
		@Override
		public PixelData clone() {
			DoublePixelData n = new DoublePixelData(this.pixelData[0].length, this.pixelData.length);
			for (int i=0; i<pixelData.length; i++)
				for (int j=0; j<pixelData[0].length; j++)
					n.set(j, i, this.get(j, i));					
			return n;
		}
		
	}
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
		
	
}
