package morphology;

import image.*;

/**
 * General class for morphology, including dilations, erosions, reconstructions, etc.
 * @author �rick Oliveira Rodrigues (erickr@id.uff.br)
 */
public class Morphology implements MorphologyConstants{
	
	private ImageOperation op = null;
	private MorphologicalReconstruction mr = null;
	
	
	public Morphology(Image img){
		this.op = new ImageOperation(img);
	}
	public Morphology(){}
	
	/**
	 * Returns a new dilated image.
	 * @param img
	 * @param structuringElement
	 * @param timesToDilate
	 * @return
	 * @throws Exception
	 */
	public Image dilate(Image img, Image structuringElement, int timesToDilate) {
		if (this.op == null) op = new ImageOperation(img);
		op.setImage(img);
		Image out = new Image(img);
		return out.dilate(structuringElement, timesToDilate);
	}
	
	/**
	 * Returns a new eroded image.
	 * @param img
	 * @param structuringElement
	 * @param timesToDilate
	 * @return
	 * @throws Exception
	 */
	public Image erode(Image img, Image structuringElement, int timesToErode) {
		if (this.op == null) op = new ImageOperation(img);
		op.setImage(img);
		Image out = new Image(img);
		return out.erode(structuringElement, timesToErode);
	}
	
	
	/**
	 * Performs a morphological reconstruction.
	 * @return
	 * @throws Exception 
	 */
	public Image reconstruct(Image marker, Image mask, Image structuringElement) throws Exception{
		if (this.mr == null) this.mr = new MorphologicalReconstruction(marker, mask, structuringElement);
		this.mr.setStructuringElement(structuringElement); this.mr.setMarker(marker); this.mr.setMask(mask);
		return mr.getReconstructedImage();
	}
	
	
	/**
	 * Cluster a binary image
	 * @param img
	 * @param structuringElement
	 * @return
	 * @throws Exception
	 */
	public Image cluster(Image img, Image structuringElement) throws Exception{
		if (this.mr == null) this.mr = new MorphologicalReconstruction();
		this.mr.setStructuringElement(structuringElement); this.mr.setMask(img);
		return mr.getClusteredImage();
	}
	
	
	//MORPHOLOGY
	public static double getDilateErodeCentralValue(Image src, Image structElement, int x, int y, int band, boolean dilate){
		double min = 0;
		double max = src.getMaximalIntensity(band);
		if (max < 255) max = 255;
		
		final int tX = (int) (Math.ceil(structElement.getHeight()/2f) - 1),
				tY = (int) (Math.ceil(structElement.getWidth()/2f) - 1);
		
		//esse for itera os valores i e j de acordo com a posi��o do structElement(structuring element) em cima da imagem source
		for (int i=(int)-tY + y; i<= tY + y; i++){
			for (int j=(int) -tX + x; j<= tX + x; j++){
				//se as vari�veis i e j n�o estiverem extrapolando os limites da imagem src e n�o forem igual �s coordenadas do pixel central do structuring element
				if (j >= 0 && i >=0 && j < src.getWidth() && i < src.getHeight() /*&& !(i == y && j == i)*/){
					double sub = 0; //subtra��o
					int structBand = (structElement.getNumBands() > band ? band : structElement.getNumBands() - 1);
					double structValue = structElement.getPixel(j-x+tX, i-y+tY, structBand);
					if (structValue == 255) structValue = max;
					if (!dilate) sub = src.getPixel(j, i, band) - structValue; //se for pra eros�o
					else sub = (max-structValue) - src.getPixel(j, i, band); //se for pra dilatar
					if (sub < min)
						min = sub;
				}
			}
		}
		double result = min+max; //para imagens bin�rias 'min' pode ser -255 ou 0
		if (dilate) result = max-result; //inverter a imagem se for pra dilatar

		//System.out.println(result);
		return result;
	}
	
}
