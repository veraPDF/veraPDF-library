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
	private final long headerOffset;
	private final String header;
	private final int headerCommentByte1;
	private final int headerCommentByte2;
	private final int headerCommentByte3;
	private final int headerCommentByte4;
    private final boolean isOptionalContentPresent;
    private final boolean isLinearised;
    private final int postEOFDataSize;
    private final Boolean doesInfoMatchXMP;
    private final String firstPageID;
    private final String lastID;
	private final boolean needsRendering;

	private final COSDictionary catalog;

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
		this.catalog = this.getCatalog();

		this.sizeOfDocument = length;
		this.indirectObjectCount = cosDocument.getObjects().size();
		this.version = cosDocument.getVersion();
		this.headerOffset = cosDocument.getHeaderOffset();
		this.header = cosDocument.getHeader();
		this.headerCommentByte1 = cosDocument.getHeaderCommentByte1();
		this.headerCommentByte2 = cosDocument.getHeaderCommentByte2();
		this.headerCommentByte3 = cosDocument.getHeaderCommentByte3();
		this.headerCommentByte4 = cosDocument.getHeaderCommentByte4();
		this.isOptionalContentPresent = parseOptionalContentPresent();
		this.postEOFDataSize = cosDocument.getPostEOFDataSize();
		this.lastID = getTrailerID((COSArray) cosDocument.getLastTrailer()
				.getDictionaryObject(ID));
		this.firstPageID = getTrailerID((COSArray) cosDocument
				.getFirstPageTrailer().getDictionaryObject(ID));
		this.isLinearised = cosDocument.getTrailer() != cosDocument
                .getLastTrailer() && cosDocument.isLinearized();
		this.doesInfoMatchXMP = XMPChecker.doesInfoMatchXMP(cosDocument);
		this.needsRendering = this.getNeedsRenderingValue();
    }

	private boolean parseOptionalContentPresent() {
		return this.catalog != null &&
				this.catalog.getDictionaryObject(COSName.OCPROPERTIES) != null;
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

	@Override
	public Long getheaderOffset() {
		return Long.valueOf(this.headerOffset);
	}

	@Override
	public String getheader() {
		return this.header;
	}

	@Override
	public Long getheaderByte1() {
		return Long.valueOf(this.headerCommentByte1);
	}

	@Override
	public Long getheaderByte2() {
		return Long.valueOf(this.headerCommentByte2);
	}

	@Override
	public Long getheaderByte3() {
		return Long.valueOf(this.headerCommentByte3);
	}

	@Override
	public Long getheaderByte4() {
		return Long.valueOf(this.headerCommentByte4);
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
    public Long getpostEOFDataSize() {
        return Long.valueOf(this.postEOFDataSize);
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

	@Override
	public Boolean getMarked() {
		if (this.catalog != null) {
			COSBase markInfo = this.catalog.getDictionaryObject(COSName.MARK_INFO);
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
		} else {
			return Boolean.FALSE;
		}
	}

	/**
	 * @return true if {@code NeedsRendering} entry contains {@code true} value
	 */
	public Boolean getNeedsRendering() {
		return Boolean.valueOf(this.needsRendering);
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
		if (this.catalog != null) {
			COSDictionary buffer = (COSDictionary) this.catalog
					.getDictionaryObject(COSName.NAMES);
			if (buffer != null) {
				COSBase base = buffer.getDictionaryObject(COSName.EMBEDDED_FILES);
				if (base instanceof COSDictionary) {
					List<Object> files = new ArrayList<>();
					this.getNamesEmbeddedFiles(files, (COSDictionary) base);
					return Collections.unmodifiableList(files);
				}
			}
		}
        return Collections.emptyList();
    }

	private void getNamesEmbeddedFiles(List<Object> files,
									   COSDictionary buffer) {
		PDEmbeddedFilesNameTreeNode root = new PDEmbeddedFilesNameTreeNode(buffer);
		try {
			final Map<String, PDComplexFileSpecification> names = root.getNames();
			if (names != null) {
				final Set<Map.Entry<String, PDComplexFileSpecification>> entries = names.entrySet();
				for (Map.Entry<String, PDComplexFileSpecification> entry : entries) {
					files.add(new PBCosFileSpecification(entry.getValue()
							.getCOSObject()));
				}
			}
		} catch (IOException e) {
			LOGGER.error(
					"Something wrong with getting embedded files - return empty list. "
							+ e.getMessage(), e);
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
        list.add(new PBCosXRef(cosDocument.subSectionHeaderSpaceSeparated(),
                cosDocument.isXrefEOLMarkersComplyPDFA()));
        return Collections.unmodifiableList(list);
    }

	private List<CosDict> getRequirements() {
		if (this.catalog != null) {
			COSName requirementsName = COSName.getPDFName(REQUIREMENTS);
			COSBase reqArray = this.catalog.getDictionaryObject(requirementsName);
			if (reqArray instanceof COSArray) {
				return this.getRequirementsList(reqArray);
			}
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

	private boolean getNeedsRenderingValue() {
		COSName needsRendering = COSName.getPDFName("NeedsRendering");
		return this.catalog != null &&
				this.catalog.getBoolean(needsRendering, false);
	}

	private COSDictionary getCatalog() {
		COSBase catalog = ((COSDocument) this.baseObject)
				.getTrailer().getDictionaryObject(COSName.ROOT);
		return catalog instanceof COSDictionary ? (COSDictionary) catalog : null;
	}

}
