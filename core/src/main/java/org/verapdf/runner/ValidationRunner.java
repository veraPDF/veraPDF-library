package org.verapdf.runner;

import org.verapdf.config.VeraPdfTaskConfig;
import org.verapdf.exceptions.validationlogic.JavaScriptEvaluatingException;
import org.verapdf.exceptions.validationlogic.NullLinkException;
import org.verapdf.exceptions.validationlogic.NullLinkNameException;
import org.verapdf.exceptions.validationlogic.NullLinkedObjectException;
import org.verapdf.exceptions.validationprofileparser.IncorrectImportPathException;
import org.verapdf.model.ModelLoader;
import org.verapdf.model.baselayer.*;
import org.verapdf.model.coslayer.CosDict;
import org.verapdf.model.coslayer.CosDocument;
import org.verapdf.validation.logic.Validator;
import org.verapdf.validation.report.model.ValidationInfo;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ValidationRunner {

    /**
     * Helper method to run validation
     * @param config validation task configuration
     * @return the validation result
     */
    public static ValidationInfo runValidation(VeraPdfTaskConfig config) {
        try {
            org.verapdf.model.baselayer.Object root = ModelLoader.getRoot(config.getInput().getPath());
            return Validator.validate(root, config.getProfile());
            //TODO: think what to do with errors
        } catch (FileNotFoundException e) {
            //wrong path to pdf file
            e.printStackTrace();
        } catch (IOException | SAXException | ParserConfigurationException e) {
            //error while parsing validation profile
            e.printStackTrace();
        } catch (NullLinkNameException e) {
            e.printStackTrace();
        } catch (IncorrectImportPathException e) {
            e.printStackTrace();
        } catch (NullLinkException e) {
            e.printStackTrace();
        } catch (JavaScriptEvaluatingException e) {
            e.printStackTrace();
        } catch (NullLinkedObjectException e) {
            e.printStackTrace();
        }
        return null;
    }

}
