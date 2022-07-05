package morphology;


import image.Image;
import similarity.SimilarityMeasure;

/**
 * Class to perform morphological reconstructions and clusterizations.
 * @author �rick Oliveira Rodrigues (erickr@id.uff.br)
 */
public class MorphologicalReconstruction {
	private Image mask = null, marker, structuringElement;

	
	public MorphologicalReconstruction(Image marker, Image mask, Image structuringElement){
		this.mask = mask;
		this.marker = marker;
		this.structuringElement = structuringElement;
	}
	public MorphologicalReconstruction(){}
	
	
	//set
	public void setStructuringElement(Image structuringElement){
		this.structuringElement = structuringElement;
	}
	public void setMask(Image mask){this.mask = mask;}
	public void setMarker(Image marker){this.marker = marker;}
	
	//has
	//public boolean hasMask(){return mask != null;}
	
	
	//get
	public Image getMask(){return this.mask;}
	public Image getStructuringElement(){return this.structuringElement;}
	public Image getMarker(){return this.marker;}
	
	public Image getReconstructedImage() throws Exception{
		return reconstructImage(this.getMarker(), this.getMask(), this.getStructuringElement());
	}
	public Image getClusteredImage() throws Exception{
		Image indexed = new Image(this.getMask());
		indexed.convertToIndexedImage();
		indexed.contrast();
		return reconstructImage(indexed, this.getMask(), this.getStructuringElement()).contrast();
	}
	private Image reconstructImage(Image marker, Image mask, Image structElement) throws Exception{
		if (!mask.isBinary()) throw new Exception("The mask image of the reconstruction must be binary!");
		
		
		Image itImg = marker.clone(), itImg2 = null;
		int count = 0;
		System.out.printf("Processing reconstruction (this may take a while)... \n");
		long startTime = System.nanoTime();
		
		
		do {
			itImg2 = itImg.clone();
			itImg.dilate(getStructuringElement(), 1);
			itImg.maskImage(mask);
			count ++;
		}while(!itImg.equals(itImg2, SimilarityMeasure.MEAN_DIFFERENCE));
		
		System.out.printf("| Total of iterations during reconstruction: %d  (in %d nano seconds). \n", count, (System.nanoTime() - startTime));
		
		return itImg;
	}


	
}
