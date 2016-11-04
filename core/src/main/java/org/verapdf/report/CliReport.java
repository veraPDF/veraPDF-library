/**
 * 
 */
package org.verapdf.report;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.results.ValidationResults;

/**
 * @author cfw
 *
 */
@XmlRootElement(name = "cliReport")
public final class CliReport {
    @XmlElement
    private final ItemDetails itemDetails;
    @XmlElement
    private final ValidationResult validationResult;
    @XmlElement
    private final FeaturesReport featuresReport;

    private CliReport() {
        this(ItemDetails.DEFAULT, ValidationResults.defaultResult(), null);
    }

    private CliReport(final ItemDetails itemDetails, final ValidationResult result,
            final FeaturesReport featuresReport) {
        this.itemDetails = itemDetails;
        this.validationResult = result;
        this.featuresReport = featuresReport;
    }

    public static CliReport fromValues(ItemDetails details, ValidationResult result,
            FeaturesReport features) {
        return new CliReport(details, result, features);
    }

}
