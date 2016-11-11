package org.verapdf.component;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlJavaTypeAdapter(AuditDurationImpl.Adapter.class)
public interface AuditDuration {

	/**
	 * @return the start
	 */
	public long getStart();

	/**
	 * @return the finish
	 */
	public long getFinish();

	/**
	 * @return the finish
	 */
	public long getDifference();

	/**
	 * @return the duration string
	 */
	public String getDuration();

}