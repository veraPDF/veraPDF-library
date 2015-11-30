/**
 * 
 */
package org.verapdf.cli;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.verapdf.cli.commands.FormatOption;
import org.verapdf.cli.commands.VeraCliArgParser;
import org.verapdf.core.ProfileException;
import org.verapdf.model.ModelParser;
import org.verapdf.pdfa.PDFAValidator;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.validation.Profiles;
import org.verapdf.pdfa.validation.ValidationProfile;
import org.verapdf.pdfa.validators.Validators;
import org.verapdf.validation.profile.parser.LegacyProfileConverter;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
final class OutputFormatter {
    private final FormatOption format;
    private final ValidationProfile profile;
    private final boolean extractFeatures;
    private final boolean logPassed;

    private OutputFormatter() throws ProfileException, FileNotFoundException, IOException {
        this(new VeraCliArgParser());
    }

    private OutputFormatter(final VeraCliArgParser args) throws ProfileException, FileNotFoundException, IOException {
        this.format = args.getFormat();
        this.profile = profileFromArgs(args);
        this.extractFeatures = args.extractFeatures();
        this.logPassed = args.logPassed();
    }
    
    public void processPaths(final List<String> pdfPaths) {
        for (String pdfPath : pdfPaths) {
            try (ModelParser parser = new ModelParser(new FileInputStream(pdfPath))) {
                
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    public static OutputFormatter createProcessorFromArgs(
            final VeraCliArgParser args) throws ProfileException, FileNotFoundException, IOException {
        return new OutputFormatter(args);
    }

    private static ValidationProfile profileFromArgs(final VeraCliArgParser args)
            throws ProfileException, FileNotFoundException, IOException {
        if (args.getProfileFile() == null) {
            return (args.getFlavour() == PDFAFlavour.NO_FLAVOUR) ? Profiles
                    .defaultProfile() : Profiles.getVeraProfileDirectory()
                    .getValidationProfileByFlavour(args.getFlavour());
        }
        // Try as a file
        try {
            return Profiles.profileFromXml(new FileInputStream(args
                    .getProfileFile()));
        } catch (JAXBException | IOException e) {
            // Do nothing as it's a parse error so try from legacy profile nex
        }

        try (InputStream toParse = new FileInputStream(args.getProfileFile())) {
            return LegacyProfileConverter.fromLegacyStream(toParse,
                    PDFAFlavour.NO_FLAVOUR);
        }
    }
}
