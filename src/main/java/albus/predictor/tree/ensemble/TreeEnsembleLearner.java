package albus.predictor.tree.ensemble;

import albus.predictor.Learner;
import albus.predictor.tree.TreeLearner;

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
