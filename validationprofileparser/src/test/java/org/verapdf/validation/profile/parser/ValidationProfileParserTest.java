package org.verapdf.validation.profile.parser; /**
 * Created by bezrukov on 4/28/15.
 */

import org.verapdf.validation.profile.model.Rule;
import org.verapdf.validation.profile.model.ValidationProfile;
import org.junit.Test;

import static org.junit.Assert.*;

public class ValidationProfileParserTest {

    @Test
    public void test() throws Exception {
        ValidationProfile prof = ValidationProfileParser.parseValidationProfile("src/test/resources/test.xml", false);

        assertEquals(prof.getAttrModel(), "org.verapdf.model.PDFA1a");
        assertEquals(prof.getName(), "PDF/A-1a validation profile");
        assertEquals(prof.getDescription(), "STR_ID_101");
        assertEquals(prof.getCreator(), "User1");
        assertEquals(prof.getCreated(), "2015-01-23T17:30:15Z");
        assertNull(prof.getHash());

        assertEquals(prof.getRoolsForObject("PDXObject").size(), 1);

        Rule rule1 = prof.getRuleById("rule1");

        assertEquals(prof.getRoolsForObject("CosDocument").get(0), rule1);

        assertEquals(rule1.getAttrObject(), "CosDocument");
        assertEquals(rule1.getFix().size(), 0);
        assertEquals(rule1.getDescription(), "STR_ID_401");
        assertEquals(rule1.getTest(), "fileHeaderOffset == 0");
        assertTrue(rule1.isHasError());
        assertEquals(rule1.getRuleError().getMessage(), "STR_ID_402");
        assertEquals(rule1.getRuleError().getArgument().size(), 1);
        assertEquals(rule1.getRuleError().getArgument().get(0), "fileHeaderOffset");
        assertEquals(rule1.getReference().getSpecification(), "ISO19005-1");
        assertEquals(rule1.getReference().getClause(), "6.1.2");

        Rule rule53 = prof.getRuleById("rule53");

        assertEquals(rule53.getAttrObject(), "PDMetadata");
        assertEquals(rule53.getDescription(), "STR_ID_608");
        assertEquals(rule53.getTest(), "isInfoDictConsistent");
        assertFalse(rule53.isHasError());
        assertEquals(rule53.getRuleError().getMessage(), "STR_ID_609");
        assertEquals(rule53.getRuleError().getArgument().size(), 0);
        assertEquals(rule53.getReference().getSpecification(), "ISO19005-1");
        assertEquals(rule53.getReference().getClause(), "6.7.3");
        assertEquals(rule53.getFix().size(), 1);
        assertEquals(rule53.getFix().get(0).getDescription(), "STR_ID_893");
        assertEquals(rule53.getFix().get(0).getInfo().getMessage(), "STR_ID_894");
        assertEquals(rule53.getFix().get(0).getError().getMessage(), "STR_ID_895");

        Rule rule35 = prof.getRuleById("rule35");

        assertEquals(rule35.getAttrObject(), "PDXObject");
        assertFalse(rule35.isHasError());
        assertNull(rule35.getDescription());
        assertNull(rule35.getTest());
        assertNull(rule35.getRuleError());
        assertNull(rule35.getReference());
        assertEquals(rule35.getFix().size(), 0);


    }

    @Test
    public void testCyrillic() throws Exception {
        ValidationProfile prof = ValidationProfileParser.parseValidationProfile("src/test/resources/testCyrillic.xml", false);

        assertEquals(prof.getAttrModel(), "org.verapdf.model.PDFA1a");
        assertEquals(prof.getName(), "PDF/A-1a validation profile");
        assertEquals(prof.getDescription(), "STR_ID_101");
        assertEquals(prof.getCreator(), "Какой-то русский человек");
        assertEquals(prof.getCreated(), "2015-01-23T17:30:15Z");
        assertNull(prof.getHash());

        assertNull(prof.getRoolsForObject("PDXObject"));

        Rule rule1 = prof.getRuleById("правило1");

        assertEquals(rule1.getAttrObject(), "CosDocument");
        assertEquals(rule1.getFix().size(), 0);
        assertEquals(rule1.getDescription(), "STR_ID_401");
        assertEquals(rule1.getTest(), "fileHeaderOffset == 0");
        assertEquals(rule1.getRuleError().getMessage(), "STR_ID_402");
        assertEquals(rule1.getRuleError().getArgument().size(), 1);
        assertEquals(rule1.getRuleError().getArgument().get(0), "fileHeaderOffset");
        assertEquals(rule1.getReference().getSpecification(), "ISO19005-1");
        assertEquals(rule1.getReference().getClause(), "6.1.2");

        Rule rule53 = prof.getRuleById("rule53");

        assertEquals(rule53.getAttrObject(), "PDMetadata");
        assertEquals(rule53.getDescription(), "STR_ID_608");
        assertEquals(rule53.getTest(), "isInfoDictConsistent");
        assertEquals(rule53.getRuleError().getMessage(), "STR_ID_609");
        assertEquals(rule53.getRuleError().getArgument().size(), 0);
        assertEquals(rule53.getReference().getSpecification(), "ISO19005-1");
        assertEquals(rule53.getReference().getClause(), "6.7.3");
        assertEquals(rule53.getFix().size(), 1);
        assertEquals(rule53.getFix().get(0).getDescription(), "STR_ID_893");
        assertEquals(rule53.getFix().get(0).getInfo().getMessage(), "STR_ID_894");
        assertEquals(rule53.getFix().get(0).getError().getMessage(), "STR_ID_895");
    }
}
