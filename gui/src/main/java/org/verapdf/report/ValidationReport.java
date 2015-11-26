package org.verapdf.report;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.verapdf.pdfa.MetadataFixerResult;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.results.ValidationResults;
import org.verapdf.pdfa.validation.Profiles;
import org.verapdf.pdfa.validation.ValidationProfile;

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

    @XmlAttribute
    private final String profile;
    @XmlAttribute
    private final boolean compliant;
    @XmlElement
    private final String statement;
    @XmlElement
    private final ValidationSummary summary;

    // @XmlElementWrapper
    // @XmlElement(name = "warning")
    // private final Set<Warning> warnings;

    private ValidationReport(final String profile, boolean compliant, String statement,
            ValidationSummary summary) {
        this.profile = profile;
        this.compliant = compliant;
        this.statement = statement;
        this.summary = summary;
    }

    private ValidationReport() {
        this(PDFAFlavour.NO_FLAVOUR.getId(), false, "", ValidationSummary.fromValues(Profiles.defaultProfile(),
                ValidationResults.defaultResult(), false));
    }

    static ValidationReport fromValues(final ValidationProfile profile, final ValidationResult result, final boolean logPassedChecks,
            final MetadataFixerResult fixerResult) {

        if (fixerResult != null) {
            return new ValidationReport(profile.getDetails().getName(), result.isCompliant(),
                    getStatement(result.isCompliant()), ValidationSummary.fromValues(profile,
                            result, logPassedChecks, fixerResult));
        }

        return new ValidationReport(profile.getDetails().getName(), result.isCompliant(),
                getStatement(result.isCompliant()), ValidationSummary.fromValues(profile,
                        result, logPassedChecks));
    }

    /**
     * @param profile 
     * @param result
     * @param logPassedChecks
     * @return
     */
    public static ValidationReport fromValues(final ValidationProfile profile, final ValidationResult result,
            final boolean logPassedChecks) {
        return new ValidationReport(profile.getDetails().getName(), result.isCompliant(),
                getStatement(result.isCompliant()),
                ValidationSummary.fromValues(profile, result, logPassedChecks));
    }

    private static String getStatement(boolean status) {
        return (status) ? COMPLIANT_STATEMENT : NONCOMPLIANT_STATEMENT;
    }
}
