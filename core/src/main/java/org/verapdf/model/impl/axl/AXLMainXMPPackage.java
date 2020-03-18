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

import com.adobe.xmp.XMPConst;
import com.adobe.xmp.impl.VeraPDFMeta;
import com.adobe.xmp.impl.VeraPDFXMPNode;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.xmplayer.MainXMPPackage;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Current class is representation of XMPPackage interface from abstract model based on adobe xmp library
 *
 * @author Maksim Bezrukov
 */
public class AXLMainXMPPackage extends AXLXMPPackage implements MainXMPPackage {

    public static final String MAIN_XMP_PACKAGE_TYPE = "MainXMPPackage";

    public static final String IDENTIFICATION = "Identification";

    public static final String UAIDENTIFICATION = "UAIdentification";

    /**
     * Constructs new object
     *
     * @param xmpMetadata          object that represents this package
     * @param isSerializationValid true if metadata is valid
     */
    public AXLMainXMPPackage(VeraPDFMeta xmpMetadata, boolean isSerializationValid, PDFAFlavour flavour) {
        super(xmpMetadata, isSerializationValid, true, false, null, MAIN_XMP_PACKAGE_TYPE, flavour);
    }

    public AXLMainXMPPackage(VeraPDFMeta xmpMetadata, boolean isSerializationValid, boolean isClosedChoiceCheck, PDFAFlavour flavour) {
        super(xmpMetadata, isSerializationValid, true, isClosedChoiceCheck, null, MAIN_XMP_PACKAGE_TYPE, flavour);
    }

    /**
     * @param link name of the link
     * @return List of all objects with link name
     */
    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        switch (link) {
            case IDENTIFICATION:
                return this.getIdentification();
            case UAIDENTIFICATION:
                return this.getUAIdentification();
            default:
                return super.getLinkedObjects(link);
        }
    }

    private List<AXLPDFUAIdentification> getUAIdentification() {
        VeraPDFMeta xmpMetadata = this.getXmpMetadata();
        if (xmpMetadata != null) {
            for (VeraPDFXMPNode node : xmpMetadata.getProperties()) {
                if (XMPConst.NS_PDFUA_ID.equals(node.getNamespaceURI())) {
                    List<AXLPDFUAIdentification> res = new ArrayList<>(1);
                    res.add(new AXLPDFUAIdentification(xmpMetadata));
                    return Collections.unmodifiableList(res);
                }
            }
        }
        return Collections.emptyList();
    }

    private List<AXLPDFAIdentification> getIdentification() {
        VeraPDFMeta xmpMetadata = this.getXmpMetadata();
        if (xmpMetadata != null) {
            for (VeraPDFXMPNode node : xmpMetadata.getProperties()) {
                if (XMPConst.NS_PDFA_ID.equals(node.getNamespaceURI())) {
                    List<AXLPDFAIdentification> res = new ArrayList<>(1);
                    res.add(new AXLPDFAIdentification(xmpMetadata));
                    return Collections.unmodifiableList(res);
                }
            }
        }
        return Collections.emptyList();
    }

    @Override
    public String getdc_title() {
        VeraPDFMeta xmpMetadata = this.getXmpMetadata();
        if (xmpMetadata != null) {
            for (VeraPDFXMPNode node : xmpMetadata.getProperties()) {
                if (XMPConst.NS_DC.equals(node.getNamespaceURI())) {
                    if(node.getPrefix().equals("dc") && node.getName().equals("title")) {
                        return node.getValue();
                    }
                }
            }
        }
        return null;
    }
}
