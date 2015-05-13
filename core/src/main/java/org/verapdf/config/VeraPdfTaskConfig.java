/**
 * 
 */
package org.verapdf.config;

public final class VeraPdfTaskConfig {

	private static final VeraPdfTaskConfig DEFAULT_INSTANCE = new VeraPdfTaskConfig();

    private final boolean validate;
    private final Input input;
    private final String profile;

	private VeraPdfTaskConfig() {
		this(false, null, null);
	}

    public VeraPdfTaskConfig(boolean validate, Input input, String profile) {
        this.validate = validate;
        this.input = input;
        this.profile = profile;
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

    public static class Builder {

        private boolean validate;
        private Input input;
        private String profile;

        public Builder() {
        }

        public Builder(VeraPdfTaskConfig config) {
            this.validate = config.validate;
            this.input = config.input;
            this.profile = config.profile;
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

        public VeraPdfTaskConfig build() {
            return new VeraPdfTaskConfig(validate, input, profile);
        }

    }

}

////Check that input path is defined
//Preconditions.checkNotNull(inputPath);
////Check that profile is defined
//Preconditions.checkNotNull(profile, "Expected profile to be non-null");

