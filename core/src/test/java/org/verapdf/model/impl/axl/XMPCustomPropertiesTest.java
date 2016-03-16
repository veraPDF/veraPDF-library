package org.verapdf.model.impl.axl;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.impl.VeraPDFMeta;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.verapdf.model.xmplayer.XMPProperty;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Maksim Bezrukov
 */
@RunWith(Parameterized.class)
public class XMPCustomPropertiesTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        List<Object[]> res = new ArrayList<>();
        res.add(new Object[]{"/org/verapdf/model/impl/axl/xmp-custom-structured-property-check-1.xml", true, true, true, true});
        res.add(new Object[]{"/org/verapdf/model/impl/axl/xmp-custom-structured-property-check-2.xml", false, false, null, null});
        res.add(new Object[]{"/org/verapdf/model/impl/axl/xmp-custom-structured-property-check-3.xml", false, true, null, false});

        for (int i = 1; i <= 25; ++i) {
            String filePassName = "/org/verapdf/model/impl/axl/xmp-types-in-extension-check-" + i + "-pass.xml";
            res.add(new Object[]{filePassName, true, true, true, true});
            String fileFailName = "/org/verapdf/model/impl/axl/xmp-types-in-extension-check-" + i + "-fail.xml";
            res.add(new Object[]{fileFailName, true, true, false, false});
        }
        for (int i = 26; i <= 35; ++i) {
            String filePassName = "/org/verapdf/model/impl/axl/xmp-types-in-extension-check-" + i + "-pass.xml";
            res.add(new Object[]{filePassName, false, true, null, true});
            String fileFailName = "/org/verapdf/model/impl/axl/xmp-types-in-extension-check-" + i + "-fail.xml";
            res.add(new Object[]{fileFailName, false, true, null, false});
        }

        res.add(new Object[]{"/org/verapdf/model/impl/axl/xmp-gps-in-extension-check-2004-pass.xml", true, true, true, true});
        res.add(new Object[]{"/org/verapdf/model/impl/axl/xmp-gps-in-extension-check-2004-fail.xml", true, true, false, false});
        res.add(new Object[]{"/org/verapdf/model/impl/axl/xmp-gps-in-extension-check-2005-pass.xml", true, true, false, true});
        res.add(new Object[]{"/org/verapdf/model/impl/axl/xmp-gps-in-extension-check-2005-fail.xml", true, true, false, false});

        return res;
    }

    @Parameterized.Parameter
    public String filePath;

    @Parameterized.Parameter(value = 1)
    public Boolean isDefinedInCurrentPackageForPDFA_1;

    @Parameterized.Parameter(value = 2)
    public Boolean isDefinedInCurrentPackageForPDFA_2_3;

    @Parameterized.Parameter(value = 3)
    public Boolean isValueTypeCorrectForPDFA_1;

    @Parameterized.Parameter(value = 4)
    public Boolean isValueTypeCorrectForPDFA_2_3;

    @Test
    public void test() throws URISyntaxException, FileNotFoundException, XMPException {
        FileInputStream in = new FileInputStream(getSystemIndependentPath(filePath));
        VeraPDFMeta meta = VeraPDFMeta.parse(in);
        AXLXMPPackage pack = new AXLXMPPackage(meta, true, null, PDFAFlavour.PDFA_1_B);
        for (Object obj : pack.getLinkedObjects(AXLXMPPackage.PROPERTIES)) {
            XMPProperty prop = (XMPProperty) obj;
            assertEquals(false, prop.getisPredefinedInXMP2004());
            assertEquals(false, prop.getisPredefinedInXMP2005());
            assertEquals(isDefinedInCurrentPackageForPDFA_1, prop.getisDefinedInCurrentPackage());
            assertEquals(false, prop.getisDefinedInMainPackage());
            assertEquals(isValueTypeCorrectForPDFA_1, prop.getisValueTypeCorrect());
        }
        AXLMainXMPPackage mainPack = new AXLMainXMPPackage(meta, true, PDFAFlavour.PDFA_1_B);
        for (Object obj : mainPack.getLinkedObjects(AXLXMPPackage.PROPERTIES)) {
            XMPProperty prop = (XMPProperty) obj;
            assertEquals(false, prop.getisPredefinedInXMP2004());
            assertEquals(false, prop.getisPredefinedInXMP2005());
            assertEquals(isDefinedInCurrentPackageForPDFA_1, prop.getisDefinedInCurrentPackage());
            assertEquals(isDefinedInCurrentPackageForPDFA_2_3, prop.getisDefinedInMainPackage());
            assertEquals(isValueTypeCorrectForPDFA_1, prop.getisValueTypeCorrect());
        }
        AXLXMPPackage pack2 = new AXLXMPPackage(meta, true, true, null, PDFAFlavour.PDFA_2_B);
        for (Object obj : pack2.getLinkedObjects(AXLXMPPackage.PROPERTIES)) {
            XMPProperty prop = (XMPProperty) obj;
            assertEquals(isDefinedInCurrentPackageForPDFA_2_3, prop.getisDefinedInCurrentPackage());
            assertEquals(false, prop.getisDefinedInMainPackage());
            assertEquals(isValueTypeCorrectForPDFA_2_3, prop.getisValueTypeCorrect());
        }
        AXLMainXMPPackage mainPack2 = new AXLMainXMPPackage(meta, true, true, PDFAFlavour.PDFA_2_B);
        for (Object obj : mainPack2.getLinkedObjects(AXLXMPPackage.PROPERTIES)) {
            XMPProperty prop = (XMPProperty) obj;
            assertEquals(isDefinedInCurrentPackageForPDFA_2_3, prop.getisDefinedInCurrentPackage());
            assertEquals(isDefinedInCurrentPackageForPDFA_2_3, prop.getisDefinedInMainPackage());
            assertEquals(isValueTypeCorrectForPDFA_2_3, prop.getisValueTypeCorrect());
        }
    }

    private static String getSystemIndependentPath(String path)
            throws URISyntaxException {
        URL resourceUrl = ClassLoader.class.getResource(path);
        Path resourcePath = Paths.get(resourceUrl.toURI());
        return resourcePath.toString();
    }
}
