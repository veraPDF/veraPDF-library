package org.verapdf.integration.tools;

import javax.xml.bind.JAXBException;

import org.verapdf.config.Input;
import org.verapdf.config.VeraPdfTaskConfig;
import org.verapdf.core.VeraPDFException;
import org.verapdf.integration.model.TestEntity;
import org.verapdf.integration.model.comparing.ComparingStrategies;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.results.ValidationResults;
import org.verapdf.runner.ValidationRunner;

/**
 * @author Timur Kamalov
 */
public class TestEntityValidator {

    public static void validate(TestEntity testEntity) throws JAXBException, VeraPDFException {
        if (testEntity.getComparingStrategy() != ComparingStrategies.IGNORE) {
            VeraPdfTaskConfig taskConfig = createTaskConfig(testEntity);
            ValidationResult info = runValidation(taskConfig);
            testEntity.setInfo(info);
        }
    }

    private static VeraPdfTaskConfig createTaskConfig(TestEntity entity) {
        VeraPdfTaskConfig.Builder taskConfigBuilder = new VeraPdfTaskConfig.Builder();
        taskConfigBuilder
						 .input(new Input(entity.getTestFile().getAbsolutePath(), false))
                         .profile(entity.getValidationProfile().getAbsolutePath())
                         .validate(true)
                         .output("")
                         .maxFailedChecks(100)
						 .maxDisplayedFailedChecks(100);

        return taskConfigBuilder.build();
    }

    private static ValidationResult runValidation(VeraPdfTaskConfig taskConfig) throws JAXBException, VeraPDFException {
        ValidationResult info = ValidationRunner.runValidation(taskConfig);
        return info;
    }

}
