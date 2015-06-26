package org.verapdf.integration.tools;

import org.verapdf.config.Input;
import org.verapdf.config.VeraPdfTaskConfig;
import org.verapdf.integration.model.ComparingStrategies;
import org.verapdf.integration.model.TestEntity;
import org.verapdf.runner.ValidationRunner;
import org.verapdf.validation.report.model.ValidationInfo;

import java.net.URISyntaxException;

/**
 * @author Timur Kamalov
 */
public class TestEntityValidator {

    public static void validate(TestEntity testEntity) throws URISyntaxException {
        if (testEntity.getComparingStrategy() != ComparingStrategies.IGNORE) {
            VeraPdfTaskConfig taskConfig = createTaskConfig(testEntity);
            ValidationInfo info = runValidation(taskConfig);
            testEntity.setInfo(info);
        }
    }

    private static VeraPdfTaskConfig createTaskConfig(TestEntity entity) throws URISyntaxException {
        VeraPdfTaskConfig.Builder taskConfigBuilder = new VeraPdfTaskConfig.Builder();
        taskConfigBuilder.input(new Input(entity.getTestFile().getAbsolutePath(), false))
                         .profile(entity.getValidationProfile().getAbsolutePath())
                         .validate(true)
                         .output(new String());
        return taskConfigBuilder.build();
    }

    private static ValidationInfo runValidation(VeraPdfTaskConfig taskConfig) {
        ValidationInfo info = ValidationRunner.runValidation(taskConfig);
        return info;
    }

}
