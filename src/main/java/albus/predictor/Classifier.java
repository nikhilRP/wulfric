package albus.predictor;

import albus.core.Instance;

/**
 * Interface for classfiers.
 * 
 * 
 * 
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
