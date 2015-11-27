/**
 *
 */
package org.verapdf.cli;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.verapdf.ReleaseDetails;
import org.verapdf.cli.commands.FormatType;
import org.verapdf.cli.commands.VeraCliArgParser;
import org.verapdf.core.ProfileException;
import org.verapdf.core.ValidationException;
import org.verapdf.features.pb.PBFeatureParser;
import org.verapdf.features.tools.FeaturesCollection;
import org.verapdf.model.ModelParser;
import org.verapdf.pdfa.PDFAValidator;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.results.ValidationResults;
import org.verapdf.pdfa.validation.ProfileDirectory;
import org.verapdf.pdfa.validation.Profiles;
import org.verapdf.pdfa.validation.ValidationProfile;
import org.verapdf.pdfa.validators.Validators;
import org.verapdf.report.FeaturesReport;
import org.verapdf.report.MachineReadableReport;
import org.verapdf.validation.profile.parser.LegacyProfileConverter;

import com.beust.jcommander.JCommander;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
public final class VeraPdfCli {
    private static final Logger LOGGER = Logger.getLogger(VeraPdfCli.class);
    private static final String APP_NAME = "veraPDF";
    private static final ReleaseDetails RELEASE_DETAILS = ReleaseDetails
            .getInstance();
    private static final String FLAVOURS_HEADING = APP_NAME
            + " supported PDF/A profiles:";
    private static final ProfileDirectory PROFILES = Profiles
            .getVeraProfileDirectory();

    private VeraPdfCli() {
        // disable default constructor
    }

    /**
     * Main CLI entry point, process the command line arguments
     *
     * @param args
     *            Java.lang.String array of command line args, to be processed
     *            using Apache commons CLI.
     */
    public static void main(final String[] args) {
        VeraCliArgParser cliArgParser = new VeraCliArgParser();
        JCommander jCommander = new JCommander(cliArgParser);
        jCommander.setProgramName(APP_NAME);

        try {
            jCommander.parse(args);
        } catch (Exception e) {
            logThrowableAndExit(e, "Couldn't parse parameters.", 1);
        }
        if (args.length == 0 || cliArgParser.isHelp()) {
            showVersionInfo();
            jCommander.usage();
            System.exit(0);
        }

        messagesFromParser(cliArgParser);
        processPaths(cliArgParser);
    }

    private static void processPaths(
            final VeraCliArgParser argParser) {
        ValidationProfile profile = profileFromInput(argParser);
        if (argParser.getFormat() == FormatType.XML) {
            processPathsVerbose(profile, argParser);
        } else {
            processPathsRaw(profile, argParser);
        }
    }

    private static void processPathsRaw(final ValidationProfile profile,
            final VeraCliArgParser argParser) {
        PDFAValidator validator = Validators.createValidator(profile,
                argParser.logPassed());
        for (String pathToValidate : argParser.getPathsToValidate()) {
            try (ModelParser parser = new ModelParser(new FileInputStream(
                    pathToValidate))) {
                ValidationResult result = validator.validate(parser);
                if (argParser.extractFeatures()) {
                    FeaturesCollection features = PBFeatureParser
                            .getFeaturesCollection(parser.getPDDocument());
                    FeaturesReport featuresReport = FeaturesReport
                            .fromValues(features);
                    FeaturesReport.toXml(featuresReport, System.out,
                            Boolean.TRUE);
                }
                ValidationResults.toXml(result, System.out, Boolean.TRUE);
            } catch (FileNotFoundException e) {
                logThrowable(e, "Could not find file: " + pathToValidate);
            } catch (IOException e) {
                logThrowable(e, "Could not read file: " + pathToValidate);
            } catch (ValidationException | JAXBException e) {
                logThrowable(e, "Exception thrown validating file: "
                        + pathToValidate);
            }
        }
    }

    private static void processPathsVerbose(final ValidationProfile profile,
            final VeraCliArgParser argParser) {
        PDFAValidator validator = Validators.createValidator(profile, true);
        for (String pathToValidate : argParser.getPathsToValidate()) {
            long start = System.currentTimeMillis();
            try (ModelParser parser = new ModelParser(new FileInputStream(
                    pathToValidate))) {
                ValidationResult result = validator.validate(parser);
                FeaturesCollection features = null;
                if (argParser.extractFeatures()) {
                    features = PBFeatureParser.getFeaturesCollection(parser
                            .getPDDocument());
                }
                MachineReadableReport report = MachineReadableReport
                        .fromValues(profile, result, argParser.logPassed(),
                                null, features, System.currentTimeMillis()
                                        - start);
                if (argParser.getFormat() == FormatType.MRR)
                    MachineReadableReport.toXml(report, System.out,
                            Boolean.TRUE);

            } catch (FileNotFoundException e) {
                logThrowable(e, "Could not find file: " + pathToValidate);
            } catch (IOException e) {
                logThrowable(e, "Could not read file: " + pathToValidate);
            } catch (ValidationException | JAXBException e) {
                logThrowable(e, "Exception thrown validating file: "
                        + pathToValidate);
            }
        }
    }

    private static ValidationProfile profileFromInput(
            final VeraCliArgParser argParser) {
        if (argParser.getProfile() == null) {
            return PROFILES.getValidationProfileByFlavour(argParser
                    .getFlavour());
        }
        // Try as a file
        try {
            return Profiles.profileFromXml(new FileInputStream(argParser
                    .getProfile()));
        } catch (JAXBException | IOException e) {
            LOGGER.warn(
                    "Couldn't parse profile, trying legacy profile parser.", e);
            // Do nothing as it's a parse error so try from legacy profile nex
        }

        try (InputStream toParse = new FileInputStream(argParser.getProfile())) {
            return LegacyProfileConverter.fromLegacyStream(toParse,
                    PDFAFlavour.NO_FLAVOUR);
        } catch (ProfileException | IOException e) {
            logThrowableAndExit(e, "ProfileException parsing: "
                    + argParser.getProfile().getName(), 1);
        }
        return Profiles.defaultProfile();
    }

    private static void logThrowableAndExit(final Throwable cause,
            final String message, final int retVal) {
        logThrowable(cause, message);
        System.exit(retVal);
    }

    private static void logThrowable(final Throwable cause, final String message) {
        LOGGER.fatal(message, cause);
        return;
    }

    private static void messagesFromParser(final VeraCliArgParser parser) {

        if (parser.listProfiles()) {
            listProfiles();
        }

        if (parser.showVersion()) {
            showVersionInfo();
        }
    }

    private static void listProfiles() {
        System.out.println(FLAVOURS_HEADING);
        for (ValidationProfile profile : PROFILES.getValidationProfiles()) {
            System.out.println("  " + profile.getPDFAFlavour().getId());
            System.out.println();
        }
    }

    private static void showVersionInfo() {
        System.out.println("Version: " + RELEASE_DETAILS.getVersion());
        System.out.println("Built: " + RELEASE_DETAILS.getBuildDate());
        System.out.println(RELEASE_DETAILS.getRights());
        System.out.println();
    }
}
