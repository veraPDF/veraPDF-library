package org.verapdf.cli.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.verapdf.pdfa.flavours.PDFAFlavour;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

/**
 * This class holds all command line options used by VeraPDF application.
 * 
 * @author Timur Kamalov
 */
public class VeraCliArgParser {
    final static VeraCliArgParser DEFAULT_ARGS = new VeraCliArgParser();
    final static String FLAG_SEP = "-";
    final static String OPTION_SEP = "--";
    final static String HELP_FLAG = FLAG_SEP + "h";
    final static String HELP = OPTION_SEP + "help";
    final static String VERSION = OPTION_SEP + "version";
    final static String FLAVOUR_FLAG = FLAG_SEP + "f";
    final static String FLAVOUR = OPTION_SEP + "flavour";
    final static String SUCCESS = OPTION_SEP + "success";
    final static String PASSED = OPTION_SEP + "passed";
    final static String LIST_FLAG = FLAG_SEP + "l";
    final static String LIST = OPTION_SEP + "list";
    final static String LOAD_PROFILE_FLAG = FLAG_SEP + "p";
    final static String LOAD_PROFILE = OPTION_SEP + "profile";
    final static String EXTRACT_FLAG = FLAG_SEP + "x";
    final static String EXTRACT = OPTION_SEP + "extract";
    final static String FORMAT = OPTION_SEP + "format";
    final static String RECURSE_FLAG = FLAG_SEP + "r";
    final static String RECURSE = OPTION_SEP + "recurse";

    @Parameter(names = { HELP_FLAG, HELP }, description = "Shows this message and exits.", help = true)
    private boolean help = false;

    @Parameter(names = { VERSION }, description = "Version information.")
    private boolean showVersion = false;

    @Parameter(names = { FLAVOUR_FLAG, FLAVOUR }, description = "Choose built in Validation Profile flavour, e.g. 1b. Alternatively supply 0 to turn off PDF/A validation.", converter = FlavourConverter.class)
    private PDFAFlavour flavour = PDFAFlavour.PDFA_1_B;

    @Parameter(names = { SUCCESS, PASSED }, description = "Logs successful validation checks.")
    private boolean passed = false;

    @Parameter(names = { LIST_FLAG, LIST }, description = "List built in Validation Profiles.")
    private boolean listProfiles = false;

    @Parameter(names = { LOAD_PROFILE_FLAG, LOAD_PROFILE }, description = "Load a Validation Profile from given path and exit if loading fails. This overrides any choice or default implied by the -f / --flavour option.", validateWith = ProfileFileValidator.class)
    private File profileFile;

    @Parameter(names = { EXTRACT_FLAG, EXTRACT }, description = "Extract and report PDF features.")
    private boolean features = false;

    @Parameter(names = { FORMAT }, description = "Choose output format:", converter = FormatConverter.class)
    private FormatOption format = FormatOption.XML;

    @Parameter(names = { RECURSE_FLAG, RECURSE }, description = "Recurse directories, only files with a .pdf extension are processed.")
    private boolean isRecurse = false;

    @Parameter(description = "FILES")
    private List<String> pdfPaths = new ArrayList<>();

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
     * @return true if to recursively process sub-dirs
     */
    public boolean isRecurse() {
        return this.isRecurse;
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
    public FormatOption getFormat() {
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
    public File getProfileFile() {
        return this.profileFile;
    }

    /**
     * @return the list of file paths
     */
    public List<String> getPdfPaths() {
        return this.pdfPaths;
    }

    /**
     * JCommander parameter converter for {@link FormatOption}, see
     * {@link IStringConverter} and {@link FormatOption#fromOption(String)}.
     * 
     * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
     * 
     */
    public static final class FormatConverter implements
            IStringConverter<FormatOption> {
        /**
         * { @inheritDoc }
         */
        @Override
        public FormatOption convert(final String value) {
            return FormatOption.fromOption(value);
        }

    }

    /**
     * JCommander parameter converter for {@link PDFAFlavour}, see
     * {@link IStringConverter} and {@link PDFAFlavour#byFlavourId(String)}.
     * 
     * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
     *
     */
    public static final class FlavourConverter implements
            IStringConverter<PDFAFlavour> {
        /**
         * { @inheritDoc }
         */
        @Override
        public PDFAFlavour convert(final String value) {
            for (PDFAFlavour flavour : PDFAFlavour.values()) {
                if (flavour.getId().equalsIgnoreCase(value))
                    return flavour;
            }
            throw new ParameterException("Illegal --flavour argument:" + value);
        }

    }

    /**
     * JCommander parameter validator for {@link File}, see
     * {@link IParameterValidator}. Enforces an existing, readable file.
     * 
     * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
     *
     */
    public static final class ProfileFileValidator implements
            IParameterValidator {
        /**
         * { @inheritDoc }
         */
        @Override
        public void validate(final String name, final String value)
                throws ParameterException {
            File profileFile = new File(value);
            if (!profileFile.isFile() || !profileFile.canRead()) {
                throw new ParameterException("Parameter " + name
                        + " must be the path to an existing, readable file, value=" + value);
            }
        }

    }
}