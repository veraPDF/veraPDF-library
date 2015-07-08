package org.verapdf.model.impl.pb.cos;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.coslayer.CosXRef;
import org.verapdf.model.impl.BaseTest;

import java.util.Random;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBCosXRefTest extends BaseTest {

    private static Boolean expectedHeaderSpacings;
    private static Boolean expectedEOLMarkers;

    @BeforeClass
    public static void setUp() {
        TYPE = "CosXRef";
        ID = null;

        Random random = new Random();
        expectedHeaderSpacings = Boolean.valueOf(random.nextBoolean());
        expectedEOLMarkers = Boolean.valueOf(random.nextBoolean());

        actual = new PBCosXRef(expectedHeaderSpacings, expectedEOLMarkers);
    }

    @Test
    public void testGetHeaderSpacingsMethod() {
        Assert.assertEquals(((CosXRef) actual).getxrefHeaderSpacingsComplyPDFA(), expectedHeaderSpacings);
    }

    @Test
    public void testGetEOLMarkersMethod() {
        Assert.assertEquals(((CosXRef) actual).getxrefEOLMarkersComplyPDFA(), expectedEOLMarkers);
    }

    @AfterClass
    public static void tearDown() {
        TYPE = null;
        expectedEOLMarkers = null;
        expectedHeaderSpacings = null;
    }
}
