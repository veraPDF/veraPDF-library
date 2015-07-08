package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosFileSpecification;
import org.verapdf.model.impl.BaseTest;

import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBCosFileSpecificationTest extends BaseTest {

    @BeforeClass
    public static void setUp() {
        TYPE = "CosFileSpecification";
        ID = null;

        COSDictionary specification = new COSDictionary();
        specification.setString(COSName.EF, "some link");

        actual = new PBCosFileSpecification(specification);
    }

    @Test
    public void testGetEFMethod() {
        Assert.assertNotEquals(((CosFileSpecification) actual).getEF(), null);
    }

    @Test
    public void testParentLink() {
        final List<? extends Object> parentLink = actual.getLinkedObjects(PBCosDict.METADATA);
        Assert.assertEquals(parentLink.size(), 0);
    }

    @Test(expected = IllegalAccessError.class)
    public void testNonexistentParentLink() {
        actual.getLinkedObjects("Wrong link.");
    }

    @AfterClass
    public static void tearDown() {
        TYPE = null;
        actual = null;
    }
}
