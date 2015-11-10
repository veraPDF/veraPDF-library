package org.verapdf.model.tools;

import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.verapdf.model.impl.pb.pd.PBoxPDOutline;
import org.verapdf.model.pdlayer.PDOutline;

import java.util.*;

/**
 * @author Evgeniy Muravitskiy
 */
public class OutlinesHelper {

	private OutlinesHelper() {
		// disable default constructor
	}

	public static List<PDOutline> getOutlines(PDDocumentCatalog catalog) {
		Map<PDOutlineItem, String> outlines = getOutlinesMap(catalog);
		if (outlines.size() > 0) {
			List<PDOutline> result = new ArrayList<>(outlines.size());
			for (Map.Entry<PDOutlineItem, String> entry : outlines.entrySet()) {
				result.add(new PBoxPDOutline(entry.getKey(), entry.getValue()));
			}
			outlines.clear();
			return Collections.unmodifiableList(result);
		} else {
			return Collections.emptyList();
		}
	}

	private static Map<PDOutlineItem, String> getOutlinesMap(PDDocumentCatalog catalog) {
		if (catalog != null) {
			PDDocumentOutline documentOutline = catalog.getDocumentOutline();
			if (documentOutline != null) {
				PDOutlineItem firstChild = documentOutline.getFirstChild();
				if (firstChild != null) {
					Deque<PDOutlineItem> stack = new ArrayDeque<>();
					stack.push(firstChild);
					return getOutlinesMap(stack);
				}
			}
		}

		return Collections.emptyMap();
	}

	private static Map<PDOutlineItem, String> getOutlinesMap(Deque<PDOutlineItem> stack) {
		Map<PDOutlineItem, String> result = new HashMap<>();
		do {
			PDOutlineItem item = stack.pop();
			PDOutlineItem nextSibling = item.getNextSibling();
			PDOutlineItem firstChild = item.getFirstChild();
			if (nextSibling != null && !result.containsKey(nextSibling)) {
				stack.add(nextSibling);
			}
			if (firstChild != null && !result.containsKey(firstChild)) {
				stack.add(firstChild);
			}
			result.put(item, IDGenerator.generateID(item));
		} while (!stack.isEmpty());

		return result;
	}

}
