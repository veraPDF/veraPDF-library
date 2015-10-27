package org.verapdf.validation.logic;

import org.junit.Test;
import org.verapdf.model.ModelLoader;
import org.verapdf.validation.report.model.*;

import java.net.URISyntaxException;
import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * @author Maksim Bezrukov
 */
public class ValidationLogicTest {

	@SuppressWarnings("unchecked")
	@Test
	public void test() throws Exception {

		ModelLoader model = new ModelLoader(getSystemIndependentPath("/validatorTest.pdf"));
		org.verapdf.model.baselayer.Object obj = model.getRoot();

		ValidationInfo info = Validator.validate(obj,
				getSystemIndependentPath("/test.xml"), false, true, -1, -1);

		assertEquals("Validation profile for testing",
				info.getProfile().getName());
		assertNull(info.getProfile().getHash());

		assertFalse(info.getResult().isCompliant());

		assertEquals(2, info.getResult().getSummary().getAttrPassedRules());
		assertEquals(2, info.getResult().getSummary().getAttrFailedRules());

		assertEquals(318, info.getResult().getSummary().getAttrPassedChecks());
		assertEquals(2, info.getResult().getSummary().getAttrFailedChecks());
		assertEquals(0, info.getResult().getSummary().getAttrCompletedMetadataFixes());
		assertEquals(0, info.getResult().getSummary().getAttrFailedMetadataFixes());
		assertEquals(0, info.getResult().getSummary().getAttrWarnings());
		assertEquals("PDF file is not compliant with Validation Profile requirements", info.getResult().getStatement());

		Details details = info.getResult().getDetails();

		assertEquals(0, details.getWarnings().size());
		assertEquals(4, details.getRules().size());

		Rule rule = null;
		for (Rule ruleCheck : details.getRules()) {
			if ("rule151".equals(ruleCheck.getID())) {
				rule = ruleCheck;
				break;
			}
		}

		assertNotNull(rule);
		assertEquals(315, rule.getPassedChecksCount());
		assertEquals(1, rule.getFailedChecksCount());

		Check check = null;
		for (Check che : rule.getChecks()) {
			if (Check.Status.FAILED.equals(che.getStatus())) {
				check = che;
			}
		}

		assertNotNull(check);

		CheckLocation checkLoc = check.getLocation();
		assertNotNull(checkLoc);
		assertEquals("CosDocument", checkLoc.getAttrLevel());
		assertEquals("root", checkLoc.getContext());

		CheckError checkerr = check.getError();
		assertNotNull(checkerr);
		assertEquals("test variable is not initialized", checkerr.getMessage());
		assertEquals(0, checkerr.getArguments().size());

		model.close();
	}

	private String getSystemIndependentPath(String path)
			throws URISyntaxException {
		return Paths.get(this.getClass().getResource(path).toURI()).toString();
	}

}
