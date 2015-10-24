package org.verapdf.runner;

import org.apache.log4j.Logger;
import org.verapdf.config.VeraPdfTaskConfig;
import org.verapdf.core.ProfileException;
import org.verapdf.core.ValidationException;
import org.verapdf.metadata.fixer.MetadataFixer;
import org.verapdf.metadata.fixer.MetadataFixerResult;
import org.verapdf.metadata.fixer.impl.pb.FixerConfigImpl;
import org.verapdf.metadata.fixer.utils.FixerConfig;
import org.verapdf.model.ModelLoader;
import org.verapdf.validation.logic.Validator;
import org.verapdf.validation.report.model.ValidationInfo;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;

import java.io.IOException;

public class ValidationRunner {

    private static final Logger LOGGER = Logger.getLogger(ValidationRunner.class);
    
    private ValidationRunner() {
        // Disable default constructor
    }

    /**
     * Helper method to run validation
     * @param config validation task configuration
     * @return the validation result
     */
    public static ValidationInfo runValidation(VeraPdfTaskConfig config) {
        try (ModelLoader loader = new ModelLoader(config.getInput().getPath())) {
            org.verapdf.model.baselayer.Object root = loader.getRoot();
			ValidationInfo info = Validator.validate(root, config.getProfile(), false,
					config.isLogPassedChecks(), config.getMaxFailedChecks(),
					config.getMaxDisplayedFailedChecks());
			if (config.getIncrementalSave() != null && !config.getIncrementalSave().trim().isEmpty()) {
				FixerConfig fixerConfig = FixerConfigImpl.getFixerConfig(loader.getPDDocument(), info);
				// TODO : what we need do with fixing result?
				MetadataFixerResult fixerResult = MetadataFixer.fixMetadata(loader.getFile(), fixerConfig);
			}
			return info;
            // TODO: Better exception handling, we need a policy and this isn't it.
            // Carl to think a little harder and tidy up, it's not a new idea I'm after,
            // more a case of ensuring we use the best of 2 methods.
        } catch (IOException | SAXException | ParserConfigurationException |
				ProfileException | XMLStreamException |
				ValidationException e) {
            //error while parsing validation profile
            LOGGER.error(e.getMessage(), e);
        }
		return null;
    }

}
