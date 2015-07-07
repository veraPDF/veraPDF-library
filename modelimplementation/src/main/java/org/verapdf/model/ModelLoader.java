package org.verapdf.model;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosDocument;
import org.verapdf.model.impl.pb.cos.PBCosDocument;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Current class is entry point to model implementation.
 *
 * @author Evgeniy Muravitskiy
 */
public final class ModelLoader {

	private static final Logger logger = Logger.getLogger(ModelLoader.class);

	private File file;
	private PDDocument document;

	public ModelLoader(String path) {
		this.file = new File(path);
	}

	/**
	 * Get {@code PDDocument} object for current file.
	 *
	 * @return {@link org.apache.pdfbox.pdmodel.PDDocument} object of pdfbox library.
	 * @throws IOException when target file is not pdf or pdf file is not contain root object
	 */
	public PDDocument getPDDocument() throws IOException {
		if (document == null) {
			if (!file.exists() || !file.isFile()) {
				logger.error("Invalid path to document '" + file.getPath() + "'. File does not exist or not a file.");
			} else {
				document = PDDocument.load(file, Boolean.FALSE, Boolean.TRUE);
			}
		}

		return document;
	}

	/**
	 * Method return root object of model implementation from pdf box model together with the hierarchy.
	 *
	 * @return root object representing by {@link org.verapdf.model.coslayer.CosDocument}
	 * @throws IOException when target file is not pdf or pdf file is not contain root object
	 */
	public CosDocument getRoot() throws IOException {
		if (document == null) {
			document = getPDDocument();
		}
		if (document == null) {
			return null;
		} else {
			return new PBCosDocument(document, file.length());
		}
	}

	/**
	 * Method return root object of model implementation from pdf box model together with the hierarchy.
	 *
	 * @param path path to PDF file
	 * @return root object representing by {@link org.verapdf.model.coslayer.CosDocument}
	 * @throws FileNotFoundException when target file is not exist
	 * @throws IOException           when target file is not pdf or pdf file is not contain root object
	 */
	public static org.verapdf.model.baselayer.Object getRoot(String path) throws IOException {
		final File file = new File(path);
		if (!file.exists()) {
			throw new FileNotFoundException("Current file '" + path + "' not exists.");
		}

		Object root;
		try (PDDocument document = PDDocument.load(file, Boolean.FALSE, Boolean.TRUE)) {
			root = new PBCosDocument(document, file.length());
		}
		return root;
	}
}
