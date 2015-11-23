package org.verapdf.features.pb.objects;

import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.FeaturesData;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.pb.tools.PBCreateNodeHelper;
import org.verapdf.features.tools.ErrorsHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

import java.util.HashSet;
import java.util.Set;

/**
 * Feature object for outlines
 *
 * @author Maksim Bezrukov
 */
public class PBOutlinesFeaturesObject implements IFeaturesObject {

	private static final int RGB_COLORS_NUMBER = 3;
	private static final int RGB_RED_COLOR_NUMBER = 0;
	private static final int RGB_GREEN_COLOR_NUMBER = 1;
	private static final int RGB_BLUE_COLOR_NUMBER = 2;

	private PDDocumentOutline outline;

	/**
	 * Constructs new OutputIntent Feature Object
	 *
	 * @param outline pdfbox class represents outlines object
	 */
	public PBOutlinesFeaturesObject(PDDocumentOutline outline) {
		this.outline = outline;
	}

	/**
	 * @return OUTLINES instance of the FeaturesObjectTypesEnum enumeration
	 */
	@Override
	public FeaturesObjectTypesEnum getType() {
		return FeaturesObjectTypesEnum.OUTLINES;
	}

	/**
	 * Reports featurereport into collection
	 *
	 * @param collection collection for feature report
	 * @return FeatureTreeNode class which represents a root node of the
	 * constructed collection tree
	 * @throws FeatureParsingException occurs when wrong features tree node constructs
	 */
	@Override
	public FeatureTreeNode reportFeatures(FeaturesCollection collection)
			throws FeatureParsingException {
		if (outline != null) {
			FeatureTreeNode root = FeatureTreeNode.createRootNode("outlines");

			if (outline.children() != null) {
				Set<PDOutlineItem> items = new HashSet<>();
				for (PDOutlineItem item : outline.children()) {
					if (!items.contains(item)) {
						createItem(item, root, collection, items);
					}
				}
			}

			collection
					.addNewFeatureTree(FeaturesObjectTypesEnum.OUTLINES, root);
			return root;
		}
		return null;
	}

	/**
	 * @return null
	 */
	@Override
	public FeaturesData getData() {
		return null;
	}

	private static void createItem(PDOutlineItem item, FeatureTreeNode root,
								   FeaturesCollection collection, Set<PDOutlineItem> items) throws FeatureParsingException {
		if (item != null) {
			items.add(item);
			FeatureTreeNode itemNode = FeatureTreeNode.createChildNode(
					"outline", root);

			PBCreateNodeHelper.addNotEmptyNode("title", item.getTitle(),
					itemNode);


			FeatureTreeNode color = FeatureTreeNode.createChildNode(
					"color", itemNode);

			PDColor clr = item.getTextColor();
			float[] rgb = clr.getComponents();
			if (rgb.length == RGB_COLORS_NUMBER) {
				color.setAttribute("red", String.valueOf(rgb[RGB_RED_COLOR_NUMBER]));
				color.setAttribute("green", String.valueOf(rgb[RGB_GREEN_COLOR_NUMBER]));
				color.setAttribute("blue", String.valueOf(rgb[RGB_BLUE_COLOR_NUMBER]));
			} else {
				ErrorsHelper.addErrorIntoCollection(collection,
						color,
						"Color must be in rgb form");
			}


			FeatureTreeNode style = FeatureTreeNode.createChildNode("style", itemNode);
			style.setAttribute("italic", String.valueOf(item.isItalic()));
			style.setAttribute("bold", String.valueOf(item.isBold()));

			for (PDOutlineItem child : item.children()) {
				if (!items.contains(child)) {
					createItem(child, itemNode, collection, items);
				}
			}
		}
	}
}
