package org.verapdf.validation.logic;

import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosDict;
import org.verapdf.model.coslayer.CosInteger;
import org.verapdf.model.pdlayer.PDAnnot;
import org.verapdf.validation.report.model.*;

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

		// TODO : fix last int value for test
		ValidationInfo info = Validator.validate(obj,
				getSystemIndependentPath("/test.xml"), false, true, 100, 100);

		assertEquals("Validation profile for testing",
				info.getProfile().getName());
		assertNull(info.getProfile().getHash());

		assertFalse(info.getResult().isCompliant());

		assertEquals(3, info.getResult().getSummary().getAttrPassedRules());
		assertEquals(4, info.getResult().getSummary().getAttrFailedRules());

		assertEquals(9, info.getResult().getSummary().getAttrPassedChecks());
		assertEquals(5, info.getResult().getSummary().getAttrFailedChecks());
		assertEquals(0, info.getResult().getSummary().getAttrCompletedMetadataFixes());
		assertEquals(0, info.getResult().getSummary().getAttrFailedMetadataFixes());
		assertEquals(0, info.getResult().getSummary().getAttrWarnings());
		assertEquals("PDF file is not compliant with Validation Profile requirements", info.getResult().getStatement());

		Details details = info.getResult().getDetails();

		assertEquals(0, details.getWarnings().size());
		assertEquals(7, details.getRules().size());

		Rule rule = null;
		for (Rule ruleCheck : details.getRules()) {
			if ("rule1".equals(ruleCheck.getID())) {
				rule = ruleCheck;
				break;
			}
		}

		assertNotNull(rule);
		assertEquals(1, rule.getPassedChecksCount());
		assertEquals(1, rule.getFailedChecksCount());

		Check check = null;
		for (Check che : rule.getChecks()) {
			if ("failed".equals(che.getStatus().toString())) {
				check = che;
			}
		}

		assertNotNull(check);

		CheckLocation checkLoc = check.getLocation();
		assertNotNull(checkLoc);
		assertEquals("Object", checkLoc.getAttrLevel());
		assertEquals("root/CosDict[0](cd1)", checkLoc.getContext());

		CheckError checkerr = check.getError();
		assertNotNull(checkerr);
		assertEquals("real size is %", checkerr.getMessage());
		assertEquals(1, checkerr.getArguments().size());
		assertEquals("3", checkerr.getArguments().get(0));
	}

	private String getSystemIndependentPath(String path)
			throws URISyntaxException {
		return Paths.get(this.getClass().getResource(path).toURI()).toString();
	}

}
