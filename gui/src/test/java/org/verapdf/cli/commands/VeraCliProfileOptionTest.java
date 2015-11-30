/**
 * 
 */
package org.verapdf.cli.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.Test;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
@SuppressWarnings("static-method")
public class VeraCliProfileOptionTest {

    /**
     * Test method for
     * {@link org.verapdf.cli.commands.VeraCliArgParser#getProfileFile()}.
     */
    @Test
    public final void testGetProfileFileDefault() {
        assertTrue(VeraCliArgParser.DEFAULT_ARGS.getProfileFile() == null);

        // Test empty String[] args doesn't change that
        VeraCliArgParser parser = new VeraCliArgParser();
        JCommander jCommander = VeraCliArgParserTest.initialiseJCommander(parser);
        jCommander.parse(new String[] {});
        assertTrue(parser.getProfileFile() == VeraCliArgParser.DEFAULT_ARGS
                .getProfileFile());

        // Test other flags & options doesn't change that
        parser = new VeraCliArgParser();
        jCommander = VeraCliArgParserTest.initialiseJCommander(parser);
        jCommander.parse(new String[] { "-l", "--flavour", "1B", "-h" });
        assertTrue(parser.getProfileFile() == VeraCliArgParser.DEFAULT_ARGS
                .getProfileFile());
    }

    /**
     * Test method for
     * {@link org.verapdf.cli.commands.VeraCliArgParser#getProfileFile()}.
     */
    @Test(expected = ParameterException.class)
    public final void testGetProfileFileFlagEmpty() {
        VeraCliArgParser parser = new VeraCliArgParser();
        JCommander jCommander = VeraCliArgParserTest.initialiseJCommander(parser);

        // Test flag works
        parser = new VeraCliArgParser();
        jCommander = VeraCliArgParserTest.initialiseJCommander(parser);
        jCommander.parse(new String[] { "-p" });
    }

    /**
     * Test method for
     * {@link org.verapdf.cli.commands.VeraCliArgParser#getProfileFile()}.
     */
    @Test(expected = ParameterException.class)
    public final void testGetProfileFileFlagEmptyFollowing() {
        VeraCliArgParser parser = new VeraCliArgParser();
        JCommander jCommander = VeraCliArgParserTest.initialiseJCommander(parser);

        // Test flag works
        parser = new VeraCliArgParser();
        jCommander = VeraCliArgParserTest.initialiseJCommander(parser);
        jCommander.parse(new String[] { "-p", "-h" });
    }

    /**
     * Test method for
     * {@link org.verapdf.cli.commands.VeraCliArgParser#getProfileFile()}.
     */
    @Test(expected = ParameterException.class)
    public final void testGetProfileFileOptionEmpty() {
        VeraCliArgParser parser = new VeraCliArgParser();
        JCommander jCommander = VeraCliArgParserTest.initialiseJCommander(parser);

        // Test flag works
        parser = new VeraCliArgParser();
        jCommander = VeraCliArgParserTest.initialiseJCommander(parser);
        jCommander.parse(new String[] { "--profile" });
    }

    /**
     * Test method for
     * {@link org.verapdf.cli.commands.VeraCliArgParser#getProfileFile()}.
     */
    @Test(expected = ParameterException.class)
    public final void testGetProfileFileOptionEmptyFollowing() {
        VeraCliArgParser parser = new VeraCliArgParser();
        JCommander jCommander = VeraCliArgParserTest.initialiseJCommander(parser);

        // Test flag works
        parser = new VeraCliArgParser();
        jCommander = VeraCliArgParserTest.initialiseJCommander(parser);
        jCommander.parse(new String[] { "--profile" , "-h"});
    }

    /**
     * Test method for
     * {@link org.verapdf.cli.commands.VeraCliArgParser#getProfileFile()}.
     */
    @Test(expected = ParameterException.class)
    public final void testGetProfileFileFlagNotFile() {
        VeraCliArgParser parser = new VeraCliArgParser();
        JCommander jCommander = VeraCliArgParserTest.initialiseJCommander(parser);

        // Test flag works
        parser = new VeraCliArgParser();
        jCommander = VeraCliArgParserTest.initialiseJCommander(parser);
        jCommander.parse(new String[] { "-p", "*"});
        assertFalse(parser.getProfileFile() == VeraCliArgParser.DEFAULT_ARGS
                .getProfileFile());
    }

    /**
     * Test method for
     * {@link org.verapdf.cli.commands.VeraCliArgParser#getProfileFile()}.
     */
    @Test(expected = ParameterException.class)
    public final void testGetProfileFileOptionNotFile() {
        VeraCliArgParser parser = new VeraCliArgParser();
        JCommander jCommander = VeraCliArgParserTest.initialiseJCommander(parser);

        // Test flag works
        parser = new VeraCliArgParser();
        jCommander = VeraCliArgParserTest.initialiseJCommander(parser);
        jCommander.parse(new String[] { "--profile", "%$3"});
        assertFalse(parser.getProfileFile() == VeraCliArgParser.DEFAULT_ARGS
                .getProfileFile());
    }

    /**
     * Test method for
     * {@link org.verapdf.cli.commands.VeraCliArgParser#getProfileFile()}.
     * @throws IOException 
     */
    @Test
    public final void testGetProfileFileFlag() throws IOException {
        VeraCliArgParser parser = new VeraCliArgParser();
        JCommander jCommander = VeraCliArgParserTest.initialiseJCommander(parser);
        File testFile = File.createTempFile("test", "xml");

        // Test flag works
        parser = new VeraCliArgParser();
        jCommander = VeraCliArgParserTest.initialiseJCommander(parser);
        jCommander.parse(new String[] { "-p", testFile.getAbsolutePath()});
        assertFalse(parser.getProfileFile() == VeraCliArgParser.DEFAULT_ARGS
                .getProfileFile());
    
        // Test flag works with other options & flags
        parser = new VeraCliArgParser();
        jCommander = VeraCliArgParserTest.initialiseJCommander(parser);
        jCommander
                .parse(new String[] { "-p", testFile.getAbsolutePath(), "--format", "html", "-h" });
        assertFalse(parser.getProfileFile() == VeraCliArgParser.DEFAULT_ARGS
                .getProfileFile());
}

    /**
     * Test method for
     * {@link org.verapdf.cli.commands.VeraCliArgParser#getProfileFile()}.
     * @throws IOException 
     */
    @Test
    public final void testGetProfileFileOption() throws IOException {
        VeraCliArgParser parser = new VeraCliArgParser();
        JCommander jCommander = VeraCliArgParserTest.initialiseJCommander(parser);
        File testFile = File.createTempFile("test", "xml");

        // Test option works
        parser = new VeraCliArgParser();
        jCommander = VeraCliArgParserTest.initialiseJCommander(parser);
        jCommander.parse(new String[] { "--profile", testFile.getAbsolutePath()});
        assertFalse(parser.getProfileFile() == VeraCliArgParser.DEFAULT_ARGS
                .getProfileFile());

        // Test option works with other options & flags
        parser = new VeraCliArgParser();
        jCommander = VeraCliArgParserTest.initialiseJCommander(parser);
        jCommander
                .parse(new String[] { "--profile", testFile.getAbsolutePath(), "--format", "html", "-h" });
        assertFalse(parser.getProfileFile() == VeraCliArgParser.DEFAULT_ARGS
                .getProfileFile());
    }

}
