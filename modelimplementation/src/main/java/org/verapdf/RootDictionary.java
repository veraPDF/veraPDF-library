package org.verapdf;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.factory.cos.PBFactory;
import org.verapdf.model.coslayer.CosDict;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Evgeniy on 5/13/2015.
 */
public class RootDictionary {

    public static CosDict getCatalog(String path) throws FileNotFoundException {
        final File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException("Current file '" + path + "' not exists.");
        }

        CosDict catalog = null;
        try (PDDocument document = PDDocument.load(file)) {
            COSDictionary pdfBoxCatalog = (COSDictionary) document.getDocument().getCatalog().getObject();
            catalog = (CosDict) PBFactory.generateCosObject(pdfBoxCatalog);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return catalog;
    }
}
