package org.verapdf.integration.tools;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBException;

import org.verapdf.core.VeraPDFException;
import org.verapdf.integration.model.TestEntity;
import org.verapdf.integration.model.comparing.ComparingStrategies;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.runner.ValidationRunner;

/**
 * @author Timur Kamalov
 */
public class TestEntityValidator {

    /**
     * @param testEntity
     * @throws JAXBException
     * @throws VeraPDFException
     * @throws IOException
     */
    public static void validate(TestEntity testEntity) throws JAXBException, VeraPDFException, IOException {
        if (testEntity.getComparingStrategy() != ComparingStrategies.IGNORE) {
            System.out.println();
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println(testEntity.getTestFileName());
            try (InputStream fis = new FileInputStream(testEntity.getTestFile())) {
                ValidationResult info = ValidationRunner.runValidation(fis);
            testEntity.setInfo(info);
            }
        }
    }


}
