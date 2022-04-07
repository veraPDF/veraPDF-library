package org.verapdf.component;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlJavaTypeAdapter(LogImpl.Adapter.class)
public interface Log {
    int getOccurrences();
    String getLevel();
    String getMessage();
}
