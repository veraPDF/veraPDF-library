package org.verapdf.model.impl.pb.cos;

import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSString;
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
				.valueOf(((COSDictionary) baseObject).getItem("Encrypt") != null);
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
		catalog.add((CosIndirect) ((COSDictionary) baseObject).getItem(CATALOG));
		return catalog;
	}
}
