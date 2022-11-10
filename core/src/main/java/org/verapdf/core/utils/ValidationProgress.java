package org.verapdf.core.utils;

public class ValidationProgress {

	private static final int UPPER_LIMIT_POWER_OF_TEN = 6;
	private static final int UPPER_POWER_OF_TEN_STEP = 5;
	private static final double UPPER_LIMIT = Math.pow(10, UPPER_LIMIT_POWER_OF_TEN);
	private static final double UPPER_LIMIT_STEP = Math.pow(10, UPPER_POWER_OF_TEN_STEP);

	private final boolean showProgress;

	private double currentStep = 1;
	private int currentPowerOfTenStep = 0;
	private int numberOfChecks;
	private int numberOfFailedChecks;
	private int numberOfProcessedObjects;
	private int numberOfObjectsToBeProcessed;

	private int lengthOfPrevProgressString = 1;

	public ValidationProgress(boolean showProgress) {
		this.showProgress = showProgress;
	}

	public void updateVariables() {
		if (showProgress) {
			System.out.println();
		}
		
		numberOfChecks = 0;
		numberOfFailedChecks = 0;
		numberOfProcessedObjects = 0;
		numberOfObjectsToBeProcessed = 1;
	}

	public void incrementNumberOfChecks() {
		++numberOfChecks;
		if (showProgress && checkCurrentNumberOfChecks()) {
			showProgressDuringValidation();
			checkAndChangeCurrentStep();
		}
	}

	public void showProgressAfterValidation() {
		if (showProgress) {
			System.err.printf("%-" + this.lengthOfPrevProgressString + "s%n", getCurrentValidationJobProgress());
		}
	}

	public void updateNumberOfFailedChecks(int numberOfFailedChecks) {
		this.numberOfFailedChecks = numberOfFailedChecks;
	}

	public void incrementNumberOfProcessedObjects() {
		++numberOfProcessedObjects;
	}

	public void updateNumberOfObjectsToBeProcessed(int numberOfObjectsToBeProcessed) {
		this.numberOfObjectsToBeProcessed = numberOfObjectsToBeProcessed;
	}

	public String getCurrentValidationJobProgressWithCommas() {
		return "Progress: "
		       + numberOfChecks + " checks, "
		       + numberOfFailedChecks + " failed, "
		       + numberOfProcessedObjects + " processed objects, "
		       + numberOfObjectsToBeProcessed + " in stack.";
	}

	private String getCurrentValidationJobProgress() {
		return "Progress: "
		       + numberOfChecks + " checks / "
		       + numberOfFailedChecks + " failed / "
		       + numberOfProcessedObjects + " processed objects / "
		       + numberOfObjectsToBeProcessed + " in stack.";
	}

	private boolean checkCurrentNumberOfChecks() {
		return numberOfChecks % currentStep == 0;
	}

	private void checkAndChangeCurrentStep() {
		if (numberOfChecks > UPPER_LIMIT) {
			return;
		}
		if (numberOfChecks == Math.pow(10, (double) currentPowerOfTenStep + 1)) {
			++currentPowerOfTenStep;
			currentStep = Math.pow(10, currentPowerOfTenStep);
		}
		if (numberOfChecks == UPPER_LIMIT) {
			currentStep = UPPER_LIMIT_STEP;
		}
	}

	private void showProgressDuringValidation() {
		String currentProgress = getCurrentValidationJobProgress();
		System.err.printf("%-" + this.lengthOfPrevProgressString + "s\r", currentProgress);
		this.lengthOfPrevProgressString = currentProgress.length();
	}
}
