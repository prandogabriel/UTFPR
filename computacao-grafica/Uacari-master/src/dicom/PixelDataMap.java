package dicom;

/**

 * 
 * Part of the {@link Dicom} class
 * 
 *  @author Érick Oliveira Rodrigues (erickr@id.uff.br).
 */
public class PixelDataMap {
	private PixelData rawpixeldata;
	
	/**
	 * Creates a PixelDataMap according to the depth (bitlength) of the values
	 * @param bitdepth
	 * @param width
	 * @param height
	 */
	public PixelDataMap(byte bitdepth, int width, int height) {
		if (bitdepth <= 8)
			rawpixeldata = new ShortPixelMap(width, height);
		else if (bitdepth <= 16)
			rawpixeldata = new IntegerPixelMap(width, height);
		else
			rawpixeldata = new LongPixelMap(width, height);
	}
	

	public long getValue(int x, int y){
		return rawpixeldata.get(x, y);
	}
	public void putValue(int x, int y, long value){
		rawpixeldata.put(x, y, value);
	}
	
	class PixelData {
		public long get(int x, int y){return 0;}
		public void put(int x, int y, long value){}
	}
	class LongPixelMap extends PixelData{
		long rawpixeldata[][];
		LongPixelMap(int width, int height){
			rawpixeldata = new long[width][height];
		}
		public long get(int x, int y){return rawpixeldata[x][y];}
		public void put(int x, int y, long value){rawpixeldata[x][y] = value;}
	}
	class IntegerPixelMap extends PixelData{
		int rawpixeldata[][];
		IntegerPixelMap(int width, int height){
			rawpixeldata = new int[width][height];
		}
		public long get(int x, int y){return rawpixeldata[x][y];}
		public void put(int x, int y, long value){rawpixeldata[x][y] = (int) value;}
	}
	class ShortPixelMap extends PixelData{
		short rawpixeldata[][];
		ShortPixelMap(int width, int height){
			rawpixeldata = new short[width][height];
		}
		public long get(int x, int y){return rawpixeldata[x][y];}
		public void put(int x, int y, long value){rawpixeldata[x][y] = (short) value;}
	}
	
}
