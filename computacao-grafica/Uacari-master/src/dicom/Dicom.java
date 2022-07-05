package dicom;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.dcm4che2.tool.dcm2xml.Dcm2Xml;

import image.Image;
import image.ImageOperation;
import image.Vector;

/**
 * Accesses DICOM data that is converted to a XML-file, respecting the
 *  data format distributed by dcm4che library website as the binary 'dcm2xml'
 *  This class requires further optimization, however, it works. One possible optimization is to stop working with text files and to work directly with
 *  the binary DICOM files. We generate a XML file from the DICOM using a third party library and read the data from the generated file (slow).
 *  
 *  @author �rick Oliveira Rodrigues (erickr@id.uff.br).
 */
public class Dicom {
	private final static int bufferedImageType = BufferedImage.TYPE_BYTE_GRAY;
	private String xmlPath = null;
	private HashMap<String, String> tag;
	private PixelDataMap pixelRawData = null; //first: x-axis, second: y-axis
	private ImageOperation op = null;

	public void dispose() throws IOException{
		if (br != null) br.close();
		xmlPath = null;
		tag.clear();
		tag = null;
		pixelRawData = null;
	}
	
	/**
	 * Instantiates a new DicomXML object if you have already generated a xmlFile.
	 * Otherwise, use the dicom constructor: DicomXML(String dicomFilePath, String outputXmlPath).
	 * @param xmlFilePath
	 */
	public Dicom(String dicomFilePath) {
		this.xmlPath = dicomFilePath;
		init(dicomFilePath, dicomFilePath + ".xml");
	}
	
	/**
	 * Reads an DICOM file (.dcm, .isa) and writes it to the outputXmlPath as a xml file (text readable).
	 * This generated XML file is used by this class to extract information.
	 * @param dicomFilePath
	 * @param outputXmlPath
	 */
	public Dicom(String dicomFilePath, String outputXmlPath){
		init(dicomFilePath, outputXmlPath);
	}

	private void init(String dicomFilePath, String outputXmlPath){
		String args[] = new String[3];
		args[0] = dicomFilePath;
		//File dicomFile = new File(dicomFilePath);
		args[1] = "-o"; //writes output file
		File newdir = new File(outputXmlPath).getParentFile();
		if (!newdir.exists()) newdir.mkdir();
		args[2] = outputXmlPath;
		Dcm2Xml.main(args);
		newdir = null;

		//then instantiates DicomXML
		this.xmlPath = outputXmlPath;
	}

	private static final String TAG_FINAL_FORMAT = "</attr>";
	private static final byte MAX_LINES_FOR_VALUE = 20;
	private static final byte TAG_INITIAL_POS = 11, TAG_FINAL_POS = 19; //on the archieve
	private BufferedReader br = null;
	private String currentline, currenttag, tagcontent = null;
	/*
	/**
	 * Receives a tag (XXXX,XXXX) both numbers in hexadecimal format and returns the file tag
	 * @param id1 - First id in hexadecimal format
	 * @param id2 - Second id in hexadecimal format
	 * @return - String file tag
	 * @throws Exception - error reading file
	 *-/
	public String getTagValue(String id1, String id2) throws Exception{
		return getTagValue(id1+id2);
		
	}
	*/
	public String getTagValue(int tag) throws Exception{
		String tagS = Integer.toString(tag);
		while (tagS.length() < 8) tagS = 0 + tagS;
		return this.getTagValue(tagS);
	}
	public String getTagValue(int groupId, int id) throws Exception{
		String tagS = Integer.toString(id);
		while (tagS.length() < 4) tagS = 0 + tagS;
		tagS = Integer.toString(groupId) + tagS;
		while (tagS.length() < 8) tagS = 0 + tagS;
		return this.getTagValue(tagS);
	}
	private FileInputStream fs = null;
	/**
	 * Receives a tag (XXXX,YYYY) both numbers in hexadecimal format and returns the file tag, 
	 * yet collapsed in one single parameter (XXXXYYYY)
	 * @throws Exception - error reading file
	 */
	public String getTagValue(String tagid) throws Exception{
		if (br == null){
			fs = new FileInputStream(xmlPath);
			br = new BufferedReader(new InputStreamReader(fs));
		}else{
			fs.getChannel().position(0);
			br = new BufferedReader(new InputStreamReader(fs));
		}
		
		boolean found = false;
		
		//halt condition (previously stored on memory)
		if (tag != null) {
			if (tag.containsKey(tagid)) return tag.get(tagid);
		}else{
			tag = new HashMap<String, String>();
		}
		
		while (((currentline = br.readLine()) != null) && !found) {
			//verify the tag indexes on each file of the archive, respecting the xml format <attr tag="F2150010" ...
			
			if (currentline.length() > TAG_FINAL_POS){
				currenttag = currentline.substring(TAG_INITIAL_POS, TAG_FINAL_POS);
				if (currenttag.equals(tagid)){
					short cont = 0;
					while (!currentline.endsWith(TAG_FINAL_FORMAT)) {
						currentline = currentline.concat(br.readLine());
						cont ++;
						if (cont > MAX_LINES_FOR_VALUE) throw new Exception("Tag format doesn't match " + TAG_FINAL_FORMAT);
					}
					tagcontent = currentline.split(">", 2)[1];
					tagcontent = tagcontent.substring(0, tagcontent.length() - TAG_FINAL_FORMAT.length());
					found = true;
					tag.put(tagid, tagcontent);
				}
			}
		}
		//br.close();
		return tagcontent;
	}
	
