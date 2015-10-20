package org.verapdf.integration.tools;

import org.verapdf.config.Input;
import org.verapdf.config.VeraPdfTaskConfig;
import org.verapdf.integration.model.TestEntity;
import org.verapdf.integration.model.comparing.ComparingStrategies;
import org.verapdf.runner.ValidationRunner;
import org.verapdf.validation.report.model.ValidationInfo;

/**
 * @author Timur Kamalov
 */
public class TestEntityValidator {

    public static void validate(TestEntity testEntity) {
        if (testEntity.getComparingStrategy() != ComparingStrategies.IGNORE) {
            VeraPdfTaskConfig taskConfig = createTaskConfig(testEntity);
            ValidationInfo info = runValidation(taskConfig);
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

    private static ValidationInfo runValidation(VeraPdfTaskConfig taskConfig) {
        ValidationInfo info = ValidationRunner.runValidation(taskConfig);
        return info;
    }

}
