package org.verapdf.model.impl.pb.pd.font;

import org.apache.fontbox.cmap.CMap;
import org.apache.pdfbox.cos.COSStream;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.external.CMapFile;
import org.verapdf.model.impl.pb.external.PBoxCMapFile;
import org.verapdf.model.impl.pb.pd.PBoxPDObject;
import org.verapdf.model.pdlayer.PDCMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBoxPDCMap extends PBoxPDObject implements PDCMap {

    public static final String CMAP_TYPE = "PDCMap";

    public static final String EMBEDDED_FILE = "embeddedFile";

    public PBoxPDCMap(CMap cMap, COSStream cMapFile) {
        super(cMap, cMapFile, CMAP_TYPE);
    }

	@Override
	public String getCMapName() {
		return this.cMap.getName();
	}

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        if (EMBEDDED_FILE.equals(link)) {
            return this.getEmbeddedFile();
        }
        return super.getLinkedObjects(link);
    }

    private List<CMapFile> getEmbeddedFile() {
        if (this.simplePDObject instanceof COSStream) {
			List<CMapFile> result = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
            result.add(new PBoxCMapFile((COSStream) this.simplePDObject));
            return Collections.unmodifiableList(result);
        }
		return Collections.emptyList();
    }

}
