package org.verapdf.integration.pdfa_1.b_6_1_10_filters;

import org.junit.Assert;
import org.junit.Test;
import org.verapdf.integration.base.BasePDFAIT;

/**
 * Filters - LZW decode
 */
public class Isartor_6_1_10_t01_fail_c_IT extends BasePDFAIT {

    @Test
    public void testValidation() throws Exception {
        Assert.assertTrue(testValidationSuccessful());
    }

    @Override
    protected String getExpectedReportFilePath() throws Exception {
        return getSystemIndependentPath(ISARTOR_EXPECTED_REPORTS_REPO_NAME + "pdfa_1/b_6_1_10_filters/6-1-10-t01-fail-c-report.xml");
    }

    @Override
    protected String getValidationProfileFilePath() throws Exception {
        return getSystemIndependentPath(VALIDATION_PROFILES_REPO_NAME + "PDF_A/1b/6.1 File structure/6.1.10 Filters/verapdf-profile-6-1-10-t01.xml");
    }

    @Override
    protected String getPdfFilePath() throws Exception {
        return getSystemIndependentPath(ISARTOR_TEST_FILES_REPO_NAME + "PDFA-1b/6.1 File structure/6.1.10 Filters/isartor-6-1-10-t01-fail-c.pdf");
    }
}
