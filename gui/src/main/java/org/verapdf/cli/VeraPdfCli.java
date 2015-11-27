/**
 *
 */
package org.verapdf.cli;

import com.beust.jcommander.JCommander;
import org.apache.log4j.Logger;
import org.verapdf.ReleaseDetails;
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

import javax.xml.bind.JAXBException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

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

        if (args.length == 0) {
            showVersionInfo();
            jCommander.usage();
            System.exit(0);
        }
        try {
            jCommander.parse(args);
        } catch (Exception e) {
            logThrowableAndExit(e, "Couldn't parse parameters.", 1);
        }
        messagesFromParser(jCommander, cliArgParser);
        processPaths(profileFromInput(cliArgParser.getProfile()), cliArgParser);
    }

    private static void processPaths(final ValidationProfile profile,
            final VeraCliArgParser argParser) {
        if (argParser.isVerbose()) {
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
                        .fromValues(profile, result, argParser.logPassed(), -1,
                                null, features, System.currentTimeMillis()
                                        - start);
                MachineReadableReport.toXml(report, System.out, Boolean.TRUE);
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

    private static ValidationProfile profileFromInput(String userInput) {
        PDFAFlavour flavour = PDFAFlavour.byFlavourId(userInput);
        if (flavour != PDFAFlavour.NO_FLAVOUR) {
            return PROFILES.getValidationProfileByFlavour(flavour);
        }
        // Try as a file
        try {
            return Profiles.profileFromXml(new FileInputStream(userInput));
        } catch (JAXBException | IOException e) {
            LOGGER.warn(
                    "Couldn't parse profile, trying legacy profile parser.", e);
            // Do nothing as it's a parse error so try from legacy profile nex
        }

        try (InputStream toParse = new FileInputStream(userInput)) {
            return LegacyProfileConverter.fromLegacyStream(toParse,
                    PDFAFlavour.NO_FLAVOUR);
        } catch (ProfileException | IOException e) {
            logThrowableAndExit(e, "ProfileException parsing: " + userInput, 1);
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

    private static void messagesFromParser(final JCommander jCommander,
            final VeraCliArgParser parser) {
        if (parser.isHelp()) {
            showVersionInfo();
            jCommander.usage();
            System.exit(0);
        }

        if (parser.listProfiles()) {
            listProfiles();
        }

        if (parser.showVersion()) {
            showVersionInfo();
        }
    }

    private static void listProfiles() {
        for (ValidationProfile profile : PROFILES.getValidationProfiles()) {
            System.out.println(FLAVOURS_HEADING);
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
