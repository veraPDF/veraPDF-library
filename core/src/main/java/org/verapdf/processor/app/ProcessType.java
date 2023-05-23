/**
 * This file is part of VeraPDF Library GUI, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * VeraPDF Library GUI is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with VeraPDF Library GUI as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * VeraPDF Library GUI as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
/**
 * 
 */
package org.verapdf.processor.app;

import java.util.EnumSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.verapdf.processor.TaskType;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 30 Oct 2016:21:39:07
 */

public enum ProcessType {
	VALIDATE("Validation", EnumSet.of(TaskType.VALIDATE)),
	FIX("fix", EnumSet.of(TaskType.VALIDATE, TaskType.FIX_METADATA)),
	EXTRACT("Features", EnumSet.of(TaskType.EXTRACT_FEATURES)),
	VALIDATE_EXTRACT("Validation and Features",EnumSet.of(TaskType.VALIDATE, TaskType.EXTRACT_FEATURES)),
	EXTRACT_FIX("extract and fix", EnumSet.of(TaskType.VALIDATE, TaskType.FIX_METADATA, TaskType.EXTRACT_FEATURES)),
	POLICY("Policy", EnumSet.of(TaskType.VALIDATE, TaskType.EXTRACT_FEATURES)),
	POLICY_FIX("policy and fix", EnumSet.of(TaskType.VALIDATE, TaskType.FIX_METADATA, TaskType.EXTRACT_FEATURES)),
	NO_PROCESS("", EnumSet.noneOf(TaskType.class));

	private static final Logger logger = Logger.getLogger(ProcessType.class.getCanonicalName());

	private final EnumSet<TaskType> tasks;
	private final String value;

	private ProcessType(final String value, EnumSet<TaskType> tasks) {
		this.value = value;
		this.tasks = EnumSet.copyOf(tasks);
	}

	public EnumSet<TaskType> getTasks() {
		return this.tasks;
	}

	public String getValue() {
		return this.value;
	}

	public static ProcessType addProcess(ProcessType base, ProcessType toAdd) {
		if (base == NO_PROCESS) {
			return toAdd;
		}
		if (toAdd == NO_PROCESS) {
			return base;
		}
		if (base == VALIDATE) {
			if (toAdd == EXTRACT) {
				return VALIDATE_EXTRACT;
			}
		} else if (base == EXTRACT) {
			if (toAdd == VALIDATE) {
				return VALIDATE_EXTRACT;
			} else if (toAdd == FIX) {
				logger.log(Level.WARNING, "Incompatible process types: Features and Fix metadata.");
				return EXTRACT;
			}
		} else if (base == FIX) {
			if (toAdd == VALIDATE_EXTRACT) {
				return EXTRACT_FIX;
			} else if (toAdd == EXTRACT) {
				logger.log(Level.WARNING, "Incompatible process types: Fix metadata and Features.");
				return FIX;
			} else if (toAdd == POLICY) {
				return POLICY_FIX;
			}
		} else if (base == VALIDATE_EXTRACT) {
			if (toAdd == FIX) {
				return EXTRACT_FIX;
			}
		} else if (base == EXTRACT_FIX) {
			if (toAdd == POLICY || toAdd == POLICY_FIX) {
				return POLICY_FIX;
			}
			return EXTRACT_FIX;
		} else if (base == POLICY) {
			if (toAdd == FIX || toAdd == EXTRACT_FIX) {
				return POLICY_FIX;
			}
			return POLICY;
		} else if (base == POLICY_FIX) {
			return POLICY_FIX;
		}
		return toAdd;
	}

	public static ProcessType[] getOptionValues() {
		return new ProcessType[] { VALIDATE, EXTRACT, VALIDATE_EXTRACT, POLICY };
	}
}
