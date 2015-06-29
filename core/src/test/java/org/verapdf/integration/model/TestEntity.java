package org.verapdf.integration.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.verapdf.validation.report.model.ValidationInfo;

import java.io.File;

/**
 * @author Timur Kamalov
 */
public class TestEntity {

    private String testFileName;
    private String validationProfileName;
    private String expectedReportName;
    private ComparingStrategies comparingStrategy;

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

    //--------------------HELPER PROPERTIES---------------------//
    private File testFile;
    private File validationProfile;
    private ValidationInfo info;
    private StrategyResource strategyResource;

    @JsonIgnore
    public File getTestFile() {
        return testFile;
    }

    public void setTestFile(File testFile) {
        this.testFile = testFile;
    }

    @JsonIgnore
    public File getValidationProfile() {
        return validationProfile;
    }

    public void setValidationProfile(File validationProfile) {
        this.validationProfile = validationProfile;
    }

    @JsonIgnore
    public ValidationInfo getInfo() {
        return info;
    }

    public void setInfo(ValidationInfo info) {
        this.info = info;
    }

    @JsonIgnore
    public StrategyResource getStrategyResource() {
        return strategyResource;
    }

    public void setStrategyResource(StrategyResource strategyResource) {
        this.strategyResource = strategyResource;
    }
}