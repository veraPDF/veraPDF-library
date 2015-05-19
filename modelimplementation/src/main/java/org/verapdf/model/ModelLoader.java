package org.verapdf.model;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.coslayer.CosDict;
import org.verapdf.model.impl.pb.cos.PBCosDict;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Current class is entry point to model implementation. Implements Singleton pattern.
 *
 * @author Evgeniy Muravitskiy
 */
public final class ModelLoader {

    private ModelLoader(){}

    /**
     * Method return root object of model implementation from pdf box model together with the hierarchy.
     * @param path path to PDF file
     * @return root object representing by {@link org.verapdf.model.coslayer.CosDict}
     * @throws FileNotFoundException when target file is not exist
     * @throws IOException when target file is not pdf or pdf file is not contain root object
     */
    public static CosDict getCatalog(String path) throws IOException {
        final File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException("Current file '" + path + "' not exists.");
        }

        CosDict catalog;
        try (PDDocument document = PDDocument.load(file)) {
            COSDictionary pdfBoxCatalog = (COSDictionary) document.getDocument().getCatalog().getObject();
            catalog = new PBCosDict(pdfBoxCatalog);
        }
        return catalog;
    }
}
