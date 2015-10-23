/**
 * 
 */
package org.verapdf.validation.profile.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;

import org.verapdf.exceptions.validationlogic.MultiplyGlobalVariableNameException;
import org.verapdf.exceptions.validationprofileparser.MissedHashTagException;
import org.verapdf.exceptions.validationprofileparser.WrongSignatureException;
import org.verapdf.pdfa.ValidationProfile;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.flavours.PDFAFlavour.Specification;
import org.verapdf.pdfa.validation.Profiles;
import org.verapdf.pdfa.validation.Reference;
import org.verapdf.pdfa.validation.Rule;
import org.verapdf.pdfa.validation.RuleId;
import org.verapdf.pdfa.validation.Variable;
import org.xml.sax.SAXException;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
public final class LegacyProfileParser {
    private static final String PART_1_LEGACY_CODE = "ISO 19005-1:2005";

    private LegacyProfileParser() {
        /** Disable default constructor */
    }

    /**
     * @param args
     * @throws XMLStreamException 
     * @throws IOException 
     * @throws SAXException 
     * @throws ParserConfigurationException 
     * @throws MultiplyGlobalVariableNameException 
     * @throws WrongSignatureException 
     * @throws MissedHashTagException 
     * @throws JAXBException 
     */
    public static void main(String[] args) throws MissedHashTagException, WrongSignatureException, MultiplyGlobalVariableNameException, ParserConfigurationException, SAXException, IOException, XMLStreamException, JAXBException {
        org.verapdf.validation.profile.model.ValidationProfile toConvert = ValidationProfileParser.parseFromFilePath("/home/cfw/GitHub/veraPDF/veraPDF-validation-profiles/PDF_A/PDFA-1B.xml", false);
        ValidationProfile profile = fromLegacyProfile(toConvert, PDFAFlavour.PDFA_1_B);
    }

    /**
     * @param toConvert
     * @return
     */
    public static ValidationProfile fromLegacyProfile(
            org.verapdf.validation.profile.model.ValidationProfile toConvert,
            final PDFAFlavour flavour) {
        Set<Rule> rules = new HashSet<>();
        for (String ruleId : toConvert.getAllRulesId()) {
            org.verapdf.validation.profile.model.Rule rule = toConvert.getRuleById(ruleId);
            rules.add(fromLegacyRule(rule));
        }
        Set<Variable> variables = new HashSet<>();
        for (org.verapdf.validation.profile.model.Variable var : toConvert.getAllVariables()) {
            variables.add(fromLegacyVariable(var));
        }
        String[] dateParts = toConvert.getCreated().split("T");
        String cleanDate = dateParts[0] + "T" + dateParts[1].replace("-", ":").replace("+03", "");
        Date created = javax.xml.bind.DatatypeConverter.parseDateTime(cleanDate).getTime();
        return Profiles.profileFromValues(flavour, toConvert.getName(),
                toConvert.getDescription(), toConvert.getCreator(), created,
                "", rules, variables);
    }

    /**
     * @param toConvert
     * @return
     */
    public static Rule fromLegacyRule(
            org.verapdf.validation.profile.model.Rule toConvert) {
        // Get the root reference for the Rule
        org.verapdf.validation.profile.model.Reference rootRef = toConvert
                .getReference();
        // Create a list for flattened refs
        List<Reference> flattenedRefs = new ArrayList<>();
        // If it's a corrigenda ref add an extra ref to the flattened list
        if (rootRef.getSpecification().length() > PART_1_LEGACY_CODE.length()) {
            flattenedRefs.add(fromLegacyReference(rootRef));
        }
        // Add to other flattened sub references
        flattenedRefs.addAll(flattenSubReferences(rootRef));
        // Get the Id
        RuleId id = fromLegacyRuleId(toConvert.getAttrID());
        // check consistency with root
        if (!id.getClause().equals(rootRef.getClause())) {
            System.out.println(toConvert.getAttrID() + "\t" + toConvert.getReference().getClause());
        }
        Rule converted = Profiles.ruleFromValues(id, toConvert.getAttrObject(),
                toConvert.getDescription().trim().replaceAll(" +", " "), toConvert.getTest(), flattenedRefs);
        return converted;
    }
    

    public static RuleId fromLegacyRuleId(final String legacyId) {
        String[] parts = legacyId.split("-");
        StringBuilder builder = new StringBuilder();
        String separator = "";
        int testNumber = 0;
        for (String part : parts) {
            if (!part.startsWith("t")) {
                builder.append(separator);
                builder.append(part);
                separator = ".";
            } else {
                testNumber = Integer.parseInt(part.substring(1));
            }
        }
        return Profiles.ruleIdFromValues(Specification.ISO_19005_1, builder.toString(), testNumber);
    }

    /**
     * @param toConvert
     * @return
     */
    public static Reference fromLegacyReference(
            org.verapdf.validation.profile.model.Reference toConvert) {
        Reference converted = Profiles.referenceFromValues(
                toConvert.getSpecification(), (toConvert.getClause() == null) ? "" : toConvert.getClause());
        return converted;
    }

    /**
     * @param toConvert
     * @return
     */
    public static Variable fromLegacyVariable(
            org.verapdf.validation.profile.model.Variable toConvert) {
        Variable converted = Profiles.variableFromValues(
                toConvert.getAttrName(), toConvert.getAttrObject(),
                toConvert.getDefaultValue(), toConvert.getValue());
        return converted;
    }

    /**
     * @param specCode
     * @return
     */
    public static PDFAFlavour.Specification partFromSpecCode(
            final String specCode) {
        if (specCode.equals(PART_1_LEGACY_CODE)) {
            return Specification.ISO_19005_1;
        }
        return Specification.NO_STANDARD;
    }

    /**
     * @param specCode
     * @return
     */
    public static PDFAFlavour flavourFromSpecCode(final String specCode) {
        if (specCode.equals(PART_1_LEGACY_CODE)) {
            return PDFAFlavour.PDFA_1_A;
        }
        return PDFAFlavour.NO_FLAVOUR;
    }

    private static List<Reference> flattenSubReferences(
            org.verapdf.validation.profile.model.Reference toFlatten) {
        List<Reference> flattenedList = new ArrayList<>();
        if (toFlatten != null && toFlatten.getReferences() != null) {
            for (org.verapdf.validation.profile.model.Reference ref : toFlatten
                    .getReferences()) {
                flattenedList.add(fromLegacyReference(ref));
                flattenedList.addAll(flattenSubReferences(ref));
            }
        }
        return flattenedList;
    }
}
