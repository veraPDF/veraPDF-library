package org.verapdf.cli.commands;

import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.Parameter;

/**
 * This class holds all command line options used by VeraPDF application.
 * @author Timur Kamalov
 */
public class VeraCliArgParser {

    @Parameter(names = {"-v", "--version"}, description = "ouput version information.")
    private boolean showVersion;

    @Parameter(names = {"-l", "--list"}, description = "lists all built in validation profiles.")
    private boolean listProfiles;

    @Parameter(names = {"-h", "--help"}, description = "shows this message and exit.", help = true)
    private boolean help;

    @Parameter(names = {"-p", "--profile"}, description = "validation profile code e.g. 1b, 1a, etc. or a path to a validation profile.")
    private String profile = "1b";

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
     * @return
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