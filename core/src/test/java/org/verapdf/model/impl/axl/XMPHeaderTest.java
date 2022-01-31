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

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import org.verapdf.xmp.XMPException;
import org.verapdf.xmp.impl.VeraPDFMeta;

/**
 * @author Maksim Bezrukov
 */
@RunWith(Parameterized.class)
public class XMPHeaderTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"org/verapdf/model/impl/axl/xmp-empty-rdf.xml", null, null},
                {"org/verapdf/model/impl/axl/xmp-header-check-1.xml", "234", null},
                {"org/verapdf/model/impl/axl/xmp-header-check-2.xml", "234", "UTF8"},
                {"org/verapdf/model/impl/axl/xmp-header-check-3.xml", "234", "UTF8"},
                {"org/verapdf/model/impl/axl/xmp-header-check-4.xml", null, null},
                {"org/verapdf/model/impl/axl/xmp-header-check-5.xml", null, "UTF8"},
                {"org/verapdf/model/impl/axl/xmp-header-check-6.xml", "234\"    encoding  =   \"UTF8", null}
        });
    }

    @Parameterized.Parameter
    public String filePath;

    @Parameterized.Parameter(value = 1)
    public String bytes;

    @Parameterized.Parameter(value = 2)
    public String encoding;

    @Test
    public void test() throws URISyntaxException, XMPException, IOException {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(this.filePath)) {
        VeraPDFMeta meta = VeraPDFMeta.parse(in);
        AXLXMPPackage pack = new AXLXMPPackage(meta, true, null, PDFAFlavour.PDFA_1_B);
        assertEquals(this.bytes, pack.getbytes());
        assertEquals(this.encoding, pack.getencoding());
        }
    }
}
