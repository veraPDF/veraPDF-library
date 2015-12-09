/**
 * 
 */
package org.verapdf.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;
import org.verapdf.cli.commands.FormatOption;
import org.verapdf.cli.commands.VeraCliArgParser;
import org.verapdf.core.ProfileException;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.validation.ProfileDirectory;
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
        ProfileDirectory directory = Profiles.getVeraProfileDirectory();
        assertTrue(directory.getValidationProfiles().size() > 0);
        for (String argVal : argVals) {
            for (ValidationProfile profile : directory
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

    static final JCommander initialiseJCommander(final VeraCliArgParser parser) {
        JCommander jCommander = new JCommander(parser);
        jCommander.setProgramName(APP_NAME);
        jCommander.setAllowParameterOverwriting(true);
        return jCommander;
    }

}
