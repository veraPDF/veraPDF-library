package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSName;
import org.junit.*;
import org.verapdf.model.coslayer.CosName;
import org.verapdf.model.impl.BaseTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBCosNameTest extends BaseTest {

    public static List<COSName> expected;
    public static List<CosName> actual;

    @BeforeClass
    public static void setUp() {
        TYPE = "CosName";
        ID = null;

        setUpCOSNames();

        actual = new ArrayList<>(expected.size());
        for (COSName name : expected) {
            actual.add(new PBCosName(name));
        }
    }

    private static void setUpCOSNames() {
        expected = new ArrayList<>(6);

        addCOSName(COSName.INDEX);
        addCOSName(COSName.ACRO_FORM);
        addCOSName(COSName.ATTACHED);
        addCOSName(COSName.BITS_PER_COORDINATE);
        final String firstCustom = "FirstCustom";
        final String secondCustom = "SecondCustom";
        expected.add(COSName.getPDFName(firstCustom, firstCustom.length()));
        expected.add(COSName.getPDFName(secondCustom, secondCustom.length()));
    }

    private static void addCOSName(COSName cosName) {
        String name = cosName.getName();
        expected.add(COSName.getPDFName(name, name.length()));
    }

    @Test
    public void testGetValueMethod() {
        for (int i = 0; i < expected.size(); i++) {
            Assert.assertEquals(expected.get(i).getName(), actual.get(i).getvalue());
        }
    }

    @Test
    public void testGetOriginalLength() {
        for (int i = 0; i < expected.size(); i++) {
            final Long originalLength = Long.valueOf(expected.get(i).getOriginalLength());
            Assert.assertEquals(originalLength, actual.get(i).getorigLength());
        }
    }

    @Test
    public void testTypeAndID() {
        for (CosName name : actual) {
            Assert.assertEquals(TYPE, name.getType());
            Assert.assertEquals(ID, name.getID());
        }
    }

    //TODO : when implement subtype test override this method

    @AfterClass
    public static void tearDown() {
        TYPE = null;
        expected.clear();
        actual.clear();
        expected = null;
        actual =null;
    }
}
