/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2024, veraPDF Consortium <info@verapdf.org>
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

import org.verapdf.xmp.XMPConst;
import org.verapdf.xmp.impl.VeraPDFMeta;
import org.verapdf.xmp.impl.VeraPDFXMPNode;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.xmplayer.MainXMPPackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Current class is representation of XMPPackage interface from abstract model based on adobe xmp library
 *
 * @author Maksim Bezrukov
 */
public class AXLMainXMPPackage extends AXLXMPPackage implements MainXMPPackage {

    public static final String MAIN_XMP_PACKAGE_TYPE = "MainXMPPackage";

    public static final String PDFA_IDENTIFICATION = "PDFAIdentification";

    public static final String PDFUA_IDENTIFICATION = "PDFUAIdentification";

    /**
     * Constructs new object
     *
     * @param xmpMetadata          object that represents this package
     * @param isSerializationValid true if metadata is valid
     */
    public AXLMainXMPPackage(VeraPDFMeta xmpMetadata, boolean isSerializationValid) {
        super(xmpMetadata, isSerializationValid, true, false, null, MAIN_XMP_PACKAGE_TYPE);
    }

    public AXLMainXMPPackage(VeraPDFMeta xmpMetadata, boolean isSerializationValid, boolean isClosedChoiceCheck) {
        super(xmpMetadata, isSerializationValid, true, isClosedChoiceCheck, null, MAIN_XMP_PACKAGE_TYPE);
    }

    /**
     * @param link name of the link
     * @return List of all objects with link name
     */
    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        switch (link) {
            case PDFA_IDENTIFICATION:
                return this.getPDFAIdentification();
            case PDFUA_IDENTIFICATION:
                return this.getPDFUAIdentification();
            default:
                return super.getLinkedObjects(link);
        }
    }

    private List<AXLPDFUAIdentification> getPDFUAIdentification() {
        VeraPDFMeta xmpMetadata = this.getXmpMetadata();
        if (xmpMetadata != null && xmpMetadata.containsPropertiesFromNamespace(XMPConst.NS_PDFUA_ID)) {
            List<AXLPDFUAIdentification> res = new ArrayList<>(1);
            res.add(new AXLPDFUAIdentification(xmpMetadata));
            return Collections.unmodifiableList(res);
        }
        return Collections.emptyList();
    }

    private List<AXLPDFAIdentification> getPDFAIdentification() {
        VeraPDFMeta xmpMetadata = this.getXmpMetadata();
        if (xmpMetadata != null && xmpMetadata.containsPropertiesFromNamespace(XMPConst.NS_PDFA_ID)) {
            List<AXLPDFAIdentification> res = new ArrayList<>(1);
            res.add(new AXLPDFAIdentification(xmpMetadata));
            return Collections.unmodifiableList(res);
        }
        return Collections.emptyList();
    }

    @Override
    public Boolean getcontainsPDFUAIdentification() {
        VeraPDFMeta xmpMetadata = this.getXmpMetadata();
        return xmpMetadata != null && xmpMetadata.containsPropertiesFromNamespace(XMPConst.NS_PDFUA_ID);
    }

    @Override
    public Boolean getcontainsPDFAIdentification() {
        VeraPDFMeta xmpMetadata = this.getXmpMetadata();
        return xmpMetadata != null && xmpMetadata.containsPropertiesFromNamespace(XMPConst.NS_PDFA_ID);
    }

    @Override
    public String getdc_title() {
        VeraPDFMeta xmpMetadata = this.getXmpMetadata();
        if (xmpMetadata != null) {
            for (VeraPDFXMPNode node : xmpMetadata.getProperties()) {
                if (XMPConst.NS_DC.equals(node.getNamespaceURI()) && "dc".equals(node.getPrefix()) &&
                        "title".equals(node.getName())) {
                    return node.getLanguageAlternative();
                }
            }
        }
        return null;
    }

    @Override
    public Set<String> getdeclarations() {
        VeraPDFMeta xmpMetadata = this.getXmpMetadata();
        if (xmpMetadata != null) {
            return xmpMetadata.getDeclarations();
        }
        return null;
    }
}
