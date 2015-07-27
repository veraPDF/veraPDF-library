package org.verapdf.runner;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;

import org.apache.log4j.Logger;
import org.verapdf.config.VeraPdfTaskConfig;
import org.verapdf.exceptions.validationlogic.JavaScriptEvaluatingException;
import org.verapdf.exceptions.validationlogic.MultiplyGlobalVariableNameException;
import org.verapdf.exceptions.validationlogic.NullLinkException;
import org.verapdf.exceptions.validationlogic.NullLinkNameException;
import org.verapdf.exceptions.validationlogic.NullLinkedObjectException;
import org.verapdf.exceptions.validationlogic.RullWithNullIDException;
import org.verapdf.exceptions.validationprofileparser.IncorrectImportPathException;
import org.verapdf.exceptions.validationprofileparser.MissedHashTagException;
import org.verapdf.exceptions.validationprofileparser.NullProfileException;
import org.verapdf.exceptions.validationprofileparser.WrongProfileEncodingException;
import org.verapdf.exceptions.validationprofileparser.WrongSignatureException;
import org.verapdf.model.ModelLoader;
import org.verapdf.validation.logic.Validator;
import org.verapdf.validation.report.model.ValidationInfo;
import org.xml.sax.SAXException;

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
        try {
            org.verapdf.model.baselayer.Object root = ModelLoader.getRoot(config.getInput().getPath());
            return Validator.validate(root, config.getProfile(), false);
            // TODO: Better exception handling, we need a policy and this isn't it.
            // Carl to think a little harder and tidy up, it's not a new idea I'm after,
            // more a case of ensuring we use the best of 2 methods.
        } catch (FileNotFoundException e) {
            //wrong path to pdf file
            LOGGER.error(e.getMessage(), e);
        } catch (IOException | SAXException | ParserConfigurationException e) {
            //error while parsing validation profile
            LOGGER.error(e.getMessage(), e);
        } catch (NullLinkNameException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IncorrectImportPathException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (NullLinkException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (JavaScriptEvaluatingException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (NullLinkedObjectException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (RullWithNullIDException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (MissedHashTagException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (WrongSignatureException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (XMLStreamException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (WrongProfileEncodingException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (MultiplyGlobalVariableNameException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (NullProfileException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

}
