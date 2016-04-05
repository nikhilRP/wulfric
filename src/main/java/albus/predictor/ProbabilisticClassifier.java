package albus.predictor;

import albus.core.Instance;

/**
 * Interface for classifiers that predicts the class probabilities.
 * 
 * 
 * 
 */
public interface ProbabilisticClassifier extends Classifier {

	/**
	 * Returns the class probabilities.
	 * 
	 * @param instance the instance to predict.
	 * @return the class probabilities.
	 */
	public double[] predictProbabilities(Instance instance);

}
