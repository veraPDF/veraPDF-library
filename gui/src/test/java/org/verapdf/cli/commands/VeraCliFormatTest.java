/**
 * 
 */
package org.verapdf.cli.commands;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
@SuppressWarnings("static-method")
public class VeraCliFormatTest {

    /**
     * Test method for
     * {@link org.verapdf.cli.commands.VeraCliArgParser#getFormat()}.
     */
    @Test
    public final void testGetFormatDefault() {
        // Test default is XML
        assertTrue(VeraCliArgParser.DEFAULT_ARGS.getFormat() == FormatOption.XML);

        VeraCliArgParser parser = new VeraCliArgParser();
        JCommander jCommander = VeraCliArgParserTest
                .initialiseJCommander(parser);

        // Test empty String[] args doesn't change that
        jCommander.parse(new String[] {});
        assertTrue(parser.getFormat() == VeraCliArgParser.DEFAULT_ARGS
                .getFormat());

        // Test other flags & options don't change that
        parser = new VeraCliArgParser();
        jCommander = VeraCliArgParserTest.initialiseJCommander(parser);
        jCommander.parse(new String[] { "-l", "--version", "--success", "-f",
                "1b" });
        assertTrue(parser.getFormat() == VeraCliArgParser.DEFAULT_ARGS
                .getFormat());
    }

    /**
     * Test method for
     * {@link org.verapdf.cli.commands.VeraCliArgParser#getFormat()}.
     */
    @Test(expected=ParameterException.class)
    public final void testGetFormatGarbage() {
        VeraCliArgParser parser = new VeraCliArgParser();
        JCommander jCommander = VeraCliArgParserTest
                .initialiseJCommander(parser);

        // Test that "rub" value throws exception
        jCommander.parse(new String[] { "--format", "somerubbish" });
    }

    /**
     * Test method for
     * {@link org.verapdf.cli.commands.VeraCliArgParser#getFormat()}.
     */
    @Test
    public final void testGetFormatXml() {
        VeraCliArgParser parser = new VeraCliArgParser();
        JCommander jCommander = VeraCliArgParserTest
                .initialiseJCommander(parser);

        // Test that "xml" value selects XML
        jCommander.parse(new String[] { "--format", "xml" });
        assertTrue(parser.getFormat() == FormatOption.XML);

        // Test that "XML" value selects XML
        parser = new VeraCliArgParser();
        jCommander = VeraCliArgParserTest.initialiseJCommander(parser);
        jCommander.parse(new String[] { "--format", "XML" });
        assertTrue(parser.getFormat() == FormatOption.XML);

        // Test other flags & options doesn't change that
        parser = new VeraCliArgParser();
        jCommander = VeraCliArgParserTest.initialiseJCommander(parser);
        jCommander.parse(new String[] { "-l", "--format", "xml", "--success",
                "-f", "1b" });
        assertTrue(parser.getFormat() == FormatOption.XML);
    }

    /**
     * Test method for
     * {@link org.verapdf.cli.commands.VeraCliArgParser#getFormat()}.
     */
    @Test
    public final void testGetFormatMrr() {
        VeraCliArgParser parser = new VeraCliArgParser();
        JCommander jCommander = VeraCliArgParserTest
                .initialiseJCommander(parser);

        // Test that "mrr" value selects MRR
        jCommander.parse(new String[] { "--format", "mrr" });
        assertTrue(parser.getFormat() == FormatOption.MRR);

        // Test that "MRR" value selects MRR
        parser = new VeraCliArgParser();
        jCommander = VeraCliArgParserTest.initialiseJCommander(parser);
        jCommander.parse(new String[] { "--format", "MRR" });
        assertTrue(parser.getFormat() == FormatOption.MRR);

        // Test other flags & options doesn't change that
        parser = new VeraCliArgParser();
        jCommander = VeraCliArgParserTest.initialiseJCommander(parser);
        jCommander.parse(new String[] { "-l", "--format", "mrr", "--passed",
                "-f", "1a" });
        assertTrue(parser.getFormat() == FormatOption.MRR);
    }

    /**
     * Test method for
     * {@link org.verapdf.cli.commands.VeraCliArgParser#getFormat()}.
     */
    @Test
    public final void testGetFormatHtml() {
        VeraCliArgParser parser = new VeraCliArgParser();
        JCommander jCommander = VeraCliArgParserTest
                .initialiseJCommander(parser);

        // Test that XML value selects XML
        jCommander.parse(new String[] { "--format", "html" });
        assertTrue(parser.getFormat() == FormatOption.HTML);

        // Test other flags & options doesn't change that
        parser = new VeraCliArgParser();
        jCommander = VeraCliArgParserTest.initialiseJCommander(parser);
        jCommander.parse(new String[] { "-h", "--format", "html", "--list",
                "-f", "2b" });
        assertTrue(parser.getFormat() == FormatOption.HTML);
    }

}
