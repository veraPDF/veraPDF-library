package org.verapdf.runner;

import org.apache.log4j.Logger;
import org.verapdf.config.VeraPdfTaskConfig;
import org.verapdf.exceptions.validationlogic.*;
import org.verapdf.exceptions.validationprofileparser.*;
import org.verapdf.model.ModelLoader;
import org.verapdf.validation.logic.Validator;
import org.verapdf.validation.report.model.ValidationInfo;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ValidationRunner {

    private final static Logger logger = Logger.getLogger(ValidationRunner.class);

    /**
     * Helper method to run validation
     * @param config validation task configuration
     * @return the validation result
     */
    public static ValidationInfo runValidation(VeraPdfTaskConfig config) {
        try {
            org.verapdf.model.baselayer.Object root = ModelLoader.getRoot(config.getInput().getPath());
            return Validator.validate(root, config.getProfile(), false);
            //TODO: think what to do with errors
        } catch (FileNotFoundException e) {
            //wrong path to pdf file
            logger.error(e.getMessage());
        } catch (IOException | SAXException | ParserConfigurationException e) {
            //error while parsing validation profile
            logger.error(e.getMessage());
        } catch (NullLinkNameException e) {
            logger.error(e.getMessage());
        } catch (IncorrectImportPathException e) {
            logger.error(e.getMessage());
        } catch (NullLinkException e) {
            logger.error(e.getMessage());
        } catch (JavaScriptEvaluatingException e) {
            logger.error(e.getMessage());
        } catch (NullLinkedObjectException e) {
            logger.error(e.getMessage());
        } catch (RullWithNullIDException e) {
            logger.error(e.getMessage());
        } catch (MissedHashTagException e) {
            logger.error(e.getMessage());
        } catch (WrongSignatureException e) {
            logger.error(e.getMessage());
        } catch (XMLStreamException e) {
            logger.error(e.getMessage());
        } catch (WrongProfileEncodingException e) {
            logger.error(e.getMessage());
        } catch (MultiplyGlobalVariableNameException e) {
            logger.error(e.getMessage());
        } catch (NullProfileException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

}
