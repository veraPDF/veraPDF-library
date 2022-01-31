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
import java.util.List;

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
public class XMPIdentificationTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays
                .asList(new Object[][] {
                        {
                                "org/verapdf/model/impl/axl/xmp-identification-check-1.xml",
                                Integer.valueOf(1), Long.valueOf(1L), "B",
                                "pdfaid", "pdfaid", "pdfaid", "pdfaid" },
                        {
                                "org/verapdf/model/impl/axl/xmp-identification-check-2.xml",
                                Integer.valueOf(1), Long.valueOf(2L), "U",
                                "custom", "custom", "custom", "custom" },
                        {
                                "org/verapdf/model/impl/axl/xmp-identification-check-3.xml",
                                Integer.valueOf(1), null, null, "custom", null,
                                "pdfaid", "pdfaid" },
                        { "org/verapdf/model/impl/axl/xmp-empty-rdf.xml",
                                Integer.valueOf(0), null, null, null, null,
                                null, null } });
    }

    @Parameterized.Parameter
    public String filePath;

    @Parameterized.Parameter(value = 1)
    public int identificationSchemaNumber;

    @Parameterized.Parameter(value = 2)
    public Long part;

    @Parameterized.Parameter(value = 3)
    public String conformance;

    @Parameterized.Parameter(value = 4)
    public String partPrefix;

    @Parameterized.Parameter(value = 5)
    public String conformancePrefix;

    @Parameterized.Parameter(value = 6)
    public String amdPrefix;

    @Parameterized.Parameter(value = 7)
    public String corrPrefix;

    @Test
    public void test() throws URISyntaxException, XMPException, IOException {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(this.filePath)) {
            VeraPDFMeta meta = VeraPDFMeta.parse(in);
            AXLMainXMPPackage pack = new AXLMainXMPPackage(meta, true,
                    PDFAFlavour.PDFA_1_B);
            List<? extends org.verapdf.model.baselayer.Object> list = pack
                    .getLinkedObjects(AXLMainXMPPackage.IDENTIFICATION);
            assertEquals(this.identificationSchemaNumber, list.size());
            if (list.size() != 0) {
                AXLPDFAIdentification identification = (AXLPDFAIdentification) list
                        .get(0);
                assertEquals(this.part, identification.getpart());
                assertEquals(this.conformance, identification.getconformance());
                assertEquals(this.partPrefix, identification.getpartPrefix());
                assertEquals(this.conformancePrefix,
                        identification.getconformancePrefix());
                assertEquals(this.amdPrefix, identification.getamdPrefix());
                assertEquals(this.corrPrefix, identification.getcorrPrefix());
            }
        }
    }
}
