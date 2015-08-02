package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.*;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.coslayer.CosArray;
import org.verapdf.model.coslayer.CosDict;
import org.verapdf.model.coslayer.CosIndirect;
import org.verapdf.model.coslayer.CosInteger;
import org.verapdf.model.impl.BaseTest;

import java.io.IOException;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBCosArrayTest extends BaseTest {

    @BeforeClass
    public static void setUp() throws IOException {
        expectedType = TYPES.contains(PBCosArray.COS_ARRAY_TYPE) ? PBCosArray.COS_ARRAY_TYPE : null;
        expectedID = null;

        setUpActual();
    }

    private static void setUpActual() throws IOException {
        COSArray array = new COSArray();
        COSDictionary dictionary = new COSDictionary();
        dictionary.setBoolean(COSName.N, true);
        COSObject object = new COSObject(dictionary);

        array.add(COSInteger.get(10));
        array.add(dictionary);
        array.add(object);
        array.add(null);

        actual = new PBCosArray(array);
    }

    @Test
    public void testGetSizeMethod() {
        Assert.assertEquals(Long.valueOf(4), ((CosArray) actual).getsize());
    }

    @Test
    public void testGetLinkedObjectsMethod() {
        List<? extends org.verapdf.model.baselayer.Object> elements =
                                                        ((CosArray) actual).getLinkedObjects(PBCosArray.ELEMENTS);
        Assert.assertEquals(3, elements.size());
        Assert.assertTrue(elements.get(0) instanceof CosInteger);
        Assert.assertTrue(elements.get(1) instanceof CosDict);
        Assert.assertTrue(elements.get(2) instanceof CosIndirect);
    }

    @AfterClass
    public static void tearDown() {
        expectedType = null;
        actual = null;
    }

}
