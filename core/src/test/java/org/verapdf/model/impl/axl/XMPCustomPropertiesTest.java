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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.verapdf.model.xmplayer.XMPProperty;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.impl.VeraPDFMeta;

/**
 * @author Maksim Bezrukov
 */
@RunWith(Parameterized.class)
public class XMPCustomPropertiesTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        List<Object[]> res = new ArrayList<>();
        res.add(new Object[] {
                "org/verapdf/model/impl/axl/xmp-custom-structured-property-check-1.xml",
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE });
        res.add(new Object[] {
                "org/verapdf/model/impl/axl/xmp-custom-structured-property-check-2.xml",
                Boolean.FALSE, Boolean.FALSE, null, null });
        res.add(new Object[] {
                "org/verapdf/model/impl/axl/xmp-custom-structured-property-check-3.xml",
                Boolean.FALSE, Boolean.TRUE, null, Boolean.FALSE });

        for (int i = 1; i <= 25; ++i) {
            String filePassName = "org/verapdf/model/impl/axl/xmp-types-in-extension-check-"
                    + i + "-pass.xml";
            res.add(new Object[] { filePassName, Boolean.TRUE, Boolean.TRUE,
                    Boolean.TRUE, Boolean.TRUE });
            String fileFailName = "org/verapdf/model/impl/axl/xmp-types-in-extension-check-"
                    + i + "-fail.xml";
            res.add(new Object[] { fileFailName, Boolean.TRUE, Boolean.TRUE,
                    Boolean.FALSE, Boolean.FALSE });
        }
        for (int i = 26; i <= 35; ++i) {
            String filePassName = "org/verapdf/model/impl/axl/xmp-types-in-extension-check-"
                    + i + "-pass.xml";
            res.add(new Object[] { filePassName, Boolean.FALSE, Boolean.TRUE,
                    null, Boolean.TRUE });
            String fileFailName = "org/verapdf/model/impl/axl/xmp-types-in-extension-check-"
                    + i + "-fail.xml";
            res.add(new Object[] { fileFailName, Boolean.FALSE, Boolean.TRUE,
                    null, Boolean.FALSE });
        }

        res.add(new Object[] {
                "org/verapdf/model/impl/axl/xmp-gps-in-extension-check-2004-pass.xml",
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE });
        res.add(new Object[] {
                "org/verapdf/model/impl/axl/xmp-gps-in-extension-check-2004-fail.xml",
                Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE });
        res.add(new Object[] {
                "org/verapdf/model/impl/axl/xmp-gps-in-extension-check-2005-pass.xml",
                Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE });
        res.add(new Object[] {
                "org/verapdf/model/impl/axl/xmp-gps-in-extension-check-2005-fail.xml",
                Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE });

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
    public void test() throws URISyntaxException, XMPException, IOException {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(this.filePath)) {
            VeraPDFMeta meta = VeraPDFMeta.parse(in);
            AXLXMPPackage pack = new AXLXMPPackage(meta, true, null,
                    PDFAFlavour.PDFA_1_B);
            for (Object obj : pack.getLinkedObjects(AXLXMPPackage.PROPERTIES)) {
                XMPProperty prop = (XMPProperty) obj;
                assertEquals(Boolean.FALSE, prop.getisPredefinedInXMP2004());
                assertEquals(Boolean.FALSE, prop.getisPredefinedInXMP2005());
                assertEquals(this.isDefinedInCurrentPackageForPDFA_1,
                        prop.getisDefinedInCurrentPackage());
                assertEquals(Boolean.FALSE, prop.getisDefinedInMainPackage());
                assertEquals(this.isValueTypeCorrectForPDFA_1,
                        prop.getisValueTypeCorrect());
            }
            AXLMainXMPPackage mainPack = new AXLMainXMPPackage(meta, true,
                    PDFAFlavour.PDFA_1_B);
            for (Object obj : mainPack
                    .getLinkedObjects(AXLXMPPackage.PROPERTIES)) {
                XMPProperty prop = (XMPProperty) obj;
                assertEquals(Boolean.FALSE, prop.getisPredefinedInXMP2004());
                assertEquals(Boolean.FALSE, prop.getisPredefinedInXMP2005());
                assertEquals(this.isDefinedInCurrentPackageForPDFA_1,
                        prop.getisDefinedInCurrentPackage());
                assertEquals(this.isDefinedInCurrentPackageForPDFA_2_3,
                        prop.getisDefinedInMainPackage());
                assertEquals(this.isValueTypeCorrectForPDFA_1,
                        prop.getisValueTypeCorrect());
            }
            AXLXMPPackage pack2 = new AXLXMPPackage(meta, true, true, null,
                    PDFAFlavour.PDFA_2_B);
            for (Object obj : pack2.getLinkedObjects(AXLXMPPackage.PROPERTIES)) {
                XMPProperty prop = (XMPProperty) obj;
                assertEquals(this.isDefinedInCurrentPackageForPDFA_2_3,
                        prop.getisDefinedInCurrentPackage());
                assertEquals(Boolean.FALSE, prop.getisDefinedInMainPackage());
                assertEquals(this.isValueTypeCorrectForPDFA_2_3,
                        prop.getisValueTypeCorrect());
            }
            AXLMainXMPPackage mainPack2 = new AXLMainXMPPackage(meta, true,
                    true, PDFAFlavour.PDFA_2_B);
            for (Object obj : mainPack2
                    .getLinkedObjects(AXLXMPPackage.PROPERTIES)) {
                XMPProperty prop = (XMPProperty) obj;
                assertEquals(this.isDefinedInCurrentPackageForPDFA_2_3,
                        prop.getisDefinedInCurrentPackage());
                assertEquals(this.isDefinedInCurrentPackageForPDFA_2_3,
                        prop.getisDefinedInMainPackage());
                assertEquals(this.isValueTypeCorrectForPDFA_2_3,
                        prop.getisValueTypeCorrect());
            }
        }
    }
}
