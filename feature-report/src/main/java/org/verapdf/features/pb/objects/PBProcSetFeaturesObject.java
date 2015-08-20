package org.verapdf.features.pb.objects;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSName;
import org.verapdf.exceptions.featurereport.FeaturesTreeNodeException;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.pb.tools.PBCreateNodeHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

import java.util.Set;

/**
 * Feature object for procSet
 *
 * @author Maksim Bezrukov
 */
public class PBProcSetFeaturesObject implements IFeaturesObject {

    private COSArray procSet;
    private String id;
    private Set<String> pageParent;
    private Set<String> patternParent;
    private Set<String> xobjectParent;
    private Set<String> fontParent;

    /**
     * Constructs new procSet features object
     *
     * @param procSet       - COSArray which represents procSet for feature report
     * @param id            - id of the object
     * @param pageParent    - set of page ids which contains the given procSet as its resources
     * @param patternParent - set of pattern ids which contains the given procSet as its resources
     * @param xobjectParent - set of xobject ids which contains the given procSet as its resources
     * @param fontParent    - set of font ids which contains the given procSet as its resources
     */
    public PBProcSetFeaturesObject(COSArray procSet, String id, Set<String> pageParent, Set<String> patternParent, Set<String> xobjectParent, Set<String> fontParent) {
        this.procSet = procSet;
        this.id = id;
        this.pageParent = pageParent;
        this.patternParent = patternParent;
        this.xobjectParent = xobjectParent;
        this.fontParent = fontParent;
    }

    /**
     * @return PROCSET instance of the FeaturesObjectTypesEnum enumeration
     */
    @Override
    public FeaturesObjectTypesEnum getType() {
        return FeaturesObjectTypesEnum.PROCSET;
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
        if (procSet != null) {
            FeatureTreeNode root = FeatureTreeNode.newRootInstance("procSet");
            root.addAttribute("id", id);

            parseParents(root);

            for (int i = 0; i < procSet.size(); ++i) {
                if (procSet.get(i) instanceof COSName) {
                    FeatureTreeNode entry = FeatureTreeNode.newChildInstanceWithValue("entry", ((COSName) procSet.get(i)).getName(), root);
                    entry.addAttribute("number", String.valueOf(i));
                }
            }

            collection.addNewFeatureTree(FeaturesObjectTypesEnum.PROCSET, root);
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

            PBCreateNodeHelper.parseIDSet(pageParent, "page", null, parents);
            PBCreateNodeHelper.parseIDSet(patternParent, "pattern", null, parents);
            PBCreateNodeHelper.parseIDSet(xobjectParent, "xobject", null, parents);
            PBCreateNodeHelper.parseIDSet(fontParent, "font", null, parents);
        }
    }
}
