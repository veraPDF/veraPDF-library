package org.verapdf.model.tools;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.verapdf.model.impl.pb.pd.PBoxPDStructElem;
import org.verapdf.model.pdlayer.PDStructElem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This is utility class for tagged pdf
 *
 * @author Evgeniy Muravitskiy
 */
public class TaggedPDFHelper {

	private static final int MAX_NUMBER_OF_ELEMENTS = 1;

	private TaggedPDFHelper() {
		// disable default constructor
	}

	/**
	 * Get all structure elements for current dictionary
	 *
	 * @param parent parent dictionary
	 * @param logger for obtain problems report
	 * @return list of structure elements
	 */
	public static List<PDStructElem> getChildren(COSDictionary parent, Logger logger) {
		COSBase children = parent.getDictionaryObject(COSName.K);
		if (children == null) {
			return Collections.emptyList();
		} else if (children instanceof COSDictionary) {
			List<PDStructElem> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			list.add(new PBoxPDStructElem((COSDictionary) children));
			return Collections.unmodifiableList(list);
		} else if (children instanceof COSArray) {
			return getChildrenFromArray(((COSArray) children), logger);
		} else {
			logger.warn("Children type of Structure Tree Root or Structure Element " +
					"must be 'COSDictionary' or 'COSArray' but got: "
					+ children.getClass().getSimpleName());
			return Collections.emptyList();
		}
	}

	/**
	 * Transform array of dictionaries to list of structure elements
	 *
	 * @param children array of children structure elements
	 * @param logger for obtain problems report
	 * @return list of structure elements
	 */
	public static List<PDStructElem> getChildrenFromArray(COSArray children, Logger logger) {
		if (children.size() > 0) {
			List<PDStructElem> list = new ArrayList<>(children.size());
			for (COSBase element : children) {
				if (element instanceof COSDictionary) {
					list.add(new PBoxPDStructElem((COSDictionary) element));
				} else {
					logger.warn("Children type of Structure Tree Root or Structure Element " +
							"in array must be 'COSDictionary' but got: "
							+ children.getClass().getSimpleName());
				}
			}
			return Collections.unmodifiableList(list);
		} else {
			return Collections.emptyList();
		}
	}
}
