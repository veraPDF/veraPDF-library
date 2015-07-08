package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSString;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.coslayer.CosString;
import org.verapdf.model.impl.BaseTest;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author Evgeniy Muravitskiy
 */
// TODO : rewrite for real document
public class PBCosStringTest extends BaseTest {

    private static CosString secondActual;

    private static String firstExpected;
    private static String secondExpected;

    @BeforeClass
    public static void setUp() throws IOException {
        TYPE = "CosString";
        ID = null;

        setExpectedResult();

        final String string = "AAFFFEEE";
        COSString value = COSString.parseHex(string);
        value.setIsHexSymbols(Boolean.FALSE);
        value.setHexCount(Long.valueOf(string.length()));

        actual = new PBCosString(value);
        secondActual = new PBCosString(new COSString(secondExpected));
    }

    private static void setExpectedResult() {
        byte[] bytes = new byte[]{(byte) 0xAA, (byte) 0xFF, (byte) 0xFE, (byte) 0xEE};
        firstExpected = new String(bytes, Charset.forName("US-ASCII"));
        secondExpected = "Hello, World!";
    }

    @Test
    public void testGetValueMethod() {
        Assert.assertEquals(firstExpected, ((CosString) actual).getvalue());
        Assert.assertEquals(secondExpected, secondActual.getvalue());
    }

    @Test
    public void testGetIsHexMethod() {
        Assert.assertTrue(((CosString) actual).getisHex());
        Assert.assertFalse(secondActual.getisHex());
    }

    @Test
    public void testGetIsHexSymbolsMethod() {
        Assert.assertFalse(((CosString) actual).getisHexSymbols());
        Assert.assertTrue(secondActual.getisHexSymbols());
    }

    @Test
    public void testGetHexCountMethod() {
        Assert.assertEquals(((CosString) actual).gethexCount(), Long.valueOf(8));
        Assert.assertEquals(secondActual.gethexCount(), Long.valueOf(0));
    }

    @AfterClass
    public static void tearDown() {
        TYPE = null;
        ID = null;

        actual = null;
        secondActual = null;
        firstExpected = null;
        secondExpected = null;
    }
}
