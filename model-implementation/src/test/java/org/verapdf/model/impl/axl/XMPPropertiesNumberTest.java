package org.verapdf.model.impl.axl;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.impl.VeraPDFMeta;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

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
public class XMPPropertiesNumberTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"/model/impl/axl/xmp-properties-number-check-1.xml", 4, 4},
                {"/model/impl/axl/xmp-properties-number-check-2.xml", 3, 3},
                {"/model/impl/axl/xmp-properties-number-check-3.xml", 3, 3},
                {"/model/impl/axl/xmp-properties-number-check-4.xml", 2, 2}
        });
    }

    @Parameterized.Parameter
    public String filePath;

    @Parameterized.Parameter(value = 1)
    public Integer metadataPropertiesNumber;

    @Parameterized.Parameter(value = 2)
    public Integer mainMetadataPropertiesNumber;

    @Test
    public void test() throws URISyntaxException, FileNotFoundException, XMPException {
        FileInputStream in = new FileInputStream(getSystemIndependentPath(filePath));
        VeraPDFMeta meta = VeraPDFMeta.parse(in);
        AXLXMPPackage pack = new AXLXMPPackage(meta, true, null);
        int packSize = pack.getLinkedObjects(AXLXMPPackage.PROPERTIES).size();
        assertEquals(metadataPropertiesNumber, Integer.valueOf(packSize));
        AXLMainXMPPackage mainPack = new AXLMainXMPPackage(meta, true);
        int mainPackSize = mainPack.getLinkedObjects(AXLXMPPackage.PROPERTIES).size();
        assertEquals(mainMetadataPropertiesNumber, Integer.valueOf(mainPackSize));
    }

    private static String getSystemIndependentPath(String path)
            throws URISyntaxException {
        URL resourceUrl = ClassLoader.class.getResource(path);
        Path resourcePath = Paths.get(resourceUrl.toURI());
        return resourcePath.toString();
    }
}
