package org.verapdf.component;

import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlJavaTypeAdapter(LogImpl.Adapter.class)
public interface Log {
    int getOccurrences();
    String getLevel();
    String getMessage();
}
