package distances;

import java.math.BigDecimal;

public class ManhattanDistance implements Distance {

	@Override
	public double compute(double x1, double y1, double x2, double y2) {
		return Math.abs(x2 - x1) + Math.abs(y2 - y1);
	}

	@Override
	public double compute(double[] x, double[] y) {
		BigDecimal bd = BigDecimal.valueOf(0);
		for (int k=0; k<x.length; k++){
			bd.add(BigDecimal.valueOf(Math.abs(y[k] - x[k])));
		}
		return bd.doubleValue();
	}

	@Override
	public boolean equals(final Object distance) {
		return (this.getClass().getName().equals(distance.getClass().getName()));
	}

}
