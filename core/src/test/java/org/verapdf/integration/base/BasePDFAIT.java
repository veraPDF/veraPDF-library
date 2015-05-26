package org.verapdf.integration.base;

import org.junit.After;
import org.junit.Before;
import org.verapdf.config.Input;
import org.verapdf.config.VeraPdfTaskConfig;
import org.verapdf.runner.ValidationRunner;
import org.verapdf.validation.report.XMLValidationReport;
import org.verapdf.validation.report.model.ValidationInfo;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class BasePDFAIT {

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
        return  compareResults(expectedReport.trim(), actualReport.trim());
    }

    private String runValidation() throws Exception {
        ValidationInfo info = ValidationRunner.runValidation(taskConfig);

        return XMLValidationReport.getXMLValidationReportAsString(info);
    }

    private String getExpectedReport() throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        InputStream inputStream = new FileInputStream(new File(getExpectedReportFilePath()));
        Document doc = documentBuilderFactory.newDocumentBuilder().parse(inputStream);
        StringWriter expectedReport = new StringWriter();
        Transformer serializer = TransformerFactory.newInstance().newTransformer();
        serializer.transform(new DOMSource(doc), new StreamResult(expectedReport));
        return expectedReport.toString();
    }

    private Boolean compareResults(String expectedReport, String actualReport) {
        return expectedReport.equals(actualReport);
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
