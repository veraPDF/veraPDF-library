package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosFileSpecification;
import org.verapdf.model.external.EmbeddedFile;
import org.verapdf.model.impl.BaseTest;
import org.verapdf.model.impl.pb.external.PBoxEmbeddedFile;

import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBCosFileSpecificationTest extends BaseTest {

    @BeforeClass
    public static void setUp() {
        expectedType = TYPES.contains(PBCosFileSpecification.COS_FILE_SPECIFICATION_TYPE) ?
														PBCosFileSpecification.COS_FILE_SPECIFICATION_TYPE : null;
        expectedID = null;

        COSDictionary specification = new COSDictionary();
        specification.setItem(COSName.EF, new COSDictionary());

        actual = new PBCosFileSpecification(specification);
    }

    @Test
    public void testGetEFMethod() {
		List<? extends Object> links = actual.getLinkedObjects(PBCosFileSpecification.EF);
		Assert.assertEquals(1, links.size());
		Object object = links.get(0);
		Assert.assertEquals(PBoxEmbeddedFile.EMBEDDED_FILE_TYPE, object.getObjectType());
		Assert.assertFalse(((EmbeddedFile) object).getisValidPDFA12());
	}

    @Test
    public void testParentLink() {
        final List<? extends Object> parentLink = actual.getLinkedObjects(PBCosDict.METADATA);
        Assert.assertEquals(parentLink.size(), 0);
    }

    @AfterClass
    public static void tearDown() {
        expectedType = null;
        actual = null;
    }
}
