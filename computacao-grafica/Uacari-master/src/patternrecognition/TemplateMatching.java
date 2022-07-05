package patternrecognition;

import image.Image;
import similarity.MeanDifference;
import similarity.SimilarityMeasure;




/**
 * Still needs to be finished.
 * @author Érick Oliveira Rodrigues (erickr@id.uff.br)
 */
public class TemplateMatching{
	private Image reference = null, template = null, referenceWindow = null;
	private SimilarityMeasure similarityMeasure = new MeanDifference(1);
	private int templateX = 0, templateY = 0;
	
	public TemplateMatching(Image reference, Image template){
		this.reference = reference;
		this.template = template;
	}
	
	//set
	public void setTemplatePosition(int x, int y){
		this.templateX = x;
		this.templateY = y;
		referenceWindow = new Image(template.getWidth(), template.getHeight());
		double pixelValue = 0;
		for (int i=0; i<template.getHeight(); i++){
			nextColumn:
			for (int j=0; j<template.getWidth(); j++){
				for (int b=0; b<template.getNumBands(); b++){
					if (j + x >= reference.getWidth() || i + y >= reference.getHeight())
						pixelValue = 0; //if exceeds the boundary of the image then treats as 0
					else
						pixelValue = reference.getPixel(j + x, i + y, b);
					
					if (template.getNumBands() > 3)
						if (template.getPixel(j, i, 3) == 0) 
							continue nextColumn;
					
					referenceWindow.setPixel(j, i, b, pixelValue);
				}
			}
		}
	}
	public void setSimilarityMeasure(SimilarityMeasure sm){
		this.similarityMeasure = sm;
	}
	
	//get
	public SimilarityMeasure getSimilarityMeasure(){return this.similarityMeasure;}
	public Image getReferenceImage(){return this.reference;}
	public Image getTemplateImage(){return this.template;}
	public Image getReferenceWindowImage(){return this.referenceWindow;}
	
	public double computeSimilarity(){
		return this.similarityMeasure.compare(referenceWindow, template);
	}
	public double computeSimilarityAt(int x, int y){
		this.setTemplatePosition(x, y);
		return this.computeSimilarity();
	}
	

}
