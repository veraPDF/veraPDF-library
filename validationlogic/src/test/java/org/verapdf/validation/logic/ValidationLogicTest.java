package org.verapdf.validation.logic;

import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosDict;
import org.verapdf.model.coslayer.CosInteger;
import org.verapdf.model.pdlayer.PDAnnot;
import org.verapdf.validation.report.model.ValidationInfo;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Maksim Bezrukov
 */
public class ValidationLogicTest {

    @SuppressWarnings("unchecked")
    @Test
    public void test() throws Exception {

        TestPDAnnot pda1 = new TestPDAnnot("pdatestFailedVersion", "pda1");
        TestPDAnnot pda2 = new TestPDAnnot("pdatest", "pda2");
        List<PDAnnot> lspd = new ArrayList<>();
        lspd.add(pda1);
        lspd.add(pda2);

        TestCosInteger in = new TestCosInteger(41, null);
        List<CosInteger> lsin = new ArrayList<>();
        lsin.add(in);

        TestCosDict cd2 = new TestCosDict(2, "cd2", new ArrayList<CosDict>(),
                lsin, new ArrayList<org.verapdf.model.baselayer.Object>());

        List<CosDict> lscd = new ArrayList<>();
        lscd.add(cd2);

        TestCosDict cd1 = new TestCosDict(3, "cd1", lscd, lsin,
                new ArrayList<org.verapdf.model.baselayer.Object>());

        List<CosDict> lscd2 = new ArrayList<>();
        lscd2.add(cd1);

        TestObject obj = new TestObject("obj", lscd2, lspd);

        ((List<Object>) cd2.getLinkedObjects("Object")).add(obj);

        ValidationInfo info = Validator.validate(obj,
                getSystemIndependentPath("/test.xml"), false);

        assertEquals(info.getProfile().getName(),
                "Validation profile for testing");
        assertNull(info.getProfile().getHash());

        assertFalse(info.getResult().isCompliant());

        assertEquals(info.getResult().getSummary().getAttrPassedRules(), 3);
        assertEquals(info.getResult().getSummary().getAttrFailedRules(), 4);

        assertEquals(info.getResult().getSummary().getAttrPassedChecks(), 9);
        assertEquals(info.getResult().getSummary().getAttrFailedChecks(), 5);

    }

    private String getSystemIndependentPath(String path)
            throws URISyntaxException {
        return Paths.get(this.getClass().getResource(path).toURI()).toString();
    }

}
