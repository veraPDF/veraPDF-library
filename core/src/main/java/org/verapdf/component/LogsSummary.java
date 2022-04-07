package org.verapdf.component;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Set;

@XmlJavaTypeAdapter(LogsSummaryImpl.Adapter.class)
public interface LogsSummary {
    int getLogsCount();
    Set<Log> getLogs();
}
