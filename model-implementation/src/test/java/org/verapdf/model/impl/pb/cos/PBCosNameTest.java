package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSName;
import org.junit.*;
import org.verapdf.model.ModelHelper;
import org.verapdf.model.coslayer.CosName;
import org.verapdf.model.impl.BaseTest;

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

        addCOSName(COSName.INDEX);
        addCOSName(COSName.ACRO_FORM);
        addCOSName(COSName.ATTACHED);
        addCOSName(COSName.BITS_PER_COORDINATE);
        final String firstCustom = "FirstCustom";
        final String secondCustom = "SecondCustom";
        //expectedNames.add(COSName.getPDFName(firstCustom, firstCustom.length()));
        //expectedNames.add(COSName.getPDFName(secondCustom, secondCustom.length()));
    }

    private static void addCOSName(COSName cosName) {
        String name = cosName.getName();
        //expectedNames.add(COSName.getPDFName(name, name.length()));
    }

	@Ignore
    @Test
    public void testGetValueMethod() {
        for (int i = 0; i < expectedNames.size(); i++) {
            //Assert.assertEquals(expectedNames.get(i).getName(), actualNames.get(i).getvalue());
        }
    }

	@Ignore
    @Test
    public void testGetOriginalLength() {
        for (int i = 0; i < expectedNames.size(); i++) {
            //final Long originalLength = Long.valueOf(expectedNames.get(i).getOriginalLength().longValue());
            //Assert.assertEquals(originalLength, actualNames.get(i).getorigLength());
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

	@Ignore
	@Override
    @Test(expected = IllegalAccessError.class)
	public void testNonexistentParentLink() {
		for (CosName name : actualNames) {
			name.getLinkedObjects("Wrong link.");
		}
	}

    @AfterClass
    public static void tearDown() {
        expectedType = null;
        expectedNames.clear();
        actualNames.clear();
        expectedNames = null;
        actualNames =null;
    }
}
