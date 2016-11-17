package org.verapdf.core.utils;

import java.io.File;
import java.nio.file.Path;

/**
 * Factory for various kinds of {@link FileOutputMapper}.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 13 Nov 2016:14:07:32
 */
public class FileOutputMappers {
	private FileOutputMappers() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Get a {@link FileOutputMapper} instance that maps output to the same
	 * directory as the source with a veraPDF_ prefix
	 * 
	 * @return a new FileOutputMapper
	 */
	public static final FileOutputMapper sibFiles() {
		return RelativeDirectoryMapper.defaultInstance();
	}

	/**
	 * Get a {@link FileOutputMapper} instance that maps output to the same
	 * directory as the source with a veraPDF_ prefix
	 * 
	 * @return a new FileOutputMapper
	 */
	public static final FileOutputMapper verSibFiles() {
		return VersioningMapper.defaultInstance();
	}

	/**
	 * Get a {@link FileOutputMapper} instance that maps output to the same
	 * directory as the source with the requested prefix
	 * 
	 * @param prefix
	 *            the requested prefix, can not be null or empty
	 * @return a new FileOutputMapper
	 */
	public static final FileOutputMapper sibFiles(final String prefix) {
		return sibFiles(prefix, AbstractFileOutputMapper.defaultSuffix);
	}

	/**
	 * Get a {@link FileOutputMapper} instance that maps output to the same
	 * directory as the source with the requested prefix
	 * 
	 * @param prefix
	 *            the requested prefix, can not be null or empty
	 * @return a new FileOutputMapper
	 */
	public static final FileOutputMapper verSibFiles(final String prefix) {
		return verSibFiles(prefix, AbstractFileOutputMapper.defaultSuffix);
	}

	/**
	 * Get a {@link FileOutputMapper} instance that maps output to the same
	 * directory as the source with the requested prefix and suffix
	 * 
	 * @param prefix
	 *            the prefix for generated files, can not be null or empty
	 * @param suffix
	 *            the suffix for generated files, can not be null
	 * @return a new FileOutputMapper
	 */
	public static final FileOutputMapper sibFiles(final String prefix, final String suffix) {
		if (prefix == null)
			throw new NullPointerException("Arg prefix can not be null");
		if (suffix == null)
			throw new NullPointerException("Arg suffix can not be null");
		return RelativeDirectoryMapper.withPrefixAndSuffix(prefix, suffix);
	}

	/**
	 * Get a {@link FileOutputMapper} instance that maps output to the same
	 * directory as the source with the requested prefix and suffix
	 * 
	 * @param prefix
	 *            the prefix for generated files, can not be null or empty
	 * @param suffix
	 *            the suffix for generated files, can not be null
	 * @return a new FileOutputMapper
	 */
	public static final FileOutputMapper verSibFiles(final String prefix, final String suffix) {
		return VersioningMapper.newInstance(sibFiles(prefix, suffix));
	}

	/**
	 * Maps to a relative subfolder of the original with no prefix or suffix
	 * 
	 * @param relativePath
	 *            the relative path to the results folder from the original
	 *            source file location, can not be null or empty
	 * @return a new FileOutputMapper
	 */
	public static final FileOutputMapper subFold(final String relativePath) {
		return subFold(relativePath, AbstractFileOutputMapper.defaultPrefix);
	}

	/**
	 * Maps to a relative subfolder of the original with no prefix or suffix
	 * 
	 * @param relativePath
	 *            the relative path to the results folder from the original
	 *            source file location, can not be null or empty
	 * @return a new FileOutputMapper
	 */
	public static final FileOutputMapper verSubFold(final String relativePath) {
		return verSubFold(relativePath, AbstractFileOutputMapper.defaultPrefix);
	}

	/**
	 * Maps to a relative subfolder of the original with no prefix or suffix
	 * 
	 * @param relativePath
	 *            the relative path to the results folder from the orignal
	 *            source file location
	 * @param prefix
	 *            the prefix for generated files, can not be null or empty
	 * @return a new FileOutputMapper
	 */
	public static final FileOutputMapper subFold(final String relativePath, final String prefix) {
		return subFold(relativePath, prefix, "");
	}

	/**
	 * Maps to a relative subfolder of the original with no prefix or suffix
	 * 
	 * @param relativePath
	 *            the relative path to the results folder from the orignal
	 *            source file location
	 * @param prefix
	 *            the prefix for generated files, can not be null or empty
	 * @return a new FileOutputMapper
	 */
	public static final FileOutputMapper verSubFold(final String relativePath, final String prefix) {
		return verSubFold(relativePath, prefix, "");
	}

