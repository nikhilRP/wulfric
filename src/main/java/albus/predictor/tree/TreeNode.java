package albus.predictor.tree;

import albus.core.Copyable;
import albus.core.Writable;

/**
 * Abstract class for tree nodes.
 * 
 * 
 * 
 */
public abstract class TreeNode implements Writable, Copyable<TreeNode> {

	/**
	 * Returns <code>true</code> if the node is a leaf.
	 * 
	 * @return <code>true</code> if the node is a leaf.
	 */
	public abstract boolean isLeaf();

}
