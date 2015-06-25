package org.verapdf.integration.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * @author Timur Kamalov
 */
public class TestSet {

    Map<String, List<TestEntity>> testSet;

    @JsonProperty("testSet")
    public Map<String, List<TestEntity>> getTestSet() {
        return testSet;
    }

    public void setTestSet(Map<String, List<TestEntity>> testSet) {
        this.testSet = testSet;
    }

}
