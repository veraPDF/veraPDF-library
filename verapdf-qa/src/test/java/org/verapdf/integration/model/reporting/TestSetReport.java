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
    private long total;
    private long failed;

    @JsonProperty("testSetReport")
    public Map<String, List<TestEntityReport>> getTestSetReport() {
        return testSetReport;
    }

    public void setTestSetReport(Map<String, List<TestEntityReport>> testSetReport) {
        this.testSetReport = testSetReport;
    }

    @JsonProperty("total")
    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    @JsonProperty("failed")
    public long getFailed() {
        return failed;
    }

    public void setFailed(long failed) {
        this.failed = failed;
    }

    public static TestSetReport fromValue(TestSet testSet) {
        TestSetReport testSetReport = new TestSetReport();
        long total = 0L;
        long failed = 0L;

        Map<String, List<TestEntity>> testSetMap = testSet.getTestSet();
        Map<String, List<TestEntityReport>> testSetReportMap = new HashMap<>();
        for (String testCorpus : testSetMap.keySet()) {
            List<TestEntity> testEntities = testSetMap.get(testCorpus);
            List<TestEntityReport> testEntityReports = new ArrayList<>();
            for (TestEntity testEntity : testEntities) {
                total++;
                testEntityReports.add(TestEntityReport.fromValue(testEntity));
                if (!testEntity.isTestPassed()) {
                    failed++;
                }
            }
            testSetReportMap.put(testCorpus, testEntityReports);
        }
        testSetReport.setTestSetReport(testSetReportMap);
        testSetReport.setTotal(total);
        testSetReport.setFailed(failed);
        return testSetReport;
    }

}
