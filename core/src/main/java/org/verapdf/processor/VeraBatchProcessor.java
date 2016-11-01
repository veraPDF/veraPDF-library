/**
 * 
 */
package org.verapdf.processor;

import java.io.File;
import java.util.Set;

/**
 * @author  <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 0.1
 * 
 * Created 30 Oct 2016:12:48:18
 */

public interface VeraBatchProcessor extends VeraProcessor {
	Set<ProcessorResultImpl> process(Set<File> toProcess);
}
