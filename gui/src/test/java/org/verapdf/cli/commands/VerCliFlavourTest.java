/**
 * 
 */
package org.verapdf.cli.commands;

import static org.junit.Assert.*;

import org.junit.Test;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
@SuppressWarnings("static-method")
public class VerCliFlavourTest {


    /**
     * Test method for
     * {@link org.verapdf.cli.commands.VeraCliArgParser#getFlavour()}.
     */
    @Test
    public final void testGetFlavourDefault() {
        // Test default is 1b
        assertTrue(VeraCliArgParser.DEFAULT_ARGS.getFlavour() == PDFAFlavour.PDFA_1_B);

        // Test empty String[] args doesn't change that
        VeraCliArgParser parser = new VeraCliArgParser();
        JCommander jCommander = VeraCliArgParserTest.initialiseJCommander(parser);
        jCommander.parse(new String[] {});
        assertTrue(parser.getFlavour() == VeraCliArgParser.DEFAULT_ARGS
                .getFlavour());

        // Test other flags & options doesn't change that
        parser = new VeraCliArgParser();
        jCommander = VeraCliArgParserTest.initialiseJCommander(parser);
        jCommander.parse(new String[] { "-l", "--version", "--profile", "-h" });
        assertTrue(parser.getFlavour() == VeraCliArgParser.DEFAULT_ARGS
                .getFlavour());
    }

    /**
     * Test method for
     * {@link org.verapdf.cli.commands.VeraCliArgParser#getFlavour()}.
     */
    @Test(expected=ParameterException.class)
    public final void testGetFlavourNoFlag() {
        VeraCliArgParser parser = new VeraCliArgParser();
        JCommander jCommander = VeraCliArgParserTest.initialiseJCommander(parser);

        // Test flag works
        parser = new VeraCliArgParser();
        jCommander = VeraCliArgParserTest.initialiseJCommander(parser);
        jCommander.parse(new String[] { "-f" });
        assertTrue(parser.getFlavour() == PDFAFlavour.NO_FLAVOUR);
    }

    /**
     * Test method for
     * {@link org.verapdf.cli.commands.VeraCliArgParser#getFlavour()}.
     */
    @Test(expected=ParameterException.class)
    public final void testGetFlavourNoFlagMultiParam() {
        VeraCliArgParser parser = new VeraCliArgParser();
        JCommander jCommander = VeraCliArgParserTest.initialiseJCommander(parser);

        // Test flag works
        parser = new VeraCliArgParser();
        jCommander = VeraCliArgParserTest.initialiseJCommander(parser);
        jCommander.parse(new String[] { "-f", "-h" });
        assertTrue(parser.getFlavour() == PDFAFlavour.NO_FLAVOUR);
    }

    /**
     * Test method for
     * {@link org.verapdf.cli.commands.VeraCliArgParser#getFlavour()}.
     */
    @Test(expected=ParameterException.class)
    public final void testGetFlavourNoOption() {
        VeraCliArgParser parser = new VeraCliArgParser();
        JCommander jCommander = VeraCliArgParserTest.initialiseJCommander(parser);

        // Test flag works
        parser = new VeraCliArgParser();
        jCommander = VeraCliArgParserTest.initialiseJCommander(parser);
        jCommander.parse(new String[] { "--flavour" });
        assertTrue(parser.getFlavour() == PDFAFlavour.NO_FLAVOUR);

    }

    /**
     * Test method for
     * {@link org.verapdf.cli.commands.VeraCliArgParser#getFlavour()}.
     */
    @Test
    public final void testGetFlavourFlagOff() {
        VeraCliArgParser parser = new VeraCliArgParser();
        JCommander jCommander = VeraCliArgParserTest.initialiseJCommander(parser);

        // Test flag works
        parser = new VeraCliArgParser();
        jCommander = VeraCliArgParserTest.initialiseJCommander(parser);
        jCommander.parse(new String[] { "-f", "0" });
        assertTrue(parser.getFlavour() == PDFAFlavour.NO_FLAVOUR);

        // Test flag works with other options & flags
        parser = new VeraCliArgParser();
        jCommander = VeraCliArgParserTest.initialiseJCommander(parser);
        jCommander.parse(new String[] { "-l", "--format", "-f", "0"});
        assertTrue(parser.getFlavour() == PDFAFlavour.NO_FLAVOUR);
    }

    /**
     * Test method for
     * {@link org.verapdf.cli.commands.VeraCliArgParser#getFlavour()}.
     */
    @Test
    public final void testGetFlavourOptionOff() {
        VeraCliArgParser parser = new VeraCliArgParser();
        JCommander jCommander = VeraCliArgParserTest.initialiseJCommander(parser);

        // Test flag works
        parser = new VeraCliArgParser();
        jCommander = VeraCliArgParserTest.initialiseJCommander(parser);
        jCommander.parse(new String[] { "--flavour", "0" });
        assertTrue("parser.getFlavour()=" + parser.getFlavour(), parser.getFlavour() == PDFAFlavour.NO_FLAVOUR);

        // Test flag works with other options & flags
        parser = new VeraCliArgParser();
        jCommander = VeraCliArgParserTest.initialiseJCommander(parser);
        jCommander.parse(new String[] { "--list", "--flavour", "0", "-h"});
        assertTrue("parser.getFlavour()=" + parser.getFlavour(), parser.getFlavour() == PDFAFlavour.NO_FLAVOUR);
    }

    /**
     * Test method for
     * {@link org.verapdf.cli.commands.VeraCliArgParser#getFlavour()}.
     */
    @Test
    public final void testGetFlavour() {
        fail("Not yet implemented"); // TODO
    }


}
