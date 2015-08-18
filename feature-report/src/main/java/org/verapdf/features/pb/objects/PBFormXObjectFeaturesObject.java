package org.verapdf.features.pb.objects;

import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.verapdf.exceptions.featurereport.FeaturesTreeNodeException;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.pb.tools.PBCreateNodeHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

import java.util.Set;

/**
 * Feature object for form xobjects
 *
 * @author Maksim Bezrukov
 */
public class PBFormXObjectFeaturesObject implements IFeaturesObject {

    private static final String ID = "id";

    private PDFormXObject formXObject;
    private String id;
    private String groupColorSpaceChild;
    private Set<String> extGStateChild;
    private Set<String> colorSpaceChild;
    private Set<String> patternChild;
    private Set<String> shadingChild;
    private Set<String> xobjectChild;
    private Set<String> fontChild;
    private Set<String> procSetChild;
    private Set<String> propertiesChild;
    private Set<String> pageParent;
    private Set<String> annotationParent;
    private Set<String> patternParent;
    private Set<String> xobjectParent;
    private Set<String> fontParent;

    /**
     * Constructs new form xobject features object
     *
     * @param formXObject          - PDFormXObject which represents form xobject for feature report
     * @param id                   - id of the object
     * @param groupColorSpaceChild - id of the group xobject which contains in the given form xobject
     * @param extGStateChild       - set of external graphics state id which contains in resource dictionary of this xobject
     * @param colorSpaceChild      - set of ColorSpace id which contains in resource dictionary of this xobject
     * @param patternChild         - set of pattern id which contains in resource dictionary of this xobject
     * @param shadingChild         - set of shading id which contains in resource dictionary of this xobject
     * @param xobjectChild         - set of XObject id which contains in resource dictionary of this xobject
     * @param fontChild            - set of font id which contains in resource dictionary of this pattern
     * @param procSetChild         - set of procedure set id awhich contains in resource dictionary of this xobject
     * @param propertiesChild      - set of properties id which contains in resource dictionary of this xobject
     * @param pageParent           - set of page ids which contains the given xobject as its resources
     * @param annotationParent     - set of annotation ids which contains the given xobject in its appearance dictionary
     * @param patternParent        - set of pattern ids which contains the given xobject as its resources
     * @param xobjectParent        - set of xobject ids which contains the given xobject as its resources
     * @param fontParent           - set of font ids which contains the given xobject as its resources
     */
    public PBFormXObjectFeaturesObject(PDFormXObject formXObject, String id, String groupColorSpaceChild, Set<String> extGStateChild, Set<String> colorSpaceChild, Set<String> patternChild, Set<String> shadingChild, Set<String> xobjectChild, Set<String> fontChild, Set<String> procSetChild, Set<String> propertiesChild, Set<String> pageParent, Set<String> annotationParent, Set<String> patternParent, Set<String> xobjectParent, Set<String> fontParent) {
        this.formXObject = formXObject;
        this.id = id;
        this.groupColorSpaceChild = groupColorSpaceChild;
        this.extGStateChild = extGStateChild;
        this.colorSpaceChild = colorSpaceChild;
        this.patternChild = patternChild;
        this.shadingChild = shadingChild;
        this.xobjectChild = xobjectChild;
        this.fontChild = fontChild;
        this.procSetChild = procSetChild;
        this.propertiesChild = propertiesChild;
        this.pageParent = pageParent;
        this.annotationParent = annotationParent;
        this.patternParent = patternParent;
        this.xobjectParent = xobjectParent;
        this.fontParent = fontParent;
    }

