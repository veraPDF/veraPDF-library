/**
 * 
 */
package org.verapdf.core.utils;

import java.io.File;

import org.verapdf.core.VeraPDFException;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 13 Nov 2016:10:47:10
 */

abstract class AbstractFileOutputMapper implements FileOutputMapper {
	protected static final String defaultPrefix = "veraPDF_";
	protected static final String defaultSuffix = "";
	protected static final String extSplitter = ".";
	private final String prefix;
	private final String suffix;

	protected AbstractFileOutputMapper() {
		this(defaultPrefix);
	}

	protected AbstractFileOutputMapper(final String prefix) {
		this(prefix, defaultSuffix);
	}

	/**
	 * 
	 */
	protected AbstractFileOutputMapper(final String prefix, final String suffix) {
		super();
		this.prefix = prefix;
		this.suffix = suffix;
	}

	@Override
	public String getPrefix() {
		return this.prefix;
	}

	@Override
	public String getSuffix() {
		return this.suffix;
	}

	@Override
	public File mapFile(final File orig) throws VeraPDFException {
		return doMapFile(orig);
	}

	abstract protected File doMapFile(final File orig) throws VeraPDFException;

	protected static String addPrefixAndSuffix(final File orig, final FileOutputMapper mapper) {
		if (orig == null)
			throw new NullPointerException("Arg orig can not be null.");
		String filename = orig.getName();
		String newName = mapper.getPrefix() + filename;
		if (!mapper.getSuffix().isEmpty() && !isExtension(mapper) && hasExtension(filename)) {
			// If we have a suffix and an extensions
			newName = newName.substring(0, newName.lastIndexOf(extSplitter)) + mapper.getSuffix()
					+ filename.substring(filename.lastIndexOf(extSplitter));
		} else
			// else it's safe to add the suffix as is
			newName = newName + mapper.getSuffix();
		return newName;
	}

	private static boolean isExtension(FileOutputMapper toTest) {
		return toTest.getSuffix().startsWith(".");
	}

	private static boolean hasExtension(final String filename) {
		return filename.lastIndexOf(extSplitter) > 0;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AbstractFileOutputMapper [prefix=" + this.prefix + ", suffix=" + this.suffix + "]";
	}
}
