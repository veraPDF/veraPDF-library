/**
 *
 */
package org.verapdf.processor;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.verapdf.component.AuditDuration;
import org.verapdf.component.Components;
import org.verapdf.core.VeraPDFException;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 30 Oct 2016:14:21:22
 */
@XmlRootElement(name = "taskResult")
class TaskResultImpl implements TaskResult {
	private static final VeraPDFException notExecutedExcept = new VeraPDFException("Not Executed");
	private static final TaskResult defaultInstance = new TaskResultImpl();
	private static final String EXCEPTION = "Exception: ";
	private static final String CAUSED_BY = " caused by exception: ";

	private final VeraPDFException exception;

	@XmlAttribute
	private final TaskType type;
	@XmlAttribute
	private final boolean isExecuted;
	@XmlAttribute
	private final boolean isSuccess;
	@XmlElement
	private final AuditDuration duration;

	@XmlElement
	public String getExceptionMessage() {
		if (this.exception == null)
			return null;

		Throwable e = this.exception;
		String res = EXCEPTION + e.getMessage();
		e = e.getCause();
		while (e != null) {
			res += CAUSED_BY + e.getMessage();
			e = e.getCause();
		}
		return res;
	}

	private TaskResultImpl() {
		this(TaskType.NONE, false, false, Components.defaultDuration(), notExecutedExcept);
	}

	private TaskResultImpl(final TaskType type, final AuditDuration duration) {
		this(type, true, true, duration, null);
	}

	private TaskResultImpl(final TaskType type, final AuditDuration duration, final VeraPDFException exception) {
		this(type, true, false, duration, exception);
	}

	private TaskResultImpl(final TaskType type, final boolean isExecuted, final boolean isSuccess,
			final AuditDuration duration, final VeraPDFException exception) {
		super();
		this.type = type;
		this.isExecuted = isExecuted;
		this.isSuccess = isSuccess;
		this.duration = duration;
		this.exception = exception;
	}

	@Override
	public TaskType getType() {
		return this.type;
	}

	@Override
	public boolean isSuccess() {
		return this.isSuccess;
	}

	@Override
	public VeraPDFException getException() {
		return this.exception;
	}

	@Override
	public boolean isExecuted() {
		return this.isExecuted;
	}

	@Override
	public AuditDuration getDuration() {
		return this.duration;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.duration == null) ? 0 : this.duration.hashCode());
		result = prime * result + ((this.exception == null) ? 0 : this.exception.hashCode());
		result = prime * result + (this.isExecuted ? 1231 : 1237);
		result = prime * result + (this.isSuccess ? 1231 : 1237);
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof TaskResultImpl)) {
			return false;
		}
		TaskResultImpl other = (TaskResultImpl) obj;
		if (this.duration == null) {
			if (other.duration != null) {
				return false;
			}
		} else if (!this.duration.equals(other.duration)) {
			return false;
		}
		if (this.exception == null) {
			if (other.exception != null) {
				return false;
			}
		} else if (!this.exception.equals(other.exception)) {
			return false;
		}
		if (this.isExecuted != other.isExecuted) {
			return false;
		}
		if (this.isSuccess != other.isSuccess) {
			return false;
		}
		return true;
	}

	static TaskResult defaultInstance() {
		return defaultInstance;
	}

	static TaskResult fromValues(final TaskType type, final AuditDuration duration) {
		return new TaskResultImpl(type, duration);
	}

	static TaskResult fromValues(final TaskType type, final AuditDuration duration, VeraPDFException exception) {
		return new TaskResultImpl(type, duration, exception);
	}

	static class Adapter extends XmlAdapter<TaskResultImpl, TaskResult> {
		@Override
		public TaskResult unmarshal(TaskResultImpl procResultImpl) {
			return procResultImpl;
		}

		@Override
		public TaskResultImpl marshal(TaskResult procResult) {
			return (TaskResultImpl) procResult;
		}
	}
}
