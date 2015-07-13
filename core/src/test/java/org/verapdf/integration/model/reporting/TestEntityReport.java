package org.verapdf.integration.model.reporting;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.verapdf.integration.model.TestEntity;
import org.verapdf.integration.model.comparing.ComparingStrategies;

/**
 * @author Timur Kamalov
 */
public class TestEntityReport {

    private String testFileName;
    private String validationProfileName;
    private String expectedReportName;
    private ComparingStrategies comparingStrategy;
    private boolean testPassed;
    private boolean exception = false;

    @JsonProperty("testFileName")
    public String getTestFileName() {
        return testFileName;
    }

    public void setTestFileName(String testFileName) {
        this.testFileName = testFileName;
    }

    @JsonProperty("validationProfileName")
    public String getValidationProfileName() {
        return validationProfileName;
    }

    public void setValidationProfileName(String validationProfileName) {
        this.validationProfileName = validationProfileName;
    }

    @JsonProperty("expectedReportName")
    public String getExpectedReportName() {
        return expectedReportName;
    }

    public void setExpectedReportName(String expectedReportName) {
        this.expectedReportName = expectedReportName;
    }

    @JsonProperty("comparingStrategy")
    public ComparingStrategies getComparingStrategy() {
        return comparingStrategy;
    }

    public void setComparingStrategy(ComparingStrategies comparingStrategy) {
        this.comparingStrategy = comparingStrategy;
    }

    @JsonProperty("testPassed")
    public boolean isTestPassed() {
        return testPassed;
    }

    public void setTestPassed(boolean testPassed) {
        this.testPassed = testPassed;
    }

    @JsonProperty("exception")
    public boolean isException() {
        return exception;
    }

    public void setException(boolean exception) {
        this.exception = exception;
    }

    public static TestEntityReport fromValue(TestEntity testEntity) {
        TestEntityReport testEntityReport = new TestEntityReport();
        testEntityReport.setTestFileName(testEntity.getTestFileName());
        testEntityReport.setValidationProfileName(testEntity.getValidationProfileName());
        testEntityReport.setExpectedReportName(testEntity.getExpectedReportName());
        testEntityReport.setComparingStrategy(testEntity.getComparingStrategy());
        testEntityReport.setTestPassed(testEntity.isTestPassed());
        testEntityReport.setException(testEntity.isException());
        return testEntityReport;
    }

}