	private short height = -1, width = - 1;
	public short getHeight() throws NumberFormatException, Exception{
		if (height == -1)
			height = Short.parseShort(this.getTagValue("00280010"));
		return height;
	}
	public short getWidth() throws NumberFormatException, Exception{
		if (width == -1)
			width = Short.parseShort(this.getTagValue("00280011"));
		return width;
	}
	private byte bitDepth = -1;
	public byte getAllocatedPixelBitDepth() throws NumberFormatException, Exception{
		if (bitDepth == -1)
			bitDepth = Byte.parseByte(this.getTagValue("00280100"));
		return bitDepth;
	}
	public byte getStoredPixelBitDepth() throws NumberFormatException, Exception{
		if (bitDepth == -1)
			bitDepth = Byte.parseByte(this.getTagValue("00280101"));
		return bitDepth;
	}

	/**
	 * Returns the raw value of a given pixel at coordinate (x, y). 
	 * (0,0) is the upper left pixel, standard orientation.
	 * @param x - x coordinate of the pixel
	 * @param y - y coordinate of the pixel
	 * @return - raw data value of a pixel at (x, y)
	 * @throws Exception
	 */
	public long getPixelRawData(int x, int y) throws Exception{
		if (this.pixelRawData != null){
			return pixelRawData.getValue(x, y);
		}else{
			this.pixelRawData = new PixelDataMap(this.getStoredPixelBitDepth(), this.getWidth(), this.getHeight());
		}
		
		
		//split at the '\' separator
		//String[] splittedrawdata = this.getTagValue("7FE00010").split("\\\\");
		String data = this.getTagValue("7FE00010");
		String conc = "";
		int counter = 0;
		for (int k=0; k<data.length(); k++){
			if (data.charAt(k) == '\\'){
				this.pixelRawData.putValue(counter % this.getWidth(), (int) counter / this.getWidth(), Long.parseLong(conc));	
				conc = ""; counter ++; continue;
			}
			conc += data.charAt(k);
		}

		/*
		for (int i=0; i<splittedrawdata.length; i++){
			this.pixelrawdata.putValue(i % this.getWidth(), (int) i / this.getWidth(), Long.parseLong(splittedrawdata[i]));
		}
		splittedrawdata = null;
		*/
		data = null;
		//System.gc();
		
		return this.pixelRawData.getValue(x, y);
	}
	
	
	/**
	 * Returns the Hounsfield value (CT) or the RM equivalent of a pixel for some cases (not supporting every 'encoding')
	 * @param x - pixel's coordinate x
	 * @param y - pixel's coordinate y
	 * @return - Housnfield value
	 * @throws Exception
	 */
	public double getPixelValue(int x, int y) throws Exception{
		//Modality LUT tag found, not converting
		if (this.hasModalityLUT()){
			throw new Exception("Modality LUT tag found on the file, conversion not yet supported.");
		}
		
		double decodedvalue;
		
		//for signed values
		if (!this.isUnsigned()){
			
			//complement of 2
			//STRING APPROACH 
			String value, value2;
			byte signal = 1;
			value = Long.toBinaryString(this.getPixelRawData(x, y));
			//for hounsfield: 16 bits *practically always
			byte bitDepth = this.getStoredPixelBitDepth();
			while (value.length() < bitDepth) 
				value = "0" + value;
			if (value.charAt(0) == '1'){//if its a negative number
				signal = -1;
				value = Long.toBinaryString((this.getPixelRawData(x, y) - 1));
				value2 = Long.toBinaryString(-(this.getPixelRawData(x, y) - 1) - 1);
				value2 = value2.substring(value2.length() - value.length());
				value = value2;
				//value = value.replaceAll("0", "X");
				//value = value.replaceAll("1", "0");
				//value = value.replaceAll("X", "1");
			} 
			decodedvalue = (double) Long.parseLong(value, 2) * signal;
			
			
			/*
			//complement of 2
			//JAVA 7 - N�O T� FUNCIONANDO PERFEITAMENTE
			byte signal = 1;
			long value;
			bs = BitSet.valueOf(new long[] {this.getPixelRawData(x, y)});
			if (bs.get(0)){
				signal = -1;
				value = ~(this.getPixelRawData(x, y) - 1);
			}
			decodedvalue = value * signal;
			*/
			
		//for unsigned values
		}else{
			decodedvalue = this.getPixelRawData(x, y);
		}
		
		
		//Rescale (slope and intercept)
		final float slope = this.getRescaleSlope(),
				intercept = this.getRescaleIntercept();
		//hounsfield = raw data pixel * slope + intercept
		return (decodedvalue * slope) + intercept;
	}
	
	
	/**
	 * Better and more accurate to use the function getDistanceBetweenSlices(slice being compared), instead.
	 * Some dicom files do not contain the slice thickness attribute, or may contain an inaccurate value.
	 * @return
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	public float getSliceThickness() throws NumberFormatException, Exception{
		return Float.parseFloat(this.getTagValue(180050));
	}
	
	private Vector pixelSpacing = null;
	public Vector getPixelSpacing() throws Exception{
		if (pixelSpacing == null) {
			String tag = this.getTagValue("00280030");
			pixelSpacing = new Vector(Float.parseFloat(tag.split("\\\\", 2)[0]), Float.parseFloat(tag.split("\\\\", 2)[1]));
			firstslope = false;
		}
		return pixelSpacing;
	}
	
	boolean firstslope = true; float slope = 0;
	public float getRescaleSlope() throws NumberFormatException, Exception{
		if (firstslope) {
			firstslope = false;
			String slopeStr = this.getTagValue("00281053");
			//if (slopeStr == null) throw new Exception("The dicom file contains some error on the slope tag. The tag may not be present (0028,1053).");
			if (slopeStr == null) slopeStr = "1";
			slope = Float.parseFloat(slopeStr);
		}
		return slope;
	}
	boolean firstintercept = true; float intercept = 0;
	public float getRescaleIntercept() throws NumberFormatException, Exception{
		if (firstintercept) {
			String interceptStr = this.getTagValue("00281052");
			if (interceptStr == null) interceptStr = "0";
			intercept = Float.parseFloat(interceptStr);
			firstintercept = false;
		}
		return intercept;
	}
	
	private Vector voxelCoord = null;
	/**
	 * The coordinates in three dimensions of the voxel (parameters: x,y) in the frame�s image plane in units of mm (millimeters). 
	 * @param x
	 * @param y
	 * @return - A vector containing position x, y and z
	 * @throws Exception 
	 */
	public Vector getVoxelCoordinates(int x, int y) throws Exception{
		if (voxelCoord != null) return voxelCoord;
		
		double Xx = this.getImageOrientationX().x, Xy = this.getImageOrientationX().y, Xz = this.getImageOrientationX().z,
			Yx = this.getImageOrientationY().x, Yy = this.getImageOrientationY().y, Yz = this.getImageOrientationY().z,	
			Sx = this.getImagePosition().x, Sy = this.getImagePosition().y, Sz = this.getImagePosition().z,
			dY = this.getPixelSpacing().y, dX = this.getPixelSpacing().x;
		
		voxelCoord = new Vector(0, 0, 0);
		voxelCoord.x = Xx*dX*x + Yx*dY*y + Sx;
		voxelCoord.y = Xy*dX*x + Yy*dY*y + Sy;
		voxelCoord.z = Xz*dX*x + Yz*dY*y + Sz;
		
		return voxelCoord;
	}
	
