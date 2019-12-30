/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Library core is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library core as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library core as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.model.impl.axl;

import com.adobe.xmp.XMPError;
import com.adobe.xmp.XMPException;
import com.adobe.xmp.impl.VeraPDFMeta;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Maxim Plushchov
 */
@RunWith(Parameterized.class)
public class XMPRdfAboutTest {

    @Parameterized.Parameter
    public String filePath;

    @Parameterized.Parameter(value = 1)
    public Boolean isRdfAboutValid;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                // two equal rdf:about
                {"/org/verapdf/model/impl/axl/xmp-rdf-about-check-1.xml", Boolean.TRUE},
                // two different rdf:about
                {"/org/verapdf/model/impl/axl/xmp-rdf-about-check-2.xml", Boolean.FALSE},
                // empty and not empty rdf:about
                {"/org/verapdf/model/impl/axl/xmp-rdf-about-check-3.xml", Boolean.TRUE},
                // not empty and missing rdf:about
                {"/org/verapdf/model/impl/axl/xmp-rdf-about-check-4.xml", Boolean.TRUE},
                // one not-empty rdf:about
                {"/org/verapdf/model/impl/axl/xmp-rdf-about-check-5.xml", Boolean.TRUE},
                // one empty rdf:about
                {"/org/verapdf/model/impl/axl/xmp-rdf-about-check-6.xml", Boolean.TRUE},
                // one missing rdf:about
                {"/org/verapdf/model/impl/axl/xmp-rdf-about-check-7.xml", Boolean.TRUE},
                // not empty and " " (1 space) rdf-about
                {"/org/verapdf/model/impl/axl/xmp-rdf-about-check-8.xml", Boolean.FALSE},
                // not empty and "  " (2 spaces) rdf-about
                {"/org/verapdf/model/impl/axl/xmp-rdf-about-check-9.xml", Boolean.FALSE},
                // two equal and one different rdf:about
                {"/org/verapdf/model/impl/axl/xmp-rdf-about-check-10.xml", Boolean.FALSE},
                // not empty, empty and missing rdf:about
                {"/org/verapdf/model/impl/axl/xmp-rdf-about-check-11.xml", Boolean.TRUE},
                // empty and " " (1 space) rdf:about
                {"/org/verapdf/model/impl/axl/xmp-rdf-about-check-12.xml", Boolean.TRUE},
                // two rdf:about, differs only by leading space
                {"/org/verapdf/model/impl/axl/xmp-rdf-about-check-13.xml", Boolean.FALSE},
                // two rdf:about, differs only by trailing space
                {"/org/verapdf/model/impl/axl/xmp-rdf-about-check-14.xml", Boolean.FALSE}
        });
    }

    @Test
    public void test() throws URISyntaxException, XMPException, IOException {
        try (FileInputStream in = new FileInputStream(getSystemIndependentPath(this.filePath))) {
            VeraPDFMeta.parse(in);
            assertTrue(isRdfAboutValid);
        } catch (XMPException e){
            assertFalse(isRdfAboutValid);
            assertEquals(XMPError.BADXMP, e.getErrorCode());
            assertEquals("Mismatched top level rdf:about values", e.getMessage());
        }
    }

    private static String getSystemIndependentPath(String path)
            throws URISyntaxException {
        URL resourceUrl = ClassLoader.class.getResource(path);
        Path resourcePath = Paths.get(resourceUrl.toURI());
        return resourcePath.toString();
    }
}