	/**
	 * Maps to a relative subfolder of the original with no prefix or suffix
	 * 
	 * @param relativePath
	 *            the relative path to the results folder from the orignal
	 *            source file location
	 * @param prefix
	 *            the prefix for generated files, can not be null or empty
	 * @return a new FileOutputMapper
	 */
	public static final FileOutputMapper subFold(final String relativePath, final String prefix, final String suffix) {
		if (relativePath == null)
			throw new NullPointerException("Arg relativePath can not be null");
		if (relativePath.isEmpty())
			throw new IllegalArgumentException("Arg relativePath can not be an empty string");
		if (prefix == null)
			throw new NullPointerException("Arg prefix can not be null");
		if (suffix == null)
			throw new NullPointerException("Arg suffix can not be null");
		return RelativeDirectoryMapper.fromValues(relativePath, prefix, suffix);
	}

	/**
	 * Maps to a relative subfolder of the original with no prefix or suffix
	 * 
	 * @param relativePath
	 *            the relative path to the results folder from the orignal
	 *            source file location
	 * @param prefix
	 *            the prefix for generated files, can not be null or empty
	 * @return a new FileOutputMapper
	 */
	public static final FileOutputMapper verSubFold(final String relativePath, final String prefix,
			final String suffix) {
		return VersioningMapper.newInstance(subFold(relativePath, prefix, suffix));
	}

	public static final FileOutputMapper toRelativeDest(final Path src, final Path dest) {
		return toRelativeDest(src, dest, AbstractFileOutputMapper.defaultPrefix);
	}

	public static final FileOutputMapper toRelativeDest(final Path src, final Path dest, final String prefix) {
		return toRelativeDest(src, dest, prefix, AbstractFileOutputMapper.defaultSuffix);
	}

	public static final FileOutputMapper toRelativeDest(final Path src, final Path dest, final String prefix,
			final String suffix) {
		if (src == null)
			throw new NullPointerException("Arg src can not be null");
		if (dest == null)
			throw new NullPointerException("Arg dest can not be null");
		if (prefix == null)
			throw new NullPointerException("Arg prefix can not be null");
		if (suffix == null)
			throw new NullPointerException("Arg suffix can not be null");
		return RelativeRootMapper.fromValues(src, dest, prefix, suffix);
	}

	public static final FileOutputMapper fold(final String path) {
		return fold(path, AbstractFileOutputMapper.defaultPrefix);
	}

	public static final FileOutputMapper verFold(final String path) {
		return verFold(path, AbstractFileOutputMapper.defaultPrefix);
	}

	public static final FileOutputMapper fold(final String path, final String prefix) {
		return fold(path, prefix, AbstractFileOutputMapper.defaultSuffix);
	}

	public static final FileOutputMapper verFold(final String path, final String prefix) {
		return verFold(path, prefix, AbstractFileOutputMapper.defaultSuffix);
	}

	/**
	 * Maps to a relative subfolder of the original with no prefix or suffix
	 * 
	 * @param relativePath
	 *            the relative path to the results folder from the orignal
	 *            source file location
	 * @param prefix
	 *            the prefix for generated files, can not be null or empty
	 * @return a new FileOutputMapper
	 */
	public static final FileOutputMapper fold(final String path, final String prefix, final String suffix) {
		if (path == null)
			throw new NullPointerException("Arg path can not be null");
		if (path.isEmpty())
			throw new IllegalArgumentException("Arg path can not be an empty string");
		if (prefix == null)
			throw new NullPointerException("Arg prefix can not be null");
		if (suffix == null)
			throw new NullPointerException("Arg suffix can not be null");
		File outputDir = new File(path);
		if (!outputDir.exists()) {
			if (!outputDir.mkdirs())
				throw new IllegalArgumentException("Can't create writeable output directory:" + path);
		}
		if (!outputDir.isDirectory() || !outputDir.canWrite()) {
			if (!outputDir.mkdirs())
				throw new IllegalArgumentException(path + " is not a writeable output directory.");
		}
		return SingleOutputDirMapper.fromValues(outputDir, prefix, suffix);
	}

	/**
	 * Maps to a relative subfolder of the original with no prefix or suffix
	 * 
	 * @param relativePath
	 *            the relative path to the results folder from the orignal
	 *            source file location
	 * @param prefix
	 *            the prefix for generated files, can not be null or empty
	 * @return a new FileOutputMapper
	 */
	public static final FileOutputMapper verFold(final String path, final String prefix, final String suffix) {
		if (path == null)
			throw new NullPointerException("Arg path can not be null");
		if (path.isEmpty())
			throw new IllegalArgumentException("Arg path can not be an empty string");
		if (prefix == null)
			throw new NullPointerException("Arg prefix can not be null");
		if (suffix == null)
			throw new NullPointerException("Arg suffix can not be null");
		File outputDir = new File(path);
		if (!outputDir.exists()) {
			if (!outputDir.mkdirs())
				throw new IllegalArgumentException("Can't create writeable output directory:" + path);
		}
		if (!outputDir.isDirectory() || !outputDir.canWrite()) {
			if (!outputDir.mkdirs())
				throw new IllegalArgumentException(path + " is not a writeable output directory.");
		}

		return VersioningMapper.newInstance(SingleOutputDirMapper.fromValues(outputDir, prefix, suffix));
	}
}
