package org.verapdf.integration.model.reporting;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.verapdf.integration.model.TestEntity;
import org.verapdf.integration.model.TestSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Timur Kamalov
 */
public class TestSetReport {

    private Map<String, List<TestEntityReport>> testSetReport;

    @JsonProperty("testSetReport")
    public Map<String, List<TestEntityReport>> getTestSetReport() {
        return testSetReport;
    }

    public void setTestSetReport(Map<String, List<TestEntityReport>> testSetReport) {
        this.testSetReport = testSetReport;
    }

    public static TestSetReport fromValue(TestSet testSet) {
        TestSetReport testSetReport = new TestSetReport();

        Map<String, List<TestEntity>> testSetMap = testSet.getTestSet();
        Map<String, List<TestEntityReport>> testSetReportMap = new HashMap<>();
        for (String testCorpus : testSetMap.keySet()) {
            List<TestEntity> testEntities = testSetMap.get(testCorpus);
            List<TestEntityReport> testEntityReports = new ArrayList<>();
            for (TestEntity testEntity : testEntities) {
                testEntityReports.add(TestEntityReport.fromValue(testEntity));
            }
            testSetReportMap.put(testCorpus, testEntityReports);
        }
        testSetReport.setTestSetReport(testSetReportMap);

        return testSetReport;
    }

}
