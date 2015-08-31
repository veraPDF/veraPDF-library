package org.verapdf.validation.profile.parser;

/**
 * @author Maksim Bezrukov
 */

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.exceptions.validationprofileparser.MissedHashTagException;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ValidationProfileSignatureCheckerTest {

    private static File copy;
    private static File xml;

    @BeforeClass
    public static void setUp() throws URISyntaxException, IOException {
        String path = getSystemIndependentPath("/test.xml");
        xml = new File(path);
        copy = new File(xml.getParent(), "copy");
        Files.copy(xml.toPath(), copy.toPath());
    }

    @Test
    public void test() throws IOException, XMLStreamException, MissedHashTagException {
        ValidationProfileSignatureChecker expected = ValidationProfileSignatureChecker.newInstance(copy);
        ValidationProfileSignatureChecker actual = ValidationProfileSignatureChecker.newInstance(xml);

        assertFalse(expected.isValidSignature());

        expected.signFile();

        assertTrue(expected.isValidSignature());
        Assert.assertEquals(expected.getHashAsString(), actual.getHashAsString());
    }

    @AfterClass
    public static void tearDown() throws IOException {
        Files.deleteIfExists(copy.toPath());
    }

    private static String getSystemIndependentPath(String path) throws URISyntaxException {
        URL resourceUrl = ClassLoader.class.getResource(path);
        Path resourcePath = Paths.get(resourceUrl.toURI());
        return resourcePath.toString();
    }

}
