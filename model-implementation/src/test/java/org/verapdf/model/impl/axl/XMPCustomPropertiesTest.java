package org.verapdf.model.impl.axl;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.impl.VeraPDFMeta;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.verapdf.model.xmplayer.XMPProperty;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * @author Maksim Bezrukov
 */
@RunWith(Parameterized.class)
public class XMPCustomPropertiesTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"/model/impl/axl/xmp-custom-structured-property-check-1.xml", false, false, true, true, true, true},
                {"/model/impl/axl/xmp-custom-structured-property-check-2.xml", false, false, false, false, null, null},
                {"/model/impl/axl/xmp-custom-structured-property-check-3.xml", false, false, false, true, null, false}
        });
    }

    @Parameterized.Parameter
    public String filePath;

    @Parameterized.Parameter(value = 1)
    public Boolean isPredefinedForPDFA_1;

    @Parameterized.Parameter(value = 2)
    public Boolean isPredefinedForPDFA_2_3;

    @Parameterized.Parameter(value = 3)
    public Boolean isDefinedInCurrentPackageForPDFA_1;

    @Parameterized.Parameter(value = 4)
    public Boolean isDefinedInCurrentPackageForPDFA_2_3;

    @Parameterized.Parameter(value = 5)
    public Boolean isValueTypeCorrectForPDFA_1;

    @Parameterized.Parameter(value = 6)
    public Boolean isValueTypeCorrectForPDFA_2_3;

    @Test
    public void testIdentification() throws URISyntaxException, FileNotFoundException, XMPException {
        FileInputStream in = new FileInputStream(getSystemIndependentPath(filePath));
        VeraPDFMeta meta = VeraPDFMeta.parse(in);
        AXLXMPPackage pack = new AXLXMPPackage(meta, true, null);
        for (Object obj : pack.getLinkedObjects(AXLXMPPackage.PROPERTIES)) {
            XMPProperty prop = (XMPProperty) obj;
            assertEquals(isPredefinedForPDFA_1, prop.getisPredefinedForPDFA_1());
            assertEquals(isPredefinedForPDFA_2_3, prop.getisPredefinedForPDFA_2_3());
            assertEquals(isDefinedInCurrentPackageForPDFA_1, prop.getisDefinedInCurrentPackageForPDFA_1());
            assertEquals(isDefinedInCurrentPackageForPDFA_2_3, prop.getisDefinedInCurrentPackageForPDFA_2_3());
            assertEquals(false, prop.getisDefinedInMainPackage());
            assertEquals(isValueTypeCorrectForPDFA_1, prop.getisValueTypeCorrectForPDFA_1());
            assertEquals(isValueTypeCorrectForPDFA_2_3, prop.getisValueTypeCorrectForPDFA_2_3());
        }
        AXLMainXMPPackage mainwPack = new AXLMainXMPPackage(meta, true);
        for (Object obj : mainwPack.getLinkedObjects(AXLXMPPackage.PROPERTIES)) {
            XMPProperty prop = (XMPProperty) obj;
            assertEquals(isPredefinedForPDFA_1, prop.getisPredefinedForPDFA_1());
            assertEquals(isPredefinedForPDFA_2_3, prop.getisPredefinedForPDFA_2_3());
            assertEquals(isDefinedInCurrentPackageForPDFA_1, prop.getisDefinedInCurrentPackageForPDFA_1());
            assertEquals(isDefinedInCurrentPackageForPDFA_2_3, prop.getisDefinedInCurrentPackageForPDFA_2_3());
            assertEquals(isDefinedInCurrentPackageForPDFA_2_3, prop.getisDefinedInMainPackage());
            assertEquals(isValueTypeCorrectForPDFA_1, prop.getisValueTypeCorrectForPDFA_1());
            assertEquals(isValueTypeCorrectForPDFA_2_3, prop.getisValueTypeCorrectForPDFA_2_3());
        }
    }

    private static String getSystemIndependentPath(String path)
            throws URISyntaxException {
        URL resourceUrl = ClassLoader.class.getResource(path);
        Path resourcePath = Paths.get(resourceUrl.toURI());
        return resourcePath.toString();
    }
}
