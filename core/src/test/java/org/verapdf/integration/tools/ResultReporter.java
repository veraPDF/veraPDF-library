package org.verapdf.integration.tools;

import org.apache.log4j.Logger;
import org.verapdf.integration.VeraPDFTestSuite;
import org.verapdf.integration.model.reporting.TestEntityReport;
import org.verapdf.integration.model.reporting.TestSetReport;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author Timur Kamalov
 */
public class ResultReporter {

    private final static Logger logger = Logger.getLogger(ResultReporter.class);

    public static final String REPORTING_METHOD_OPT_NAME = "test.reporting.method";
    public static final String CONSOLE_REPORTING_OPT = "CONSOLE";

    public static void reportTestSetResult(TestSetReport report) throws IOException {
        Properties prop = new Properties();
        InputStream inputStream = ClassLoader.class.getResourceAsStream(VeraPDFTestSuite.TEST_RESOURCES_DIRECTORY_PREFIX + VeraPDFTestSuite.VERA_PDF_TEST_SUITE_PROPERTIES_PATH);
        prop.load(inputStream);
        String reportingMethod = prop.getProperty(REPORTING_METHOD_OPT_NAME);
        switch (reportingMethod) {
            case CONSOLE_REPORTING_OPT : reportToConsole(report);
            default: return;
        }
    }

    private static void reportToConsole(TestSetReport report) {
        logger.info("Total tests run : " + report.getTotal());
        logger.info("Failed tests : " + report.getFailed());
        for (Map.Entry<String, List<TestEntityReport>> entry : report.getTestSetReport().entrySet()) {
            logger.info("Test corpus : " + entry.getKey());
            for (TestEntityReport entityReport : entry.getValue()) {
                if (entityReport.isTestPassed()) {
                    logger.info(entityReport.getTestFileName() + " " + entityReport.getValidationProfileName() + " status : PASSED");
                } else {
                    logger.warn(entityReport.getTestFileName() + " " + entityReport.getValidationProfileName() + " status : FAILED");
                }
            }
        }

    }

}
