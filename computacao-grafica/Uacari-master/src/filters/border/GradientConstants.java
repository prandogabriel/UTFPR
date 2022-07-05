package filters.border;

public interface GradientConstants {
	public static final AverageType TYPE_MEAN = AverageType.TYPE_MEAN,
			TYPE_SUM = AverageType.TYPE_SUM, TYPE_DIFFERENCE = AverageType.TYPE_DIFFERENCE, TYPE_PRODUCT = AverageType.TYPE_PRODUCT,
			TYPE_DIVISION_X = AverageType.TYPE_DIVISION_X, TYPE_DIVISION_Y = AverageType.TYPE_DIVISION_Y;
	public static enum AverageType{TYPE_MEAN, TYPE_SUM, TYPE_DIFFERENCE, TYPE_PRODUCT, TYPE_DIVISION_X, TYPE_DIVISION_Y};
}