    /**
     * @return FORM_XOBJECT instance of the FeaturesObjectTypesEnum enumeration
     */
    @Override
    public FeaturesObjectTypesEnum getType() {
        return FeaturesObjectTypesEnum.FORM_XOBJECT;
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
        if (formXObject != null) {
            FeatureTreeNode root = FeatureTreeNode.newRootInstance("form");
            root.addAttribute(ID, id);

            parseParents(root);

            PBCreateNodeHelper.addBoxFeature("bbox", formXObject.getBBox(), root);
            parseFloatMatrix(formXObject.getMatrix().getValues(), FeatureTreeNode.newChildInstance("matrix", root));

            if (formXObject.getGroup() != null) {
                FeatureTreeNode groupNode = FeatureTreeNode.newRootInstance("group");
                if (formXObject.getGroup().getSubType() != null) {
                    PBCreateNodeHelper.addNotEmptyNode("subtype", formXObject.getGroup().getSubType().getName(), root);
                    if ("Transparency".equals(formXObject.getGroup().getSubType().getName())) {
                        if (groupColorSpaceChild != null) {
                            FeatureTreeNode clr = FeatureTreeNode.newChildInstance("colorSpace", root);
                            clr.addAttribute(ID, groupColorSpaceChild);
                        }

                        FeatureTreeNode.newChildInstanceWithValue("isolated", String.valueOf(formXObject.getGroup().isIsolated()), root);
                        FeatureTreeNode.newChildInstanceWithValue("knockout", String.valueOf(formXObject.getGroup().isKnockout()), root);
                    }

                }
            }

            FeatureTreeNode.newChildInstanceWithValue("structParents", String.valueOf(formXObject.getStructParents()), root);

            parseResources(root);

            collection.addNewFeatureTree(FeaturesObjectTypesEnum.FORM_XOBJECT, root);
            return root;
        }

        return null;
    }

    private void parseFloatMatrix(float[][] array, FeatureTreeNode parent) throws FeaturesTreeNodeException {
        for (int i = 0; i < array.length; ++i) {
            for (int j = 0; j < array.length - 1; ++j) {
                FeatureTreeNode element = FeatureTreeNode.newChildInstance("element", parent);
                element.addAttribute("row", String.valueOf(i));
                element.addAttribute("column", String.valueOf(j));
                element.addAttribute("value", String.valueOf(array[i][j]));
            }
        }
    }

    private void parseParents(FeatureTreeNode root) throws FeaturesTreeNodeException {
        if ((pageParent != null && !pageParent.isEmpty()) ||
                (annotationParent != null && !annotationParent.isEmpty()) ||
                (patternParent != null && !patternParent.isEmpty()) ||
                (xobjectParent != null && !xobjectParent.isEmpty()) ||
                (fontParent != null && !fontParent.isEmpty())) {
            FeatureTreeNode parents = FeatureTreeNode.newChildInstance("parents", root);

            PBCreateNodeHelper.parseIDSet(pageParent, "page", null, parents);
            PBCreateNodeHelper.parseIDSet(annotationParent, "annotation", null, parents);
            PBCreateNodeHelper.parseIDSet(patternParent, "pattern", null, parents);
            PBCreateNodeHelper.parseIDSet(xobjectParent, "xobject", null, parents);
            PBCreateNodeHelper.parseIDSet(fontParent, "font", null, parents);
        }
    }

    private void parseResources(FeatureTreeNode root) throws FeaturesTreeNodeException {

        if ((extGStateChild != null && !extGStateChild.isEmpty()) ||
                (colorSpaceChild != null && !colorSpaceChild.isEmpty()) ||
                (patternChild != null && !patternChild.isEmpty()) ||
                (shadingChild != null && !shadingChild.isEmpty()) ||
                (xobjectChild != null && !xobjectChild.isEmpty()) ||
                (fontChild != null && !fontChild.isEmpty()) ||
                (procSetChild != null && !procSetChild.isEmpty()) ||
                (propertiesChild != null && !propertiesChild.isEmpty())) {
            FeatureTreeNode resources = FeatureTreeNode.newChildInstance("resources", root);

            PBCreateNodeHelper.parseIDSet(extGStateChild, "graphicsState", "graphicsStates", resources);
            PBCreateNodeHelper.parseIDSet(colorSpaceChild, "colorSpace", "colorSpaces", resources);
            PBCreateNodeHelper.parseIDSet(patternChild, "pattern", "patterns", resources);
            PBCreateNodeHelper.parseIDSet(shadingChild, "shading", "shadings", resources);
            PBCreateNodeHelper.parseIDSet(xobjectChild, "xobject", "xobjects", resources);
            PBCreateNodeHelper.parseIDSet(fontChild, "font", "fonts", resources);
            PBCreateNodeHelper.parseIDSet(procSetChild, "procSet", "procSets", resources);
            PBCreateNodeHelper.parseIDSet(propertiesChild, "propertiesDict", "propertiesDicts", resources);
        }
    }
}
