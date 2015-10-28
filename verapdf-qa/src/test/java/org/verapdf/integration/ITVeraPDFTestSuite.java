package org.verapdf.integration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.pdfa.qa.RuleDirectory;
import org.verapdf.pdfa.validation.Rule;

public class ITVeraPDFTestSuite {
    private static RuleDirectory RULES;
    @BeforeClass
    public static void initialise() throws FileNotFoundException, IOException {
        RULES = RuleDirectory.loadFromDir(new File("../../veraPDF-validation-profiles/PDF_A/1b/"));
        for (Rule rule : RULES.getItems()) {
            System.out.println(rule);
        }
    }

    @Test
    public void performValidation() throws URISyntaxException, IOException {
    }
}