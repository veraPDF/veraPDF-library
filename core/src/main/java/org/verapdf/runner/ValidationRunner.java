package org.verapdf.runner;

import org.apache.log4j.Logger;
import org.verapdf.config.VeraPdfTaskConfig;
import org.verapdf.exceptions.validationlogic.MultiplyGlobalVariableNameException;
import org.verapdf.exceptions.validationlogic.NullLinkException;
import org.verapdf.exceptions.validationlogic.NullLinkNameException;
import org.verapdf.exceptions.validationlogic.NullLinkedObjectException;
import org.verapdf.exceptions.validationprofileparser.MissedHashTagException;
import org.verapdf.exceptions.validationprofileparser.WrongSignatureException;
import org.verapdf.metadata.fixer.MetadataFixer;
import org.verapdf.model.ModelLoader;
import org.verapdf.validation.logic.Validator;
import org.verapdf.validation.report.model.ValidationInfo;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

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
				MetadataFixer fixer = new MetadataFixer(loader.getPDDocument(), info);
				fixer.fixDocument(loader.getFile());
			}
			return info;
            // TODO: Better exception handling, we need a policy and this isn't it.
            // Carl to think a little harder and tidy up, it's not a new idea I'm after,
            // more a case of ensuring we use the best of 2 methods.
        } catch (IOException | SAXException | ParserConfigurationException |
				NullLinkNameException | NullLinkException | NullLinkedObjectException |
				MissedHashTagException | WrongSignatureException | XMLStreamException |
				MultiplyGlobalVariableNameException | TransformerException | URISyntaxException e) {
            //error while parsing validation profile
            LOGGER.error(e.getMessage(), e);
        }
		return null;
    }

}
