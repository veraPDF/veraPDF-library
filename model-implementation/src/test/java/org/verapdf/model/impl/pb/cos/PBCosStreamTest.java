package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.*;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosStream;
import org.verapdf.model.impl.BaseTest;

import java.io.IOException;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBCosStreamTest extends BaseTest {

    private static final Long expectedOriginLength = Long.valueOf(10);
    private static String[] expectedFiltersArray;
    public static final String expectedFFilterName = COSName.ASCII85_DECODE.getName();

    @BeforeClass
    public static void setUp() throws IOException {
        expectedType = TYPES.contains(PBCosStream.COS_STREAM_TYPE) ? PBCosStream.COS_STREAM_TYPE : null;
        expectedID = null;

        COSStream stream = getCosStream();

        actual = new PBCosStream(stream);
    }

    private static COSStream getCosStream() throws IOException {
        COSArray array = new COSArray();
        expectedFiltersArray = new String[2];

        array.add(COSName.LZW_DECODE);
        array.add(COSName.FLATE_DECODE);

        expectedFiltersArray[0] = COSName.LZW_DECODE.getName();
        expectedFiltersArray[1] = COSName.FLATE_DECODE.getName();

        COSObject object = new COSObject(new COSDictionary());
        object.setObjectNumber(Long.valueOf(10));
        object.setGenerationNumber(Integer.valueOf(0));

        COSStream stream = new COSStream(new COSDictionary());
        stream.setOriginLength(expectedOriginLength);
        stream.setInt(COSName.LENGTH, expectedOriginLength.intValue() - 1);
        stream.setItem(COSName.FILTER, array);
        stream.setItem(COSName.F_FILTER, COSName.ASCII85_DECODE);
        stream.setItem(COSName.F_DECODE_PARMS, object);
        return stream;
    }

    @Test
    public void testGetLengthMethod() {
        Assert.assertEquals(expectedOriginLength - 1, ((CosStream) actual).getLength().longValue());
    }

    @Test
    public void testGetFiltersMethod() {
        String[] actualFilters = ((CosStream) actual).getfilters().split(" ");
        Assert.assertEquals(expectedFiltersArray.length, actualFilters.length);
        for (int index = 0; index < actualFilters.length; index++) {
            Assert.assertEquals(expectedFiltersArray[index], actualFilters[index]);
        }
    }

    @Test
    public void testGetFMethod() {
        Assert.assertTrue(((CosStream) actual).getF() == null);
    }

    @Test
    public void testFFilterMethod() {
        Assert.assertEquals(expectedFFilterName, ((CosStream) actual).getFFilter());
    }

    @Test
    public void testFDecodeParmsMethod() {
        Assert.assertTrue(((CosStream) actual).getFDecodeParms() != null);
    }

    @Test
    public void testGetSpacingCompliesPDFAMethod() {
        Assert.assertTrue(((CosStream) actual).getspacingCompliesPDFA());
    }

    @Test
    public void testGetIsLengthCorrect() {
        Assert.assertFalse(((CosStream) actual).getisLengthCorrect());
    }

    @Test
    public void testParentLink() {
        final List<? extends Object> parentLink = actual.getLinkedObjects(PBCosDict.METADATA);
        Assert.assertEquals(parentLink.size(), 0);
    }

    @AfterClass
    public static void tearDown() {
        expectedType = null;
        actual = null;
        for (int i = 0; i < expectedFiltersArray.length; i++) {
            expectedFiltersArray[i] = null;
        }
        expectedFiltersArray = null;
    }
}
