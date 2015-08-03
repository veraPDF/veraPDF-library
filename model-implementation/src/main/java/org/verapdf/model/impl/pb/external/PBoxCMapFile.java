package org.verapdf.model.impl.pb.external;

import org.verapdf.model.external.CMapFile;

/**
 * @author Timur Kamalov
 */
public class PBoxCMapFile extends PBoxExternal implements CMapFile {

    public static final String CMAP_FILE_TYPE = "CMapFile";

    public PBoxCMapFile() {
        setType(CMAP_FILE_TYPE);
    }

}
