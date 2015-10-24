package org.verapdf.model.impl.pb.cos;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDEmbeddedFilesNameTreeNode;
import org.apache.pdfbox.pdmodel.common.filespecification.PDComplexFileSpecification;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.*;
import org.verapdf.model.impl.pb.pd.PBoxPDDocument;
import org.verapdf.model.tools.XMPChecker;

import java.io.IOException;
import java.util.*;

/**
 * Low-level PDF Document object
 *
 * @author Evgeniy Muravitskiy
 */
public class PBCosDocument extends PBCosObject implements CosDocument {

    private static final Logger LOGGER = Logger.getLogger(PBCosDocument.class);

    /** Type name for PBCosDocument */
    public static final String COS_DOCUMENT_TYPE = "CosDocument";

    public static final String TRAILER = "trailer";
    public static final String XREF = "xref";
    public static final String INDIRECT_OBJECTS = "indirectObjects";
    public static final String DOCUMENT = "document";
    public static final String EMBEDDED_FILES = "EmbeddedFiles";
    public static final String ID = "ID";
	public static final String REQUIREMENTS = "Requirements";

    private PDDocument pdDocument;

    private final long sizeOfDocument;
    private final long indirectObjectCount;
    private final float version;
    private final boolean isBinaryHeaderPDFACompliant;
    private final boolean isPDFHeaderPDFACompliant;
    private final boolean isOptionalContentPresent;
    private final boolean isLinearised;
    private final Boolean isEofPDFACompliant;
    private final Boolean doesInfoMatchXMP;
    private final String firstPageID;
    private final String lastID;

    /**
     * Default constructor
     * @param pdDocument pdfbox PDDocument
     * @param length original length of the document
     */
	public PBCosDocument(PDDocument pdDocument, long length) {
        this(pdDocument.getDocument(), length);
        this.pdDocument = pdDocument;
    }

    /**
     * Constructor using pdfbox COSDocument without length
     * @param cosDocument pdfbox COSDocument
     */
    public PBCosDocument(COSDocument cosDocument) {
        this(cosDocument, -1);
    }

    /**
     * Constructor using pdfbox COSDocument
     * @param cosDocument pdfbox COSDocument
     * @param length original length of the document
     */
    public PBCosDocument(COSDocument cosDocument, long length) {
        super(cosDocument, COS_DOCUMENT_TYPE);
        this.sizeOfDocument = length;
        this.indirectObjectCount = cosDocument.getObjects().size();
        this.version = cosDocument.getVersion();
        this.isBinaryHeaderPDFACompliant = !(cosDocument
                .getNonValidCommentContent().booleanValue()
                || cosDocument.getNonValidCommentLength().booleanValue() || cosDocument
                .getNonValidCommentStart().booleanValue());
        this.isPDFHeaderPDFACompliant = !cosDocument.getNonValidHeader()
                .booleanValue();
        this.isOptionalContentPresent = parseOptionalContentPresent(cosDocument);
        this.isEofPDFACompliant = cosDocument.getEofComplyPDFA();
        this.lastID = getTrailerID((COSArray) cosDocument.getLastTrailer()
                .getDictionaryObject(ID));
        this.firstPageID = getTrailerID((COSArray) cosDocument
                .getFirstPageTrailer().getDictionaryObject(ID));
        this.isLinearised = cosDocument.getTrailer() != cosDocument
                .getLastTrailer() && cosDocument.isLinearized().booleanValue();
        this.doesInfoMatchXMP = XMPChecker.doesInfoMatchXMP(cosDocument);
    }

	private boolean parseOptionalContentPresent(
			final COSDocument cosDocument) {
		try {
			COSDictionary root = (COSDictionary) (cosDocument).getCatalog()
					.getObject();
			return root.getItem(COSName.OCPROPERTIES) != null;
		} catch (IOException e) {
			LOGGER.debug("No document catalog found", e);
			return false;
		}
	}

    /**
     * Number of indirect objects in the document
     */
    @Override
    public Long getnrIndirects() {
        return Long.valueOf(this.indirectObjectCount);
    }

    /**
     * @return version of pdf document
     */
    @Override
    public Double getversion() {
        return Double.valueOf(this.version);
    }

    /**
     * Size of the byte sequence representing the document
     */
    @Override
    public Long getsize() {
        return Long.valueOf(sizeOfDocument);
    }

    /**
     * true if the second line of the document is a comment with at least 4
     * symbols in the code range 128-255 as required by PDF/A standard
     */
    @Override
    public Boolean getbinaryHeaderComplyPDFA() {

        return Boolean.valueOf(this.isBinaryHeaderPDFACompliant);
    }

    /**
     * true if first line of document complies PDF/A standard
     */
    @Override
    public Boolean getpdfHeaderCompliesPDFA() {
        return Boolean.valueOf(this.isPDFHeaderPDFACompliant);
    }

    /**
     * true if catalog contain OCProperties key
     */
    @Override
    public Boolean getisOptionalContentPresent() {
        return Boolean.valueOf(this.isOptionalContentPresent);
    }

    /**
     * EOF must complies PDF/A standard
     */
    @Override
    public Boolean geteofCompliesPDFA() {
        return this.isEofPDFACompliant;
    }

    /**
     * @return ID of first page trailer
     */
    @Override
    public String getfirstPageID() {
        return this.firstPageID;
    }

    /**
     * @return ID of last document trailer
     */
    @Override
    public String getlastID() {
        return this.lastID;
    }

    private static String getTrailerID(COSArray ids) {
        if (ids != null) {
            StringBuilder builder = new StringBuilder();
            for (COSBase id : ids) {
                for (byte aByte : ((COSString) id).getBytes()) {
                    builder.append((char) (aByte & 0xFF));
                }
                builder.append(' ');
            }
            // need to discard last whitespace
            return builder.toString().substring(0, builder.length() - 1);
        }
        return null;
    }

