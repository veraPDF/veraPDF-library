package org.verapdf.integration.pdfa_1.b_6_1_4_xref_table;

import org.junit.Assert;
import org.junit.Test;
import org.verapdf.integration.base.BasePDFAIT;

/**
 * Xref table - subsection spacings
 */
public class Isartor_6_1_4_t02_fail_a_IT extends BasePDFAIT {

    @Test
    public void testValidation() throws Exception {
        Assert.assertTrue(testValidationSuccessful());
    }

    @Override
    protected String getExpectedReportFilePath() throws Exception {
        return getSystemIndependentPath(ISARTOR_EXPECTED_REPORTS_REPO_NAME + "pdfa_1/b_6_1_4_xref_table/6-1-4-t02-fail-a-report.xml");
    }

    @Override
    protected String getValidationProfileFilePath() throws Exception {
        return getSystemIndependentPath(VALIDATION_PROFILES_REPO_NAME + "PDF_A/1b/6.1 File structure/6.1.4 Cross reference table/verapdf-profile-6-1-4-t02.xml");
    }

    @Override
    protected String getPdfFilePath() throws Exception {
        return getSystemIndependentPath(ISARTOR_TEST_FILES_REPO_NAME + "PDFA-1b/6.1 File structure/6.1.4 Cross reference trailer/isartor-6-1-4-t02-fail-a.pdf");
    }
}
