package org.verapdf.pdfa.parsers.pkcs7;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class PKCS7Test {
    private PKCS7 parser;
    String[] testPaths = new String[]{"src/test/resources/org/verapdf/pdfa/parsers/pkcs7/signed.txt",
            "src/test/resources/org/verapdf/pdfa/parsers/pkcs7/signed2.txt",
            "src/test/resources/org/verapdf/pdfa/parsers/pkcs7/signed3.txt",
            "src/test/resources/org/verapdf/pdfa/parsers/pkcs7/signed4.txt"};
    int[] correctNumOfCert = new int[]{3, 1, 1, 1};

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
