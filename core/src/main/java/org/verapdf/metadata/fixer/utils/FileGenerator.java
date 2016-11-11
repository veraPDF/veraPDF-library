package org.verapdf.metadata.fixer.utils;

import java.io.File;

/**
 * Class for generate non existing file.
 * Returned file must point to file for which
 * {@link File#exists()} return false.
 *
 * @author Evgeniy Muravitskiy
 */
public class FileGenerator {

	private FileGenerator() {
		// hide default constructor
	}

	/**
	 * Create new unique file which store near source file.
	 * Current method not return existing file -
	 * {@link File#exists()} return {@code false}
	 *
	 * @param sourcePath path to source file
	 * @return non-existing file which stored near source file
	 * @throws NullPointerException if {@code sourcePath} is {@code null}
	 */
	public static File createOutputFile(String sourcePath) {
		return createOutputFile(new File(sourcePath));
	}

	/**
	 * Create new unique file which store near source file.
	 * Current method not return existing file -
	 * {@link File#exists()} return {@code false}
	 *
	 * @param source source file
	 * @return non-existing file which stored near source file
	 * @throws IllegalArgumentException if {@code source} is {@code null}
	 */
	public static File createOutputFile(File source) {
		return createOutputFile(source, MetadataFixerConstants.DEFAULT_PREFIX);
	}

	/**
	 * Create new unique file which store near source file.
	 * Current method not return existing file -
	 * {@link File#exists()} return {@code false}.
	 * {@code prefix} must be not {@code null} and not empty.
	 *
	 * @param source source file
	 * @param prefix prefix for result file name
	 * @return non-existing file which stored near source file
	 * @throws IllegalArgumentException if {@code source} or
	 *                                  {@code prefix} are {@code null} or empty
	 */
	public static File createOutputFile(File source, String prefix) {
		if (source == null) {
			throw new IllegalArgumentException("Incorrect source file");
		}

		return createOutputFile(source.getAbsoluteFile().getParentFile(), source.getName(), prefix);
	}

	/**
	 * Create new unique file which on {@code folderFile} with
	 * {@code fileName} name and has redefined prefix for name.
	 * Current method not return existing file -
	 * {@link File#exists()} return {@code false}.
	 * {@code folderFile}, {@code fileName} and {@code prefix}
	 * must be not {@code null} and not empty.
	 *
	 * @param folderFile folder for store result file
	 * @param fileName   name of the result file
	 * @param prefix     prefix for the result file name
	 * @return non-existing file which stored near source file
	 * @throws IllegalArgumentException if {@code folderFile} or
	 * {@code fileName}, or {@code prefix} are {@code null}
	 */
	public static File createOutputFile(File folderFile, String fileName, String prefix) {
		if (folderFile == null) {
			throw new IllegalArgumentException("Incorrect path to folder.");
		}

		if (fileName == null || fileName.trim().isEmpty()) {
			throw new IllegalArgumentException("Incorrect file name");
		}

		if (prefix == null) {
			throw new IllegalArgumentException("Incorrect prefix is used");
		}

		StringBuilder resultPath = new StringBuilder();
		resultPath.append(folderFile.getAbsoluteFile().getAbsolutePath())
				.append(File.separator)
				.append(prefix);
		String extension = getExtension(fileName, resultPath);

		return createOutputFile(resultPath.toString(), extension, 0);
	}

	private static String getExtension(String name, StringBuilder resultPath) {
		String[] split = name.split("[.]");
		if (split.length > 1) {
			resultPath.append(split[0]);
			for (int i = 1; i < split.length - 1; i++) {
				resultPath.append('.').append(split[i]);
			}
			return '.' + split[split.length - 1];
		}
		return "";
	}

	private static File createOutputFile(String path, String extension, int index) {
		// TODO: create test for this method
		while (true) {
			String resPath = path + (index != 0 ? "(" + index + ")" : "") + extension;
			File resultFile = new File(resPath);
			if (!resultFile.exists()) {
				return resultFile;
			}
			++index;
		}
	}

}
