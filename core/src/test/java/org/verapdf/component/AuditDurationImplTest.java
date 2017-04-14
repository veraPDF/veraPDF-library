package org.verapdf.component;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AuditDurationImplTest {

    @Test
    public void testGetStringDuration() {
        long second = 1000;
        long minute = 60 * second;
        long hour = 60 * minute;
        assertEquals("00:00:00:000", AuditDurationImpl.getStringDuration(0));
        assertEquals("01:23:45:678", AuditDurationImpl.getStringDuration(
                hour + 23 * minute + 45 * second + 678));
    }

}
