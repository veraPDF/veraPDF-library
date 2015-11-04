package org.verapdf.integration.tools;

import org.apache.log4j.Logger;
import org.verapdf.integration.ITVeraPDFTestSuite;
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

	private static final Logger LOGGER = Logger.getLogger(ResultReporter.class);

	public static final String REPORTING_METHOD_OPT_NAME = "test.reporting.method";
	public static final String CONSOLE_REPORTING_OPT = "CONSOLE";

	public static void reportTestSetResult(TestSetReport report) throws IOException {
		Properties prop = new Properties();
		if (CONSOLE_REPORTING_OPT.equals(prop.getProperty(REPORTING_METHOD_OPT_NAME))) {
		    reportToConsole(report);
		}
	}

	private static void reportToConsole(TestSetReport report) {
		LOGGER.info("Total tests run : " + report.getTotal());
		LOGGER.info("Failed tests : " + report.getFailed());
		for (Map.Entry<String, List<TestEntityReport>> entry : report.getTestSetReport().entrySet()) {
			LOGGER.info("Test corpus : " + entry.getKey());
			for (TestEntityReport entityReport : entry.getValue()) {
				if (entityReport.isException()) {
					LOGGER.error(entityReport.getTestFileName() + " " + entityReport.getValidationProfileName() + " status : EXCEPTION");
				} else if (entityReport.isTestPassed()) {
					LOGGER.info(entityReport.getTestFileName() + " " + entityReport.getValidationProfileName() + " status : PASSED");
				} else {
					LOGGER.warn(entityReport.getTestFileName() + " " + entityReport.getValidationProfileName() + " status : FAILED");
				}
			}
		}
	}

}

