/**
 * 
 */
package org.verapdf.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.verapdf.cli.commands.VeraCliArgParser;
import org.verapdf.core.ProfileException;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.validation.ProfileDirectory;
import org.verapdf.pdfa.validation.Profiles;
import org.verapdf.pdfa.validation.ValidationProfile;
import org.verapdf.validation.profile.parser.LegacyProfileConverter;

import com.beust.jcommander.JCommander;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
@SuppressWarnings("static-method")
public class VeraPdfCliProcessorProfileTest {

    /**
     * Test method for
     * {@link org.verapdf.cli.VeraPdfCliProcessor#createProcessorFromArgs(org.verapdf.cli.commands.VeraCliArgParser)}
     * .
     * 
     * @throws IOException
     * @throws FileNotFoundException
     * @throws ProfileException
     * @throws JAXBException
     */
    @Test
    public final void testCreateProcessorFromArgsNewProfile()
            throws ProfileException, FileNotFoundException, IOException,
            JAXBException {
        VeraCliArgParser parser = new VeraCliArgParser();
        JCommander jCommander = VeraPdfCliProcessorTest
                .initialiseJCommander(parser);
        jCommander.parse(new String[] {});
        VeraPdfCliProcessor proc = VeraPdfCliProcessor
                .createProcessorFromArgs(parser);
        assertTrue(proc.validator.getProfile().getPDFAFlavour() == PDFAFlavour.PDFA_1_B);
        ProfileDirectory directory = Profiles.getVeraProfileDirectory();
        assertTrue(directory.getValidationProfiles().size() > 0);
        for (ValidationProfile profile : directory.getValidationProfiles()) {
            File tmpProfile = File.createTempFile("verapdf", "profile");
            try (OutputStream os = new FileOutputStream(tmpProfile)) {
                Profiles.profileToXml(profile, os, Boolean.FALSE);
                testWithProfileFile(profile.getPDFAFlavour(), tmpProfile);
            }
        }
    }

    /**
     * Test method for
     * {@link org.verapdf.cli.VeraPdfCliProcessor#createProcessorFromArgs(org.verapdf.cli.commands.VeraCliArgParser)}
     * .
     * 
     * @throws IOException
     * @throws FileNotFoundException
     * @throws ProfileException
     * @throws JAXBException
     * @throws URISyntaxException
     */
    @Test
    public final void testCreateProcessorFromArgsOldProfile()
            throws ProfileException, FileNotFoundException, IOException,
            JAXBException, URISyntaxException {
        VeraCliArgParser parser = new VeraCliArgParser();
        JCommander jCommander = VeraPdfCliProcessorTest
                .initialiseJCommander(parser);
        jCommander.parse(new String[] {});
        for (File profileFile : getProfiles()) {
            testWithProfileFile(PDFAFlavour.fromString(profileFile.getName()),
                    profileFile);
        }
    }

    private static void testWithProfileFile(final PDFAFlavour flavour,
            final File profileFile) throws ProfileException,
            FileNotFoundException, IOException, JAXBException {
        String[] argVals = new String[] { "-p", "--profile" };
        VeraCliArgParser parser = new VeraCliArgParser();
        JCommander jCommander = VeraPdfCliProcessorTest
                .initialiseJCommander(parser);
        for (String arg : argVals) {
            jCommander
                    .parse(new String[] { arg, profileFile.getAbsolutePath() });
            VeraPdfCliProcessor proc = VeraPdfCliProcessor
                    .createProcessorFromArgs(parser);
            try (InputStream is = new FileInputStream(profileFile)) {
                ValidationProfile profile = Profiles.profileFromXml(is);
                if (profile.equals(Profiles.defaultProfile())
                        || (profile.getHexSha1Digest()
                                .equals("sha-1 hash code"))) {
                    try (InputStream lis = new FileInputStream(profileFile)) {
                        profile = LegacyProfileConverter.fromLegacyStream(lis);
                    }
                }
                assertEquals(flavour, proc.validator.getProfile()
                        .getPDFAFlavour());
                assertTrue(profile != proc.validator.getProfile());
                assertEquals(
                        Profiles.profileToXml(profile, Boolean.TRUE)
                                + "\n"
                                + Profiles.profileToXml(
                                        proc.validator.getProfile(),
                                        Boolean.TRUE), profile.getRules(),
                        proc.validator.getProfile().getRules());
            }
        }
    }

    private static File[] getProfiles() throws URISyntaxException {
        URL resourceUrl = ClassLoader.class
                .getResource("/org/verapdf/profiles");
        File file = new File(resourceUrl.toURI());
        return file.listFiles();
    }
}
