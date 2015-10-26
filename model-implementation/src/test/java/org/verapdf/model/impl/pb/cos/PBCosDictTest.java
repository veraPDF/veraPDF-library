package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosDict;
import org.verapdf.model.coslayer.CosInteger;
import org.verapdf.model.coslayer.CosName;
import org.verapdf.model.coslayer.CosStream;
import org.verapdf.model.impl.BaseTest;
import org.verapdf.model.pdlayer.PDMetadata;

import java.util.List;

import static org.verapdf.model.impl.pb.cos.PBCosDict.COS_DICT_TYPE;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBCosDictTest extends BaseTest {

    private static long expectedLength;

    @BeforeClass
    public static void setUp() {
        expectedType = TYPES.contains(COS_DICT_TYPE) ? COS_DICT_TYPE : null;
        expectedID = null;

        COSDictionary dictionary = new COSDictionary();

        for (int index = 1; index < 6; index++) {
            dictionary.setInt(COSName.getPDFName(String.valueOf(index)), index);
        }
        dictionary.setItem(COSName.METADATA, new COSStream(new COSDictionary()));

        expectedLength = dictionary.size();

        actual = new PBCosDict(dictionary);
    }

    @Test
    public void testGetSizeMethod() {
        Assert.assertEquals(expectedLength, ((CosDict) actual).getsize().longValue());
    }

    @Test
    public void testGetKeysLink() {
        List<CosName> keys = (List<CosName>) actual
                .getLinkedObjects(PBCosDict.KEYS);
        for (int index = 1; index < expectedLength; index++) {
			String expected = keys.get(index - 1).getinternalRepresentation();
			Assert.assertEquals(expected, String.valueOf(index));
        }
		String expected = keys.get(keys.size() - 1).getinternalRepresentation();
		Assert.assertEquals(expected, COSName.METADATA.getName());
    }

    @Test
    public void testGetValuesLink() {
        List<? extends org.verapdf.model.baselayer.Object> values = actual
                .getLinkedObjects(PBCosDict.VALUES);
        for (int index = 1; index < expectedLength; index++) {
            Object object = values.get(index - 1);
            Assert.assertTrue(object instanceof CosInteger);
			Long expected = ((CosInteger) object).getintValue();
			Assert.assertEquals(expected, Long.valueOf(index));
        }
		Object object = values.get(values.size() - 1);
		Assert.assertTrue(object instanceof CosStream);
    }

    @Test
    public void testGetMetadataLink() {
        List<? extends Object> metadata = actual.getLinkedObjects(PBCosDict.METADATA);
        Assert.assertEquals(1, metadata.size());
        Assert.assertTrue(metadata.get(0) instanceof PDMetadata);
    }

}
