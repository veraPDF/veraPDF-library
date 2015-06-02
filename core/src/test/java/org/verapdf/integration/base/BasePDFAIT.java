package org.verapdf.integration.base;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.After;
import org.junit.Before;
import org.verapdf.config.Input;
import org.verapdf.config.VeraPdfTaskConfig;
import org.verapdf.runner.ValidationRunner;
import org.verapdf.validation.report.XMLValidationReport;
import org.verapdf.validation.report.model.ValidationInfo;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public abstract class BasePDFAIT {

    protected final static String TEST_FILES_REPO_NAME = "/test-resources/veraPDF-corpus-PDFA-1b/";
    protected final static String VALIDATION_PROFILES_REPO_NAME = "/test-resources/veraPDF-validation-profiles/";


    protected VeraPdfTaskConfig taskConfig;

    @Before
    public void initialize() throws Exception {
        taskConfig = createTaskConfig();
    }

    private VeraPdfTaskConfig createTaskConfig() throws Exception {
        VeraPdfTaskConfig.Builder taskConfigBuilder = new VeraPdfTaskConfig.Builder();
        taskConfigBuilder.input(new Input(getPdfFilePath(), false))
                         .profile(getValidationProfileFilePath())
                         .validate(true)
                         .output(new String());
        return taskConfigBuilder.build();
    }

    protected Boolean testValidationSuccessful() throws Exception {
        String actualReport = runValidation();
        String expectedReport = getExpectedReport();
        return compareResults(expectedReport, actualReport);
    }

    private String runValidation() throws Exception {
        ValidationInfo info = ValidationRunner.runValidation(taskConfig);

        return XMLValidationReport.getXMLValidationReportAsString(info);
    }

    private String getExpectedReport() throws Exception {
    	File expectedReport = new File(getExpectedReportFilePath());
    	try (Scanner reportScanner = new Scanner(expectedReport)) {
            return reportScanner.useDelimiter("\\Z").next();
    	}
    }

    private Boolean compareResults(String expectedReport, String actualReport) throws Exception {
        XMLUnit.setIgnoreWhitespace(true);
        Diff diff = XMLUnit.compareXML(expectedReport, actualReport);
        return diff.identical();
    }

    @After
    public void cleanup() {
        taskConfig = null;
    }

    protected abstract String getPdfFilePath() throws Exception;

    protected abstract String getValidationProfileFilePath() throws Exception;

    protected abstract String getExpectedReportFilePath() throws Exception;

    protected String getSystemIndependentPath(String path) throws Exception {
        URL resourceUrl = getClass().getResource(path);
        Path resourcePath = Paths.get(resourceUrl.toURI());
        return resourcePath.toString();
    }

}
