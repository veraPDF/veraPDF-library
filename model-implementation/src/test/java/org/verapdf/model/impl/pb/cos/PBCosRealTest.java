package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSFloat;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.impl.BaseTest;

import java.util.Random;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBCosRealTest extends BaseTest {

    private static float expected;

    @BeforeClass
    public static void setUp() {
        expectedType = TYPES.contains(PBCosReal.COS_REAL_TYPE) ? PBCosReal.COS_REAL_TYPE : null;
        expectedID = null;

        Random random = new Random();
        expected = random.nextFloat();

        actual = new PBCosReal(new COSFloat(expected));
    }

    @Test
    // @carlwilson temporarily ignored as it's a "boxing box" issue
    public void testGetIntegerMethod() {
        Assert.assertEquals(Long.valueOf((long) expected), ((CosReal) actual).getintValue());
    }

    @Test
    public void testGetDoubleMethod() {
        Assert.assertEquals(expected, ((CosReal) actual).getrealValue().doubleValue(), 0.00001);
    }

    @Test
    public void testGetRealMethod() {
        Assert.assertEquals(expected + "", ((CosReal) actual).getstringValue());
    }


    @AfterClass
    public static void tearDown() {
        expectedType = null;
        actual = null;
    }
}
