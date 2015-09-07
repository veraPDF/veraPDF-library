package org.verapdf.model.impl.pb.pd.font;

import org.apache.fontbox.cmap.CMap;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.external.CMapFile;
import org.verapdf.model.impl.pb.pd.PBoxPDObject;
import org.verapdf.model.pdlayer.PDCMap;

import java.util.Collections;
import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBoxPDCMap extends PBoxPDObject implements PDCMap {

    public static final String CMAP_TYPE = "PDCMap";

    public static final String EMBEDDED_FILE = "embeddedFile";

    public PBoxPDCMap(CMap cMap) {
        super(cMap, CMAP_TYPE);
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        if (EMBEDDED_FILE.equals(link)) {
            return getEmbeddedFile();
        }
        return super.getLinkedObjects(link);
    }

    private static List<CMapFile> getEmbeddedFile() {
        // TODO : implement me
		return Collections.emptyList();
    }

    @Override
    public String getCMapName() {
        return this.cMap.getName();
    }

}
