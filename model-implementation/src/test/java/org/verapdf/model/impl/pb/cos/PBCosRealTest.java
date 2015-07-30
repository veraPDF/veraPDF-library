package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSFloat;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.impl.BaseTest;

import java.util.Random;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBCosRealTest extends BaseTest {

    private static Float expected;

    @BeforeClass
    public static void setUp() {
        expectedType = TYPES.contains(PBCosReal.COS_REAL_TYPE) ? PBCosReal.COS_REAL_TYPE : null;
        expectedID = null;

        Random random = new Random();
        expected = random.nextFloat();

        actual = new PBCosReal(new COSFloat(expected.floatValue()));
    }

    @Test
    public void testGetIntegerMethod() {
        Assert.assertEquals(expected.longValue(), ((CosReal) actual).getintValue().longValue());
    }

    @Test
    public void testGetDoubleMethod() {
        Assert.assertEquals(expected.doubleValue(), ((CosReal) actual).getrealValue().doubleValue(), 0.00001);
    }

    @Test
    public void testGetRealMethod() {
        final Double aDouble = Double.valueOf(expected.toString());
        Assert.assertEquals(aDouble.toString(), ((CosReal) actual).getstringValue());
    }


    @AfterClass
    public static void tearDown() {
        expectedType = null;
        expected = null;
        actual = null;
    }
}
