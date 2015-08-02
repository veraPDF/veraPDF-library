package org.verapdf.model.impl.pb.pd.font;

import org.apache.fontbox.cmap.CMap;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.external.CMapFile;
import org.verapdf.model.impl.pb.pd.PBoxPDObject;
import org.verapdf.model.pdlayer.PDCMap;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBoxPDCMap extends PBoxPDObject implements PDCMap {

	public static final String CMAP_TYPE = "PDCMap";
	public static final String EMBEDDED_FILE = "embeddedFile";

	public PBoxPDCMap(CMap cMap) {
		super(cMap);
		setType(CMAP_TYPE);
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		List<? extends Object> list;

		switch (link) {
			case EMBEDDED_FILE:
				list = getEmbeddedFile();
				break;
			default:
				list = super.getLinkedObjects(link);
				break;
		}

		return list;
	}

	private static List<CMapFile> getEmbeddedFile() {
		List<CMapFile> list = new ArrayList<>();
		return list;
	}

	@Override
	public String getCMapName() {
		String cMapName = cMap.getName();
		return cMapName;
	}

}
