package distances;


public interface Distance {
	public static final Distance EUCLIDEAN_DISTANCE = new EuclideanDistance(), MANHATTAN_DISTANCE = new ManhattanDistance(), 
			CHEBYSHEV_DISTANCE = new ChebyshevDistance(), MINKOWSKI_DISTANCE = new MinkowskiDistance(3);
	
	
	public double compute(double x1, double y1, double x2, double y2);
	/**
	 * x and y are two points that have x.length or y.length dimensions
	 * @param x
	 * @param y
	 * @return
	 */
	public double compute(double[] x, double[] y);

	
	
}
