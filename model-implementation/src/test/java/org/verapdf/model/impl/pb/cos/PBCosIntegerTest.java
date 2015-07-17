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

    private static Long expected;

    @BeforeClass
    public static void setUp() {
        expectedType = "CosInteger";
        expectedID = null;

        Random random = new Random();
        expected = random.nextLong();

        COSInteger integer = COSInteger.get(expected);
        actual = new PBCosInteger(integer);
    }

    @Test
    public void testGetIntegerMethod() {
        Assert.assertTrue(((CosInteger) actual).getintValue().equals(expected));
    }

    @Test
    public void testGetStringMethod() {
        Assert.assertEquals(Double.valueOf(expected.toString()).toString(), ((CosInteger) actual).getstringValue());
    }

    @Test
    public void testGetRealMethod() {
        Assert.assertTrue(expected.longValue() == ((CosInteger) actual).getrealValue());
    }

    @AfterClass
    public static void tearDown() {
        expectedType = null;
        expected = null;
        actual = null;
    }
}
