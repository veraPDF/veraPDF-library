package org.verapdf.metadata.fixer.utils;

import java.io.File;

/**
 * @author Evgeniy Muravitskiy
 */
public class FileGenerator {

	public static final String DEFAULT_PREFIX = "veraPDF_";

	public static File createOutputFile(String sourcePath) {
		return createOutputFile(new File(sourcePath));
	}

	public static File createOutputFile(File source) {
		return createOutputFile(source, DEFAULT_PREFIX);
	}

	public static File createOutputFile(String sourcePath, String prefix) {
		return createOutputFile(new File(sourcePath), prefix);
	}

	public static File createOutputFile(File source, String prefix) {
		if (source == null) {
			throw new IllegalArgumentException("Incorrect source file");
		}

		return createOutputFile(source.getAbsoluteFile().getParent(), source.getName(), prefix);
	}

	public static File createOutputFile(String folderPath, String fileName, String prefix) {
		if (folderPath == null || folderPath.trim().isEmpty()) {
			throw new IllegalArgumentException("Incorrect path to folder.");
		}

		if (fileName == null || fileName.trim().isEmpty()) {
			throw new IllegalArgumentException("Incorrect file name");
		}

		if (prefix == null || prefix.trim().isEmpty()) {
			throw new IllegalArgumentException("Incorrect prefix is used");
		}

		StringBuilder resultPath = new StringBuilder();
		resultPath.append(folderPath)
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
		while (true) {
			String resPath = path + (index != 0 ? "(" + index + ")" : "") + extension;
			File resultFile = new File(resPath);
			if (!resultFile.exists()) {
				return resultFile;
			} else {
				index++;
			}
		}
	}

}
