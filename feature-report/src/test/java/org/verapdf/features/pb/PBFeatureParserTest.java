package org.verapdf.features.pb;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.Test;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.pb.objects.*;
import org.verapdf.features.tools.FeaturesCollection;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

/**
 * @author Maksim Bezrukov
 */
public class PBFeatureParserTest {

    @Test
    public void featuresObjectsTypeTest() {
        assertEquals(FeaturesObjectTypesEnum.ANNOTATION, new PBAnnotationFeaturesObject(null, null, null, null, null, null).getType());
        assertEquals(FeaturesObjectTypesEnum.COLORSPACE, new PBColorSpaceFeaturesObject(null, null, null, null, null, null, null, null, null, null).getType());
        assertEquals(FeaturesObjectTypesEnum.DOCUMENT_SECURITY, new PBDocSecurityFeaturesObject(null).getType());
        assertEquals(FeaturesObjectTypesEnum.EMBEDDED_FILE, new PBEmbeddedFileFeaturesObject(null, 0).getType());
        assertEquals(FeaturesObjectTypesEnum.EXT_G_STATE, new PBExtGStateFeaturesObject(null, null, null, null, null, null, null).getType());
        assertEquals(FeaturesObjectTypesEnum.FONT, new PBFontFeaturesObject(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null).getType());
        assertEquals(FeaturesObjectTypesEnum.FORM_XOBJECT, new PBFormXObjectFeaturesObject(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null).getType());
        assertEquals(FeaturesObjectTypesEnum.ICCPROFILE, new PBICCProfileFeaturesObject(null, null, null, null).getType());
        assertEquals(FeaturesObjectTypesEnum.IMAGE_XOBJECT, new PBImageXObjectFeaturesObject(null, null, null, null, null, null, null, null, null, null).getType());
        assertEquals(FeaturesObjectTypesEnum.INFORMATION_DICTIONARY, new PBInfoDictFeaturesObject(null).getType());
        assertEquals(FeaturesObjectTypesEnum.LOW_LEVEL_INFO, new PBLowLvlInfoFeaturesObject(null).getType());
        assertEquals(FeaturesObjectTypesEnum.METADATA, new PBMetadataFeaturesObject(null).getType());
        assertEquals(FeaturesObjectTypesEnum.OUTLINES, new PBOutlinesFeaturesObject(null).getType());
        assertEquals(FeaturesObjectTypesEnum.OUTPUTINTENT, new PBOutputIntentsFeaturesObject(null, null, null).getType());
        assertEquals(FeaturesObjectTypesEnum.PAGE, new PBPageFeaturesObject(null, null, null, null, null, null, null, null, null, null, null, 0).getType());
        assertEquals(FeaturesObjectTypesEnum.PROCSET, new PBProcSetFeaturesObject(null, null, null, null, null, null).getType());
        assertEquals(FeaturesObjectTypesEnum.PROPERTIES, new PBPropertiesDictFeaturesObject(null, null, null, null, null, null).getType());
        assertEquals(FeaturesObjectTypesEnum.SHADING, new PBShadingFeaturesObject(null, null, null, null, null, null, null).getType());
        assertEquals(FeaturesObjectTypesEnum.PATTERN, new PBShadingPatternFeaturesObject(null, null, null, null, null, null, null, null).getType());
        assertEquals(FeaturesObjectTypesEnum.PATTERN, new PBTilingPatternFeaturesObject(null, null, null, null, null, null, null, null, null, null, null, null, null, null).getType());
    }

    @Test
    public void test() throws URISyntaxException, IOException {
        File pdf = new File(getSystemIndependentPath("/FR.pdf"));
        PDDocument document = PDDocument.load(pdf, false, true);
        FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document);
        //TODO: finish this test
    }

    private static String getSystemIndependentPath(String path) throws URISyntaxException {
        URL resourceUrl = ClassLoader.class.getResource(path);
        Path resourcePath = Paths.get(resourceUrl.toURI());
        return resourcePath.toString();
    }
}
