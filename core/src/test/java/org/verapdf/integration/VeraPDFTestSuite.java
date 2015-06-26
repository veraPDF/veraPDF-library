package org.verapdf.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.integration.model.*;
import org.verapdf.integration.tools.ResultComparator;
import org.verapdf.integration.tools.TestEntityValidator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class VeraPDFTestSuite {

    private final static String VERA_PDF_TEST_SUITE_PROPERTIES_PATH = "test-config.properties";
    private final static String VERA_PDF_TEST_SUITE_FILE_PATH_PROPERTY = "test.set.path";
    private final static String VERA_PDF_VALIDATION_PROFILES_DIRECTORY = "veraPDF-validation-profiles/";
    private final static String TEST_RESOURCES_DIRECTORY_PREFIX = "/test-resources/";
    private final static String REPORTS_DIRECTORY_PREFIX = "/reports/";

    private final static ObjectMapper MAPPER = new ObjectMapper();

    private static Map<String, File> validationProfilesMap;
    private static TestSet TEST_SET;

    @BeforeClass
    public static void initializeTestSet() throws IOException, URISyntaxException {
        Properties prop = new Properties();
        InputStream inputStream = ClassLoader.class.getResourceAsStream(TEST_RESOURCES_DIRECTORY_PREFIX + VERA_PDF_TEST_SUITE_PROPERTIES_PATH);
        prop.load(inputStream);
        String testSetPath = prop.getProperty(VERA_PDF_TEST_SUITE_FILE_PATH_PROPERTY);
        TEST_SET = MAPPER.readValue(new File(getSystemIndependentPath(testSetPath)), TestSet.class);
        validationProfilesMap = loadTestResources(TEST_RESOURCES_DIRECTORY_PREFIX + VERA_PDF_VALIDATION_PROFILES_DIRECTORY);
    }

    @Test
    public void performValidation() throws URISyntaxException, IOException {
        for (String testCorpus : TEST_SET.getTestSet().keySet()) {
            Map<String, File> testFiles = loadTestResources(TEST_RESOURCES_DIRECTORY_PREFIX + testCorpus);
            Map<String, File> expectedReports = loadTestResources(TEST_RESOURCES_DIRECTORY_PREFIX + REPORTS_DIRECTORY_PREFIX);
            List<TestEntity> corpusTestSet = TEST_SET.getTestSet().get(testCorpus);
            for (TestEntity testEntity : corpusTestSet) {
                if (testFiles.containsKey(testEntity.getTestFileName()) && validationProfilesMap.containsKey(testEntity.getValidationProfileName())) {
                    attachResources(testEntity, testFiles, expectedReports);
                    TestEntityValidator.validate(testEntity);
                    Assert.assertTrue(ResultComparator.compare(testEntity));
                } else {
                    //TODO : throw some exception
                }
            }
        }
    }

    private static Map<String, File> loadTestResources(String directory) throws URISyntaxException {
        Collection<File> testFilesCollection = FileUtils.listFiles(new File(getSystemIndependentPath(directory)), null, true);
        return transformFileCollectionToMap(testFilesCollection);
    }

    private static void attachResources(TestEntity testEntity, Map<String, File> testFiles, Map<String, File> expectedReports) throws IOException {
        testEntity.setTestFile(testFiles.get(testEntity.getTestFileName()));
        testEntity.setValidationProfile(validationProfilesMap.get(testEntity.getValidationProfileName()));
        attachStrategyResource(testEntity, expectedReports);
    }

    private static void attachStrategyResource(TestEntity testEntity, Map<String, File> expectedReports) throws IOException {
        if (testEntity.getComparingStrategy().equals(ComparingStrategies.STATS)) {
            if (expectedReports.containsKey(testEntity.getExpectedReportName())) {
                testEntity.setStrategyResource(MAPPER.readValue(expectedReports.get(testEntity.getExpectedReportName()), StatsStrategyResource.class));
            }
        } else if (testEntity.getComparingStrategy().equals(ComparingStrategies.COMPARE)) {
            if (expectedReports.containsKey(testEntity.getExpectedReportName())) {
                testEntity.setStrategyResource(MAPPER.readValue(expectedReports.get(testEntity.getExpectedReportName()), CompareStrategyResource.class));
            }
        }
    }

    private static Map<String, File> transformFileCollectionToMap(Collection<File> files) {
        Map<String, File> result = new HashMap<>();
        for (File file : files) {
            result.put(file.getName(), file);
        }
        return result;
    }

    private static String getSystemIndependentPath(String path) throws URISyntaxException {
        URL resourceUrl = ClassLoader.class.getResource(path);
        Path resourcePath = Paths.get(resourceUrl.toURI());
        return resourcePath.toString();
    }

}