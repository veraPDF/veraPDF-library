/**
 * 
 */
package org.verapdf.cli.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.beust.jcommander.JCommander;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
@SuppressWarnings("static-method")
public class VeraCliGetPathsTest {

    /**
     * Test method for
     * {@link org.verapdf.cli.commands.VeraCliArgParser#getPdfPaths()}.
     */
    @Test
    public final void testGetPathsDefault() {
        // Test default is empty list
        assertTrue(VeraCliArgParser.DEFAULT_ARGS.getPdfPaths().isEmpty());

        // Test that empty args don't change that
        VeraCliArgParser parser = new VeraCliArgParser();
        JCommander jCommander = VeraCliArgParserTest
                .initialiseJCommander(parser);
        jCommander.parse(new String[] {});
        assertEquals(VeraCliArgParser.DEFAULT_ARGS.getPdfPaths(),
                parser.getPdfPaths());

        // Test other flags & options don't change that
        parser = new VeraCliArgParser();
        jCommander = VeraCliArgParserTest.initialiseJCommander(parser);
        jCommander.parse(new String[] { "-l", "--success", "--format", "html",
                "-h" });
        assertEquals(VeraCliArgParser.DEFAULT_ARGS.getPdfPaths(),
                parser.getPdfPaths());
    }

    /**
     * Test method for
     * {@link org.verapdf.cli.commands.VeraCliArgParser#getPdfPaths()}.
     */
    @Test
    public final void testGetPaths() {
        VeraCliArgParser parser = new VeraCliArgParser();
        JCommander jCommander = VeraCliArgParserTest.initialiseJCommander(parser);

        // Test flag works
        jCommander.parse(new String[] { "path 1", "path 2", "path 3" });
        assertNotEquals(parser.getPdfPaths(), VeraCliArgParser.DEFAULT_ARGS
                .getPdfPaths());
    
        // Test flag works with other options & flags
        parser = new VeraCliArgParser();
        jCommander = VeraCliArgParserTest.initialiseJCommander(parser);
        jCommander.parse(new String[] { "-l", "--success", "--format", "html",
                "-h", "path 1", "path 2", "path 3" });
        assertNotEquals(parser.getPdfPaths(), VeraCliArgParser.DEFAULT_ARGS
                .getPdfPaths());
    }

}
