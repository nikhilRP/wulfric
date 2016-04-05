package albus.cmdline.options;

import albus.cmdline.Argument;

public class HoldoutValidatedLearnerOptions extends LearnerOptions {

	@Argument(name = "-v", description = "valid set path")
	public String validPath = null;
	
	@Argument(name = "-e", description = "evaluation metric (default: default metric of task)")
	public String metric = null;
	
}
