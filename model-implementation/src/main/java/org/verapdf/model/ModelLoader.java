package org.verapdf.model;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.coslayer.CosDocument;
import org.verapdf.model.impl.pb.cos.PBCosDocument;

import javax.xml.ws.WebServiceException;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;

/**
 * Current class is entry point to model implementation.
 *
 * @author Evgeniy Muravitskiy
 */
public final class ModelLoader implements Closeable {

    private static final Logger LOGGER = Logger.getLogger(ModelLoader.class);

    private File file;
    private PDDocument document;

    public ModelLoader(String path) {
        this.file = new File(path);
    }

    /**
     * Get {@code PDDocument} object for current file.
     *
     * @return {@link org.apache.pdfbox.pdmodel.PDDocument} object of pdfbox
     *         library.
     * @throws IOException
     *             when target file is not pdf or pdf file is not contain root
     *             object
     */
    public PDDocument getPDDocument() throws IOException {
        if (document == null) {
            if (!file.exists() || !file.isFile()) {
                LOGGER.error("Invalid path to document '" + file.getPath()
                        + "'. File does not exist or not a file.");
            } else {
                document = PDDocument.load(file, false, true);
            }
        }

        return document;
    }

    /**
     * Method return root object of model implementation from pdf box model
     * together with the hierarchy.
     *
     * @return root object representing by
     *         {@link org.verapdf.model.coslayer.CosDocument}
     * @throws IOException
     *             when target file is not pdf or pdf file is not contain root
     *             object
     */
    public CosDocument getRoot() throws IOException {
        if (document == null) {
            document = getPDDocument();
        }
        return document != null ? new PBCosDocument(document, file.length())
                : null;
    }

	@Override
	public void close() throws WebServiceException {
		try {
			if (document != null) {
				document.close();
			}
		} catch (IOException e) {
			LOGGER.error("Problems with close document '" + file.getAbsolutePath() + "'.", e);
		}
	}
}
