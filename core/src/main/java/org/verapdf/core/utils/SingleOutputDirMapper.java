/**
 * 
 */
package org.verapdf.core.utils;

import java.io.File;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 16 Nov 2016:07:19:45
 */

public class SingleOutputDirMapper extends AbstractFileOutputMapper {
	private final File outputDir;

	private SingleOutputDirMapper(final File outputDir) {
		this(outputDir, defaultPrefix);
	}

	private SingleOutputDirMapper(final File outputDir, final String prefix) {
		this(outputDir, prefix, defaultSuffix);
	}

	private SingleOutputDirMapper(final File outputDir, final String prefix, final String suffix) {
		super(prefix, suffix);
		this.outputDir = outputDir;
	}

	@Override
	protected File doMapFile(File orig) {
		String newName = addPrefixAndSuffix(orig, this);
		return new File(this.outputDir, newName);
	}

	static SingleOutputDirMapper fromValues(final File outputDir, final String prefix, final String suffix) {
		return new SingleOutputDirMapper(outputDir, prefix, suffix);
	}
}
