package org.verapdf.integration.pdfa_1.b_6_1_12_implementationlimits;

import org.junit.Assert;
import org.junit.Test;
import org.verapdf.integration.base.BasePDFAIT;

/**
 * Implementation limits - min Integer value test
 */
public class VeraPDF_6_1_12_t01_fail_a_IT extends BasePDFAIT {

    public static final String PACKAGE = "/testfiles/pdfa_1/b_6_1_12_implementationlimits/1/";

    @Test
    public void testValidation() throws Exception {
        Assert.assertTrue(testValidationSuccessful());
    }

    @Override
    protected String getExpectedReportFilePath() throws Exception {
        return getSystemIndependentPath(PACKAGE + "6-1-12-t01-fail-a-report.xml");
    }

    @Override
    protected String getValidationProfileFilePath() throws Exception {
        return getSystemIndependentPath(PACKAGE + "6-1-12-t01-fail-a-profile.xml");
    }

    @Override
    protected String getPdfFilePath() throws Exception {
        return getSystemIndependentPath(PACKAGE + "6-1-12-t01-fail-a.pdf");
    }

}
