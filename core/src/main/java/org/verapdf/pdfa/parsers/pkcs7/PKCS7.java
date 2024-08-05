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
package org.verapdf.pdfa.parsers.pkcs7;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PKCS7 {
    private static final Logger LOGGER = Logger.getLogger(PKCS7.class.getCanonicalName());

    private List<X509CertificateImpl> certificates = new ArrayList<>();
    private int signerInfosLength = 0;

    public PKCS7(byte[] bytes) throws IOException {
        try {
            parse(new DEREncodedInputStream(bytes));
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "Error while parsing the encoded bytes", e);
            throw new IOException("Error while parsing the encoded bytes", e);
        }
    }

    public PKCS7(List<X509CertificateImpl> certificates) {
        this.certificates = certificates;
    }

    public int getSignerInfosLength() {
        return signerInfosLength;
    }

    public List<X509CertificateImpl> getCertificates() {
        return certificates;
    }

    private void parse(DEREncodedInputStream in) throws IOException {
        try {
            in.mark(in.available());
            parse(in, false);
        } catch (IOException e1) {
            try {
                in.reset();
                parse(in, true);
            } catch (IOException e2) {
                throw new IOException(e2.getMessage(), e1);
            }
        }
    }

    private void parse(DEREncodedInputStream derIn, boolean oldStyle) throws IOException {
        ContentInfo contentInfo = new ContentInfo(derIn, oldStyle);
        DEREncodedValue content = contentInfo.getContent();
        byte[] contentType = contentInfo.contentType;

        if (Arrays.equals(contentType, ContentInfo.SIGNED_DATA_OID)) {
            parseSignedData(content);
        } else if (Arrays.equals(contentType, ContentInfo.OLD_SIGNED_DATA_OID)) {
            // This is for backwards compatibility with JDK 1.1.x
            parseOldSignedData(content);
        } else {
            signerInfosLength = 0;
            certificates = new ArrayList<>();
            LOGGER.log(Level.WARNING, "Content type is not signed data OID");
        }
    }

    private DEREncodedInputStream parseDataBeforeCertificates(DEREncodedValue val) throws IOException {
        DEREncodedInputStream dis = val.toDEREncodedInputStream();
        //ignore version, algorithm IDs, contentInfo
        dis.getBigInteger();
        dis.getSet(1);
        new ContentInfo(dis, false);
        return dis;
    }

    private void parseSignedData(DEREncodedValue val) throws IOException {
        DEREncodedInputStream dis = parseDataBeforeCertificates(val);

        // check if certificates are provided
        if ((byte) (dis.peekByte()) == (byte) 0xA0) {
            DEREncodedValue[] certValues = dis.getSet(2, true);
            certificates = new ArrayList<>();
            for (DEREncodedValue certVal : certValues) {
                try {
                    byte tag = certVal.getValueTag();
                    if (tag == DEREncodedValue.TAG_SEQUENCE) {
                        certificates.add(new X509CertificateImpl(certVal));
                    }
                } catch (CertificateException ce) {
                    throw new IOException(ce.getMessage(), ce);
                }
            }
        }

        // ignore CRLs if CRLs are provided
        if ((byte) (dis.peekByte()) == (byte) 0xA1) {
            dis.getSet(1, true);
        }

        // signerInfos
        signerInfosLength = dis.getSet(1).length;
    }

    private void parseOldSignedData(DEREncodedValue val) throws IOException {
        DEREncodedInputStream dis = parseDataBeforeCertificates(val);

        DEREncodedValue[] certValues = dis.getSet(2);
        certificates = new ArrayList<>();

        for (DEREncodedValue certValue : certValues) {
            try {
                certificates.add(new X509CertificateImpl(certValue));
            } catch (CertificateException ce) {
                throw new IOException(ce.getMessage(), ce);
            }
        }

        // ignore CRLs
        dis.getSet(0);

        // signerInfos
        signerInfosLength = dis.getSet(1).length;
    }

    public static class ContentInfo {
        public static final byte[] SIGNED_DATA_OID = new byte[]{42, -122, 72, -122, -9, 13, 1, 7, 2};
        public static final byte[] OLD_SIGNED_DATA_OID = new byte[]{42, -122, 72, -61, -5, 77, 1, 7, 2};

        protected DEREncodedValue content;
        protected byte[] contentType;

        public ContentInfo(DEREncodedInputStream in, boolean isOldStyle) throws IOException {
            DEREncodedValue[] info = in.getSequence(2);
            if (info.length != 1 && info.length != 2) {
                throw new IOException("Invalid length for content info");
            }
            contentType = new DEREncodedInputStream(info[0].toByteArray()).getOID();
            content = isOldStyle ? info[1] : (info.length == 2 ?
                    (new DEREncodedInputStream(info[1].toByteArray())).getSet(1, true)[0] : null);
        }

        public DEREncodedValue getContent() {
            return content;
        }
    }
}
