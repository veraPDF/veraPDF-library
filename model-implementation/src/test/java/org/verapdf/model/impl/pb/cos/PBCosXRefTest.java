package org.verapdf.model.impl.pb.cos;

import org.junit.*;
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
        expectedType = TYPES.contains(PBCosXRef.COS_XREF_TYPE) ? PBCosXRef.COS_XREF_TYPE : null;
        expectedID = null;

        Random random = new Random();
        expectedHeaderSpacings = Boolean.valueOf(random.nextBoolean());
        expectedEOLMarkers = Boolean.valueOf(random.nextBoolean());

        actual = new PBCosXRef(expectedHeaderSpacings, expectedEOLMarkers);
    }

    @Test
    public void testGetHeaderSpacingsMethod() {
        Assert.assertEquals(((CosXRef) actual).getsubsectionHeaderSpaceSeparated(), expectedHeaderSpacings);
    }

    @Test
    public void testGetEOLMarkersMethod() {
        Assert.assertEquals(((CosXRef) actual).getxrefEOLMarkersComplyPDFA(), expectedEOLMarkers);
    }

    @AfterClass
    public static void tearDown() {
        expectedType = null;
        expectedEOLMarkers = null;
        expectedHeaderSpacings = null;
    }
}
