package org.verapdf.model.impl;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Evgeniy Muravitskiy
 */

public abstract class BaseTest {

    protected static org.verapdf.model.baselayer.Object actual;

    protected static String TYPE;
    protected static String ID;

    @Test
    public void testTypeAndID() {
        Assert.assertEquals(TYPE, actual.getType());
        Assert.assertEquals(ID, actual.getID());
    }
}
