package wulfric.predictor;

import wulfric.core.Instance;

/**
 * Interface for classfiers.
 */
public interface Classifier extends Predictor {

	/**
	 * Classifies an instance.
	 * 
	 * @param instance the instance to classify.
	 * @return a classified value.
	 */
	public int classify(Instance instance);

}
