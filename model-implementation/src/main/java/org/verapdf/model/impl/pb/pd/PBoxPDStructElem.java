package org.verapdf.model.impl.pb.pd;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosUnicodeName;
import org.verapdf.model.impl.pb.cos.PBCosUnicodeName;
import org.verapdf.model.pdlayer.PDStructElem;
import org.verapdf.model.tools.TaggedPDFHelper;

import java.util.ArrayList;
import java.util.Collections;
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
	/** Link name for {@code S} key */
	public static final String STRUCTURE_TYPE = "S";

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
	@Override
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
		switch (link) {
			case CHILDREN:
				return this.getChildren();
			case STRUCTURE_TYPE:
				return this.getStructureType();
			default:
				return super.getLinkedObjects(link);
		}
	}

	private List<PDStructElem> getChildren() {
		return TaggedPDFHelper.getChildren(((COSDictionary) this.simplePDObject), LOGGER);
	}

	private List<CosUnicodeName> getStructureType() {
		COSBase type = ((COSDictionary) this.simplePDObject).getDictionaryObject(COSName.S);
		if (type instanceof COSName) {
			ArrayList<CosUnicodeName> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			list.add(new PBCosUnicodeName((COSName) type));
			return Collections.unmodifiableList(list);
		}
		return Collections.emptyList();
	}
}
