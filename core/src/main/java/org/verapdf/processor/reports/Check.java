/**
 * 
 */
package org.verapdf.processor.reports;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * @author  <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 0.1
 * 
 * Created 10 Nov 2016:08:50:42
 */
@XmlJavaTypeAdapter(CheckImpl.Adapter.class)
public interface Check {
	public String getStatus();
	public String getContext();
}
