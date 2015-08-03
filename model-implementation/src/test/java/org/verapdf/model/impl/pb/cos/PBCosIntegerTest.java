package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSInteger;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.coslayer.CosInteger;
import org.verapdf.model.impl.BaseTest;

import java.util.Random;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBCosIntegerTest extends BaseTest {

    private static long expected;

    @BeforeClass
    public static void setUp() {
        expectedType = TYPES.contains(PBCosInteger.COS_INTEGER_TYPE) ? PBCosInteger.COS_INTEGER_TYPE : null;
        expectedID = null;

        Random random = new Random();
        expected = random.nextLong();

        COSInteger integer = COSInteger.get(expected);
        actual = new PBCosInteger(integer);
    }

    @Test
    public void testGetIntegerMethod() {
        Assert.assertTrue(((CosInteger) actual).getintValue().equals(Long.valueOf(expected)));
    }

    @Test
    public void testGetStringMethod() {
        Assert.assertEquals(Double.valueOf(expected).toString(), ((CosInteger) actual).getstringValue());
    }

    @Test
    public void testGetRealMethod() {
        Assert.assertTrue(expected == ((CosInteger) actual).getrealValue().doubleValue());
    }

    @AfterClass
    public static void tearDown() {
        expectedType = null;
        actual = null;
    }
}
