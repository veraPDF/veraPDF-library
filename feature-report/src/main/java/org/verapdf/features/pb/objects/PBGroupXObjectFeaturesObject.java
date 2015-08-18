package org.verapdf.features.pb.objects;

import org.apache.pdfbox.pdmodel.graphics.form.PDGroup;
import org.verapdf.exceptions.featurereport.FeaturesTreeNodeException;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.pb.tools.PBCreateNodeHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

import java.util.Set;

/**
 * Features object for group xobject
 *
 * @author Maksim Bezrukov
 */
public class PBGroupXObjectFeaturesObject implements IFeaturesObject {

    private static final String ID = "id";

    private PDGroup groupXObject;
    private String id;
    private String colorSpaceChild;
    private Set<String> xobjectParent;

    /**
     * Constructs new form xobject features object
     *
     * @param groupXObject    - PDFormXObject which represents tilling pattern for feature report
     * @param id              - id of the object
     * @param colorSpaceChild - ColorSpace id which contains in a dictionary of this xobject
     * @param xobjectParent   - set of xobject ids which contains the given xobject as its resources
     */
    public PBGroupXObjectFeaturesObject(PDGroup groupXObject, String id, String colorSpaceChild, Set<String> xobjectParent) {
        this.groupXObject = groupXObject;
        this.id = id;
        this.colorSpaceChild = colorSpaceChild;
        this.xobjectParent = xobjectParent;
    }

    /**
     * @return GROUP_XOBJECT instance of the FeaturesObjectTypesEnum enumeration
     */
    @Override
    public FeaturesObjectTypesEnum getType() {
        return FeaturesObjectTypesEnum.GROUP_XOBJECT;
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
        if (groupXObject != null) {
            FeatureTreeNode root = FeatureTreeNode.newRootInstance("form");
            root.addAttribute(ID, id);

            parseParents(root);

            PBCreateNodeHelper.addNotEmptyNode("subtype", groupXObject.getSubType().getName(), root);

            if ("Transparency".equals(groupXObject.getSubType().getName())) {
                if (colorSpaceChild != null) {
                    FeatureTreeNode clr = FeatureTreeNode.newChildInstance("colorSpace", root);
                    clr.addAttribute(ID, colorSpaceChild);
                }

                FeatureTreeNode.newChildInstanceWithValue("isolated", String.valueOf(groupXObject.isIsolated()), root);
                FeatureTreeNode.newChildInstanceWithValue("knockout", String.valueOf(groupXObject.isKnockout()), root);
            }

            collection.addNewFeatureTree(FeaturesObjectTypesEnum.FORM_XOBJECT, root);
            return root;
        }

        return null;
    }

    private void parseParents(FeatureTreeNode root) throws FeaturesTreeNodeException {
        if (xobjectParent != null && !xobjectParent.isEmpty()) {
            FeatureTreeNode parents = FeatureTreeNode.newChildInstance("parents", root);
            PBCreateNodeHelper.parseIDSet(xobjectParent, "xobject", null, parents);
        }
    }
}
