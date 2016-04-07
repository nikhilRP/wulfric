package wulfric.predictor;

import java.io.BufferedReader;
import java.io.PrintWriter;

import wulfric.core.Copyable;
import wulfric.core.Writable;

/**
 * Interface for predictors.
 *
 */
public interface Predictor extends Writable, Copyable<Predictor> {
	
	/**
	 * Reads in this predictor. This method is used in {@link wulfric.predictor.io.PredictorReader}.
	 * 
	 * @param in the reader.
	 * @throws Exception
	 */
	public void read(BufferedReader in) throws Exception;

	/**
	 * Writes this predictor. This method is used in {@link wulfric.predictor.io.PredictorWriter}.
	 * 
	 * @param out the writer.
	 * @throws Exception
	 */
	public void write(PrintWriter out) throws Exception;

}
