package org.verapdf.model.impl.pb.external;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.verapdf.model.external.CMapFile;

/**
 * Current class is representation of CMapFile of pdf document
 * implemented by Apache PDFBox library
 *
 * @author Timur Kamalov
 */
public class PBoxCMapFile extends PBoxExternal implements CMapFile {

	/** Type name for {@code PBoxCMapFile} */
	public static final String CMAP_FILE_TYPE = "CMapFile";

	private final COSStream fileStream;
	private final int parentWMode;

	/**
	 * Default constructor.
	 *
	 * @param fileStream stream of CMapFile
	 * @param parentWMode {@code WMode} value of parent dictionary
	 */
    public PBoxCMapFile(final COSStream fileStream, final int parentWMode) {
        super(CMAP_FILE_TYPE);
        this.fileStream = fileStream;
		this.parentWMode = parentWMode;
    }

	/**
	 * @return value of {@code WMode} key
	 */
	public Long getWMode() {
		return Long.valueOf(this.fileStream.getInt(COSName.getPDFName("WMode"), 0));
	}

	/**
	 * @return value of {@code WMode} key of parent dictionary
	 */
	public Long getdictWMode() {
		return Long.valueOf(this.parentWMode);
	}

}
