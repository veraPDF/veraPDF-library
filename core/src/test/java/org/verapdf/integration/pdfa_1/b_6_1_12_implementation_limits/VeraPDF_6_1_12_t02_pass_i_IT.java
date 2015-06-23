package org.verapdf.integration.pdfa_1.b_6_1_12_implementation_limits;

import org.junit.Assert;
import org.junit.Test;

/**
 * Implementation limits - Number value
 */
public class VeraPDF_6_1_12_t02_pass_i_IT extends org.verapdf.integration.base.BasePDFAIT{

    @Test
    public void testValidation() throws Exception  {
        Assert.assertTrue(testValidationSuccessful());
    }

    @Override
    protected String getExpectedReportFilePath() throws Exception {
        return getSystemIndependentPath(VERAPDF_EXPECTED_REPORTS_REPO_NAME + "pdfa_1/b_6_1_12_implementation_limits/6-1-12-t02-pass-i-report.xml");
    }

    @Override
    protected String getValidationProfileFilePath() throws Exception {
        return getSystemIndependentPath(VALIDATION_PROFILES_REPO_NAME + "PDF_A/1b/6.1 File structure/6.1.12 Implementation limits/verapdf-profile-6-1-12-t02.xml");
    }

    @Override
    protected String getPdfFilePath() throws Exception {
        return getSystemIndependentPath(VERA_PDF_TEST_FILES_REPO_NAME + "PDF_A-1b/6.1 File structure/6.1.12 Implementation limits/veraPDF test suite 6-1-12-t02-pass-i.pdf");
    }
}

