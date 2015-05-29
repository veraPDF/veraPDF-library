package org.verapdf.validation.logic;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosDict;
import org.verapdf.model.coslayer.CosInteger;
import org.verapdf.model.pdlayer.PDAnnot;
import org.verapdf.validation.report.model.ValidationInfo;

import static org.junit.Assert.*;

/**
 * Created by bezrukov on 5/7/15.
 */
public class ValidationLogicTest {

    @Test
    public void test() throws Exception {

        TestPDAnnot pda1 = new TestPDAnnot("pdatestFailedVersion","pda1");
        TestPDAnnot pda2 = new TestPDAnnot("pdatest","pda2");
        List<PDAnnot> lspd = new ArrayList<>();
        lspd.add(pda1);
        lspd.add(pda2);

        TestCosInteger in = new TestCosInteger(41, null);
        List<CosInteger> lsin = new ArrayList<>();
        lsin.add(in);

        TestCosDict cd2 = new TestCosDict(2, "cd2", new ArrayList<CosDict>(), lsin, new ArrayList<org.verapdf.model.baselayer.Object>());

        List<CosDict> lscd = new ArrayList<>();
        lscd.add(cd2);

        TestCosDict cd1 = new TestCosDict(3, "cd1", lscd, lsin, new ArrayList<org.verapdf.model.baselayer.Object>());


        List<CosDict> lscd2 = new ArrayList<>();
        lscd2.add(cd1);

        TestObject obj = new TestObject("obj", lscd2, lspd);


        ((List<Object>) cd2.getLinkedObjects("Object")).add(obj);


        ValidationInfo info = Validator.validate(obj, getSystemIndependentPath("/test.xml"));

        assertEquals(info.getProfile().getName(), "Validation profile for testing");
        assertEquals(info.getProfile().getHash(), "Some hash");

        assertEquals(info.getResult().isCompliant(), false);

        assertEquals(info.getResult().getSummary().getAttrPassedRules(), 2);
        assertEquals(info.getResult().getSummary().getAttrFailedRules(), 3);

        assertEquals(info.getResult().getSummary().getAttrPassedChecks(), 6);
        assertEquals(info.getResult().getSummary().getAttrFailedChecks(), 4);

    }

    private String getSystemIndependentPath(String path) throws URISyntaxException {
        return Paths.get(this.getClass().getResource(path).toURI()).toString();
    }

}
