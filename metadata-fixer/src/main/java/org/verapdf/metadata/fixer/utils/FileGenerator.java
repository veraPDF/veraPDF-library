package org.verapdf.metadata.fixer.utils;

import java.io.File;

/**
 * @author Evgeniy Muravitskiy
 */
public class FileGenerator {

	public static final String DEFAULT_PREFIX = "veraPDF_";

	public static File createNonExistFile(String sourcePath) {
		return createNonExistFile(new File(sourcePath));
	}

	public static File createNonExistFile(String sourcePath, String prefix) {
		return createNonExistFile(new File(sourcePath), prefix);
	}

	public static File createNonExistFile(File source) {
		return createNonExistFile(source, DEFAULT_PREFIX);
	}

	public static File createNonExistFile(File source, String prefix) {
		if (prefix == null || prefix.trim().isEmpty()) {
			throw new IllegalArgumentException("Incorrect prefix is used");
		}

		if (source == null) {
			throw new IllegalArgumentException("Incorrect source file");
		}

		StringBuilder resultPath = new StringBuilder();
		resultPath.append(source.getAbsoluteFile().getParent())
				.append(File.separator)
				.append(prefix);
		String extension = appendName(source, resultPath);

		return createNonExistFile(resultPath.toString(), extension, 0);
	}

	private static String appendName(File source, StringBuilder resultPath) {
		String[] split = source.getName().split("[.]");
		if (split.length > 1) {
			resultPath.append(split[0]);
			for (int i = 1; i < split.length - 1; i++) {
				resultPath.append('.').append(split[i]);
			}
			return  '.' + split[split.length - 1];
		}
		return "";
	}

	private static File createNonExistFile(String path, String extension, int index) {
		String resPath = path + (index != 0 ? "(" + index + ")" : "") + extension;
		File resultFile = new File(resPath);
		return !resultFile.exists() ? resultFile :
				createNonExistFile(path, extension, index + 1);
	}

}
