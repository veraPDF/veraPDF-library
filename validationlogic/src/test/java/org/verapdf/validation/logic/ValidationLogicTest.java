package org.verapdf.validation.logic;

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

        TestPDAnnot pda1 = new TestPDAnnot("pdatestLOOOOOL","pda1");
        TestPDAnnot pda2 = new TestPDAnnot("pdatest","pda2");
        List<PDAnnot> lspd = new ArrayList<>();
        lspd.add(pda1);
        lspd.add(pda2);

        TestCosInteger in = new TestCosInteger(41, "cosint");
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


        ValidationInfo info = Validator.validate(obj, "src/test/resources/test.xml");

        assertEquals(info.getProfile().getName(), "Validation profile for testing");
        assertEquals(info.getProfile().getHash(), "Some hash");

        assertEquals(info.getResult().isCompliant(), false);

        assertEquals(info.getResult().getSummary().getAttr_passedRules(), 1);
        assertEquals(info.getResult().getSummary().getAttr_failedRules(), 3);

        assertEquals(info.getResult().getSummary().getAttr_passedChecks(), 3);
        assertEquals(info.getResult().getSummary().getAttr_failedChecks(), 3);
    }

}
