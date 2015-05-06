package org.verapdf.impl.pb;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDocument;
import org.verapdf.factory.cos.PBFactory;
import org.verapdf.model.baselayer.*;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosDocument;
import org.verapdf.model.coslayer.CosIndirect;
import org.verapdf.model.coslayer.CosTrailer;
import org.verapdf.model.pdlayer.PDDocument;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evgeniy Muravitskiy on 5/4/15.
 * Low-level PDF Document object
 */
public class PBCosDocument extends PBCosObject implements CosDocument {

    private final static String TRAILER = "trailer";
    private final static String INDIRECT_OBJECTS = "indirectObjects";
    private final static String DOCUMENT = "document";

    private Integer sizeOfDocument;

    public PBCosDocument(COSDocument baseObject) {
        super(baseObject);
    }

    /**  Number of indirect objects in the document
     */
    @Override
    public Integer getnrIndirects() {
        return ((COSDocument)baseObject).getObjects().size();
    }

    /**  Size of the byte sequence representing the document
     */
    @Override
    public Integer getsize() {
        return sizeOfDocument;
    }

    @Override
    public List<org.verapdf.model.baselayer.Object> getLinkedObjects(String s) {
        List<org.verapdf.model.baselayer.Object> list;
        switch (s) {
            case TRAILER:
                list = gettrailer();
                break;
            case INDIRECT_OBJECTS:
                list = getindirectObjects();
                break;
            case DOCUMENT:
                list = getdocument();
                break;
            default:
                throw new IllegalArgumentException("Unknown link " + s + " for " + get_type());
        }

        return list;
    }

    /**  trailer dictionary
     */
    protected List<Object> gettrailer() {
        List<Object> trailer = new ArrayList<>();
        trailer.add(PBFactory.generateCosObject(CosTrailer.class, ((COSDocument) baseObject).getTrailer()));
        return trailer;
    }

    /**  all indirect objects referred from the xref table
     */
    protected List<Object> getindirectObjects() {
        List<Object> indirects = new ArrayList<>();
        for (COSBase object : ((COSDocument)baseObject).getObjects())
            indirects.add(PBFactory.generateCosObject(CosIndirect.class, object));
        return indirects;
    }

    /**  true if the second line of the document is a comment with at least 4 symbols in the code range 128-255 as required by PDF/A standard
     */
    @Override
    public Boolean getbinaryHeaderComplyPDFA() {
        ((COSDocument)baseObject).getVersion();
        return null;
    }

    /**  link to the high-level PDF Document structure
     */
    protected List<Object> getdocument() {
        System.err.println("Trying get PDDocument from CosDocument.\r\n" +
                           "Current feature not supported yet. Method always return null.");
        return null;
    }

    public void setSizeOfDocument(Integer sizeOfDocument) {
        this.sizeOfDocument = sizeOfDocument;
    }
}
