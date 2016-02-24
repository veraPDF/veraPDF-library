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
public class XMPHeaderTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"/model/impl/axl/xmp-empty-rdf.xml", null, null},
                {"/model/impl/axl/xmp-header-check-1.xml", "234", null},
                {"/model/impl/axl/xmp-header-check-2.xml", "234", "UTF8"},
                {"/model/impl/axl/xmp-header-check-3.xml", "234", "UTF8"},
                {"/model/impl/axl/xmp-header-check-4.xml", null, null},
                {"/model/impl/axl/xmp-header-check-5.xml", null, "UTF8"},
                {"/model/impl/axl/xmp-header-check-6.xml", "234\"    encoding  =   \"UTF8", null}
        });
    }

    @Parameterized.Parameter
    public String filePath;

    @Parameterized.Parameter(value = 1)
    public String bytes;

    @Parameterized.Parameter(value = 2)
    public String encoding;

    @Test
    public void test() throws URISyntaxException, FileNotFoundException, XMPException {
        FileInputStream in = new FileInputStream(getSystemIndependentPath(filePath));
        VeraPDFMeta meta = VeraPDFMeta.parse(in);
        AXLXMPPackage pack = new AXLXMPPackage(meta, true, null);
        assertEquals(bytes, pack.getbytes());
        assertEquals(encoding, pack.getencoding());
    }

    private static String getSystemIndependentPath(String path)
            throws URISyntaxException {
        URL resourceUrl = ClassLoader.class.getResource(path);
        Path resourcePath = Paths.get(resourceUrl.toURI());
        return resourcePath.toString();
    }
}
