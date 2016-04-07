package wulfric.predictor.evaluation;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import wulfric.cmdline.Argument;
import wulfric.cmdline.CmdLineParser;
import wulfric.core.Instance;
import wulfric.core.Instances;
import wulfric.core.io.InstancesReader;
import wulfric.predictor.Classifier;
import wulfric.predictor.Learner.Task;
import wulfric.predictor.ProbabilisticClassifier;
import wulfric.predictor.Regressor;
import wulfric.predictor.io.PredictorReader;
import wulfric.util.OptimUtils;

/**
 * Class for making predictions.
 */
public class Predictor {

	/**
	 * Makes predictions for a dataset.
	 * 
	 * @param regressor the model.
	 * @param instances the dataset.
	 * @param path the output path.
	 * @param residual <code>true</code> if residuals are the output.
	 * @throws IOException
	 */
	public static void predict(Regressor regressor, Instances instances, String path, boolean residual)
			throws IOException {
		PrintWriter out = new PrintWriter(path);
		if (residual) {
			for (Instance instance : instances) {
				double pred = regressor.regress(instance);
				out.println(instance.getTarget() - pred);
			}
		} else {
			for (Instance instance : instances) {
				double pred = regressor.regress(instance);
				out.println(pred);
			}
		}
		out.flush();
		out.close();
	}

	/**
	 * Makes predictions for a dataset.
	 * 
	 * @param classifier the model.
	 * @param instances the dataset.
	 * @param path the output path.
	 * @throws IOException
	 */
	public static void predict(Classifier classifier, Instances instances, String path) throws IOException {
		PrintWriter out = new PrintWriter(path);
		for (Instance instance : instances) {
			int pred = classifier.classify(instance);
			out.println(pred);
		}
		out.flush();
		out.close();
	}

	static class Options {

		@Argument(name = "-r", description = "attribute file path")
		String attPath = null;

		@Argument(name = "-d", description = "data set path", required = true)
		String dataPath = null;

		@Argument(name = "-m", description = "model path", required = true)
		String modelPath = null;

		@Argument(name = "-p", description = "prediction path")
		String predictionPath = null;

		@Argument(name = "-R", description = "residual path")
		String residualPath = null;

		@Argument(name = "-g", description = "task between classification (c) and regression (r) (default: r)")
		String task = "r";

		@Argument(name = "-P", description = "output probablity (default: false)")
		boolean prob = false;

	}

	/**
	 * Makes predictions on a dataset.
	 * 
	 * <pre>
	 * usage: wulfric.predictor.evaluation.Predictor
	 * -d	data set path
	 * -m	model path
	 * [-r]	attribute file path
	 * [-p]	prediction path
	 * [-R]	residual path
	 * [-g]	task between classification (c) and regression (r) (default: r)
	 * [-P]	output probablity (default: false)
	 * </pre>
	 * 
	 * @param args the command line arguments.
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Options opts = new Options();
		CmdLineParser parser = new CmdLineParser(Predictor.class, opts);
		Task task = null;
		try {
			parser.parse(args);
			task = Task.get(opts.task);
		} catch (IllegalArgumentException e) {
			parser.printUsage();
			System.exit(1);
		}

		Instances instances = InstancesReader.read(opts.attPath, opts.dataPath);
		wulfric.predictor.Predictor predictor = PredictorReader.read(opts.modelPath);

		switch (task) {
			case REGRESSION:
				Regressor regressor = (Regressor) predictor;
				double rmse = Evaluator.evalRMSE(regressor, instances);
				System.out.println("RMSE on Test: " + rmse);

				if (opts.predictionPath != null) {
					PrintWriter out = new PrintWriter(opts.predictionPath);
					for (Instance instance : instances) {
						double pred = regressor.regress(instance);
						out.println(pred);
					}
					out.flush();
					out.close();
				}

				if (opts.residualPath != null) {
					PrintWriter out = new PrintWriter(opts.residualPath);
					for (Instance instance : instances) {
						double pred = regressor.regress(instance);
						out.println(instance.getTarget() - pred);
					}
					out.flush();
					out.close();
				}

				break;
			case CLASSIFICATION:
				Classifier classifier = (Classifier) predictor;
				double error = Evaluator.evalError(classifier, instances);
				System.out.println("Error rate on Test: " + (error * 100) + " %");

				if (opts.predictionPath != null) {
					if (opts.prob) {
						PrintWriter out = new PrintWriter(opts.predictionPath);
						ProbabilisticClassifier probClassifier = (ProbabilisticClassifier) predictor;
						for (Instance instance : instances) {
							double[] pred = probClassifier.predictProbabilities(instance);
							out.println(Arrays.toString(pred));
						}
						out.flush();
						out.close();
					} else {
						PrintWriter out = new PrintWriter(opts.predictionPath);
						for (Instance instance : instances) {
							double pred = classifier.classify(instance);
							out.println((int) pred);
						}
						out.flush();
						out.close();
					}
				}

				if (opts.residualPath != null) {
					if (predictor instanceof Regressor) {
						PrintWriter out = new PrintWriter(opts.residualPath);
						Regressor regressingClassifier = (Regressor) predictor;
						for (Instance instance : instances) {
							double pred = regressingClassifier.regress(instance);
							int cls = (int) instance.getTarget();
							out.println(OptimUtils.getPseudoResidual(pred, cls));
						}
						out.flush();
						out.close();
					} else {
						System.out.println("Warning: Classifier does not support outputing pseudo residual.");
					}
				}

				break;
			default:
				break;
		}
	}

}
