package org.verapdf.core.utils;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

public class LogsFilter implements Filter {
    @Override
    public boolean isLoggable(LogRecord record) {
        String msg = record.getSourceClassName();
        return msg.startsWith("org.verapdf");
    }
}
