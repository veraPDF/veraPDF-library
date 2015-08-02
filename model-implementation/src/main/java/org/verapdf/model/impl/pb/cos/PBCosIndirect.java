package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSObject;
import org.verapdf.model.coslayer.CosIndirect;
import org.verapdf.model.coslayer.CosObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Current class is representation of CosIndirect interface of abstract model.
 * This class is analogue of COSObject in pdfbox.
 *
 * @author Evgeniy Muravitskiy
 */
public class PBCosIndirect extends PBCosObject implements CosIndirect {

	public static final String DIRECT_OBJECT = "directObject";
    /** Type name for PBCosBool */
	public static final String COS_INDIRECT_TYPE = "CosIndirect";

	public PBCosIndirect(COSBase directObject) {
		super(directObject, COS_INDIRECT_TYPE);
	}

	@Override
	public List<? extends org.verapdf.model.baselayer.Object> getLinkedObjects(String link) {
		List<? extends org.verapdf.model.baselayer.Object> list;

		switch (link) {
			case DIRECT_OBJECT:
				list = getDirectObject();
				break;
			default:
				list = super.getLinkedObjects(link);
				break;
		}

		return list;
	}

	/**
	 * Get the direct contents of the indirect object
	 */
	protected List<CosObject> getDirectObject() {
		List<CosObject> list = new ArrayList<>();
		COSBase base = baseObject instanceof COSObject ? ((COSObject) baseObject).getObject() : baseObject;
		list.add(base != null ? getFromValue(base) : PBCosNull.NULL);
		return list;
	}

	/**
	 * true if the words 'obj' and 'endobj' are surrounded by the correct spacings according to PDF/A standard
	 */
	@Override
	public Boolean getspacingCompliesPDFA() {
		return Boolean.valueOf(((COSObject) baseObject).isEndOfObjectComplyPDFA().booleanValue()
				&& ((COSObject) baseObject).isHeaderFormatComplyPDFA().booleanValue()
				&& ((COSObject) baseObject).isHeaderOfObjectComplyPDFA().booleanValue());
	}
}
