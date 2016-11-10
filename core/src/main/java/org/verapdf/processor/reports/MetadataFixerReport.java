/**
 * 
 */
package org.verapdf.processor.reports;

import java.util.List;

/**
 * @author  <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 0.1
 * 
 * Created 9 Nov 2016:07:47:03
 */
public interface MetadataFixerReport {
	public String getStatus();
	public int getFixCount();
	public List<String> getFixes();
	public List<String> getErrors();
}
