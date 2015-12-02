/**
 * 
 */
package org.verapdf.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.verapdf.cli.commands.FormatOption;
import org.verapdf.cli.commands.VeraCliArgParser;
import org.verapdf.core.ProfileException;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.validation.Profiles;
import org.verapdf.pdfa.validation.ValidationProfile;

import com.beust.jcommander.JCommander;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
@SuppressWarnings("static-method")
public class VeraPdfCliProcessorTest {
    private final static String APP_NAME = VeraPdfCliProcessorTest.class
            .getName();

    /**
     * Test method for
     * {@link org.verapdf.cli.VeraPdfCliProcessor#createProcessorFromArgs(org.verapdf.cli.commands.VeraCliArgParser)}
     * .
     * 
     * @throws IOException
     * @throws FileNotFoundException
     * @throws ProfileException
     */
    @Test
    public final void testCreateProcessorFromArgsFormat()
            throws ProfileException, FileNotFoundException, IOException {
        VeraCliArgParser parser = new VeraCliArgParser();
        JCommander jCommander = initialiseJCommander(parser);
        jCommander.parse(new String[] {});
        VeraPdfCliProcessor proc = VeraPdfCliProcessor
                .createProcessorFromArgs(parser);
        assertTrue(proc.format == FormatOption.XML);
        for (FormatOption format : FormatOption.values()) {
            parser = new VeraCliArgParser();
            jCommander = initialiseJCommander(parser);
            jCommander.parse(new String[] { "--format", format.getOption() });
            proc = VeraPdfCliProcessor.createProcessorFromArgs(parser);
            assertTrue(proc.format == format);
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
     */
    @Test
    public final void testCreateProcessorFromArgsLogPassed()
            throws ProfileException, FileNotFoundException, IOException {
        String[] argVals = new String[] { "--passed", "--success" };
        VeraCliArgParser parser = new VeraCliArgParser();
        JCommander jCommander = initialiseJCommander(parser);
        jCommander.parse(new String[] {});
        VeraPdfCliProcessor proc = VeraPdfCliProcessor
                .createProcessorFromArgs(parser);
        assertFalse(proc.logPassed);

        for (String argVal : argVals) {
            jCommander.parse(new String[] { argVal });
            proc = VeraPdfCliProcessor.createProcessorFromArgs(parser);
            assertTrue(proc.logPassed);
            parser = new VeraCliArgParser();
            jCommander = initialiseJCommander(parser);
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
     */
    @Test
    public final void testCreateProcessorFromArgsExtract()
            throws ProfileException, FileNotFoundException, IOException {
        String[] argVals = new String[] { "-x", "--extract" };
        VeraCliArgParser parser = new VeraCliArgParser();
        JCommander jCommander = initialiseJCommander(parser);
        jCommander.parse(new String[] {});
        VeraPdfCliProcessor proc = VeraPdfCliProcessor
                .createProcessorFromArgs(parser);
        assertFalse(proc.extractFeatures);
        for (String argVal : argVals) {
            jCommander.parse(new String[] { argVal });
            proc = VeraPdfCliProcessor.createProcessorFromArgs(parser);
            assertTrue(proc.extractFeatures);
            parser = new VeraCliArgParser();
            jCommander = initialiseJCommander(parser);
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
     */
    @Test
    public final void testCreateProcessorFromArgsFlavour()
            throws ProfileException, FileNotFoundException, IOException {
        String[] argVals = new String[] { "-f", "--flavour" };
        VeraCliArgParser parser = new VeraCliArgParser();
        JCommander jCommander = initialiseJCommander(parser);
        jCommander.parse(new String[] {});
        VeraPdfCliProcessor proc = VeraPdfCliProcessor
                .createProcessorFromArgs(parser);
        assertTrue(proc.validator.getProfile().getPDFAFlavour() == PDFAFlavour.PDFA_1_B);
        for (String argVal : argVals) {
            for (ValidationProfile profile : Profiles.getVeraProfileDirectory()
                    .getValidationProfiles()) {
                jCommander.parse(new String[] { argVal,
                        profile.getPDFAFlavour().getId() });
                proc = VeraPdfCliProcessor.createProcessorFromArgs(parser);
                assertTrue(proc.validator.getProfile() == profile);
                parser = new VeraCliArgParser();
                jCommander = initialiseJCommander(parser);
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
     */
    @Test
    public final void testCreateProcessorFromArgsNoFlavour()
            throws ProfileException, FileNotFoundException, IOException {
        String[] argVals = new String[] { "-f", "--flavour" };
        VeraCliArgParser parser = new VeraCliArgParser();
        JCommander jCommander = initialiseJCommander(parser);
        jCommander.parse(new String[] {});
        VeraPdfCliProcessor proc = VeraPdfCliProcessor
                .createProcessorFromArgs(parser);
        assertTrue(proc.validator.getProfile().getPDFAFlavour() == PDFAFlavour.PDFA_1_B);
        for (String argVal : argVals) {
            jCommander.parse(new String[] { argVal,
                    PDFAFlavour.NO_FLAVOUR.getId() });
            proc = VeraPdfCliProcessor.createProcessorFromArgs(parser);
            assertTrue(proc.validator == null);
            parser = new VeraCliArgParser();
            jCommander = initialiseJCommander(parser);
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
     */
    @Test
    public final void testCreateProcessorFromArgsProfile()
            throws ProfileException, FileNotFoundException, IOException, JAXBException {
        VeraCliArgParser parser = new VeraCliArgParser();
        JCommander jCommander = initialiseJCommander(parser);
        jCommander.parse(new String[] {});
        VeraPdfCliProcessor proc = VeraPdfCliProcessor
                .createProcessorFromArgs(parser);
        assertTrue(proc.validator.getProfile().getPDFAFlavour() == PDFAFlavour.PDFA_1_B);

        for (ValidationProfile profile : Profiles.getVeraProfileDirectory()
                .getValidationProfiles()) {
            File tmpProfile = File.createTempFile("verapdf", "profile");
            try (OutputStream os = new FileOutputStream(tmpProfile)) {
                Profiles.profileToXml(profile, os,
                        Boolean.FALSE);
                testWithProfileFile(tmpProfile);
            }
        }
    }
    
    private static void testWithProfileFile(final File profileFile) throws ProfileException, FileNotFoundException, IOException, JAXBException {
        String[] argVals = new String[] { "-p", "--profile" };
        VeraCliArgParser parser = new VeraCliArgParser();
        JCommander jCommander = initialiseJCommander(parser);
        for (String arg : argVals) {
            jCommander.parse(new String[] {arg, profileFile.getAbsolutePath()});
            VeraPdfCliProcessor proc = VeraPdfCliProcessor.createProcessorFromArgs(parser);
            try (InputStream is = new FileInputStream(profileFile)) {
                ValidationProfile profile = Profiles.profileFromXml(is);
                assertTrue(profile != proc.validator.getProfile());
                assertEquals(profile, proc.validator.getProfile());
            }
        }

    }

    static final JCommander initialiseJCommander(final VeraCliArgParser parser) {
        JCommander jCommander = new JCommander(parser);
        jCommander.setProgramName(APP_NAME);
        jCommander.setAllowParameterOverwriting(true);
        return jCommander;
    }

}
