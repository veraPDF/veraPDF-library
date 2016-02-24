package org.verapdf.model.impl.pb.external;

import org.apache.fontbox.cmap.CMap;
import org.apache.fontbox.cmap.CMapParser;
import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.verapdf.model.external.CMapFile;

import java.io.IOException;

/**
 * Current class is representation of CMapFile of pdf document
 * implemented by Apache PDFBox library
 *
 * @author Timur Kamalov
 */
public class PBoxCMapFile extends PBoxExternal implements CMapFile {

    private static final Logger LOGGER = Logger.getLogger(PBoxCMapFile.class);

    /**
     * Type name for {@code PBoxCMapFile}
     */
    public static final String CMAP_FILE_TYPE = "CMapFile";

    private final COSStream fileStream;

    /**
     * Default constructor.
     *
     * @param fileStream stream of CMapFile
     */
    public PBoxCMapFile(final COSStream fileStream) {
        super(CMAP_FILE_TYPE);
        this.fileStream = fileStream;
    }

    /**
     * @return value of {@code WMode} key
     */
    public Long getWMode() {
        try {
            CMap map = new CMapParser().parse(fileStream.getUnfilteredStream());
            return Long.valueOf(map.getWMode());
        } catch (IOException e) {
            LOGGER.error("Could not parse CMap", e);
        }
        return null;
    }

    /**
     * @return value of {@code WMode} key of parent dictionary
     */
    public Long getdictWMode() {
        return Long.valueOf(this.fileStream.getInt(COSName.getPDFName("WMode"), 0));
    }

}