/**
 * 
 */
package org.verapdf.cli.commands;

import static org.junit.Assert.*;

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
        fail("Not yet implemented"); // TODO
    }

}
