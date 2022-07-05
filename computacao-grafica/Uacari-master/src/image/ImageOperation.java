package image;


import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import morphology.*;


import static image.Image.InterpolationType.*;
/**
 * Some image operations. Including various Java's Graphics2D derived operations.
 * @author �rick Oliveira Rodrigues (erickr@id.uff.br)
 */
public class ImageOperation {

	private static volatile boolean firstTime = true;
	private BufferedImage outBuffImg = null;
	private Graphics2D g2d = null;
	
	private Image associatedImg = null;
	
	private Object antiAlias = RenderingHints.VALUE_ANTIALIAS_OFF, interpolation = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
	
	public ImageOperation(Image associatedImage){
		this.associatedImg = associatedImage;
		try{
			if (firstTime) {System.setProperty("sun.java2d.opengl","True"); firstTime = false;} //enabling opengl
		}catch (Exception e){};
	}
	public void setImage(Image associatedImg){this.associatedImg = associatedImg;}

	
	public void setAntiAliasing(boolean setOn){
		if (setOn) this.antiAlias = RenderingHints.VALUE_ANTIALIAS_ON;
		else this.antiAlias = RenderingHints.VALUE_ANTIALIAS_OFF;
	}
	private void setRenderingKeys(Graphics2D g2d){
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antiAlias);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, interpolation);
	}
	/**
	 * Sets the interpolation when the image is rendered using Java's Graphics2D
	 * @param interpolationType - the types can be Image.BICUBIC, Image.BILINEAR or Image.NEAREST_NEIGHBOR
	 */
	public void setInterpolation(Object interpolationType){
		this.interpolation = interpolationType;
	}
	
	/*
	public static BufferedImage cloneImage(BufferedImage img){
		Graphics2D g2d;
		BufferedImage outBuffImg = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		g2d = outBuffImg.createGraphics();
		g2d.drawImage(img, null, 0, 0);
		g2d.dispose();
		return outBuffImg;
	}
	*/
	
	public BufferedImage translate(int x, int y) throws Exception {
	    outBuffImg = new BufferedImage(associatedImg.getWidth(), associatedImg.getHeight(), associatedImg.getType());
	    g2d = outBuffImg.createGraphics();
	    setRenderingKeys(g2d);
	    g2d.translate(x, y);
	    g2d.drawImage(associatedImg.getBufferedImage(), null, 0, 0);
	    g2d.dispose();
	    associatedImg.updateImage(outBuffImg);
	    return outBuffImg;
	}
	public BufferedImage scale(double x, double y) throws Exception{
		outBuffImg = new BufferedImage((int)Math.ceil(associatedImg.getWidth()*x), (int)Math.ceil(associatedImg.getHeight()*y), associatedImg.getType());
		g2d = outBuffImg.createGraphics();
		setRenderingKeys(g2d);
		g2d.scale(x, y);
		g2d.drawImage(associatedImg.getBufferedImage(), null, 0, 0);
		g2d.dispose();
		associatedImg.updateImage(outBuffImg);
		return outBuffImg;
	}
	public BufferedImage scaleWithFixedSize(double x, double y) throws Exception{
		outBuffImg = new BufferedImage((int)(associatedImg.getWidth()), (int)(associatedImg.getHeight()), associatedImg.getType());
		g2d = outBuffImg.createGraphics();
		setRenderingKeys(g2d);
		g2d.scale(x, y);
		g2d.drawImage(associatedImg.getBufferedImage(), null, 0, 0);
		g2d.dispose();
		associatedImg.updateImage(outBuffImg);
		return outBuffImg;
	}
	public BufferedImage rotate(double theta, boolean translateToOrigin) throws Exception{
		//convert to degrees
		theta = 1.57*theta/90;
		//
		outBuffImg = new BufferedImage((int)(associatedImg.getWidth()), (int)(associatedImg.getHeight()), associatedImg.getType());
		
		g2d = outBuffImg.createGraphics();
		setRenderingKeys(g2d);
		if (translateToOrigin)
			g2d.rotate(theta, associatedImg.getWidth()/2, associatedImg.getHeight()/2);
		else
			g2d.rotate(theta);
		g2d.drawImage(associatedImg.getBufferedImage(), null, 0, 0);
		g2d.dispose();
		
		//if (translateToOrigin)
		//	outBuffImg = this.translate(associatedImg.getWidth()/2, associatedImg.getHeight()/2);
		
		associatedImg.updateImage(outBuffImg);
		return outBuffImg;
	}
	public BufferedImage transform(AffineTransform t/*, Object interpolation*/) {
		outBuffImg = new BufferedImage(associatedImg.getWidth(), associatedImg.getHeight(), associatedImg.getType());
		g2d = outBuffImg.createGraphics();
		setRenderingKeys(g2d);
		//AffineTransformOp atop = new AffineTransformOp(t, interpolation); JRE7
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		        RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g2d.transform(t);
		g2d.drawImage(associatedImg.getBufferedImage(), 0, 0, null);
		g2d.dispose();
		associatedImg.updateImage(outBuffImg);
		return outBuffImg;
	}
	
	public BufferedImage drawLine(Vector p1, Vector p2, Color c) throws Exception{
		outBuffImg = new BufferedImage(associatedImg.getWidth(), associatedImg.getHeight(), associatedImg.getType());
		g2d = outBuffImg.createGraphics();
		setRenderingKeys(g2d);
		g2d.drawImage(associatedImg.getBufferedImage(), null, 0, 0);
		g2d.setColor(c);
		g2d.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
		g2d.dispose();
		associatedImg.updateImage(outBuffImg);
		return outBuffImg;
	}
	
	public BufferedImage drawRectangle(int x, int y, int width, int height, Color c) throws Exception{
		outBuffImg = new BufferedImage(associatedImg.getWidth(), associatedImg.getHeight(), associatedImg.getType());
		g2d = outBuffImg.createGraphics();
		setRenderingKeys(g2d);
		g2d.drawImage(associatedImg.getBufferedImage(), null, 0, 0);
		g2d.setColor(c);
		g2d.fillRect(x, y, width, height);
		//g2d.drawRect(x, y, width, height);
		g2d.dispose();
		associatedImg.updateImage(outBuffImg);
		return outBuffImg;
	}
	
	public BufferedImage drawRectangleOutline(int x, int y, int width, int height, Color c) throws Exception{
		outBuffImg = new BufferedImage(associatedImg.getWidth(), associatedImg.getHeight(), associatedImg.getType());
		g2d = outBuffImg.createGraphics();
		setRenderingKeys(g2d);
		g2d.drawImage(associatedImg.getBufferedImage(), null, 0, 0);
		g2d.setColor(c);
		//g2d.fillRect(x, y, width, height);
		g2d.drawRect(x, y, width, height);
		g2d.dispose();
		associatedImg.updateImage(outBuffImg);
		return outBuffImg;
	}
	
	public BufferedImage drawString(int x, int y, String str, Color c, Font f) throws Exception{
		outBuffImg = new BufferedImage(associatedImg.getWidth(), associatedImg.getHeight(), associatedImg.getType());
		g2d = outBuffImg.createGraphics();
		setRenderingKeys(g2d);
		g2d.drawImage(associatedImg.getBufferedImage(), null, 0, 0);
		if (c != null) g2d.setColor(c);
		else g2d.setColor(Color.YELLOW);
		if (f != null) g2d.setFont(f);
		g2d.drawString(str, x, y);
		g2d.dispose();
		associatedImg.updateImage(outBuffImg);
		return outBuffImg;
	}
	
	public Image blendImages(final Image topImage, final int x, final int y){
		for (int b=0; b<associatedImg.getNumBands(); b++){
			for (int i=0; i<topImage.getHeight(); i++){
				for (int j=0; j<topImage.getWidth(); j++){
					if ((i + y) >= associatedImg.getHeight() || (j + x) >= associatedImg.getWidth()) continue;
					
					int pB = (topImage.getNumBands() <= b) ? topImage.getNumBands() - 1 : b;
						
						associatedImg.setPixel(j + x, i + y, b, topImage.getPixel(j, i, pB));
					
				}
			}
		}
		return associatedImg;
	}
	
	/*
	public BufferedImage blendImages(BufferedImage topImg, int x, int y) throws Exception{return blendImages(topImg, new Point2D.Float(x, y));}
	public BufferedImage blendImages(BufferedImage topImg, Vector topImgPosition) throws Exception{
		return blendImages(topImg, new Point2D.Float((float)topImgPosition.x, (float)topImgPosition.y));
	}
	
	public Image blendImages(BufferedImage topImg, Point2D topImgPosition) throws Exception{
		
		outBuffImg = new BufferedImage(associatedImg.getWidth(), associatedImg.getHeight(), topImg.getType());
		// paint both images, preserving the alpha channels
		
		g2d = (Graphics2D) outBuffImg.getGraphics();
		setRenderingKeys(g2d);
		g2d.drawImage(associatedImg.getBufferedImage(), 0, 0, null);
		g2d.drawImage(topImg, (int)topImgPosition.getX(), (int)topImgPosition.getY(), null);
		g2d.dispose();
		associatedImg.updateImage(outBuffImg);
	
		return outBuffImg;
	}
	
	public BufferedImage blendImages(Image topImg, Point2D topImgPosition) throws Exception{
		return blendImages(topImg.getBufferedImage(), topImgPosition);
	}
	*/
	
	public Image invert(){
		for (int b=0; b<associatedImg.getNumBands(); b++){
			double maxTone = associatedImg.getMaximalIntensity(b);
				for (int i=0; i<associatedImg.getHeight(); i++){
					for (int j=0; j<associatedImg.getWidth(); j++){
						associatedImg.setPixel(j, i, b, maxTone - associatedImg.getPixel(j, i, b));
					}
				}
		}
		return associatedImg;
		/*
		outBuffImg = new BufferedImage(associatedImg.getWidth(), associatedImg.getHeight(), associatedImg.getType());
		r = outBuffImg.getRaster();
		for (int i=0; i<associatedImg.getHeight(); i++){
			for (int j=0; j<associatedImg.getWidth(); j++){
				for (int b=0; b<r.getNumBands(); b++){
					r.setSample(j, i, b, 255 - r.getSample(j, i, b));
				}
			}
		}
		return outBuffImg;
		*/
	}
	
	public Image intersect(Image imgToIntersect){
		double value = 0;
		for (int i=0; i<associatedImg.getHeight(); i++){
			for (int j=0; j<associatedImg.getWidth(); j++){
				for (int b=0; b<associatedImg.getNumBands(); b++){
					value = (associatedImg.getPixel(j, i, b) == imgToIntersect.getPixel(j, i, b)) ? associatedImg.getPixel(j, i, b) : 0;
					associatedImg.setPixel(j, i, b, value);
				}
			}
		}
		return associatedImg;
	}
	public Image getMaskedImage(Image mask){
		for (int b=0; b<associatedImg.getNumBands(); b++){
			for (int i=0; i<associatedImg.getHeight(); i++){
				for (int j=0; j<associatedImg.getWidth(); j++){
					int band = b; if (mask.getNumBands() == 1) band = 0;
					if (mask.getPixel(j, i, band) <= 0){
						associatedImg.setPixel(j, i, b, 0);
					}
				}
			}
		}
		return associatedImg;
	}
	
	public Image addBrightness(int valueToAdd){
		for (int b=0; b<associatedImg.getNumBands(); b++){
			for (int i=0; i<associatedImg.getHeight(); i++){
				for (int j=0; j<associatedImg.getWidth(); j++){
					associatedImg.setPixel(j, i, b, associatedImg.getPixel(j, i, b) + valueToAdd);
				}
			}
		}
		return associatedImg;
	}
	
	public Image subtract(Image imgToSubtract){
			for (int b=0; b<associatedImg.getNumBands(); b++){
				for (int i=0; i<associatedImg.getHeight(); i++){
					for (int j=0; j<associatedImg.getWidth(); j++){
						associatedImg.setPixel(j, i, b, associatedImg.getPixel(j, i, b) - imgToSubtract.getPixel(j, i, b));
					}	
				}
			}
		return associatedImg;
	}
	public Image add(Image imgToSum){
		for (int b=0; b<associatedImg.getNumBands(); b++){
			for (int i=0; i<associatedImg.getHeight(); i++){
				for (int j=0; j<associatedImg.getWidth(); j++){
					associatedImg.setPixel(j, i, b, associatedImg.getPixel(j, i, b) + imgToSum.getPixel(j, i, b));
				}
			}
		}
		return associatedImg;
	}

	
	
	//threshold values
	int upperValue = 255, lowerValue = 0;
	
	public void setThresholdImageUpperValue(int value){this.upperValue = value;}
	public void setThresholdImageLowerValue(int value){this.lowerValue = value;}

	public Image threshold(int thresholdLevel){
		for (int i=0; i<associatedImg.getHeight(); i++){
			for (int j=0; j<associatedImg.getWidth(); j++){
				for (int b=0; b<associatedImg.getNumBands(); b++){
					if (associatedImg.getPixel(j, i, b) > thresholdLevel)
						associatedImg.setPixel(j, i, b, upperValue /*255*/);
					else
						associatedImg.setPixel(j, i, b, lowerValue /*0*/);
				}
			}
		}
		return associatedImg;
	}
	public Image threshold(int lowerThresholdLevel, int upperThresholdLevel){
		for (int i=0; i<associatedImg.getHeight(); i++){
			for (int j=0; j<associatedImg.getWidth(); j++){
				for (int b=0; b<associatedImg.getNumBands(); b++){
					if (associatedImg.getPixel(j, i, b) > lowerThresholdLevel && associatedImg.getPixel(j, i, b) < upperThresholdLevel)
						associatedImg.setPixel(j, i, b, upperValue /*255*/);
					else
						associatedImg.setPixel(j, i, b, lowerValue /*0*/);
				}
			}
		}
		return associatedImg;
	}
	public Image smoothThreshold(int lowerThresholdLevel, int upperThresholdLevel){
		for (int i=0; i<associatedImg.getHeight(); i++){
			for (int j=0; j<associatedImg.getWidth(); j++){
				for (int b=0; b<associatedImg.getNumBands(); b++){
					if (!(associatedImg.getPixel(j, i, b) > lowerThresholdLevel && associatedImg.getPixel(j, i, b) <= upperThresholdLevel))
						associatedImg.setPixel(j, i, b, lowerValue);
				}
			}
		}
		return associatedImg;
	}
	
	public Image toGray(){
		if (associatedImg.getNumBands() == 1 || associatedImg.isGray()) return associatedImg;
		for (int i=0; i<associatedImg.getHeight(); i++){
			for (int j=0; j<associatedImg.getWidth(); j++){
				for (int b=0; b<associatedImg.getNumBands(); b++){
					associatedImg.setPixel(j, i, b, (int)(0.2126f*associatedImg.getPixel(j, i, 0) + 0.7152f*associatedImg.getPixel(j, i, 1) + 
							0.0722f*associatedImg.getPixel(j, i, 2)));
				}
			}
		}
		return associatedImg;
	}
	
	public Image antiAlias(){
		int count = 0;
		double outValue = 0;
		//"bicubic"
		for (int b=0; b<associatedImg.getNumBands(); b++){
			for (int i=1; i<associatedImg.getHeight()-1; i++){
				for (int j=1; j<associatedImg.getWidth()-1; j++){
					count = 1;
					outValue = associatedImg.getPixel(j, i, b);
					if (associatedImg.getPixel(j - 1, i, b) == associatedImg.getPixel(j, i + 1, b)){
						outValue += associatedImg.getPixel(j - 1, i, b);
						count ++;
					}
					if (associatedImg.getPixel(j, i + 1, b) == associatedImg.getPixel(j + 1, i, b)){
						outValue += associatedImg.getPixel(j, i + 1, b);
						count ++;
					}
					if (associatedImg.getPixel(j + 1, i, b) == associatedImg.getPixel(j, i - 1, b) ){
						outValue += associatedImg.getPixel(j + 1, i, b);
						count ++;
					}
					if (associatedImg.getPixel(j, i - 1, b) == associatedImg.getPixel(j - 1, i, b)){
						outValue += associatedImg.getPixel(j, i - 1, b);
						count ++;
					}
					if (count > 1){
						associatedImg.setPixel(j, i, b, outValue/count);
					}
				}
			}
		}
		
		return associatedImg;
	}

	public Image dilateOrErode(Image structElement, boolean dilation){
		Image aux = new Image(associatedImg);
		
		double value = 0;
		for (int i=0; i<associatedImg.getHeight(); i++){
			for (int j=0; j<associatedImg.getWidth(); j++){
				for (int b=0; b<associatedImg.getNumBands(); b++){
					value = Morphology.getDilateErodeCentralValue(aux, structElement, j, i, b, dilation);
					associatedImg.setPixel(j, i, b, value);
				}
			}
		}
		
		aux.dispose();
		aux = null;
		return associatedImg;
	}
}
