/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2025, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Library core is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library core as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library core as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
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
