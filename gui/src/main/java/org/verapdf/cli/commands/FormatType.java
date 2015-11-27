/**
 * 
 */
package org.verapdf.cli.commands;

import org.verapdf.report.MachineReadableReport;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
public enum FormatType {
    /**
     * Output in XML format
     */
    XML("xml"),
    /**
     * Output in {@link MachineReadableReport} XML format
     */
    MRR("mmr"),
    /**
     * Output in HTML format
     */
    HTML("html");

    private final String option;

    private FormatType(final String option) {
        this.option = option;
    }

    /**
     * @return the option string for the {@code FormatType} instance.
     */
    public String getOption() {
        return this.option;
    }

    @Override
    public String toString() {
        return this.getOption();
    }

    /**
     * Performs a match against the parameter {@code String option} of each
     * {@code FormatType}'s option and returns a matching instance. Defaults to
     * returning {@link FormatType#XML} if no match can be made.
     * 
     * @param option
     *            the string CLI option to compare
     * @return a matching {@code FormatType} instance or {@code XML} if no match
     *         can be made.
     */
    public static FormatType fromOption(final String option) {
        for (FormatType format : FormatType.values()) {
            if (format.option.startsWith(option.toLowerCase()))
                return format;
        }
        return XML;
    }

    /**
     * @return a String comprising a comma separated list of all format options.
     */
    public static String listFormatOptions() {
        StringBuilder builder = new StringBuilder();
        String separator = "";
        for (FormatType format : FormatType.values()) {
            builder.append(separator + format.option);
            separator = ", ";
        }
        return builder.toString();
    }
}
