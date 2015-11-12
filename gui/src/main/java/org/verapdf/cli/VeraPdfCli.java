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
import org.verapdf.model.ModelParser;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.results.ValidationResults;
import org.verapdf.pdfa.validation.ProfileDirectory;
import org.verapdf.pdfa.validation.Profiles;
import org.verapdf.pdfa.validation.ValidationProfile;
import org.verapdf.pdfa.validation.Validator;
import org.verapdf.validation.profile.parser.LegacyProfileConverter;

import javax.xml.bind.JAXBException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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
            LOGGER.fatal("Couldn't parse parameters.", e);
			return;
        }
        if (!messagesFromParser(jCommander, cliArgParser)) {
			ValidationProfile profile = profileFromInput(cliArgParser.getProfile());
			if (profile != null) {
				processPaths(cliArgParser.getPathsToValidate(), profile);
			}
		}

    }
    private static void processPaths(final List<String> paths,
            final ValidationProfile profile) {
        for (String pathToValidate : paths) {
            try (InputStream fis = new FileInputStream(pathToValidate)) {
                ValidationResults.toXml(validate(fis, profile), System.out, Boolean.TRUE);
            } catch (FileNotFoundException e) {
				LOGGER.fatal("Could not find file: " + pathToValidate, e);
            } catch (IOException e) {
				LOGGER.fatal("Could not read file: " + pathToValidate, e);
            } catch (ValidationException | JAXBException e) {
                LOGGER.fatal("Exception thrown validating file: " + pathToValidate, e);
            }
        }
    }

    private static ValidationResult validate(final InputStream toValidate,
            final ValidationProfile profile) throws IOException, ValidationException {
        try (ModelParser parser = new ModelParser(toValidate)) {
                return Validator.validate(profile,
                        parser.getRoot(), false);
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
            LOGGER.warn("Couldn't parse profile, trying legacy profile parser.", e);
            // Do nothing as it's a parse error so try from legacy profile nex
        }

        try (InputStream toParse = new FileInputStream(userInput)) {
            return LegacyProfileConverter.fromLegacyStream(toParse, PDFAFlavour.NO_FLAVOUR);
        } catch (ProfileException | IOException e) {
            LOGGER.fatal("ProfileException parsing: "+ userInput, e);
			return null;
        }
    }

    private static boolean messagesFromParser(final JCommander jCommander,
            final VeraCliArgParser parser) {
        if (parser.isHelp()) {
            showVersionInfo();
            jCommander.usage();
            return true;
        }

        if (parser.listProfiles()) {
            listProfiles();
        }

        if (parser.showVersion()) {
            showVersionInfo();
        }
		return false;
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