package wulfric.predictor.tree;

import java.util.Collections;
import java.util.List;

import wulfric.core.Instance;
import wulfric.core.Instances;
import wulfric.util.tuple.DoublePair;
import wulfric.util.tuple.IntDoublePair;

/**
 * Abstract class for learning regression trees.
 * 
 * 
 *
 */
public abstract class RTreeLearner extends TreeLearner {
	
	@Override
	public abstract RTree build(Instances instances);
	
	protected void getHistogram(Instances instances, List<IntDoublePair> pairs, List<Double> uniqueValues, double w,
			double s, List<DoublePair> histogram) {
		if (pairs.size() > 0) {
			double lastValue = pairs.get(0).v2;
			double totalWeight = instances.get(pairs.get(0).v1).getWeight();
			double sum = instances.get(pairs.get(0).v1).getTarget() * totalWeight;

			for (int i = 1; i < pairs.size(); i++) {
				IntDoublePair pair = pairs.get(i);
				double value = pair.v2;
				double weight = instances.get(pairs.get(i).v1).getWeight();
				double resp = instances.get(pairs.get(i).v1).getTarget();
				if (value != lastValue) {
					uniqueValues.add(lastValue);
					histogram.add(new DoublePair(totalWeight, sum));
					lastValue = value;
					totalWeight = weight;
					sum = resp * weight;
				} else {
					totalWeight += weight;
					sum += resp * weight;
				}
			}
			uniqueValues.add(lastValue);
			histogram.add(new DoublePair(totalWeight, sum));
		}

		if (pairs.size() != instances.size()) {
			// Zero entries are present
			double sumWeight = 0;
			double sumTarget = 0;
			for (DoublePair pair : histogram) {
				sumWeight += pair.v1;
				sumTarget += pair.v2;
			}

			double weightOnZero = w - sumWeight;
			double sumOnZero = s - sumTarget;
			int idx = Collections.binarySearch(uniqueValues, ZERO);
			if (idx < 0) {
				// This should always happen
				uniqueValues.add(-idx - 1, ZERO);
				histogram.add(-idx - 1, new DoublePair(weightOnZero, sumOnZero));
			}
		}
	}

	protected boolean getStats(Instances instances, double[] stats) {
		stats[0] = stats[1] = stats[2] = 0;
		if (instances.size() == 0) {
			return true;
		}
		double firstTarget = instances.get(0).getTarget();
		boolean stdIs0 = true;
		for (Instance instance : instances) {
			double weight = instance.getWeight();
			double target = instance.getTarget();
			stats[0] += weight;
			stats[1] += weight * target;
			if (stdIs0 && target != firstTarget) {
				stdIs0 = false;
			}
		}
		stats[2] = stats[1] / stats[0];
		if (Double.isNaN(stats[2])) {
			stats[2] = 0;
		}
		return stdIs0;
	}

}
