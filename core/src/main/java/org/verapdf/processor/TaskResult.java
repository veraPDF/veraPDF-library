/**
 * 
 */
package org.verapdf.processor;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.verapdf.component.AuditDuration;
import org.verapdf.core.VeraPDFException;

/**
 * @author  <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 0.1
 * 
 * Created 30 Oct 2016:13:42:47
 */
@XmlJavaTypeAdapter(TaskResultImpl.Adapter.class)
public interface TaskResult {
	public boolean isExecuted();
	public boolean isSuccess();
	public TaskType getType();
	public VeraPDFException getException();
	public AuditDuration getDuration();
}
