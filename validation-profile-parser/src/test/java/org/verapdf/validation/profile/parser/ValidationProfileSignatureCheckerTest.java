package org.verapdf.validation.profile.parser;

/**
 * @author Maksim Bezrukov
 */

import org.junit.Test;

import java.io.File;
import java.nio.file.Files;

import static org.junit.Assert.*;

public class ValidationProfileSignatureCheckerTest {

    @Test
    public void test() throws Exception {
        Files.copy(new File("src/test/resources/test.xml").toPath(), new File("src/test/resources/testCopy.xml").toPath());
        ValidationProfileSignatureChecker checker = ValidationProfileSignatureChecker.newInstance(new File("src/test/resources/testCopy.xml"));

        assertFalse(checker.isValidSignature());
        assertEquals("2b9bbbdf26757dad5f451c88c35cf5bdf2ee5315", checker.getHashAsString());
        checker.signFile();
        assertTrue(checker.isValidSignature());

        Files.deleteIfExists(new File("validation-profile-parser/src/test/resources/testCopy.xml").toPath());
    }
}
