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
        res.add(new Object[]{"/model/impl/axl/xmp-custom-structured-property-check-1.xml", true, true, true, true});
        res.add(new Object[]{"/model/impl/axl/xmp-custom-structured-property-check-2.xml", false, false, null, null});
        res.add(new Object[]{"/model/impl/axl/xmp-custom-structured-property-check-3.xml", false, true, null, false});

        for (int i = 1; i <= 25; ++i) {
            String filePassName = "/model/impl/axl/xmp-types-in-extension-check-" + i + "-pass.xml";
            res.add(new Object[]{filePassName, true, true, true, true});
            String fileFailName = "/model/impl/axl/xmp-types-in-extension-check-" + i + "-fail.xml";
            res.add(new Object[]{fileFailName, true, true, false, false});
        }
        for (int i = 26; i <= 35; ++i) {
            String filePassName = "/model/impl/axl/xmp-types-in-extension-check-" + i + "-pass.xml";
            res.add(new Object[]{filePassName, false, true, null, true});
            String fileFailName = "/model/impl/axl/xmp-types-in-extension-check-" + i + "-fail.xml";
            res.add(new Object[]{fileFailName, false, true, null, false});
        }

        res.add(new Object[]{"/model/impl/axl/xmp-gps-in-extension-check-2004-pass.xml", true, true, true, true});
        res.add(new Object[]{"/model/impl/axl/xmp-gps-in-extension-check-2004-fail.xml", true, true, false, false});
        res.add(new Object[]{"/model/impl/axl/xmp-gps-in-extension-check-2005-pass.xml", true, true, false, true});
        res.add(new Object[]{"/model/impl/axl/xmp-gps-in-extension-check-2005-fail.xml", true, true, false, false});

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
        AXLXMPPackage pack = new AXLXMPPackage(meta, true, null);
        for (Object obj : pack.getLinkedObjects(AXLXMPPackage.PROPERTIES)) {
            XMPProperty prop = (XMPProperty) obj;
            assertEquals(false, prop.getisPredefinedInXMP2004());
            assertEquals(false, prop.getisPredefinedInXMP2005());
            //TODO: isDefinedInCurrentPackage different check for pdfa-1 and pdfa-2
            assertEquals(isDefinedInCurrentPackageForPDFA_1, prop.getisDefinedInCurrentPackage());
//            assertEquals(isDefinedInCurrentPackageForPDFA_2_3, prop.getisDefinedInCurrentPackageForPDFA_2_3());
            assertEquals(false, prop.getisDefinedInMainPackage());
            //TODO: isValueTypeCorrect different check for pdfa-1 and pdfa-2
            assertEquals(isValueTypeCorrectForPDFA_1, prop.getisValueTypeCorrect());
//            assertEquals(isValueTypeCorrectForPDFA_2_3, prop.getisValueTypeCorrectForPDFA_2_3());
        }
        AXLMainXMPPackage mainwPack = new AXLMainXMPPackage(meta, true);
        for (Object obj : mainwPack.getLinkedObjects(AXLXMPPackage.PROPERTIES)) {
            XMPProperty prop = (XMPProperty) obj;
            assertEquals(false, prop.getisPredefinedInXMP2004());
            assertEquals(false, prop.getisPredefinedInXMP2005());
            //TODO: isDefinedInCurrentPackage different check for pdfa-1 and pdfa-2
            assertEquals(isDefinedInCurrentPackageForPDFA_1, prop.getisDefinedInCurrentPackage());
//            assertEquals(isDefinedInCurrentPackageForPDFA_2_3, prop.getisDefinedInCurrentPackageForPDFA_2_3());
            assertEquals(isDefinedInCurrentPackageForPDFA_2_3, prop.getisDefinedInMainPackage());
            //TODO: isValueTypeCorrect different check for pdfa-1 and pdfa-2
            assertEquals(isValueTypeCorrectForPDFA_1, prop.getisValueTypeCorrect());
//            assertEquals(isValueTypeCorrectForPDFA_2_3, prop.getisValueTypeCorrectForPDFA_2_3());
        }
    }

    private static String getSystemIndependentPath(String path)
            throws URISyntaxException {
        URL resourceUrl = ClassLoader.class.getResource(path);
        Path resourcePath = Paths.get(resourceUrl.toURI());
        return resourcePath.toString();
    }
}
