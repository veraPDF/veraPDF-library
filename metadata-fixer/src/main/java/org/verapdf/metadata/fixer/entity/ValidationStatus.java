package org.verapdf.metadata.fixer.entity;

/**
 * @author Evgeniy Muravitskiy
 */
public enum ValidationStatus {

	VALID(3),

	INVALID_METADATA(2),
	// TODO : rename entries
	INVALID_STRUCTURE(1),

	INVALID_DOCUMENT(0);

	private final int index;

	ValidationStatus(int index) {
		this.index = index;
	}

	public static ValidationStatus valueOf(int index) {
		switch (index) {
			case 0:
				return INVALID_DOCUMENT;
			case 1:
				return INVALID_STRUCTURE;
			case 2:
				return INVALID_METADATA;
			case 3:
				return VALID;
			default:
				throw new IllegalArgumentException("No enum constant for index: " + index);
		}
	}

	public ValidationStatus getStatus(ValidationStatus status) {
		int highBit = status.index & this.index & 2;
		int lowBit = status.index & this.index & 1;
		switch (highBit | lowBit) {
			case 0:
				return INVALID_DOCUMENT;
			case 1:
				return INVALID_STRUCTURE;
			case 2:
				return INVALID_METADATA;
			case 3:
				return VALID;
			default:
				throw new IllegalArgumentException("Result of transform is " + (highBit | lowBit));
		}
	}
}
