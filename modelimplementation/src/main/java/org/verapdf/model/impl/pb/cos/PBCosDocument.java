package org.verapdf.model.impl.pb.cos;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDEmbeddedFilesNameTreeNode;
import org.apache.pdfbox.pdmodel.common.filespecification.PDComplexFileSpecification;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.*;
import org.verapdf.model.tools.XMPChecker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Low-level PDF Document object
 *
 * @author Evgeniy Muravitskiy
 */
public class PBCosDocument extends PBCosObject implements CosDocument {

    private final static Logger logger = Logger.getLogger(PBCosDocument.class);

    public final static String TRAILER = "trailer";
    public final static String XREF = "xref";
    public final static String INDIRECT_OBJECTS = "indirectObjects";
    public final static String DOCUMENT = "document";
    public final static String EMBEDDED_FILES = "EmbeddedFiles";
    public final static String ID = "ID";

    private PDDocument pdDocument;

    private Long sizeOfDocument = new Long(-1);

    public PBCosDocument(COSDocument baseObject) {
        super(baseObject);
        setType("CosDocument");
    }

    public PBCosDocument(PDDocument pdDocument, long length) {
        super(pdDocument.getDocument());
        setType("CosDocument");
        this.pdDocument = pdDocument;
        sizeOfDocument = Long.valueOf(length);
    }

    /**  Number of indirect objects in the document
     */
    @Override
    public Long getnrIndirects() {
        return Long.valueOf(((COSDocument) baseObject).getObjects().size());
    }

    /**  Size of the byte sequence representing the document
     */
    @Override
    public Long getsize() {
        return sizeOfDocument;
    }

    /**  true if the second line of the document is a comment with at least 4 symbols in the code range 128-255 as required by PDF/A standard
     */
    @Override
    public Boolean getbinaryHeaderComplyPDFA() {

        return Boolean.valueOf(!(((COSDocument) baseObject).getNonValidCommentContent().booleanValue() ||
                ((COSDocument) baseObject).getNonValidCommentLength().booleanValue() ||
                ((COSDocument) baseObject).getNonValidCommentStart().booleanValue()));
    }

    /** true if first line of document complies PDF/A standard
     */
    @Override
    public Boolean getpdfHeaderCompliesPDFA() {
        return Boolean.valueOf(!((COSDocument) baseObject).getNonValidHeader().booleanValue());
    }

    /** true if catalog contain OCProperties key
     */
    @Override
    public Boolean getisOptionalContentPresent() {
        try {
            COSDictionary root = (COSDictionary) ((COSDocument) baseObject).getCatalog().getObject();
            return Boolean.valueOf(root.getItem(COSName.OCPROPERTIES) != null);
        } catch (IOException e) {
            return Boolean.FALSE;
        }
    }

    /** EOF must complies PDF/A standard
     */
    @Override
	public Boolean geteofCompliesPDFA() {
        return ((COSDocument) baseObject).getEofComplyPDFA();
    }

    /**
     * @return ID of first page trailer
     */
    public String getfirstPageID() {
        return getTrailerID((COSArray) ((COSDocument) baseObject).getFirstPageTrailer().getItem(ID));
    }

    /**
     * @return ID of last document trailer
     */
    public String getlastID() {
        return getTrailerID((COSArray) ((COSDocument) baseObject).getLastTrailer()
                .getItem(ID));
    }

    private String getTrailerID(COSArray ids) {
        if (ids != null) {
            StringBuilder builder = new StringBuilder();
            for (COSBase id : ids) {
                builder.append(((COSString) id).getASCII()).append(' ');
            }
            // need to discard last whitespace
            return builder.toString().substring(0, builder.length() - 2);
        } else {
            return null;
        }
    }

    /**
     * @return true if the current document is linearized
     */
    public Boolean getisLinearized() {
        COSDocument document = (COSDocument) this.baseObject;
        boolean res = document.getTrailer() != document.getLastTrailer() && document.isLinearized();
        return Boolean.valueOf(res);
    }

    /**
     * @return true if XMP content matches Info dictionary content
     */
    @Override
	public Boolean getdoesInfoMatchXMP() {
        return XMPChecker.doesInfoMatchXMP((COSDocument) baseObject);
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
            case XREF:
                list = this.getXRef();
                break;
            case EMBEDDED_FILES:
                list = this.getEmbeddedFiles();
                break;
            default:
                list = super.getLinkedObjects(link);
        }

        return list;
    }

    /**
     * @return list of embedded files
     */
    private List<Object> getEmbeddedFiles() {
        List<Object> files = new ArrayList<>();
        try {
            COSDictionary buffer = (COSDictionary) pdDocument.getDocument().getCatalog().getObject();
            buffer = getCosDictionary(buffer.getItem(COSName.NAMES));
            if (buffer != null) {
                buffer = getCosDictionary(buffer.getItem(COSName.EMBEDDED_FILES));
            }
            getNamesEmbeddedFiles(files, buffer);
        } catch (IOException e) {
            logger.error("Something wrong with getting embedded files - return empty list. " + e.getMessage());
        }
        return files;
    }

    private void getNamesEmbeddedFiles(List<Object> files, COSDictionary buffer) throws IOException {
        PDEmbeddedFilesNameTreeNode root = null;
        if (buffer != null) {
            root = new PDEmbeddedFilesNameTreeNode(buffer);
        }
        if (root != null) {
            final Set<Map.Entry<String, PDComplexFileSpecification>> entries = root.getNames().entrySet();
            for (Map.Entry<String, PDComplexFileSpecification> entry : entries) {
                files.add(new PBCosFileSpecification(entry.getValue().getCOSObject()));
            }
        }
    }

    private COSDictionary getCosDictionary(COSBase item) throws IOException {
        COSDictionary buffer = null;
        if (item instanceof COSDictionary) {
            buffer = (COSDictionary) item;
        } else if (item instanceof COSObject) {
            buffer = (COSDictionary) ((COSObject) item).getObject();
        }
        return buffer;
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
            indirects.add(new PBCosIndirect(object));
        }
        return indirects;
    }

    /**  link to the high-level PDF Document structure
     */
    // TODO : add support of this feature
    private List<Object> getDocument() {
        logger.warn("Trying get PDDocument from CosDocument.\r\n" +
                "Current feature not supported yet. Method always return null.");
        return new ArrayList<>();
    }

    /** link to cross reference table properties
     */
    private List<? extends Object> getXRef() {
        List<CosXRef> xref = new ArrayList<>();
        final COSDocument document = (COSDocument) this.baseObject;
        xref.add(new PBCosXRef(document.isXRefSpacingsCompliesPDFA(), document.isXRefEOLCompliesPDFA()));
        return xref;
    }
}
