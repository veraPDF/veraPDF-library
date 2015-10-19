package org.verapdf.metadata.fixer.utils.flavour;

import java.awt.*;

/**
 * @author Evgeniy Muravitskiy
 */
public enum Level {

	NO_LEVEL('f'),

	A('a'),

	B('b'),

	U('u');

	private final char value;

	Level(char value) {
		this.value = value;
	}

	// TODO : implement me
	public String getName() {
		throw new IllegalComponentStateException("Implement this method");
	}

	public String getCode() {
		return String.valueOf(this.value);
	}
}
