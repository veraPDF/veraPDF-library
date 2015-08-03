package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosDict;
import org.verapdf.model.coslayer.CosIndirect;
import org.verapdf.model.coslayer.CosNull;
import org.verapdf.model.impl.BaseTest;

import java.io.IOException;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBCosIndirectTest extends BaseTest {

    private static PBCosIndirect secondActual;

    private static final long objectNumber = 10;
    private static final int generationNumber = 0;

    @BeforeClass
    public static void setUp() throws IOException {
        expectedType = TYPES.contains(PBCosIndirect.COS_INDIRECT_TYPE) ? PBCosIndirect.COS_INDIRECT_TYPE : null;
        expectedID = String.valueOf(objectNumber) + " " + String.valueOf(generationNumber);

        actual = new PBCosIndirect(createObject(null, Boolean.TRUE));
        secondActual = new PBCosIndirect(createObject(new COSDictionary(), Boolean.FALSE));
    }

    private static COSObject createObject(COSBase base, Boolean spacings) throws IOException {
        COSObject object = new COSObject(base);
        object.setObjectNumber(objectNumber);
        object.setGenerationNumber(generationNumber);
        object.setHeaderFormatComplyPDFA(spacings);
        return object;
    }

    @Test
    public void testGetSpacingsMethod() {
        Assert.assertTrue(((CosIndirect) actual).getspacingCompliesPDFA().booleanValue());
        Assert.assertFalse((secondActual).getspacingCompliesPDFA().booleanValue());
    }

    @Test
    public void testGetDirectLink() {
        Object firstDirect = getDirectLink(((CosIndirect) actual));
        Object secondDirect = getDirectLink(secondActual);

        Assert.assertTrue(firstDirect instanceof CosNull);
        Assert.assertTrue(secondDirect instanceof CosDict);
    }

    private static Object getDirectLink(CosIndirect indirect) {
        List<? extends Object> direct = indirect.getLinkedObjects(PBCosIndirect.DIRECT_OBJECT);
        Assert.assertEquals(direct.size(), 1);
        return direct.get(0);
    }

    @AfterClass
    public static void tearDown() {
        expectedType = null;
        expectedID = null;
        actual = null;
        secondActual = null;
    }
}
