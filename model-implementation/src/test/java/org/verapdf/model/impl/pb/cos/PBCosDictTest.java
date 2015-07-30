package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.junit.AfterClass;
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

/**
 * @author Evgeniy Muravitskiy
 */
public class PBCosDictTest extends BaseTest {

    private static Long expectedLength;

    @BeforeClass
    public static void setUp() {
        expectedType = TYPES.contains(PBCosDict.COS_DICTIONARY_TYPE) ? PBCosDict.COS_DICTIONARY_TYPE : null;
        expectedID = null;

        COSDictionary dictionary = new COSDictionary();

        for (int index = 1; index < 6; index++) {
            dictionary.setInt(COSName.getPDFName(String.valueOf(index)), index);
        }
        dictionary.setItem(COSName.METADATA, new COSStream(new COSDictionary()));

        expectedLength = Long.valueOf(dictionary.size());

        actual = new PBCosDict(dictionary);
    }

    @Test
    public void testGetSizeMethod() {
        Assert.assertEquals(expectedLength, ((CosDict) actual).getsize());
    }

    @Test
    public void testGetKeysLink() {
        List<CosName> keys = (List<CosName>) actual.getLinkedObjects(PBCosDict.KEYS);
        for (int index = 1; index < expectedLength; index++) {
            Assert.assertEquals(keys.get(index - 1).getvalue(), String.valueOf(index));
        }
        Assert.assertEquals(keys.get(keys.size() - 1).getvalue(), COSName.METADATA.getName());
    }

    @Test
    public void testGetValuesLink() {
        List<? extends org.verapdf.model.baselayer.Object> values = actual.getLinkedObjects(PBCosDict.VALUES);
        for (int index = 1; index < expectedLength; index++) {
            Object object = values.get(index - 1);
            Assert.assertTrue(object instanceof CosInteger);
            Assert.assertEquals(((CosInteger) object).getintValue(), Long.valueOf(index));
        }
        Assert.assertTrue(values.get(values.size() - 1) instanceof CosStream);
    }

    @Test
    public void testGetMetadataLink() {
        List<PDMetadata> metadata = (List<PDMetadata>) actual.getLinkedObjects(PBCosDict.METADATA);
        Assert.assertTrue(metadata.size() == 1);
        Assert.assertTrue(metadata.get(0) != null);
    }

    @AfterClass
    public static void tearDown() {
        expectedType = null;
        actual = null;
        expectedLength = null;
    }
}