	/**
	 * Returns the distance between this slice and the one passed as parameter in mm (millimeters).
	 * To know more check the section C.7.6.2 of the Dicom Standard: http://dicom.nema.org/Dicom/2011/11_03pu.pdf
	 * @param dcm2 - slice to compare the distance from
	 * @return - the distance between this and dcm2
	 * @throws Exception
	 */
	public double getDistanceBetweenSlices(Dicom dcm2) throws Exception{
		return Math.abs(this.getVoxelCoordinates(0, 0).z - dcm2.getVoxelCoordinates(0, 0).z);
	}
	
	private Vector imgPos = null;
	public Vector getImagePosition() throws Exception{
		if (imgPos != null) return imgPos;
		String pos[] = this.getTagValue(20, 32).split("\\\\");
		imgPos = new Vector(Float.parseFloat(pos[0]), Float.parseFloat(pos[1]), Float.parseFloat(pos[2]));
		return imgPos;
	}
	private Vector imgOrientationX = null;
	public Vector getImageOrientationX() throws Exception{
		if (imgOrientationX != null) return imgOrientationX;
		String orient[] = this.getTagValue(20, 37).split("\\\\");
		imgOrientationX = new Vector(Float.parseFloat(orient[0]), Float.parseFloat(orient[1]), Float.parseFloat(orient[2]));
		return imgOrientationX;
	}
	private Vector imgOrientationY = null;
	public Vector getImageOrientationY() throws Exception{
		if (imgOrientationY != null) return imgOrientationY;
		String orient[] = this.getTagValue(20, 37).split("\\\\");
		imgOrientationY = new Vector(Float.parseFloat(orient[3]), Float.parseFloat(orient[4]), Float.parseFloat(orient[5]));
		return imgOrientationY;
	}
	/*
	/**
	 * Retrieves an image based from the housnfield data present on the DICOM file.
	 * The transformation is linear.
	 * E.g.: Assuming we want to retrieve the hounsfield values from 0 to 10 and to cover an entire output 8-bit depth image (from 0 to 255),
	 * we would call this method like: "getHounsfieldImage(0, 10, 0, 255)".
	 * The value 0 of the hounsfield scale will be 0 on the image and the value 10 will be expanded to be equal to 255.
	 * 
	 * If we want to do a shift of values then the difference between the two limits must be equal.
	 * @param hounsfieldInfLimit - The inferior limit on the hounsfield scale (inclusive)
	 * @param hounsfieldSupLimit - The superior limit on the hounsfield scale (inclusive)
	 * @param infLimit - The inferior limit of the retrieved image (inclusive)
	 * @param supLimit - The superior limit of the retrieved image (inclusive)
	 * @return
	 * @throws Exception 
	 *-/
	public BufferedImage getHounsfieldImage(int hounsfieldInfLimit, int hounsfieldSupLimit, int infLimit, int supLimit, int bufferedImageType) throws Exception{
		double hvalue = 0;
		final double a_f = (supLimit - infLimit)/(hounsfieldSupLimit - hounsfieldInfLimit), b_f = supLimit - hounsfieldSupLimit*a_f;
		BufferedImage bi = new BufferedImage(this.getHeight(), this.getWidth(), bufferedImageType);
		WritableRaster r = bi.getRaster();
		int result = 0;
		for (int i=0; i<this.getHeight(); i++){
			for (int j=0; j<this.getWidth(); j++){
				hvalue = this.getHounsfieldValue(j, i);
				if (hvalue >= hounsfieldInfLimit && hvalue <= hounsfieldSupLimit){
					result = (int)((a_f*hvalue) + b_f);
					r.setSample(j, i, 0, result);
				}
			}
		}
		

		return bi;
	}
	*/
	
	
	/**
	 * Retrieves an image based from the Hounsfield data or RM equivalent present in the DICOM file.
	 * E.g.: Assuming we want to retrieve the hounsfield values from 0 to 10 and to cover an entire output 8-bit depth image (from 0 to 255),
	 * we would call this method like: "getHounsfieldImage(0, 10, 0, 255)".
	 * The value 0 of the hounsfield scale will be 0 on the image and the value 10 will be expanded to be equal to 255.
	 * 
	 * If we want to do a shift of values then the difference between the two limits must be equal.
	 * @param hInfLimit - The inferior limit on the hounsfield scale (inclusive)
	 * @param hSupLimit - The superior limit on the hounsfield scale (inclusive)
	 * @param infLimit - The inferior limit of the retrieved image (inclusive)
	 * @param supLimit - The superior limit of the retrieved image (inclusive)
	 * @return
	 * @throws Exception 
	 */
	public Image getImage(float hInfLimit, float hSupLimit, float infLimit, float supLimit, boolean superiorToWhite) throws Exception{
		return new Image(this.getBufferedImage(hInfLimit, hSupLimit, infLimit, supLimit, superiorToWhite));
	}
	
