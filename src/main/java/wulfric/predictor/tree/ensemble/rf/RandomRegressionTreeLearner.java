package wulfric.predictor.tree.ensemble.rf;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import wulfric.core.Attribute;
import wulfric.core.Instances;
import wulfric.predictor.tree.RegressionTree;
import wulfric.predictor.tree.RegressionTreeLeaf;
import wulfric.predictor.tree.RegressionTreeLearner;
import wulfric.predictor.tree.TreeInteriorNode;
import wulfric.predictor.tree.TreeNode;
import wulfric.util.Permutation;
import wulfric.util.Random;
import wulfric.util.tuple.DoublePair;
import wulfric.util.tuple.IntDoublePair;

/**
 * Class for learning random regression trees.
 *
 * 
 *
 */
public class RandomRegressionTreeLearner extends RegressionTreeLearner {

	protected int numFeatures;
	protected Permutation perm;

	/**
	 * Constructor.
	 */
	public RandomRegressionTreeLearner() {
		numFeatures = -1;
		alpha = 0.01;
		mode = Mode.ALPHA_LIMITED;
	}

	/**
	 * Returns the maximum number of features to consider for each node.
	 *
	 * @return the maximum number of features to consider for each node.
	 */
	public int getNumFeatures() {
		return numFeatures;
	}

	/**
	 * Sets the maximum number of features to consider for each node.
	 *
	 * @param numFeatures the new maximum number of features.
	 */
	public void setNumFeatures(int numFeatures) {
		this.numFeatures = numFeatures;
	}

	@Override
	public RegressionTree build(Instances instances) {
		if (numFeatures < 0) {
			numFeatures = instances.getAttributes().size() / 3;
		}
		if (perm == null || perm.size() != instances.getAttributes().size()) {
			perm = new Permutation(instances.getAttributes().size());
		}
		RegressionTree rt = null;
		switch (mode) {
			case ALPHA_LIMITED:
				rt = buildAlphaLimitedTree(instances, alpha);
				break;
			case NUM_LEAVES_LIMITED:
				rt = buildNumLeafLimitedTree(instances, maxNumLeaves);
				break;
			case DEPTH_LIMITED:
				rt = buildDepthLimitedTree(instances, maxDepth);
				break;
			case MIN_LEAF_SIZE_LIMITED:
				rt = buildMinLeafSizeLimitedTree(instances, minLeafSize);
			default:
				break;
		}
		return rt;
	}

	protected TreeNode createNode(Dataset dataset, int limit, double[] stats) {
		boolean stdIs0 = getStats(dataset.instances, stats);
		final double totalWeights = stats[0];
		final double sum = stats[1];
		final double weightedMean = stats[2];

		// 1. Check basic leaf conditions
		if (stats[0] < limit || stdIs0) {
			RegressionTreeLeaf node = new RegressionTreeLeaf(weightedMean);
			return node;
		}

		// 2. Find best split
		double bestEval = Double.POSITIVE_INFINITY;
		List<IntDoublePair> splits = new ArrayList<>();
		List<Attribute> attributes = dataset.instances.getAttributes();
		int[] a = perm.permute().getPermutation();
		Set<Integer> selected = new HashSet<>(numFeatures);
		for (int i = 0; i < numFeatures; i++) {
			selected.add(a[i]);
		}
		for (int i = 0; i < attributes.size(); i++) {
			Attribute attribute = attributes.get(i);
			if (!selected.contains(i)) {
				continue;
			}
			int attIndex = attribute.getIndex();
			List<IntDoublePair> sortedList = dataset.sortedLists.get(i);
			List<Double> uniqueValues = new ArrayList<>(sortedList.size());
			List<DoublePair> histogram = new ArrayList<>(sortedList.size());
			getHistogram(dataset.instances, sortedList, uniqueValues, totalWeights, sum, histogram);

			if (uniqueValues.size() > 1) {
				DoublePair split = split(uniqueValues, histogram, totalWeights, sum);
				if (split.v2 <= bestEval) {
					IntDoublePair splitPoint = new IntDoublePair(attIndex, split.v1);
					if (split.v2 < bestEval) {
						splits.clear();
						bestEval = split.v2;
					}
					splits.add(splitPoint);
				}
			}
		}
		if (bestEval < Double.POSITIVE_INFINITY) {
			Random rand = Random.getInstance();
			IntDoublePair splitPoint = splits.get(rand.nextInt(splits.size()));
			int attIndex = splitPoint.v1;
			TreeNode node = new TreeInteriorNode(attIndex, splitPoint.v2);
			stats[3] = bestEval + totalWeights * weightedMean * weightedMean;
			return node;
		} else {
			RegressionTreeLeaf node = new RegressionTreeLeaf(weightedMean);
			return node;
		}
	}

}
