package albus.predictor.tree;

import albus.predictor.Regressor;

/**
 * Interface for regression trees.
 * 
 * 
 *
 */
public interface RTree extends Regressor {

	/**
	 * Multiplies this tree with a constant.
	 * 
	 * @param c the constant.
	 */
	public void multiply(double c);
	
	/**
	 * Returns a deep copy of this tree.
	 */
	public RTree copy();
	
}