	/**
	 * Retrieves an image based from the Hounsfield data or RM equivalent present in the DICOM file.
	 * E.g.: Assuming we want to retrieve the hounsfield values from 0 to 10 and to cover an entire output 8-bit depth image (from 0 to 255),
	 * we would call this method like: "getHounsfieldImage(0, 10, 0, 255)".
	 * The value 0 of the hounsfield scale will be 0 on the image and the value 10 will be expanded to be equal to 255.
	 * 
	 * If we want to do a shift of values then the difference between the two limits must be equal.
	 * @param hInfLimit - The inferior limit on the hounsfield scale (inclusive)
	 * @param hSupLimit - The superior limit on the hounsfield scale (inclusive)
	 * @param infLimit - The inferior limit of the retrieved image (inclusive)
	 * @param supLimit - The superior limit of the retrieved image (inclusive)
	 * @return
	 * @throws Exception 
	 */
	private BufferedImage getBufferedImage(float hInfLimit, float hSupLimit, float infLimit, float supLimit, boolean superiorToWhite) throws Exception{
		double hvalue = 0;
		final double a_f = (double)(supLimit - infLimit)/(hSupLimit - hInfLimit), 
						b_f = supLimit - hSupLimit*a_f;

		BufferedImage bi = new BufferedImage(this.getHeight(), this.getWidth(), bufferedImageType);
		WritableRaster r = bi.getRaster();
		double result = 0;
		for (int i=0; i<this.getHeight(); i++){
			for (int j=0; j<this.getWidth(); j++){
				hvalue = this.getPixelValue(j, i);
				if (hvalue >= hInfLimit && hvalue <= hSupLimit){
					result = a_f*hvalue + b_f;
					r.setSample(j, i, 0, result);
				}else if (hvalue > hSupLimit && superiorToWhite)
					r.setSample(j, i, 0, supLimit);

			}
		}
		

		return bi;
	}
	
	
	/*
	public AffineTransform getResizedFatImageTransformation(Vector atlasStandardPosition, BufferedImage atlasImg) throws NumberFormatException, Exception{
		AffineTransform at = new AffineTransform();
		
		float cX = 1, cY = 1;
		//check to see if there's a need to change the image to match the desired pixelSpacing
		if (this.getDesiredPixelSpacing() != null){
			double dX = Math.abs(this.getDesiredPixelSpacing().x - this.getPixelSpacing().x),
					dY = Math.abs(this.getDesiredPixelSpacing().y - this.getPixelSpacing().y);
			if (dX > 0.01f || dY > 0.01f){// only if the spacing is significant
				cX = (float) (1f/(getDesiredPixelSpacing().x/this.getPixelSpacing().x));
				cY = (float) (1f/(getDesiredPixelSpacing().y/this.getPixelSpacing().y));
				
				int rXOffset = (int) ((this.getWidth() - this.getWidth()*cX)/4), 
						rYOffset = (int) ((this.getHeight() - this.getHeight()*cY)/4);
				
				at.scale(cX, cY);
				at.translate(rXOffset, rYOffset);
			}
		}
		
		return at;
	}
	public BufferedImage getTransformedFatImage(AffineTransform at) throws Exception{
		if (op == null) op = new ImageOperation(new Image(this.getFatBufferedImage()));
		op.setInterpolation(transfInterpolation);
		 return op.transform(at);
	}
	*/
	
	
	
