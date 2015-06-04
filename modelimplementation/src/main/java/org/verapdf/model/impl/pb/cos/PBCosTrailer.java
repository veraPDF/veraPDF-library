package org.verapdf.model.impl.pb.cos;

import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.cos.*;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosIndirect;
import org.verapdf.model.coslayer.CosTrailer;

/**
 * Trailer of the document.
 *
 * @author Evgeniy Muravitskiy
 */
public class PBCosTrailer extends PBCosDict implements CosTrailer {

	public final static String CATALOG = "Catalog";

	public PBCosTrailer(COSDictionary pdfBoxObject) {
		super(pdfBoxObject);
		setType("CosTrailer");
	}

	/**
	 * @return true if the current document is encrypted
	 */
	@Override
	public Boolean getisEncrypted() {
		return Boolean
				.valueOf(((COSDictionary) baseObject).getItem(COSName.ENCRYPT) != null);
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		List<? extends Object> list;
		switch (link) {
		case CATALOG:
			list = getCatalog();
			break;
		default:
			list = super.getLinkedObjects(link);
			break;
		}
		return list;
	}

	private List<CosIndirect> getCatalog() {
		List<CosIndirect> catalog = new ArrayList<>(1);
		COSBase base = ((COSDictionary) baseObject).getItem(COSName.ROOT);
		catalog.add(new PBCosIndirect(base));
		return catalog;
	}
}
