package distances;

public class ChebyshevDistance implements Distance{

	@Override
	public double compute(double x1, double y1, double x2, double y2) {
		return Math.max(x2-x1, y2-y1);
	}

	@Override
	public double compute(double[] x, double[] y) {
		double max = y[0] - x[0];
		for (int k=1; k<x.length; k++){
			if (y[k] - x[k] > max) {
				max = y[k] - x[k];
			}
		}
		return max;
	}

	@Override
	public boolean equals(final Object distance) {
		return (this.getClass().getName().equals(distance.getClass().getName()));
	}

	
}
