/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Library core is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library core as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library core as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.metadata.fixer.utils;

import org.verapdf.metadata.fixer.utils.model.ProcessedObjects;
import org.verapdf.metadata.fixer.utils.parser.ProcessedObjectsParser;
import org.verapdf.metadata.fixer.utils.parser.XMLProcessedObjectsParser;
import org.verapdf.pdfa.results.TestAssertion;
import org.verapdf.pdfa.results.TestAssertion.Status;
import org.verapdf.pdfa.validation.profiles.Rule;
import org.verapdf.pdfa.validation.profiles.ValidationProfile;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Current class check status of validation according to failed rules,
 * validation profile and list of Metadata failed rule types.
 * <p>
 * Metadata failed rule type is list of rules that belongs to metadata check
 * rules. This rules can be obtain by {@link ProcessedObjectsParser}. Default
 * implementation of this interface is {@link XMLProcessedObjectsParser}.
 * </p>
 *
 * @author Evgeniy Muravitskiy
 *
 * @see Rule
 * @see ValidationProfile
 * @see ValidationStatus
 */
public class ProcessedObjectsInspector {

    private ProcessedObjectsInspector() {
        // hide default constructor
    }

    /**
     * Return validation status according to failed rules and validation
     * profile. {@link ProcessedObjects} obtain from xml file.
     * @param assertions 
     *
     * @param profile
     *            validation profile
     * @return validation status
     * @throws URISyntaxException 
     * @throws IOException 
     * @throws ParserConfigurationException 
     * @throws SAXException 
     */
    public static ValidationStatus validationStatus(
            List<TestAssertion> assertions, ValidationProfile profile)
            throws URISyntaxException, IOException,
            ParserConfigurationException, SAXException {
        ProcessedObjectsParser parser = XMLProcessedObjectsParser.getInstance();
        return validationStatus(assertions, profile, parser);
    }

    /**
     * Return validation status according to failed rules and validation
     * profile. {@link ProcessedObjects} obtain by
     * {@link ProcessedObjectsParser}.
     * @param assertions 
     *
     * @param profile
     *            validation profile
     * @param parser
     *            parse some data to {@code ProcessedObjects}
     * @return validation status
     * @throws IOException 
     * @throws URISyntaxException 
     * @throws ParserConfigurationException 
     * @throws SAXException 
     */
    public static ValidationStatus validationStatus(
            List<TestAssertion> assertions, ValidationProfile profile,
            ProcessedObjectsParser parser) throws IOException,
            URISyntaxException, ParserConfigurationException, SAXException {
        return validationStatus(assertions, profile,
                parser.getProcessedObjects(profile.getPDFAFlavour()));
    }

    /**
     * Return validation status according to failed rules, validation profile
     * and metadata failed rules type
     * @param assertions 
     *
     * @param profile
     *            validation profile
     * @param objects
     *            metadata failed rules type
     * @return validation status
     */
    public static ValidationStatus validationStatus(
            List<TestAssertion> assertions, ValidationProfile profile,
            ProcessedObjects objects) {
        ValidationStatus status = ValidationStatus.VALID;

        for (TestAssertion assertion : assertions) {
            if (assertion.getStatus() == Status.FAILED) {
                status = checkCurrentRule(profile, objects, assertion, status);
            }
        }

        return status;
    }

    private static ValidationStatus checkCurrentRule(ValidationProfile profile,
            ProcessedObjects objects, TestAssertion rule,
            ValidationStatus status) {
        Rule profileRule = profile.getRuleByRuleId(rule.getRuleId());
        String objectType = profileRule.getObject();
        if (objects.contains(objectType, profileRule.getTest())) {
            return ValidationStatus.INVALID_METADATA.getStatus(status);
        }
        return ValidationStatus.INVALID_STRUCTURE.getStatus(status);
    }

}
