package org.verapdf.model.impl.pb.pd;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.tools.TaggedPDFHelper;

import java.util.List;

/**
 * Current class is representation of node of structure tree root.
 * Implemented by Apache PDFBox.
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDStructElem extends PBoxPDObject implements PDStructElem {

	private static final Logger LOGGER = Logger.getLogger(PBoxPDStructElem.class);

	/** Type name for {@code PBoxPDStructElem} */
	public static final String STRUCTURE_ELEMENT_TYPE = "PDStructElem";

	/** Link name for {@code K} key */
	public static final String CHILDREN = "K";

	/**
	 * Default constructor
	 *
	 * @param structElemDictionary dictionary of structure element
	 */
	public PBoxPDStructElem(COSDictionary structElemDictionary) {
		super(structElemDictionary, STRUCTURE_ELEMENT_TYPE);
	}

	/**
	 * @return Type entry of current structure element
	 */
	public String getType() {
		COSBase value = ((COSDictionary) this.simplePDObject).getDictionaryObject(COSName.TYPE);
		if (value instanceof COSName) {
			return ((COSName) value).getName();
		} else {
			LOGGER.warn("In struct element type expected 'COSName' but got: "
					+ value.getClass().getSimpleName());
			return null;
		}
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		if (CHILDREN.equals(link)) {
			return this.getChildren();
		}
		return super.getLinkedObjects(link);
	}

	private List<PDStructElem> getChildren() {
		return TaggedPDFHelper.getChildren(((COSDictionary) this.simplePDObject), LOGGER);
	}
}
