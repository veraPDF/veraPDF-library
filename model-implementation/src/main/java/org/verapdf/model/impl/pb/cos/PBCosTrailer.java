package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosIndirect;
import org.verapdf.model.coslayer.CosTrailer;

import java.util.ArrayList;
import java.util.List;

/**
 * Trailer of the document.
 *
 * @author Evgeniy Muravitskiy
 */
public class PBCosTrailer extends PBCosDict implements CosTrailer {

	public static final String COS_TRAILER_TYPE = "CosTrailer";

	public final static String CATALOG = "Catalog";

	public PBCosTrailer(COSDictionary pdfBoxObject) {
		super(pdfBoxObject);
		setType(COS_TRAILER_TYPE);
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
