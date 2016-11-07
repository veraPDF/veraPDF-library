/**
 * 
 */
package org.verapdf.component;

import java.io.Closeable;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 27 Oct 2016:00:13:03
 */

public interface Component extends Closeable {
	public ComponentDetails getDetails();
}
