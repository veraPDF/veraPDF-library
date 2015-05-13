package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDocument;
import org.verapdf.model.factory.cos.PBFactory;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosDocument;
import org.verapdf.model.coslayer.CosIndirect;
import org.verapdf.model.coslayer.CosTrailer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evgeniy Muravitskiy on 5/4/15.
 * <p>
 *     Low-level PDF Document object
 * </p>
 */
public class PBCosDocument extends PBCosObject implements CosDocument {

    public final static String TRAILER = "trailer";
    public final static String INDIRECT_OBJECTS = "indirectObjects";
    public final static String DOCUMENT = "document";

    private Integer sizeOfDocument = -1;

    public PBCosDocument(COSDocument baseObject) {
        super(baseObject);
        setType("CosDocument");
    }

    /**  Number of indirect objects in the document
     */
    @Override
    public Integer getnrIndirects() {
        return ((COSDocument) baseObject).getObjects().size();
    }

    /**  Size of the byte sequence representing the document
     */
    @Override
    public Integer getsize() {
        return sizeOfDocument;
    }

    @Override
    public List<? extends org.verapdf.model.baselayer.Object> getLinkedObjects(String link) {
        List<? extends org.verapdf.model.baselayer.Object> list;

        switch (link) {
            case TRAILER:
                list = this.getTrailer();
                break;
            case INDIRECT_OBJECTS:
                list = this.getIndirectObjects();
                break;
            case DOCUMENT:
                list = this.getDocument();
                break;
            default:
                list = super.getLinkedObjects(link);
        }

        return list;
    }

    /**  trailer dictionary
     */
    private List<CosTrailer> getTrailer() {
        List<CosTrailer> trailer = new ArrayList<>();
        trailer.add((CosTrailer) PBFactory.generateCosObject(CosTrailer.class, ((COSDocument) baseObject).getTrailer()));
        return trailer;
    }

    /**  all indirect objects referred from the xref table
     */
    private List<CosIndirect> getIndirectObjects() {
        List<CosIndirect> indirects = new ArrayList<>();
        for (COSBase object : ((COSDocument) baseObject).getObjects()) {
            indirects.add((CosIndirect) PBFactory.generateCosObject(CosIndirect.class, object));
        }
        return indirects;
    }

    /**  link to the high-level PDF Document structure
     */
    // TODO : add support of this feature
    private List<Object> getDocument() {
        System.err.println("Trying get PDDocument from CosDocument.\r\n" +
                "Current feature not supported yet. Method always return null.");
        return null;
    }

    /**  true if the second line of the document is a comment with at least 4 symbols in the code range 128-255 as required by PDF/A standard
     */
    @Override
    public Boolean getbinaryHeaderComplyPDFA() {
        ((COSDocument) baseObject).getVersion();
        return null;
    }
}
