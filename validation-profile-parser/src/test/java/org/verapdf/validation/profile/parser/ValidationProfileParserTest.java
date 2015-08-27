package org.verapdf.validation.profile.parser;

/**
 * @author Maksim Bezrukov
 */

import org.junit.Test;
import org.verapdf.validation.profile.model.Rule;
import org.verapdf.validation.profile.model.ValidationProfile;
import org.verapdf.validation.profile.model.Variable;

import java.util.List;

import static org.junit.Assert.*;

public class ValidationProfileParserTest {

    @Test
    public void test() throws Exception {
        ValidationProfile prof = ValidationProfileParser.parseFromFilePath("src/test/resources/test.xml", false);

        assertEquals("org.verapdf.model.PDFA1a", prof.getModel());
        assertEquals("PDF/A-1a validation profile", prof.getName());
        assertEquals("STR_ID_101", prof.getDescription());
        assertEquals("User1", prof.getCreator());
        assertEquals("2015-01-23T17:30:15Z", prof.getCreated());
        assertNull(prof.getHash());

        assertEquals(1, prof.getRoolsForObject("PDXObject").size());

        Rule rule1 = prof.getRuleById("rule1");

        assertEquals(rule1, prof.getRoolsForObject("CosDocument").get(0));

        assertEquals("CosDocument", rule1.getAttrObject());
        assertEquals(0, rule1.getFixes().size());
        assertEquals("STR_ID_401", rule1.getDescription());
        assertEquals("fileHeaderOffset == 0", rule1.getTest());
        assertEquals("STR_ID_402", rule1.getRuleError().getMessage());
        assertEquals(1, rule1.getRuleError().getArgument().size());
        assertEquals("fileHeaderOffset", rule1.getRuleError().getArgument().get(0));
        assertEquals("ISO19005-1", rule1.getReference().getSpecification());
        assertEquals("6.1.2", rule1.getReference().getClause());
        assertEquals(0, rule1.getReference().getReferences().size());

        Rule rule53 = prof.getRuleById("rule53");

        assertEquals("PDMetadata", rule53.getAttrObject());
        assertEquals("STR_ID_608", rule53.getDescription());
        assertEquals("isInfoDictConsistent", rule53.getTest());
        assertEquals("STR_ID_609", rule53.getRuleError().getMessage());
        assertEquals(0, rule53.getRuleError().getArgument().size());
        assertEquals("ISO19005-1", rule53.getReference().getSpecification());
        assertEquals("6.7.3", rule53.getReference().getClause());
        assertEquals(1, rule53.getFixes().size());
        assertEquals("STR_ID_893", rule53.getFixes().get(0).getDescription());
        assertEquals("STR_ID_894", rule53.getFixes().get(0).getInfo());
        assertEquals("STR_ID_895", rule53.getFixes().get(0).getError());
        assertEquals("fix1", rule53.getFixes().get(0).getID());

        Rule rule35 = prof.getRuleById("rule35");

        assertEquals("PDXObject", rule35.getAttrObject());
        assertNull(rule35.getDescription());
        assertNull(rule35.getTest());
        assertNull(rule35.getRuleError());
        assertNull(rule35.getReference());
        assertEquals(0, rule35.getFixes().size());

        assertNull(prof.getRuleById(null));

        List<String> allRulesId = prof.getAllRulesId();

        assertEquals(3, allRulesId.size());
        assertTrue(allRulesId.contains("rule1"));
        assertTrue(allRulesId.contains("rule53"));
        assertTrue(allRulesId.contains("rule35"));

        assertEquals(0, prof.getAllVariables().size());

        assertTrue(rule1.toString().startsWith("Rule [attrID=rule1, attrObject=CosDocument, description=STR_ID_401, test=fileHeaderOffset == 0, ruleError=org.verapdf.validation.profile.model.RuleError"));

        Rule rule1Copy = new Rule(rule1.getAttrID(), rule1.getAttrObject(), rule1.getDescription(), rule1.getRuleError(), rule1.getTest(), rule1.getReference(), rule1.getFixes());
        assertTrue(rule1.equals(rule1Copy));
        assertEquals(rule1Copy.hashCode(), rule1.hashCode());
    }

    @Test
    public void testCyrillic() throws Exception {
        ValidationProfile prof = ValidationProfileParser.parseFromFilePath("src/test/resources/testCyrillic.xml", false);

        assertEquals("org.verapdf.model.PDFA1a", prof.getModel());
        assertEquals("PDF/A-1a validation profile", prof.getName());
        assertEquals("STR_ID_101", prof.getDescription());
        assertEquals("Какой-то русский человек", prof.getCreator());
        assertEquals("2015-01-23T17:30:15Z", prof.getCreated());
        assertNull(prof.getHash());

        assertNull(prof.getRoolsForObject("PDXObject"));

        Rule rule1 = prof.getRuleById("правило1");

        assertEquals("CosDocument", rule1.getAttrObject());
        assertEquals(0, rule1.getFixes().size());
        assertEquals("STR_ID_401", rule1.getDescription());
        assertEquals("fileHeaderOffset == 0", rule1.getTest());
        assertEquals("STR_ID_402", rule1.getRuleError().getMessage());
        assertEquals(1, rule1.getRuleError().getArgument().size());
        assertEquals("fileHeaderOffset", rule1.getRuleError().getArgument().get(0));
        assertEquals("ISO19005-1", rule1.getReference().getSpecification());
        assertEquals("6.1.2", rule1.getReference().getClause());

        Rule rule53 = prof.getRuleById("rule53");

        assertEquals("PDMetadata", rule53.getAttrObject());
        assertEquals("STR_ID_608", rule53.getDescription());
        assertEquals("isInfoDictConsistent", rule53.getTest());
        assertEquals("STR_ID_609", rule53.getRuleError().getMessage());
        assertEquals(0, rule53.getRuleError().getArgument().size());
        assertEquals("ISO19005-1", rule53.getReference().getSpecification());
        assertEquals("6.7.3", rule53.getReference().getClause());
        assertEquals(1, rule53.getFixes().size());
        assertEquals("STR_ID_893", rule53.getFixes().get(0).getDescription());
        assertEquals("STR_ID_894", rule53.getFixes().get(0).getInfo());
        assertEquals("STR_ID_895", rule53.getFixes().get(0).getError());

        Variable var1 = prof.getVariablesForObject("Object").get(0);

        assertEquals("Object", var1.getAttrObject());
        assertEquals("varName1", var1.getAttrName());
        assertEquals("null", var1.getDefaultValue());
        assertEquals("5", var1.getValue());

    }
}
