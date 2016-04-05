package albus.cmdline.options;

import albus.cmdline.Argument;

public class LearnerWithTaskOptions extends LearnerOptions {

	@Argument(name = "-g", description = "task between classification (c) and regression (r) (default: r)")
	public String task = "r";
	
}
