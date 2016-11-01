/**
 * 
 */
package org.verapdf.pdfa.validation.validators;

import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.validation.profiles.ValidationProfile;

/**
 * @author  <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 0.1
 * 
 * Created 26 Oct 2016:00:04:41
 */

public interface ValidatorConfig {
	public boolean isRecordPasses();
	public int getMaxFails();
	public int getMaxFailsPerRule();
	public PDFAFlavour getFlavour();
	public ValidationProfile getProfile();
}
