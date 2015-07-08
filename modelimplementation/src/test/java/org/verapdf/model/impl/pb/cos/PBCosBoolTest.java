package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSBoolean;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.coslayer.CosBool;
import org.verapdf.model.impl.BaseTest;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBCosBoolTest extends BaseTest {

    @BeforeClass
    public static void setUp() {
        TYPE = "CosBool";
        ID = null;

        COSBoolean bool = COSBoolean.getBoolean(Boolean.TRUE);
        actual = new PBCosBool(bool);
    }

    @Test
    public void testBooleanValue() {
        Assert.assertTrue(((CosBool) actual).getvalue().equals(Boolean.TRUE));
    }

    @Test
    public void testTypeAndID() {
        Assert.assertEquals(TYPE, actual.getType());
        Assert.assertEquals(ID, actual.getID());
    }

    @AfterClass
    public static void tearDown() {
        TYPE = null;
        actual = null;
    }
}