	private BufferedImage fatImg = null;
	public BufferedImage getFatBufferedImage() throws Exception{
		//range interno de gordura
		if (fatImg == null) 
			fatImg = this.getBufferedImage(-200, -30, 0, 255, false); 
		return fatImg;
	}
	
	public Image getTransformedImageOnRange(AffineTransform at, float minRange, float maxRange) throws Exception{
		return new Image(this.getTransformedBufferedImageOnRange(at, minRange, maxRange));
	}
	private final Object transfInterpolation = ((Image.InterpolationType)Image.InterpolationType.BICUBIC).getType();
	private BufferedImage getTransformedBufferedImageOnRange(AffineTransform at, float minRange, float maxRange) throws Exception {
		if (op == null) op = new ImageOperation(new Image(this.getBufferedImageOnRange(minRange, maxRange)));
		op.setInterpolation(transfInterpolation);
		return op.transform(at);
	}
	public Image getImageOnRange(float minRange, float maxRange) throws Exception{
		return new Image(this.getBufferedImageOnRange(minRange, maxRange));
	}
	private BufferedImage getBufferedImageOnRange(float minRange, float maxRange) throws Exception{
		return this.getBufferedImage(minRange, maxRange, 0, 255, true); //0 ~~ 255 -> 8-bit depth image
	}
	
