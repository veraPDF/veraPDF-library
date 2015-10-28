package org.verapdf.integration;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;

import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.pdfa.qa.CorpusDirectory;
import org.verapdf.pdfa.qa.CorpusItemId;
import org.verapdf.pdfa.qa.RuleDirectory;
import org.verapdf.pdfa.validation.RuleId;

public class ITVeraPDFTestSuite {
    private static RuleDirectory RULES;
    private static CorpusDirectory CORPUS;
    @BeforeClass
    public static void initialise()  {
        try {
            RULES = RuleDirectory.loadFromDir(new File("../../veraPDF-validation-profiles/PDF_A/1b/"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Error loading rules", e);
        }
        try {
            CORPUS = CorpusDirectory.loadFromDir(new File("../../veraPDF-corpus/PDF_A-1b/"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Error loading corpus", e);
        }
    }

    @Test
    public void performValidation() throws URISyntaxException, IOException {
        for (RuleId ruleId : RULES.getKeys()) {
            System.out.println(ruleId);
            for (CorpusItemId corpusId : CORPUS.getCorpusIdForRule(ruleId)){
                Path path = CORPUS.getItem(corpusId);
                System.out.println(path);
                System.out.println(path.toFile().exists());
                System.out.println();
            }
        }
    }
}
