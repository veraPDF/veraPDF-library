package org.verapdf.cli.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

/**
 * This class holds all command line options used by VeraPDF application.
 * @author Timur Kamalov
 */
@Parameters(commandNames = "verapdf")
public class CommandVeraPDF extends Command {

    @Parameter(names = "--validate", required = true)
    private boolean validate;

    @Parameter(names = "--input", required = true)
    private String inputPath;

    @Parameter(names = "--urlinput")
    private boolean inputPathURL;

    @Parameter(names = "--profile")
    private String profile;

	@Parameter(names = "--log-passed-checks")
	private boolean logPassedChecks = false;

	@Parameter(names = "--max-failed-checks")
	private int maxFailedChecks = 100;

	@Parameter(names = "--max-displayed")
	private int maxDisplayedFailedChecks = 100;

    @Parameter(names = "--output")
    private String output;

    /**
     * @return the validate
     */
    public boolean isValidate() {
        return this.validate;
    }

    /**
     * @return the inputPath
     */
    public String getInputPath() {
        return this.inputPath;
    }

    /**
     * @return the inputPathURL
     */
    public boolean isInputPathURL() {
        return this.inputPathURL;
    }

    /**
     * @return the profile
     */
    public String getProfile() {
        return this.profile;
    }

	/**
	 * @return the logPassedChecks
	 */
	public boolean isLogPassedChecks() {
		return this.logPassedChecks;
	}

	/**
	 * @return maximum amount of failed checks for each rule during validation
	 */
	public int getMaxFailedChecks() {
		return this.maxFailedChecks;
	}

	public int getMaxDisplayedFailedChecks() {
		return maxDisplayedFailedChecks;
	}

	/**
     * @return the output
     */
    public String getOutput() {
        return this.output;
    }
}