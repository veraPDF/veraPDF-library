package org.verapdf.validation.profile.parser;

/**
 * @author Maksim Bezrukov
 */

import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class ValidationProfileSignatureCheckerTest {

    private static String getSystemIndependentPath(String path) throws URISyntaxException {
        URL resourceUrl = ClassLoader.class.getResource(path);
        Path resourcePath = Paths.get(resourceUrl.toURI());
        return resourcePath.toString();
    }

    @Test
    public void test() throws Exception {
        String path = getSystemIndependentPath("/test.xml");
        File xml = new File(path);
        File copy = new File(xml.getParent(), "copy");
        Files.copy(xml.toPath(), copy.toPath());
        ValidationProfileSignatureChecker checker = ValidationProfileSignatureChecker.newInstance(copy);

        assertFalse(checker.isValidSignature());
        assertEquals("2b9bbbdf26757dad5f451c88c35cf5bdf2ee5315", checker.getHashAsString());
        checker.signFile();
        assertTrue(checker.isValidSignature());

        Files.deleteIfExists(copy.toPath());
    }
}
