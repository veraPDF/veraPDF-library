package org.verapdf.cli.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.verapdf.pdfa.flavours.PDFAFlavour;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.Parameter;

/**
 * This class holds all command line options used by VeraPDF application.
 * 
 * @author Timur Kamalov
 */
public class VeraCliArgParser {

    @Parameter(names = { "--version" }, description = "Version information.")
    private boolean showVersion = false;

    @Parameter(names = { "-l", "--profiles" }, description = "List built in Validation Profiles.")
    private boolean listProfiles = false;

    @Parameter(names = { "--format" }, description = "Choose output format:", converter = FormatConverter.class)
    private FormatType format = FormatType.XML;

    @Parameter(names = { "-h", "--help" }, description = "Shows this message and exits.", help = true)
    private boolean help = false;

    @Parameter(names = { "--success", "--passed" }, description = "Logs successful validation checks.")
    private boolean passed = false;

    @Parameter(names = { "-f", "--flavour" }, description = "Choose built in Validation Profile flavour:", converter = FlavourConverter.class)
    private PDFAFlavour flavour = PDFAFlavour.PDFA_1_B;

    @Parameter(names = { "-p", "--profile" }, description = "Load a Validation Profile from given path and exit if loading fails. This overrides any choice or default implied by the -f / --flavour option.")
    private File validatioProfile = null;

    @Parameter(names = { "-x", "--features", "--report" }, description = "Extract and report PDF features.")
    private boolean features = false;

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
    public FormatType getFormat() {
        return this.format;
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
    public PDFAFlavour getFlavour() {
        return this.flavour;
    }

    /**
     * @return the {@link File} object for the validation profile
     */
    public File getProfile() {
        return this.validatioProfile;
    }
    /**
     * @return the list of file paths for validation
     */
    public List<String> getPathsToValidate() {
        return this.filePaths;
    }

    private static final class FormatConverter implements
            IStringConverter<FormatType> {
        /**
         * { @inheritDoc }
         */
        @Override
        public FormatType convert(final String value) {
            return FormatType.fromOption(value);
        }

    }

    private static final class FlavourConverter implements
            IStringConverter<PDFAFlavour> {
        /**
         * { @inheritDoc }
         */
        @Override
        public PDFAFlavour convert(final String value) {
            return PDFAFlavour.byFlavourId(value);
        }

    }
}