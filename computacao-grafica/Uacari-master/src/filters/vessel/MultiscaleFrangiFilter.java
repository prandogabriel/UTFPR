package filters.vessel;

import filters.Filter;
import image.Image;

/**
 * Class that returns the mean of 5 Frangi filter responses (varying min, max and sigma step) and beta.
 */
public class MultiscaleFrangiFilter extends Filter {
    private FrangiFilter f1 = null, f2 = null, f3 = null, f4 = null, f5 = null;

    public MultiscaleFrangiFilter(){
        f1 = new FrangiFilter();
        f2 = new FrangiFilter();
        f3 = new FrangiFilter();
        f4 = new FrangiFilter();
        f5 = new FrangiFilter();

        //instantiate the five frangi filters
        f1 = new FrangiFilter();
        f1.setMinSigma(0); f1.setMaxSigma(1);
        f1.setSigmaStep(0.5f);

        f2.setMinSigma(1); f2.setMaxSigma(1);
        f2.setSigmaStep(0.25f);

        f3.setMinSigma(0); f3.setMaxSigma(3);
        f3.setSigmaStep(1);

        f4.setMinSigma(0); f4.setMaxSigma(1);
        f4.setBeta(2f, 3);
        f4.setSigmaStep(0.2f);

        f5.setMinSigma(0); f5.setMaxSigma(3);
        f5.setBeta(2f, 3f);
        f5.setSigmaStep(0.6f);
    }

    @Override
    public double getFilteredPixel(final Image image, final int x, final int y, final int band) {
        return image.getPixelBoundaryMode(x, y, band);
    }

    public Image applyFilter(final Image image) {
        Image i1 = image.clone().applyFilter(f1).multiply(0.2f),
                i2 = image.clone().applyFilter(f2).multiply(0.2f),
                i3 = image.clone().applyFilter(f3).multiply(0.2f),
                i4 = image.clone().applyFilter(f4).multiply(0.2f),
                i5 = image.clone().applyFilter(f5).multiply(0.2f);

        //add images
        i1.add(i2);
        i1.add(i3);
        i1.add(i4);
        i1.add(i5);

        return i1;
    }

}
