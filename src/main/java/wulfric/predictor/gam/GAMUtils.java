package wulfric.predictor.gam;

import java.util.List;

import wulfric.core.Attribute;
import wulfric.core.BinnedAttribute;
import wulfric.core.NominalAttribute;
import wulfric.core.NumericalAttribute;
import wulfric.predictor.function.Array1D;
import wulfric.predictor.function.LinearFunction;
import wulfric.predictor.glm.GLM;

class GAMUtils {

	static GAM getGAM(GLM glm, List<Attribute> attList) {
		double[] w = glm.coefficients(0);
		
		GAM gam = new GAM();
		int k = 0;
		for (Attribute attribute : attList) {
			int attIndex = attribute.getIndex();
			int[] term = new int[] {attIndex};
			if (attribute instanceof NumericalAttribute) {
				LinearFunction func = new LinearFunction(attIndex, -w[k++]);
				gam.add(term, func);
			} else if (attribute instanceof BinnedAttribute) {
				BinnedAttribute binnedAttribute = (BinnedAttribute) attribute;
				int size = binnedAttribute.getNumBins();
				double[] predictions = new double[size];
				for (int j = 0; j < predictions.length && k < w.length; j++) {
					predictions[j] = -w[k++];
				}
				Array1D ary = new Array1D(attIndex, predictions);
				gam.add(term, ary);
			} else if (attribute instanceof NominalAttribute) {
				NominalAttribute nominalAttribute = (NominalAttribute) attribute;
				int size = nominalAttribute.getCardinality();
				double[] predictions = new double[size];
				for (int j = 0; j < predictions.length && k < w.length; j++) {
					predictions[j] = -w[k++];
				}
				Array1D ary = new Array1D(attIndex, predictions);
				gam.add(term, ary);
			}
		}
		gam.setIntercept(-glm.intercept(0));
		
		return gam;
	}
	
}
