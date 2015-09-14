/**
 *
 */
package org.verapdf.config;

/**
 * Encapsulation of the VeraPDF processing configuration. This class deals only
 * with the processing and doesn't concern itself with any "non-repeatable", or
 * technical details.
 * <p/>
 * Specifically this means that it cares not about the processing environment
 * (e.g. temp directory, how many threads, etc.). Nor does it care about the
 * particular file or stream to be operated on. It helps to think of the class
 * as capturing all aspects of an invocation that can be reused across all
 * environments and on any file.
 *
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
public final class VeraPdfTaskConfig {

    private final boolean validate;
    private final Input input;
    private final String profile;
	private final boolean logPassedChecks;
	private final int maxFailedChecks;
	private final int maxDisplayedFailedChecks;
    private final String output;

    public VeraPdfTaskConfig(boolean validate, Input input, String profile,
							 boolean logPassedChecks, int maxFailedChecks,
							 int maxDisplayedFailedChecks, String output) {
        this.validate = validate;
        this.input = input;
        this.profile = profile;
		this.logPassedChecks = logPassedChecks;
		this.maxFailedChecks = maxFailedChecks;
		this.maxDisplayedFailedChecks = maxDisplayedFailedChecks;
        this.output = output;
    }

    /**
     * @return the validate
     */
    public boolean isValidate() {
        return this.validate;
    }

    /**
     * @return the input
     */
    public Input getInput() {
        return input;
    }

    /**
     * @return the profile
     */
    public String getProfile() {
        return profile;
    }

	/**
	 * @return is need to log passed checks
	 */
	public boolean isLogPassedChecks() {
		return logPassedChecks;
	}

	/**
	 * @return maximum amount of failed checks for each rule during validation
	 */
	public int getMaxFailedChecks() {
		return maxFailedChecks;
	}

	/**
	 * @return	maximum displayed amount of failed checks for each rule during validation
	 */
	public int getMaxDisplayedFailedChecks() {
		return maxDisplayedFailedChecks;
	}

	/**
     * @return the output
     */
    public String getOutput() {
        return output;
    }
    @SuppressWarnings("hiding")
    public static class Builder {

        private boolean validate;
        private Input input;
        private String profile;
		private boolean logPassedChecks;
		private int maxFailedChecks;
		private int maxDisplayedFailedChecks;
		private String output;

		public Builder() {

        }

        public Builder validate(boolean validate) {
            this.validate = validate;
            return this;
        }

        public Builder input(Input input) {
            this.input = input;
            return this;
        }

        public Builder profile(String profile) {
            this.profile = profile;
            return this;
        }

		public Builder logPassedChecks(boolean logPassedChecks) {
			this.logPassedChecks = logPassedChecks;
			return this;
		}

		public Builder maxFailedChecks(int failedChecksCount) {
			this.maxFailedChecks = failedChecksCount;
			return this;
		}

		public Builder maxDisplayedFailedChecks(int failedChecksCount) {
			this.maxDisplayedFailedChecks = failedChecksCount;
			return this;
		}

        public Builder output(String output) {
            this.output = output;
            return this;
        }

        public VeraPdfTaskConfig build() {
            return new VeraPdfTaskConfig(this.validate, this.input, this.profile,
					this.logPassedChecks, this.maxFailedChecks, this.maxDisplayedFailedChecks,
					this.output);
        }

    }

}