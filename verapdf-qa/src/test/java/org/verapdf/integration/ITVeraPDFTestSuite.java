package org.verapdf.integration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.JAXBException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.core.ValidationException;
import org.verapdf.model.ModelLoader;
import org.verapdf.pdfa.qa.CorpusDirectory;
import org.verapdf.pdfa.qa.CorpusItemId;
import org.verapdf.pdfa.qa.RuleDirectory;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.results.ValidationResults;
import org.verapdf.pdfa.validation.ProfileDetails;
import org.verapdf.pdfa.validation.Profiles;
import org.verapdf.pdfa.validation.Rule;
import org.verapdf.pdfa.validation.RuleId;
import org.verapdf.pdfa.validation.ValidationProfile;
import org.verapdf.pdfa.validation.Validator;

public class ITVeraPDFTestSuite {
    private static RuleDirectory RULES;
    private static CorpusDirectory CORPUS;

    @BeforeClass
    public static void initialise() {
        try {
            RULES = RuleDirectory.loadFromDir(new File(
                    "../../veraPDF-validation-profiles/PDF_A/1b/"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Error loading rules", e);
        }
        try {
            CORPUS = CorpusDirectory.loadFromDir(new File(
                    "../../veraPDF-corpus/PDF_A-1b/"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Error loading corpus", e);
        }
    }

    @Test
    public void performValidation() throws URISyntaxException, IOException {
        for (RuleId ruleId : RULES.getKeys()) {
            Set<Rule> rules = new HashSet<>();
            rules.add(RULES.getItem(ruleId));
            System.out.println();
            System.out.println("RULE =============================================================");
            System.out.println("Rule: " + ruleId);
            for (CorpusItemId corpusId : CORPUS.getCorpusIdForRule(ruleId)){
                System.out.println();
                System.out.println("CORPUS -------------------------------------------------------");
                System.out.println("Corpus: " + corpusId);
                ProfileDetails details = Profiles.profileDetailsFromValues(CORPUS.getFlavour().getPart().getName(), CORPUS.getFlavour().getPart().getDescription(), "testsuite", new Date());
                ValidationProfile profile = Profiles.profileFromValues(CORPUS.getFlavour(), details, "", rules, Collections.EMPTY_SET);
                Path path = CORPUS.getItem(corpusId);
                try (ModelLoader loader = new ModelLoader(new FileInputStream(path.toFile()))) {
                    ValidationResult result = Validator.validate(profile, loader.getRoot(), false);
                    ValidationResults.toXml(result, System.out, Boolean.TRUE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
