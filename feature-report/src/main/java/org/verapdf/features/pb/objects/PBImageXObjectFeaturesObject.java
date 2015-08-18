package org.verapdf.features.pb.objects;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.verapdf.exceptions.featurereport.FeaturesTreeNodeException;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.pb.tools.PBCreateNodeHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

import java.io.IOException;
import java.util.Set;

/**
 * Features object for image xobject
 *
 * @author Maksim Bezrukov
 */
public class PBImageXObjectFeaturesObject implements IFeaturesObject {

    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger
            .getLogger(PBImageXObjectFeaturesObject.class);

    private static final String ID = "id";

    private PDImageXObject imageXObject;
    private String id;
    private String colorSpaceChild;
    private String maskChild;
    private String sMaskChild;
    private Set<String> alternatesChild;
    private Set<String> pageParent;
    private Set<String> patternParent;
    private Set<String> xobjectParent;
    private Set<String> fontParent;

    /**
     * Constructs new shading features object
     *
     * @param imageXObject    - PDImageXObject which represents image xobject for feature report
     * @param id              - id of the object
     * @param colorSpaceChild - colorSpace id which contains in this image xobject
     * @param maskChild       - image xobject id which contains in this image xobject as it's mask
     * @param sMaskChild      - image xobject id which contains in this image xobject as it's smask
     * @param alternatesChild - set of image xobject ids which contains in this image xobject as alternates
     * @param pageParent      - set of page ids which contains the given image xobject as its resources
     * @param patternParent   - set of pattern ids which contains the given image xobject state as its resources
     * @param xobjectParent   - set of xobject ids which contains the given image xobject state as its resources
     * @param fontParent      - set of font ids which contains the given image xobject state as its resources
     */
    public PBImageXObjectFeaturesObject(PDImageXObject imageXObject, String id, String colorSpaceChild, String maskChild, String sMaskChild, Set<String> alternatesChild, Set<String> pageParent, Set<String> patternParent, Set<String> xobjectParent, Set<String> fontParent) {
        this.imageXObject = imageXObject;
        this.id = id;
        this.colorSpaceChild = colorSpaceChild;
        this.maskChild = maskChild;
        this.sMaskChild = sMaskChild;
        this.alternatesChild = alternatesChild;
        this.pageParent = pageParent;
        this.patternParent = patternParent;
        this.xobjectParent = xobjectParent;
        this.fontParent = fontParent;
    }

    /**
     * @return IMAGE_XOBJECT instance of the FeaturesObjectTypesEnum enumeration
     */
    @Override
    public FeaturesObjectTypesEnum getType() {
        return FeaturesObjectTypesEnum.IMAGE_XOBJECT;
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
        if (imageXObject != null) {
            FeatureTreeNode root = FeatureTreeNode.newRootInstance("image");
            root.addAttribute(ID, id);

            parseParents(root);

            FeatureTreeNode.newChildInstanceWithValue("width", String.valueOf(imageXObject.getWidth()), root);
            FeatureTreeNode.newChildInstanceWithValue("height", String.valueOf(imageXObject.getHeight()), root);

            if (colorSpaceChild != null) {
                FeatureTreeNode shading = FeatureTreeNode.newChildInstance("colorSpace", root);
                shading.addAttribute(ID, colorSpaceChild);
            }

            FeatureTreeNode.newChildInstanceWithValue("bitsPerComponent", String.valueOf(imageXObject.getBitsPerComponent()), root);
            FeatureTreeNode.newChildInstanceWithValue("imageMask", String.valueOf(imageXObject.isStencil()), root);

            if (maskChild != null) {
                FeatureTreeNode mask = FeatureTreeNode.newChildInstance("mask", root);
                mask.addAttribute(ID, maskChild);
            }

            FeatureTreeNode.newChildInstanceWithValue("interpolate", String.valueOf(imageXObject.getInterpolate()), root);
            PBCreateNodeHelper.parseIDSet(alternatesChild, "alternate", "alternates", root);
            if (sMaskChild != null) {
                FeatureTreeNode mask = FeatureTreeNode.newChildInstance("sMask", root);
                mask.addAttribute(ID, sMaskChild);
            }
            FeatureTreeNode.newChildInstanceWithValue("structParent", String.valueOf(imageXObject.getStructParent()), root);

            try {
                if (imageXObject.getStream().getFilters() != null && !imageXObject.getStream().getFilters().isEmpty()) {
                    FeatureTreeNode filters = FeatureTreeNode.newChildInstance("filters", root);
                    for (COSName name : imageXObject.getStream().getFilters()) {
                        PBCreateNodeHelper.addNotEmptyNode("filter", name.getName(), filters);
                    }
                }
            } catch (IOException e) {
                LOGGER.info(e);
            }
            collection.addNewFeatureTree(FeaturesObjectTypesEnum.IMAGE_XOBJECT, root);
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
