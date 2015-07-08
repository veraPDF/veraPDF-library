package org.verapdf.features.pb.objects;

import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.verapdf.exceptions.featurereport.FeaturesTreeNodeException;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.tools.ErrorsHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

/**
 * Feature object for outlines
 *
 * @author Maksim Bezrukov
 */
public class PBOutlinesFeaturesObject implements IFeaturesObject {

    private PDDocumentOutline outline;

    /**
     * Constructs new OutputIntent Feature Object
     *
     * @param outline - pdfbox class represents outlines object
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
     * @param collection - collection for feature report
     * @return FeatureTreeNode class which represents a root node of the constructed collection tree
     * @throws FeaturesTreeNodeException - occurs when wrong features tree node constructs
     */
    @Override
    public FeatureTreeNode reportFeatures(FeaturesCollection collection) throws FeaturesTreeNodeException {
        if (outline != null) {
            FeatureTreeNode root = FeatureTreeNode.newInstance("outlines", null);

            if (outline.children() != null) {
                for (PDOutlineItem item : outline.children()) {
                    createItem(item, root, collection);
                }
            }

            collection.addNewFeatureTree(FeaturesObjectTypesEnum.OUTLINES, root);
            return root;
        } else {
            return null;
        }
    }

    private void createItem(PDOutlineItem item, FeatureTreeNode root, FeaturesCollection collection) throws FeaturesTreeNodeException {
        if (item != null) {
            FeatureTreeNode itemNode = FeatureTreeNode.newInstance("outline", root);

            if (item.getTitle() != null) {
                FeatureTreeNode title = FeatureTreeNode.newInstance("title", item.getTitle(), itemNode);
            }

            if (item.getTextColor() != null) {
                FeatureTreeNode color = FeatureTreeNode.newInstance("color", itemNode);

                PDColor clr = item.getTextColor();
                float[] rgb = clr.getComponents();
                if (rgb.length == 3) {
                    FeatureTreeNode red = FeatureTreeNode.newInstance("red", String.valueOf(rgb[0]), color);
                    FeatureTreeNode green = FeatureTreeNode.newInstance("green", String.valueOf(rgb[1]), color);
                    FeatureTreeNode blue = FeatureTreeNode.newInstance("blue", String.valueOf(rgb[2]), color);
                } else {
                    color.addAttribute(ErrorsHelper.ERRORID, ErrorsHelper.OUTLINESCOLOR_ID);
                    ErrorsHelper.addErrorIntoCollection(collection, ErrorsHelper.OUTLINESCOLOR_ID, ErrorsHelper.OUTLINESCOLOR_MESSAGE);
                }
            }

            FeatureTreeNode italic = FeatureTreeNode.newInstance("italic", String.valueOf(item.isItalic()), itemNode);
            FeatureTreeNode bold = FeatureTreeNode.newInstance("bold", String.valueOf(item.isBold()), itemNode);

            for (PDOutlineItem child : item.children()) {
                createItem(child, itemNode, collection);
            }
        }
    }
}
