package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosIndirect;
import org.verapdf.model.coslayer.CosTrailer;
import org.verapdf.model.impl.BaseTest;

import java.io.IOException;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBCosTrailerTest extends BaseTest {

    @BeforeClass
    public static void setUp() throws IOException {
        expectedType = TYPES.contains(PBCosTrailer.COS_TRAILER_TYPE) ? PBCosTrailer.COS_TRAILER_TYPE : null;
        expectedID = null;

        COSDictionary trailer = new COSDictionary();
        COSObject root = new COSObject(new COSDictionary());
        trailer.setInt(COSName.SIZE, 10);
        trailer.setItem(COSName.ROOT, root);
        trailer.setItem(COSName.ENCRYPT, root);

        actual = new PBCosTrailer(trailer);
    }

    @Test
    public void testGetIsEncryptedMethod() {
        Assert.assertTrue(((CosTrailer) actual).getisEncrypted().booleanValue());
    }

    @Test
    public void testCatalogLink() {
        List<? extends Object> catalog = actual.getLinkedObjects(PBCosTrailer.CATALOG);
        Assert.assertEquals(catalog.size(), 1);
        Assert.assertTrue(catalog.get(0) instanceof CosIndirect);
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
