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

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class PKCS7Test {
    private PKCS7 parser;
    final String[] testPaths = new String[]{"src/test/resources/org/verapdf/pdfa/parsers/pkcs7/signed.txt",
            "src/test/resources/org/verapdf/pdfa/parsers/pkcs7/signed2.txt",
            "src/test/resources/org/verapdf/pdfa/parsers/pkcs7/signed3.txt",
            "src/test/resources/org/verapdf/pdfa/parsers/pkcs7/signed4.txt"};
    final int[] correctNumOfCert = new int[]{3, 1, 1, 1};

    @Test
    public void getSignerInfosLength() throws IOException {
        for (String path : testPaths) {
            parser = new PKCS7(Files.readAllBytes(Paths.get(path)));
            Assert.assertEquals(1, parser.getSignerInfosLength());
        }
    }

    @Test
    public void getCertificates() throws IOException {
        for (int i = 0; i < testPaths.length; ++i) {
            parser = new PKCS7(Files.readAllBytes(Paths.get(testPaths[i])));
            List<X509CertificateImpl> certificates = parser.getCertificates();
            Assert.assertEquals(correctNumOfCert[i], certificates.size());
            for (X509CertificateImpl certificate : certificates) {
                Assert.assertEquals(2, certificate.getVersion());
            }
        }
    }
}
