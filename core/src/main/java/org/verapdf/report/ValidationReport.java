package org.verapdf.report;

import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.results.ValidationResults;
import org.verapdf.pdfa.validation.Profiles;
import org.verapdf.pdfa.validation.ValidationProfile;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Maksim Bezrukov
 */
@XmlRootElement(name = "result")
public class ValidationReport {
    private final static String STATEMENT_PREFIX = "PDF file is ";
    private final static String NOT_INSERT = "not ";
    private final static String STATEMENT_SUFFIX = "compliant with Validation Profile requirements.";
    private final static String COMPLIANT_STATEMENT = STATEMENT_PREFIX
            + STATEMENT_SUFFIX;
    private final static String NONCOMPLIANT_STATEMENT = STATEMENT_PREFIX
            + NOT_INSERT + STATEMENT_SUFFIX;
    private final static String ERROR_STATEMENT = "Could not finish validation due to unexpected error.";

    @XmlAttribute
    private final String profile;
    @XmlAttribute
    private final Boolean compliant;
    @XmlElement
    private final String statement;
    @XmlElement
    private final ValidationDetails details;


    private ValidationReport(final String profile, Boolean compliant, String statement,
                             ValidationDetails details) {
        this.profile = profile;
        this.compliant = compliant;
        this.statement = statement;
        this.details = details;
    }

    private ValidationReport() {
        this(PDFAFlavour.NO_FLAVOUR.getId(), Boolean.FALSE, "", ValidationDetails.fromValues(
                ValidationResults.defaultResult(), false, 0));
    }

    static ValidationReport createErrorReport() {
        return createErrorReport(ERROR_STATEMENT);
    }

    static ValidationReport createErrorReport(String errorMessage) {
        return new ValidationReport(null, null, errorMessage, null);
    }

    /**
     * @param profile 
     * @param result
     * @param logPassedChecks
     * @return
     */
    static ValidationReport fromValues(final ValidationProfile profile, final ValidationResult result,
                                              final boolean logPassedChecks, final int maxNumberOfDisplayedFailedChecks) {
        if (result == null) {
            return null;
        }
        return new ValidationReport(profile.getDetails().getName(), Boolean.valueOf(result.isCompliant()),
                getStatement(result.isCompliant()),
                ValidationDetails.fromValues(result, logPassedChecks, maxNumberOfDisplayedFailedChecks));
    }

    private static String getStatement(boolean status) {
        return status ? COMPLIANT_STATEMENT : NONCOMPLIANT_STATEMENT;
    }
}
