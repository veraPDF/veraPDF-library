/**
 * 
 */
package org.verapdf.pdfa.validation.validators;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.verapdf.pdfa.flavours.PDFAFlavour;

/**
 * @author  <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 0.1
 * 
 * Created 26 Oct 2016:00:04:41
 */
@XmlJavaTypeAdapter(ValidatorConfigImpl.Adapter.class)
public interface ValidatorConfig {
	public boolean isRecordPasses();
	public int getMaxFails();
	public PDFAFlavour getFlavour();
}
