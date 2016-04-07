package wulfric.predictor.tree.ensemble;

import wulfric.predictor.Learner;
import wulfric.predictor.tree.TreeLearner;

/**
 * Class for learning tree ensembles.
 * 
 * 
 *
 */
public abstract class TreeEnsembleLearner extends Learner {

	protected TreeLearner treeLearner;
	
	public TreeLearner getTreeLearner() {
		return treeLearner;
	}
	
	public void setTreeLearner(TreeLearner treeLearner) {
		this.treeLearner = treeLearner;
	}
	
}
