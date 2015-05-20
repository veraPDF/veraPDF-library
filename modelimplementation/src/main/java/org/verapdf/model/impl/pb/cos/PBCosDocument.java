package org.verapdf.model.impl.pb.cos;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDocument;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosDocument;
import org.verapdf.model.coslayer.CosIndirect;
import org.verapdf.model.coslayer.CosTrailer;

import java.util.ArrayList;
import java.util.List;

/**
 * Low-level PDF Document object
 *
 * @author Evgeniy Muravitskiy
 */
public class PBCosDocument extends PBCosObject implements CosDocument {

    private final static Logger logger = Logger.getLogger(PBCosDocument.class);

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

    public void setSize(Integer sizeOfDocument) {
        this.sizeOfDocument = sizeOfDocument;
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
        trailer.add(new PBCosTrailer(((COSDocument) baseObject).getTrailer()));
        return trailer;
    }

    /**  all indirect objects referred from the xref table
     */
    private List<CosIndirect> getIndirectObjects() {
        List<CosIndirect> indirects = new ArrayList<>();
        for (COSBase object : ((COSDocument) baseObject).getObjects()) {
            indirects.add(new PBCosIndirect(object, true));
        }
        return indirects;
    }

    /**  link to the high-level PDF Document structure
     */
    // TODO : add support of features below
    private List<Object> getDocument() {
        logger.warn("Trying get PDDocument from CosDocument.\r\n" +
                "Current feature not supported yet. Method always return null.");
        return null;
    }

    /**  true if the second line of the document is a comment with at least 4 symbols in the code range 128-255 as required by PDF/A standard
     */
    @Override
    public Boolean getbinaryHeaderComplyPDFA() {
        logger.warn("Feature of CosDocument about binary header comply PDFA not supported yet.");
        return null;
    }
}
