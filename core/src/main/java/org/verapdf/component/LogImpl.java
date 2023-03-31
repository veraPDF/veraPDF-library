package org.verapdf.component;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlValue;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;

final class LogImpl implements Log {
    @XmlAttribute
    private final int occurrences;
    @XmlAttribute
    private final String level;
    @XmlValue
    private final String message;

    private LogImpl(final int occurrences, String level, final String message) {
        this.occurrences = occurrences;
        this.level = level;
        this.message = message;
    }

    @Override
    public int getOccurrences() {
        return occurrences;
    }

    @Override
    public String getLevel() {
        return level;
    }

    @Override
    public String getMessage() {
        return message;
    }

    static class Adapter extends XmlAdapter<LogImpl, Log> {
        @Override
        public Log unmarshal(LogImpl log) {
            return log;
        }

        @Override
        public LogImpl marshal(Log log) {
            return (LogImpl) log;
        }
    }

    static Log fromValues(final int occurrences, final String level, final String message) {
        if (occurrences <= 0) {
            throw new IllegalArgumentException("Argument occurrences can not be negative");
        }
        if (level == null) {
            throw new IllegalArgumentException("Argument level can not be null");
        }
        if (message == null) {
            throw new IllegalArgumentException("Argument message can not be null");
        }
        return new LogImpl(occurrences, level, message);
    }

}
