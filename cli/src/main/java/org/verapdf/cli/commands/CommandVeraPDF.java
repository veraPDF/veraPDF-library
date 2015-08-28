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

    @Parameter(names = "--output")
    private String output;

    /**
     * @return the validate
     */
    public boolean isValidate() {
        return validate;
    }

    /**
     * @return the inputPath
     */
    public String getInputPath() {
        return inputPath;
    }

    /**
     * @return the inputPathURL
     */
    public boolean isInputPathURL() {
        return inputPathURL;
    }

    /**
     * @return the profile
     */
    public String getProfile() {
        return profile;
    }

	/**
	 * @return the logPassedChecks
	 */
	public boolean isLogPassedChecks() {
		return logPassedChecks;
	}

	/**
     * @return the output
     */
    public String getOutput() {
        return output;
    }
}