/**
 * 
 */
package org.verapdf.core.utils;

import java.io.File;
import java.nio.file.Path;

import org.verapdf.core.VeraPDFException;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 13 Nov 2016:22:14:34
 */

final class RelativeRootMapper extends AbstractFileOutputMapper {
	private final Path srcRoot;
	private final Path destRoot;

	private RelativeRootMapper(final Path src, final Path dest) {
		this(src, dest, defaultPrefix);
	}

	private RelativeRootMapper(final Path src, final Path dest, final String prefix) {
		this(src, dest, prefix, defaultSuffix);
	}

	private RelativeRootMapper(final Path src, final Path dest, final String prefix, final String suffix) {
		super(prefix, suffix);
		this.srcRoot = src.normalize();
		this.destRoot = dest.normalize();
	}

	@Override
	protected File doMapFile(File orig) throws VeraPDFException {
		String newName = addPrefixAndSuffix(orig, this);
		return makeNewFile(orig, this.srcRoot, this.destRoot, newName);
	}

	private static File makeNewFile(final File orig, final Path src, final Path dest, String newName)
			throws VeraPDFException {
		Path relSrc = orig.getParentFile().toPath().relativize(src);
		Path absDest = dest.resolve(relSrc);
		File parent = absDest.toFile();
		if (!parent.exists() && !parent.mkdirs()) {
			throw new VeraPDFException("Cannot create directory: " + parent);
		}
		return new File(parent, newName);
	}

	static RelativeRootMapper fromValues(final Path src, final Path dest, final String prefix, final String suffix) {
		return new RelativeRootMapper(src, dest, prefix, suffix);
	}
}
