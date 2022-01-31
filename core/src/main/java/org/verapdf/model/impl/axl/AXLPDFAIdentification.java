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

import java.util.logging.Level;
import java.util.logging.Logger;

import org.verapdf.model.xmplayer.PDFAIdentification;

import org.verapdf.xmp.XMPConst;
import org.verapdf.xmp.XMPException;
import org.verapdf.xmp.impl.VeraPDFMeta;
import org.verapdf.xmp.impl.VeraPDFXMPNode;

/**
 * @author Maksim Bezrukov
 */
public class AXLPDFAIdentification extends AXLXMPObject implements PDFAIdentification {

    private static final Logger LOGGER = Logger
            .getLogger(AXLPDFAIdentification.class.getName());

    public static final String PDFA_IDENTIFICATION = "PDFAIdentification";

    private final VeraPDFMeta metadata;

    public AXLPDFAIdentification(VeraPDFMeta metadata) {
        super(PDFA_IDENTIFICATION);
        this.metadata = metadata;
    }

    @Override
    public Long getpart() {
        try {
            Integer part = this.metadata.getIdentificationPart();
            return part == null ? null : Long.valueOf(part.longValue());
        } catch (XMPException e) {
            LOGGER.log(Level.FINE, "Can not get identification part", e);
            return null;
        }
    }

    @Override
    public String getconformance() {
        try {
            return this.metadata.getIdentificationConformance();
        } catch (XMPException e) {
            LOGGER.log(Level.FINE, "Can not get identification conformance", e);
            return null;
        }
    }

    @Override
    public String getpartPrefix() {
        VeraPDFXMPNode property = this.metadata.getProperty(XMPConst.NS_PDFA_ID, "part");
        return property == null ? null : property.getPrefix();
    }

    @Override
    public String getconformancePrefix() {
        VeraPDFXMPNode property = this.metadata.getProperty(XMPConst.NS_PDFA_ID, "conformance");
        return property == null ? null : property.getPrefix();
    }

    @Override
    public String getamdPrefix() {
        VeraPDFXMPNode property = this.metadata.getProperty(XMPConst.NS_PDFA_ID, "amd");
        return property == null ? null : property.getPrefix();
    }

    @Override
    public String getcorrPrefix() {
        VeraPDFXMPNode property = this.metadata.getProperty(XMPConst.NS_PDFA_ID, "corr");
        return property == null ? null : property.getPrefix();
    }

    @Override
    public String getrevPrefix() {
        VeraPDFXMPNode property = this.metadata.getProperty(XMPConst.NS_PDFA_ID, "rev");
        return property == null ? null : property.getPrefix();
    }

    @Override
    public String getrev() {
        try {
            return this.metadata.getRevisionYear();
        } catch (XMPException e) {
            LOGGER.log(Level.FINE, "Can not get revision year", e);
            return null;
        }
    }
}
