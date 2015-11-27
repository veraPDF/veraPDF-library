package org.verapdf.cli.commands;

import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.Parameter;

/**
 * This class holds all command line options used by VeraPDF application.
 * @author Timur Kamalov
 */
public class VeraCliArgParser {

    @Parameter(names = {"--version"}, description = "ouput version information.")
    private boolean showVersion;

    @Parameter(names = {"-l", "--list"}, description = "lists all built in validation profiles.")
    private boolean listProfiles;

    @Parameter(names = {"-v", "--verbose"}, description = "outputs results in verbose format.")
    private boolean verbose;

    @Parameter(names = {"-h", "--help"}, description = "shows this message and exit.", help = true)
    private boolean help;

    @Parameter(names = {"--passed"}, description = "logs passed checks.")
    private boolean passed;

    @Parameter(names = {"-p", "--profile"}, description = "validation profile code e.g. 1b, 1a, etc. or a path to a validation profile.")
    private String profile = "1b";

    @Parameter(names = {"-f", "--features"}, description = "report PDF features.")
    private boolean features;

	@Parameter(description = "FILES")
	private List<String> filePaths = new ArrayList<>();
	
    /**
     * @return true if version information requested
     */
    public boolean showVersion() {
        return this.showVersion;
    }

    /**
     * @return true if a list of supported profiles requested
     */
    public boolean listProfiles() {
        return this.listProfiles;
    }

    /**
     * @return true if help requested
     */
    public boolean isHelp() {
        return this.help;
    }

    /**
     * @return true if verbose output requested
     */
    public boolean isVerbose() {
        return this.verbose;
    }

    /**
     * @return true if log passed checks requested
     */
    public boolean logPassed() {
        return this.passed;
    }

    /**
     * @return true if PDF Feature extraction requested
     */
    public boolean extractFeatures() {
        return this.features;
    }

    /**
     * @return the validation flavour string id
     */
    public String getProfile() {
        return this.profile;
    }
    
    /**
     * @return the list of file paths for validation
     */
    public List<String> getPathsToValidate() {
        return this.filePaths;
    }
}