package org.verapdf.model.impl.pb.pd;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDStructureTreeRoot;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.pdlayer.PDStructElem;
import org.verapdf.model.pdlayer.PDStructTreeRoot;
import org.verapdf.model.tools.TaggedPDFHelper;

import java.util.List;

/**
 * Current class is representation of PDF`s logical structure facilities.
 * Implemented by Apache PDFBox.
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDStructTreeRoot extends PBoxPDObject implements PDStructTreeRoot {

	private static final Logger LOGGER = Logger.getLogger(PBoxPDStructTreeRoot.class);

	/** Type name for {@code PBoxPDStructTreeRoot} */
	public static final String STRUCT_TREE_ROOT_TYPE = "PDStructTreeRoot";

	/** Link name for {@code K} key */
	public static final String CHILDREN = "K";

	/**
	 * Default constructor
	 *
	 * @param treeRoot structure tree root implementation
	 */
	public PBoxPDStructTreeRoot(PDStructureTreeRoot treeRoot) {
		super(treeRoot, STRUCT_TREE_ROOT_TYPE);
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		if (CHILDREN.equals(link)) {
			return this.getChildren();
		}
		return super.getLinkedObjects(link);
	}

	private List<PDStructElem> getChildren() {
		COSDictionary parent = ((PDStructureTreeRoot) this.simplePDObject).getCOSObject();
		return TaggedPDFHelper.getChildren(parent, LOGGER);
	}

}
