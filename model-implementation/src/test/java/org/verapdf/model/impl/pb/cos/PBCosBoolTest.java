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
        expectedType = TYPES.contains(PBCosBool.COS_BOOLEAN_TYPE) ? PBCosBool.COS_BOOLEAN_TYPE : null;
        expectedID = null;

        COSBoolean bool = COSBoolean.getBoolean(Boolean.TRUE);
        actual = PBCosBool.valueOf(bool);
    }

    @Test
    public void testBooleanValue() {
        Assert.assertTrue(((CosBool) actual).getvalue().equals(Boolean.TRUE));
    }

    @AfterClass
    public static void tearDown() {
        expectedType = null;
        actual = null;
    }
}
