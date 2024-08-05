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
import java.math.BigInteger;
import java.security.cert.CertificateException;
import java.security.cert.CertificateParsingException;
import java.util.Arrays;

public class X509CertificateImpl {
    private int version = 0;

    public X509CertificateImpl(DEREncodedValue value) throws CertificateException {
        try {
            parse(value);
        } catch (IOException e) {
            throw new CertificateException("Error while initializing certificate. ", e);
        }
    }

    public int getVersion() {
        return version;
    }

    private void parse(DEREncodedValue val) throws CertificateException, IOException {
        if (val.in != null && val.valueTag == DEREncodedValue.TAG_SEQUENCE) {
            DEREncodedValue[] sequence = new DEREncodedValue[3];
            for (int i = 0; i < 3; ++i) {
                sequence[i] = val.in.getDEREncodedValue();
            }
            if (val.in.available() != 0) {
                throw new CertificateParsingException("Extra signed data available = " + val.in.available() + " bytes");
            }

            byte[] algID = parseAlgId(sequence[1]);
            if (sequence[1].in.available() != 0) {
                throw new CertificateParsingException("Algorithm ID field contains extra data");
            }
            sequence[2].getBitString();
            if (sequence[2].in.available() != 0) {
                throw new CertificateParsingException("Signed fields contain extra data");
            }
            // The CertificateInfo
            if (sequence[0].valueTag != DEREncodedValue.TAG_SEQUENCE) {
                throw new CertificateParsingException("Invalid certificate signed fields");
            }
            parseCertificateInfo(sequence[0].in, algID);
        } else {
            throw new CertificateParsingException("Invalid DER-encoded certificate data");
        }
    }

    private byte[] parseAlgId(DEREncodedValue val) throws IOException {
        if (val.valueTag == DEREncodedValue.TAG_SEQUENCE) {
            DEREncodedInputStream in = val.toDEREncodedInputStream();
            byte[] algId = in.getOID();
            DEREncodedValue params = in.available() == 0 ? null : in.getDEREncodedValue();
            if (params != null && params.valueTag == DEREncodedValue.TAG_NULL && !params.areEmpty()) {
                throw new IOException("Invalid null tag");
            }
            if (in.available() != 0) {
                throw new IOException("Algorithm ID contains extra data");
            }
            return algId;
        }
        throw new IOException("Tag is not a sequence");
    }

    private void parseCertificateInfo(DEREncodedInputStream in, byte[] algID) throws IOException, CertificateException {
        parseVersionAndSerialNumber(in);
        // the "inner" and "outer" signature algorithms must match
        if (!Arrays.equals(algID, parseAlgId(in.getDEREncodedValue()))) {
            throw new CertificateException("Signature algorithms mismatch");
        }

        // Issuer name
        DEREncodedValue[] nameSeq = parseX500Name(in);
        validateX500Name(nameSeq, "Empty issuer DN isn't allowed in X509Certificates");

        // validity:  SEQUENCE { start date, end date }
        parseValidity(in);

        // subject name
        nameSeq = parseX500Name(in);
        if (version == 0) {
            validateX500Name(nameSeq, "Empty subject DN isn't allowed in V1 X509Certificates");
        }

        // ignore public key
        DEREncodedValue val = in.getDEREncodedValue();
        if (val.valueTag != DEREncodedValue.TAG_SEQUENCE) {
            throw new IOException("Subject key is corrupted");
        }
        parseAlgId(val.in.getDEREncodedValue());

        // If more data available, make sure version is not V1
        if (in.available() != 0) {
            if (version == 0) {
                throw new CertificateParsingException("V1 X509Certificate contains extra data");
            }
            parseExtraData(in);
        }
    }

    private void parseVersionAndSerialNumber(DEREncodedInputStream in) throws IOException {
        // Version
        DEREncodedValue tmp = in.getDEREncodedValue();
        if (tmp.isContextSpecific((byte) 0)) {
            if (tmp.isConstructed()) {
                tmp = tmp.in.getDEREncodedValue();
                BigInteger result = tmp.getBigInteger();
                if (result.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) {
                    throw new IOException("Integer greater than maximum valid value");
                }
                if (result.compareTo(BigInteger.valueOf(Integer.MIN_VALUE)) < 0) {
                    throw new IOException("Integer less than minimum valid value");
                }
                version = result.intValue();
                if (tmp.in.available() != 0) {
                    throw new IOException("X509Certificate version has bad format");
                }
            }
            tmp = in.getDEREncodedValue();
        }
        // Serial number ... an integer
        tmp.getBigInteger();
        if (tmp.in.available() != 0) {
            throw new IOException("Serial number contains extra data");
        }
    }

    private DEREncodedValue[] parseX500Name(DEREncodedInputStream in) throws IOException {
        DEREncodedValue[] values;
        byte[] bytes = in.inputBuffer.toByteArray();
        try {
            values = in.getSequence(5);
        } catch (IOException e) {
            if (bytes == null) {
                values = null;
            } else {
                bytes = new DEREncodedValue(DEREncodedValue.TAG_SEQUENCE, bytes).toByteArray();
                values = new DEREncodedInputStream(bytes).getSequence(5);
            }
        }
        return values;
    }

    private void validateX500Name(DEREncodedValue[] names, String message) throws IOException, CertificateParsingException {
        if (names != null) {
            for (DEREncodedValue value : names) {
                if (value.valueTag != DEREncodedValue.TAG_SET) {
                    throw new IOException("Invalid set tag in X500 name");
                }
                if (new DEREncodedInputStream(value.toByteArray()).getSet(5).length == 0) {
                    throw new CertificateParsingException(message);
                }
            }
        }
    }

    private void parseValidity(DEREncodedInputStream in) throws IOException {
        DEREncodedValue derVal = in.getDEREncodedValue();
        if (derVal.valueTag != DEREncodedValue.TAG_SEQUENCE) {
            throw new IOException("Invalid starting sequence tag");
        }
        if (derVal.in.available() == 0) {
            throw new IOException("Certificate validity contains no data");
        }
        DEREncodedValue[] sequence = (new DEREncodedInputStream(derVal.toByteArray())).getSequence(2);
        if (sequence.length != 2 ||
                sequence[0].valueTag != DEREncodedValue.TAG_UTC_TIME && sequence[0].valueTag != DEREncodedValue.TAG_GENERALIZED_TIME ||
                sequence[1].valueTag != DEREncodedValue.TAG_UTC_TIME && sequence[1].valueTag != DEREncodedValue.TAG_GENERALIZED_TIME) {
            throw new IOException("Error while parsing certificate validity");
        }
    }

    private void parseExtraData(DEREncodedInputStream in) throws CertificateParsingException, IOException {
        // ignore issuerUniqueId if present
        DEREncodedValue tmp = in.getDEREncodedValue();
        if (tmp.isContextSpecific((byte) 1)) {
            if (in.available() == 0) {
                return;
            }
            tmp = in.getDEREncodedValue();
        }
        // ignore subjectUniqueId if present
        if (tmp.isContextSpecific((byte) 2)) {
            if (in.available() == 0) {
                return;
            }
            in.getDEREncodedValue();
        }
        // ignore the extensions
        if (version != 2) {
            throw new CertificateParsingException("Extensions not allowed in v2 certificate");
        }
    }
}
