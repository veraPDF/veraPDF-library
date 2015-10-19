package org.verapdf.metadata.fixer.utils.flavour;

import java.awt.*;

/**
 * @author Evgeniy Muravitskiy
 */
public enum Part {

	NO_STANDARD(0),

	ISO_19005_1(1),

	ISO_19005_2(2),

	ISO_19005_3(3);

	private final int value;

	Part(int value) {
		this.value = value;
	}

	public final int getPartNumber() {
		return this.value;
	}

	// TODO : implement me
	public final String getId() {
		throw new IllegalComponentStateException("Implement this method");
	}

	// TODO : implement me
	public final String getName() {
		throw new IllegalComponentStateException("Implement this method");
	}

	// TODO : implement me
	public final String getDescription() {
		throw new IllegalComponentStateException("Implement this method");
	}

	// TODO : implement me
	public final String getYear() {
		throw new IllegalComponentStateException("Implement this method");
	}

}
