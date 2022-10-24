package org.verapdf.core.utils;

import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class ConsoleLogsFilter implements Filter {
    @Override
    public boolean isLoggable(LogRecord record) {
        String msg = record.getSourceClassName();
        return msg.startsWith("org.verapdf") || record.getLevel().intValue() > Level.FINE.intValue();
    }
}
