package org.verapdf.model;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.coslayer.CosDocument;
import org.verapdf.model.impl.pb.cos.PBCosDocument;

/**
 * Current class is entry point to model implementation.
 *
 * @author Evgeniy Muravitskiy
 */
public final class ModelLoader implements Closeable {

    private static final Logger LOGGER = Logger.getLogger(ModelLoader.class);

    private PDDocument document;
	private final long size;

    /**
     * @param toLoad
     * @throws IOException
     */
    public ModelLoader(InputStream toLoad) throws IOException {
		this.size = toLoad.available();
		this.document = PDDocument.load(toLoad, false, true);
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
        return this.document;
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
        return new PBCosDocument(this.document, this.size);
    }

	@Override
	public void close() {
		try {
            if (this.document != null) {
                this.document.close();
            }
		} catch (IOException e) {
            LOGGER.error("Problems with document close.", e);
        }
	}
}
