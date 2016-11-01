/**
 * 
 */
package org.verapdf.processor;

import java.util.NoSuchElementException;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
public enum FormatOption {
    /**
     * Output in XML format
     */
    XML("xml"),
    /**
     * Output in {@link MachineReadableReport} XML format
     */
    MRR("mrr"),
    /**
     * Output in brief format
     */
    TEXT("text");

    private final String option;

    private FormatOption(final String option) {
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
     * returning {@link FormatOption#XML} if no match can be made.
     * 
     * @param option
     *            the string CLI option to compare
     * @return a matching {@code FormatType} instance or {@code XML} if no match
     *         can be made.
     */
    public static FormatOption fromOption(final String option) {
        for (FormatOption format : FormatOption.values()) {
            if (format.toString().equalsIgnoreCase(option))
                return format;
        }
        throw new NoSuchElementException("No FormatOption with option value: " + option + " found.");
    }
}
