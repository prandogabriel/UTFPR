package filters.connectivity;

import filters.Filter;
import image.Image;

/**
 * Connectivity filter. This filter should be run with a large stack size (use -Xss when launching the program).
 * Created by: Erick Oliveira Rodrigues
 */
public class Connectivity extends Filter {
    private double threshold = 0;
    private Image kernel = new Image(new int[][]{{0,1,0},{1,0,1},{0,1,0}});

    /**
     * Sets the threshold to consider a pixel for "counting" the connectivity. Standard threshold is 0. If > 0 then it is a pixel to keep counting the connectivity.
     * @param threshold
     */
    public void setThreshold(final double threshold){
        this.threshold = threshold;
    }

    /**
     * Sets the kernel used to connect the points in the connectivity filter
     */
    public void setKernel(final Image kernel){
        this.kernel = kernel;
    }

    @Override
    public double getFilteredPixel(final Image image, final int x, final int y, final int band) {
        boolean[][] visitMap = new boolean[image.getHeight()][image.getWidth()];

        long[] score = new long[1]; //array for java to treat it as an object
        checkPixel(image, x, y, band, visitMap, score);

        return score[0];
    }

    private void checkPixel(final Image image, final int x, final int y, final int band, final boolean[][] visitMap, final long[] score){
        if (x < 0 || y < 0 || x >= image.getWidth() || y >= image.getHeight())
            return;
        
        if (image.getPixel(x, y, band) > this.threshold && !visitMap[y][x]) {
            score[0]++;
            visitMap[y][x] = true; //mark as visited
        }else
            return;

        //kernel-based vicinity access
        int midX = kernel.getWidth()/2, midY = kernel.getHeight()/2;
        for (int i=0; i<kernel.getHeight(); i++){
            for (int j=0; j<kernel.getWidth(); j++){
                int posX = -midX + x + j,
                        posY = -midY + y + i;

                if (posX == x && posY == y) continue;

                if (kernel.getPixel(j, i) > 0)
                    checkPixel(image, posX, posY, band, visitMap, score);
            }
        }

        return;
    }

    public Image applyFilter(final Image image) {
        Image out = super.applyFilter(image);
        out.stretchOrShrinkRange(0, 255);
        return out;
    }

}
