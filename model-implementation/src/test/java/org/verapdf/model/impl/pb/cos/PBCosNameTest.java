package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSName;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.ModelHelper;
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
        expectedType = TYPES.contains(PBCosName.COS_NAME_TYPE) ? PBCosName.COS_NAME_TYPE : null;
        expectedID = null;

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
            final Long originalLength = Long.valueOf(expected.get(i).getOriginalLength().longValue());
            Assert.assertEquals(originalLength, actual.get(i).getorigLength());
        }
    }

    @Override
    @Test
    public void testTypeAndID() {
        for (CosName name : actual) {
            Assert.assertEquals(expectedType, name.getType());
            Assert.assertEquals(expectedID, name.getID());
        }
    }

	@Override
	@Test
	public void testLinksMethod() {
		for (CosName name : actual) {
			List<String> expectedLinks = ModelHelper.getListOfLinks(name.getType());
			for (String link : expectedLinks) {
				Assert.assertNotNull(name.getLinkedObjects(link));
			}
			expectedLinks.clear();
		}
	}

	@Override
    @Test(expected = IllegalAccessError.class)
	public void testNonexistentParentLink() {
		for (CosName name : actual) {
			name.getLinkedObjects("Wrong link.");
		}
	}

    @AfterClass
    public static void tearDown() {
        expectedType = null;
        expected.clear();
        actual.clear();
        expected = null;
        actual =null;
    }
}
