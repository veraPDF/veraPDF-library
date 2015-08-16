package org.verapdf.features.pb.objects;

import org.apache.pdfbox.pdmodel.graphics.shading.PDShading;
import org.verapdf.exceptions.featurereport.FeaturesTreeNodeException;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.pb.tools.PBCreateNodeHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

import java.util.Set;

/**
 * Features object for shading
 *
 * @author Maksim Bezrukov
 */
public class PBShadingFeaturesObject implements IFeaturesObject {

    private static final String ID = "id";

    private PDShading shading;
    private String id;
    private String colorSpaceChild;
    private Set<String> pageParent;
    private Set<String> patternParent;
    private Set<String> xobjectParent;
    private Set<String> fontParent;

    /**
     * Constructs new shading features object
     *
     * @param shading         - PDTilingPattern which represents tilling pattern for feature report
     * @param id              - id of the object
     * @param colorSpaceChild - colorSpace id which contains in this shading pattern
     * @param pageParent      - set of page ids which contains the given extended graphics state as its resources
     * @param patternParent   - set of pattern ids which contains the given extended graphics state as its resources
     * @param xobjectParent   - set of xobject ids which contains the given extended graphics state as its resources
     * @param fontParent      - set of font ids which contains the given extended graphics state as its resources
     */
    public PBShadingFeaturesObject(PDShading shading, String id, String colorSpaceChild, Set<String> pageParent, Set<String> patternParent, Set<String> xobjectParent, Set<String> fontParent) {
        this.shading = shading;
        this.id = id;
        this.colorSpaceChild = colorSpaceChild;
        this.pageParent = pageParent;
        this.patternParent = patternParent;
        this.xobjectParent = xobjectParent;
        this.fontParent = fontParent;
    }

    /**
     * @return SHADING instance of the FeaturesObjectTypesEnum enumeration
     */
    @Override
    public FeaturesObjectTypesEnum getType() {
        return FeaturesObjectTypesEnum.SHADING;
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
        if (shading != null) {
            FeatureTreeNode root = FeatureTreeNode.newRootInstance("shading");
            root.addAttribute(ID, id);

            parseParents(root);

            FeatureTreeNode.newChildInstanceWithValue("shadingType", String.valueOf(shading.getShadingType()), root);

            if (colorSpaceChild != null) {
                FeatureTreeNode shading = FeatureTreeNode.newChildInstance("colorSpace", root);
                shading.addAttribute(ID, colorSpaceChild);
            }

            PBCreateNodeHelper.addBoxFeature("bbox", shading.getBBox(), root);

            FeatureTreeNode.newChildInstanceWithValue("antiAlias", String.valueOf(shading.getAntiAlias()), root);

            collection.addNewFeatureTree(FeaturesObjectTypesEnum.SHADING, root);
            return root;
        }

        return null;
    }

    private void parseParents(FeatureTreeNode root) throws FeaturesTreeNodeException {
        if ((pageParent != null && !pageParent.isEmpty()) ||
                (patternParent != null && !patternParent.isEmpty()) ||
                (xobjectParent != null && !xobjectParent.isEmpty()) ||
                (fontParent != null && !fontParent.isEmpty())) {
            FeatureTreeNode parents = FeatureTreeNode.newChildInstance("parents", root);

            if (pageParent != null) {
                for (String id : pageParent) {
                    FeatureTreeNode node = FeatureTreeNode.newChildInstance("page", parents);
                    node.addAttribute(ID, id);
                }
            }
            if (patternParent != null) {
                for (String id : patternParent) {
                    FeatureTreeNode node = FeatureTreeNode.newChildInstance("pattern", parents);
                    node.addAttribute(ID, id);
                }
            }
            if (xobjectParent != null) {
                for (String id : xobjectParent) {
                    FeatureTreeNode node = FeatureTreeNode.newChildInstance("xobject", parents);
                    node.addAttribute(ID, id);
                }
            }
            if (fontParent != null) {
                for (String id : fontParent) {
                    FeatureTreeNode node = FeatureTreeNode.newChildInstance("font", parents);
                    node.addAttribute(ID, id);
                }
            }
        }
    }
}
