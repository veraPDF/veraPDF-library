package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSName;
import org.junit.*;
import org.verapdf.model.ModelHelper;
import org.verapdf.model.coslayer.CosName;
import org.verapdf.model.impl.BaseTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBCosNameTest extends BaseTest {

    public static List<COSName> expectedNames;
    public static List<CosName> actualNames;

    @BeforeClass
    public static void setUp() {
        expectedType = TYPES.contains(PBCosName.COS_NAME_TYPE) ? PBCosName.COS_NAME_TYPE : null;
        expectedID = null;

        setUpCOSNames();

        actualNames = new ArrayList<>(expectedNames.size());
        for (COSName name : expectedNames) {
            actualNames.add(new PBCosName(name));
        }
    }

    private static void setUpCOSNames() {
        expectedNames = new ArrayList<>(6);

        expectedNames.add(COSName.INDEX);
        expectedNames.add(COSName.ACRO_FORM);
        expectedNames.add(COSName.ATTACHED);
        expectedNames.add(COSName.BITS_PER_COORDINATE);
		expectedNames.add(COSName.getPDFName("FirstCustom"));
		expectedNames.add(COSName.getPDFName("SecondCustom"));
    }

    @Test
    public void testGetValueMethod() {
        for (int i = 0; i < expectedNames.size(); i++) {
			String expected = expectedNames.get(i).getName();
			String actualValue = actualNames.get(i).getinternalRepresentation();
			Assert.assertEquals(expected, actualValue);
        }
    }

    @Test
    public void testGetOriginalLength() {
        for (int i = 0; i < expectedNames.size(); i++) {
            Long originalLength = Long.valueOf(expectedNames.get(i).getName().length());
			Long actaulValue = Long.valueOf(actualNames.get(i).getinternalRepresentation().length());
            Assert.assertEquals(originalLength, actaulValue);
        }
    }

    @Override
    @Test
    public void testTypeAndID() {
        for (CosName name : actualNames) {
            Assert.assertEquals(expectedType, name.getObjectType());
            Assert.assertEquals(expectedID, name.getID());
        }
    }

	@Override
	@Test
	public void testLinksMethod() {
		for (CosName name : actualNames) {
			List<String> expectedLinks = ModelHelper.getListOfLinks(name.getObjectType());
			for (String link : expectedLinks) {
				Assert.assertNotNull(name.getLinkedObjects(link));
			}
			expectedLinks.clear();
		}
	}

	@Override
    @Test(expected = IllegalAccessError.class)
	public void testNonexistentParentLink() {
		for (CosName name : actualNames) {
			name.getLinkedObjects("Wrong link.");
		}
	}

    @AfterClass
    public static void tearDown() throws IOException {
		BaseTest.tearDown();
        expectedNames.clear();
        actualNames.clear();
        expectedNames = null;
        actualNames =null;
    }
}
