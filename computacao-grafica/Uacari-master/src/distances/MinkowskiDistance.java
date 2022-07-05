package distances;

import java.math.BigDecimal;

public class MinkowskiDistance implements Distance{
	
	private int p = 3;
	
	public MinkowskiDistance(int p){
		this.p = p;
	}
	public MinkowskiDistance(){
		
	}
	
	public void setP(int p){
		this.p = p;
	}

	@Override
	public double compute(double x1, double y1, double x2, double y2) {
		return Math.pow(Math.pow(Math.abs(x2 - x1), 2) + Math.pow(Math.abs(y2 - y1), 2), 1f/p);
	}

	@Override
	public double compute(double[] x, double[] y) {
		BigDecimal bd = BigDecimal.valueOf(0);
		for (int k=0; k<x.length; k++){
			bd = bd.add(BigDecimal.valueOf(Math.pow(Math.abs(y[k] - x[k]), p)));
		}
		//bd = bd.pow(1f/g);
		return Math.pow(bd.doubleValue(), 1f/p);
	}

	
	@Override
	public boolean equals(final Object distance) {
		return (this.getClass().getName().equals(distance.getClass().getName()));
	}

}
