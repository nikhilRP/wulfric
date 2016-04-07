package wulfric.predictor.function;

/**
 * Interface for bivariate real functions.
 */
public interface BivariateFunction {

	/**
	 * Computes the value for the function.
	 * 
	 * @param x the 1st argument.
	 * @param y the 2nd argument.
	 * @return the value for the function.
	 */
	public double evaluate(double x, double y);

}