	/**
	 * Returns an image rescaled to the appropriate pixelSpacing, as desired (in millimiters).
	 * @param pixelSpacingX - the desired pixel spacing on the x direction
	 * @param pixelSpacingY - the desired pixel spacing on the y direction
	 * @param minRange - the minimal that should be regarded on the dicom image
	 * @param maxRange - the maximal range that should be regarded on the dicom image
	 * @return
	 * @throws Exception
	 */
	public Image getRescaledImage(float pixelSpacingX, float pixelSpacingY, float minRange, float maxRange) throws Exception{
		double ratioX = this.getPixelSpacing().x/(float)pixelSpacingX,
				ratioY = this.getPixelSpacing().y/(float)pixelSpacingY;
		Image out = new Image(this.getBufferedImageOnRange(minRange, maxRange));
		out.scale(ratioX, ratioY);
		out.translate((int)((-out.getWidth()*ratioX + out.getWidth())/2), (int)((-out.getHeight()*ratioY + out.getHeight())/2));
		return out;
	}

	/*
	private Vector getDesiredPixelSpacing(){return this.desiredPixelSpacing;}
	private Vector desiredPixelSpacing = null;
	
	 * Sets the desired real spacing in 'mm' between two consecutives pixels.
	 * If null is passed as parameter then the pixelspacing on the DICOM file will be regarded.
	 * @param spacingX - Pixel spacing in mm on the X-axis
	 * @param spacingY - Pixel spacing in mm on the Y-axis
	 
	public void setPixelSpacing(float spacingX, float spacingY){
		desiredPixelSpacing = new Vector(spacingX, spacingY);
	}
	*/
	
	
	
	//has is
	private boolean unsigned = false;
	private final boolean firsttimeunsigned = true;
	public boolean isUnsigned() throws Exception{
		if (firsttimeunsigned){
			unsigned = this.getTagValue("00280103").charAt(0) == '0';
			if (this.getTagValue("00280103").charAt(0) != '0' && this.getTagValue("00280103").charAt(0) != '1')
				throw new Exception("Pixel format tag not present on file or it has an invalid value.");
		}
		return unsigned;
	}
	private boolean modalityLut, firstTimeLut;
	public boolean hasModalityLUT() throws Exception{
		if (firstTimeLut){
			modalityLut = (this.getTagValue("00283000") != null);
		}
		return modalityLut;
	}
}