    /**
     * @return true if the current document is linearized
     */
    @Override
    public Boolean getisLinearized() {
        return Boolean.valueOf(this.isLinearised);
    }

    /**
     * @return true if XMP content matches Info dictionary content
     */
    @Override
    public Boolean getdoesInfoMatchXMP() {
        return this.doesInfoMatchXMP;
    }

	public Boolean getMarked() {
		try {
			COSDictionary object = (COSDictionary)
					((COSDocument) this.baseObject).getCatalog().getObject();
			COSBase markInfo = object.getDictionaryObject(COSName.MARK_INFO);
			if (markInfo == null) {
				return Boolean.FALSE;
			} else if (markInfo instanceof COSDictionary) {
				COSName marked = COSName.getPDFName("Marked");
				boolean value = ((COSDictionary) markInfo).getBoolean(marked, false);
				return Boolean.valueOf(value);
			} else {
				LOGGER.warn("MarkedInfo must be a 'COSDictionary' but got: "
						+ markInfo.getClass().getSimpleName());
				return Boolean.FALSE;
			}
		} catch (IOException e) {
			LOGGER.error("Problems with catalog obtain", e);
			return Boolean.FALSE;
		}
	}

    @Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
			case TRAILER:
				return this.getTrailer();
			case INDIRECT_OBJECTS:
				return this.getIndirectObjects();
			case DOCUMENT:
				return this.getDocument();
			case XREF:
				return this.getXRefs();
			case EMBEDDED_FILES:
				return this.getEmbeddedFiles();
			case REQUIREMENTS:
				return this.getRequirements();
			default:
				return super.getLinkedObjects(link);
		}
	}

    /**
     * @return list of embedded files
     */
    private List<Object> getEmbeddedFiles() {
        try {
            COSDictionary buffer = (COSDictionary)
					((COSDocument) this.baseObject).getCatalog().getObject();
            buffer = (COSDictionary) buffer.getDictionaryObject(
					COSName.NAMES);
            if (buffer != null) {
                COSBase base = buffer.getDictionaryObject(COSName.EMBEDDED_FILES);
				if (base instanceof COSDictionary) {
					List<Object> files = new ArrayList<>();
					this.getNamesEmbeddedFiles(files, (COSDictionary) base);
					return Collections.unmodifiableList(files);
				}
			}
        } catch (IOException e) {
            LOGGER.error(
                    "Something wrong with getting embedded files - return empty list. "
                            + e.getMessage(), e);
        }
        return Collections.emptyList();
    }

	private void getNamesEmbeddedFiles(List<Object> files,
									   COSDictionary buffer) throws IOException {
		PDEmbeddedFilesNameTreeNode root = new PDEmbeddedFilesNameTreeNode(buffer);
        final Map<String, PDComplexFileSpecification> names = root.getNames();
        if (names != null) {
            final Set<Map.Entry<String, PDComplexFileSpecification>> entries = names.entrySet();
            for (Map.Entry<String, PDComplexFileSpecification> entry : entries) {
                files.add(new PBCosFileSpecification(entry.getValue()
                        .getCOSObject()));
            }
        }
	}

    /**
     * trailer dictionary
     */
    private List<CosTrailer> getTrailer() {
		COSDocument cosDocument = (COSDocument) this.baseObject;
        List<CosTrailer> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
        list.add(new PBCosTrailer(cosDocument.getTrailer()));
        return Collections.unmodifiableList(list);
    }

    /**
     * all indirect objects referred from the xref table
     */
    private List<CosIndirect> getIndirectObjects() {
		List<COSObject> objects = ((COSDocument) this.baseObject)
				.getObjects();
		List<CosIndirect> list = new ArrayList<>(objects.size());
		for (COSObject object : objects) {
            list.add(new PBCosIndirect(object));
        }
        return Collections.unmodifiableList(list);
    }

    /**
     * link to the high-level PDF Document structure
     */
    private List<org.verapdf.model.pdlayer.PDDocument> getDocument() {
		if (pdDocument != null) {
			List<org.verapdf.model.pdlayer.PDDocument> document =
					new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			document.add(new PBoxPDDocument(pdDocument));
			return Collections.unmodifiableList(document);
		}
        return Collections.emptyList();
    }

    /**
     * link to cross reference table properties
     */
    private List<CosXRef> getXRefs() {
		COSDocument cosDocument = (COSDocument) this.baseObject;
        List<CosXRef> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
        list.add(new PBCosXRef(cosDocument.isXRefSpacingsCompliesPDFA(),
                cosDocument.isXRefEOLCompliesPDFA()));
        return Collections.unmodifiableList(list);
    }

	private List<CosDict> getRequirements() {
		try {
			COSDictionary object = (COSDictionary)
					((COSDocument) this.baseObject).getCatalog().getObject();
			COSName requirementsName = COSName.getPDFName("Requirements");
			COSBase reqArray = object.getDictionaryObject(requirementsName);
			if (reqArray instanceof COSArray) {
				return this.getRequirementsList(reqArray);
			}
		} catch (IOException e) {
			LOGGER.warn("Problems with catalog obtain.", e);
		}
		return Collections.emptyList();
	}

	private List<CosDict> getRequirementsList(COSBase reqArray) {
		ArrayList<CosDict> list = new ArrayList<>(((COSArray) reqArray).size());
		for (COSBase element : (COSArray) reqArray) {
			if (element instanceof COSDictionary) {
				list.add(new PBCosDict((COSDictionary) element));
			}
		}
		return Collections.unmodifiableList(list);
	}
}
