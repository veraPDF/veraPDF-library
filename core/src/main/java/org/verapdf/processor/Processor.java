/**
 * 
 */
package org.verapdf.processor;

import java.util.Collection;

import org.verapdf.ReleaseDetails;
import org.verapdf.component.Component;

/**
 * @author  <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 0.1
 * 
 * Created 2 Nov 2016:11:22:06
 */
public interface Processor extends Component {
	public ProcessorConfig getConfig();
	public Collection<ReleaseDetails> getDependencies();
}
