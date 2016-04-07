package wulfric.predictor;

import wulfric.core.Instance;

/**
 * Interface for regressors.
 */
public interface Regressor extends Predictor {

	/**
	 * Regresses an instance.
	 * 
	 * @param instance the instance to regress.
	 * @return a regressed value.
	 */
	public double regress(Instance instance);

}
