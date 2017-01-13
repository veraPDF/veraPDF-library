package org.verapdf.component;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlJavaTypeAdapter(AuditDurationImpl.Adapter.class)
public interface AuditDuration {

	long getStart();

	long getFinish();

	long getDifference();

	String getDuration();

}