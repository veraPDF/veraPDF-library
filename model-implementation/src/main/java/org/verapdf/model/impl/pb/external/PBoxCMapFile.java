package org.verapdf.model.impl.pb.external;

import org.apache.pdfbox.cos.COSStream;
import org.verapdf.model.external.CMapFile;

/**
 * @author Timur Kamalov
 */
public class PBoxCMapFile extends PBoxExternal implements CMapFile {

    private COSStream fileStream;

    public static final String CMAP_FILE_TYPE = "CMapFile";

    public PBoxCMapFile(COSStream fileStream) {
        super(CMAP_FILE_TYPE);
        this.fileStream = fileStream;
    }

}
