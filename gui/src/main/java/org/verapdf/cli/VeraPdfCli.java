/**
 *
 */
package org.verapdf.cli;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;

import org.verapdf.ReleaseDetails;
import org.verapdf.cli.commands.VeraCliArgParser;
import org.verapdf.core.ProfileException;
import org.verapdf.core.ValidationException;
import org.verapdf.model.ModelLoader;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.results.ValidationResults;
import org.verapdf.pdfa.validation.ProfileDirectory;
import org.verapdf.pdfa.validation.Profiles;
import org.verapdf.pdfa.validation.ValidationProfile;
import org.verapdf.pdfa.validation.Validator;
import org.verapdf.validation.profile.parser.LegacyProfileConverter;
import org.verapdf.validation.profile.parser.ValidationProfileParser;
import org.xml.sax.SAXException;

import com.beust.jcommander.JCommander;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
public final class VeraPdfCli {
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
        } catch (Throwable t) {
            logThrowableAndExit(t, "Couldn't parse parameters.", 1);
        }
        messagesFromParser(jCommander, cliArgParser);
        processPaths(cliArgParser.getPathsToValidate(),
                profileFromInput(cliArgParser.getProfile()));

    }

    private static ValidationProfile profileFromInput(String userInput) {
        PDFAFlavour flavour = PDFAFlavour.byFlavourId(userInput);
        if (flavour != PDFAFlavour.NO_FLAVOUR) {
            return PROFILES.getValidationProfileByFlavour(flavour);
        }
        Path profilePath = Paths.get(userInput);
        URL profileUrl = null;
        try {
            profileUrl = profilePath.toUri().toURL();
            return tryProfileFromURL(profileUrl);
        } catch (IOException e) {
            logThrowableAndExit(e, "Couldn't decode or parse a path from: "
                    + userInput, 1);
        } catch (JAXBException e) {
            // Do nothing as it's a parse error so try from legacy profile nex
        }
        try {
            return tryLegacyProfileFromStream(profileUrl);
        } catch (ProfileException | ParserConfigurationException | SAXException
                | IOException | XMLStreamException e) {
            logThrowableAndExit(e, "Couldn't decode or parse a path from: "
                    + userInput, 1);
        }
        return Profiles.defaultProfile();
    }

    private static ValidationProfile tryProfileFromURL(final URL url)
            throws IOException, JAXBException {
        try (InputStream is = url.openStream()) {
            return Profiles.profileFromXml(is);
        }
    }

    private static ValidationProfile tryLegacyProfileFromStream(final URL url)
            throws ProfileException, ParserConfigurationException,
            SAXException, IOException, XMLStreamException {
        try (InputStream is = url.openStream()) {
            return LegacyProfileConverter.fromLegacyProfile(
                    ValidationProfileParser.parseFromStream(is, false),
                    PDFAFlavour.NO_FLAVOUR);
        }
    }

    private static void logThrowableAndExit(final Throwable cause,
            final String message, final int retVal) {
        System.err.println(message);
        cause.printStackTrace();
        System.exit(retVal);
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

    private static void processPaths(final List<String> paths,
            final ValidationProfile profile) {
        for (String pathToValidate : paths) {
            try (ModelLoader loader = new ModelLoader(new FileInputStream(
                    pathToValidate))) {
                ValidationResult result = Validator.validate(profile,
                        loader.getRoot(), false);
                ValidationResults.toXml(result, System.out, Boolean.TRUE);
            } catch (FileNotFoundException e) {
                System.err.println("Could not find file:" + pathToValidate);
            } catch (IOException e) {
                System.err.println("IOException validating file:"
                        + pathToValidate);
                e.printStackTrace();
            } catch (ValidationException | JAXBException e) {
                System.err.println("ValidationException validating file:"
                        + pathToValidate);
                e.printStackTrace();
            }
        }
    }
}