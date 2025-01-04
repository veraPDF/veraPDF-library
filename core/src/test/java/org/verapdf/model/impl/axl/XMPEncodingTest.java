/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2025, veraPDF Consortium <info@verapdf.org>
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

import org.verapdf.xmp.XMPException;
import org.verapdf.xmp.impl.VeraPDFMeta;

/**
 * @author Maxim Plushchov
 */
@RunWith(Parameterized.class)
public class XMPEncodingTest {

    @Parameterized.Parameter
    public String filePath;

    @Parameterized.Parameter(value = 1)
    public String actualEncoding;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                // UTF-8 without BOM
                {"org/verapdf/model/impl/axl/xmp-encoding-check-1.xml", "UTF-8"},
                // UTF-8 with BOM
                {"org/verapdf/model/impl/axl/xmp-encoding-check-2.xml", "UTF-8"},
                // UTF-16 LE without BOM
                {"org/verapdf/model/impl/axl/xmp-encoding-check-3.xml", "UTF-16LE"},
                // UTF-16 LE with BOM
                {"org/verapdf/model/impl/axl/xmp-encoding-check-4.xml", "UTF-16LE"},
                // UTF-16 BE without BOM
                {"org/verapdf/model/impl/axl/xmp-encoding-check-5.xml", "UTF-16BE"},
                // UTF-16 BE with BOM
                {"org/verapdf/model/impl/axl/xmp-encoding-check-6.xml", "UTF-16BE"},
                // UTF-32 BE without BOM
                {"org/verapdf/model/impl/axl/xmp-encoding-check-7.xml", "ISO-10646-UCS-4"},
                // UTF-32 BE with BOM
                {"org/verapdf/model/impl/axl/xmp-encoding-check-8.xml", "UTF-32BE"},
        });
    }

    @Test
    public void test() throws URISyntaxException, XMPException, IOException {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(this.filePath)) {
            VeraPDFMeta veraPDFMeta = VeraPDFMeta.parse(in);
            assertEquals(actualEncoding, veraPDFMeta.getActualEncoding());
        }
    }
}
